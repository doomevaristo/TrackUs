package com.marcosevaristo.tcc001.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marcosevaristo.tcc001.App;
import com.marcosevaristo.tcc001.model.Cidade;
import com.marcosevaristo.tcc001.model.Linha;
import com.marcosevaristo.tcc001.utils.CollectionUtils;
import com.marcosevaristo.tcc001.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    private static SQLiteHelper sqLiteHelper = App.getSqLiteHelper();

    private QueryBuilder() {}

    public static List<Linha> getFavoritos(String nroLinha) {
        List<Linha> lLinhas = new ArrayList<>();
        Linha linhaAux;
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(getSelectAllFavoritos(nroLinha), null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        while(cursor.moveToNext()) {
            linhaAux = new Linha();
            linhaAux.setNumero(cursor.getString(0));
            linhaAux.setTitulo(cursor.getString(1));
            linhaAux.setSubtitulo(cursor.getString(2));
            linhaAux.setCidade(new Cidade(cursor.getString(3)));
            lLinhas.add(linhaAux);
        }

        cursor.close();
        return lLinhas;
    }

    private static String getSelectAllFavoritos(String nroLinha) {
        StringBuilder sb = new StringBuilder("SELECT ").append(SQLiteObjectsHelper.TLinhas.getColunasParaSelect()).append(" FROM ");
        sb.append(SQLiteObjectsHelper.TFavoritos.TABLE_NAME).append(" FAV ");
        sb.append(" INNER JOIN ").append(SQLiteObjectsHelper.TLinhas.TABLE_NAME).append(" LINHA ON LINHA.");
        sb.append(SQLiteObjectsHelper.TLinhas._ID).append(" = FAV.").append(SQLiteObjectsHelper.TFavoritos.COLUMN_LINHA);
        if(StringUtils.isNotBlank(nroLinha)) {
            sb.append(" WHERE ").append(SQLiteObjectsHelper.TLinhas.COLUMN_NUMERO).append(" LIKE '%").append(nroLinha).append("%' ");
        }
        sb.append(" ORDER BY ").append(SQLiteObjectsHelper.TLinhas.COLUMN_NUMERO).append(" DESC");
        return sb.toString();
    }

    public static long insereFavorito(Linha linha) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SQLiteObjectsHelper.TLinhas.COLUMN_NUMERO, linha.getNumero());
        values.put(SQLiteObjectsHelper.TLinhas.COLUMN_TITULO, linha.getTitulo());
        values.put(SQLiteObjectsHelper.TLinhas.COLUMN_SUBTITULO, linha.getSubtitulo());
        //values.put(SQLiteObjectsHelper.TLinhas.COLUMN_CIDADE, linha.getCidade().getId());
        db.beginTransaction();
        Long linhaId = db.insert(SQLiteObjectsHelper.TLinhas.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(SQLiteObjectsHelper.TFavoritos.COLUMN_LINHA, linhaId);
        Long favoritoId = db.insert(SQLiteObjectsHelper.TFavoritos.TABLE_NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        return favoritoId;
    }

    public static int deletaFavorito(Linha linha) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        StringBuilder sbWhere = new StringBuilder();
        sbWhere.append(SQLiteObjectsHelper.TLinhas.COLUMN_NUMERO).append(" = ?");

        return db.delete(SQLiteObjectsHelper.TLinhas.TABLE_NAME, sbWhere.toString(), new String[]{linha.getNumero()});
    }

    private static boolean colunasInformadasClausulasNaoInformadas(String[] columns, List<Cidade> lCidades) {
        return CollectionUtils.isEmpty(lCidades) && columns != null && columns.length > 0;
    }

}
