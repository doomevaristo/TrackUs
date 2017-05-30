package com.marcosevaristo.tcc001.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marcosevaristo.tcc001.App;
import com.marcosevaristo.tcc001.model.Cidade;
import com.marcosevaristo.tcc001.model.Linha;
import com.marcosevaristo.tcc001.utils.CollectionUtils;
import com.marcosevaristo.tcc001.utils.SQLiteHelper;
import com.marcosevaristo.tcc001.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    private QueryBuilder instance = null;
    private static SQLiteHelper sqLiteHelper = App.getSqLiteHelper();

    public QueryBuilder getInstance() {
        if(instance == null) {
            instance = new QueryBuilder();
        }
        return instance;
    }

    private QueryBuilder() {}

    public static List<Linha> getFavoritos() {
        List<Linha> lLinhas = new ArrayList<>();
        Linha linhaAux;
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(getSelectAllFavoritos(), null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        while(!cursor.isAfterLast()) {
            linhaAux = new Linha();
            linhaAux.setNumero(cursor.getString(0));
            linhaAux.setTitulo(cursor.getString(1));
            linhaAux.setSubtitulo(cursor.getString(2));
            linhaAux.setCidade(new Cidade(cursor.getString(3)));
            lLinhas.add(linhaAux);
        }

        return lLinhas;
    }

    private static String getSelectAllFavoritos() {
        StringBuilder sb = new StringBuilder("SELECT ").append(DatabaseObjectsHelper.TLinhas.getColunasParaSelect()).append(" FROM ");
        sb.append(DatabaseObjectsHelper.TFavoritos.TABLE_NAME);
        sb.append(" INNER JOIN ").append(DatabaseObjectsHelper.TLinhas.TABLE_NAME).append(" ON ");
        sb.append(DatabaseObjectsHelper.TLinhas.COLUMN_NUMERO).append(" = ").append(DatabaseObjectsHelper.TFavoritos.COLUMN_LINHA);
        sb.append(" ORDER BY ").append(DatabaseObjectsHelper.TLinhas.COLUMN_NUMERO).append(" DESC");
        return sb.toString();
    }

    public static long insereFavorito(Linha linha) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseObjectsHelper.TLinhas.COLUMN_NUMERO, linha.getNumero());
        values.put(DatabaseObjectsHelper.TLinhas.COLUMN_TITULO, linha.getTitulo());
        values.put(DatabaseObjectsHelper.TLinhas.COLUMN_SUBTITULO, linha.getSubtitulo());
        values.put(DatabaseObjectsHelper.TLinhas.COLUMN_CIDADE, linha.getCidade().getId());
        db.insert(DatabaseObjectsHelper.TLinhas.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(DatabaseObjectsHelper.TFavoritos.COLUMN_LINHA, linha.getNumero());
        return db.insert(DatabaseObjectsHelper.TFavoritos.TABLE_NAME, null, values);
    }

    public void deletaFavorito() {

    }

    public Linha getFavorito(String linhaId) {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

        String[] colunasRetorno = {DatabaseObjectsHelper.TFavoritos.COLUMN_LINHA};
        StringBuilder sbWhere = new StringBuilder();

        if(StringUtils.isNotBlank(linhaId)) {
            sbWhere.append(DatabaseObjectsHelper.TFavoritos.COLUMN_LINHA).append(" = ? ");
        }

        String[] whereArgs = {};
        StringBuilder sbOrderBy = new StringBuilder();

        if(StringUtils.isNotBlank(linhaId)) {
            whereArgs[0] = linhaId;
        }

        sbOrderBy.append(DatabaseObjectsHelper.TFavoritos.COLUMN_LINHA).append(" DESC");

        Cursor c = db.query(DatabaseObjectsHelper.TFavoritos.TABLE_NAME, colunasRetorno, sbWhere.toString(), whereArgs,
                null, null,sbOrderBy.toString());

        c.moveToFirst();

        Long favoritoId = c.getLong(c.getColumnIndexOrThrow(DatabaseObjectsHelper.TFavoritos._ID));

        return null;
    }

    public static String getSelectAll(String tableName) {
        return "SELECT * FROM "+tableName;
    }

    public static String getSelectSemWhere(String tableName, String[] columns) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        if(columns == null || columns.length == 0) {
            return getSelectAll(tableName);
        } else {
            int countAux = 0;
            for(String umaColuna : columns) {
                countAux++;
                sb.append(umaColuna);
                if(countAux < columns.length) {
                    sb.append(",");
                }
            }
        }
        sb.append("FROM ").append(tableName);
        return sb.toString();
    }

    public static String getSelectWhereCidade(String tableName, String[] columns, List<Cidade> lCidades) {
        StringBuilder sb = new StringBuilder(getSelectSemWhere(tableName, columns));

        if(colunasInformadasClausulasNaoInformadas(columns, lCidades)) {
            return sb.toString();
        }

        int countAux = 0;
        for(Cidade umaCidade : lCidades) {
            countAux++;
            if(countAux == 1) {
                sb.append("WHERE FAV_CIDADEID IN ('");
            }
            sb.append(umaCidade.getId().toString()).append("'");
            if(countAux < columns.length) {
                sb.append(",");
            } else {
                sb.append(")");
            }
        }
        return sb.toString();
    }

    private static boolean colunasInformadasClausulasNaoInformadas(String[] columns, List<Cidade> lCidades) {
        return CollectionUtils.isEmpty(lCidades) && columns != null && columns.length > 0;
    }

}
