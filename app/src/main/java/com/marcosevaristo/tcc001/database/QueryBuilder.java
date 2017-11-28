package com.marcosevaristo.tcc001.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marcosevaristo.tcc001.App;
import com.marcosevaristo.tcc001.model.Linha;
import com.marcosevaristo.tcc001.model.Municipio;
import com.marcosevaristo.tcc001.utils.CollectionUtils;
import com.marcosevaristo.tcc001.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    private static SQLiteHelper sqLiteHelper = App.getSqLiteHelper();

    private QueryBuilder() {}

    public static List<Linha> getFavoritos(String idFirebase) {
        List<Linha> lLinhas = new ArrayList<>();
        Linha linhaAux;
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(getSelectAllFavoritos(idFirebase), null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        for (int i = 0; i < cursor.getCount(); i++) {
            linhaAux = new Linha();
            linhaAux.setIdSql(cursor.getLong(0));
            linhaAux.setId(cursor.getString(1));
            linhaAux.setNumero(cursor.getString(2));
            linhaAux.setTitulo(cursor.getString(3));
            linhaAux.setSubtitulo(cursor.getString(4));
            linhaAux.setMunicipio(new Municipio(cursor.getString(5), cursor.getString(6)));
            linhaAux.setEhFavorito(true);
            lLinhas.add(linhaAux);
            cursor.moveToNext();
        }

        cursor.close();
        return lLinhas;
    }

    private static String getSelectAllFavoritos(String idFirebase) {
        StringBuilder sb = new StringBuilder("SELECT ").append(SQLiteObjectsHelper.TLinhasFavoritas.getInstance().getColunasParaSelect()).append(" FROM ");
        sb.append(SQLiteObjectsHelper.TLinhasFavoritas.TABLE_NAME).append(" LIN ");
        if(StringUtils.isNotBlank(idFirebase)) {
            sb.append(" WHERE ").append(SQLiteObjectsHelper.TLinhasFavoritas.COLUMN_IDFIREBASE).append(" = '").append(idFirebase).append("' ");
        }
        sb.append(" ORDER BY ").append(SQLiteObjectsHelper.TLinhasFavoritas.COLUMN_MUNICIPIONOME).append(" ASC, ").append(SQLiteObjectsHelper.TLinhasFavoritas.COLUMN_NUMERO);
        return sb.toString();
    }

    public static void updateFavorito(Linha linha) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        List<Linha> lFavoritos = getFavoritos(linha.getId());
        ContentValues values = new ContentValues();
        db.beginTransaction();
        if(CollectionUtils.isEmpty(lFavoritos)) {
            values.put(SQLiteObjectsHelper.TLinhasFavoritas.COLUMN_NUMERO, linha.getNumero());
            values.put(SQLiteObjectsHelper.TLinhasFavoritas.COLUMN_TITULO, linha.getTitulo());
            values.put(SQLiteObjectsHelper.TLinhasFavoritas.COLUMN_SUBTITULO, linha.getSubtitulo());
            values.put(SQLiteObjectsHelper.TLinhasFavoritas.COLUMN_MUNICIPIOID, App.getMunicipio().getId());
            values.put(SQLiteObjectsHelper.TLinhasFavoritas.COLUMN_MUNICIPIONOME, App.getMunicipio().getNome());
            values.put(SQLiteObjectsHelper.TLinhasFavoritas.COLUMN_IDFIREBASE, linha.getId());
            linha.setIdSql(db.insert(SQLiteObjectsHelper.TLinhasFavoritas.TABLE_NAME, null, values));
        } else {
            StringBuilder whereClause = new StringBuilder();
            whereClause.append(SQLiteObjectsHelper.TLinhasFavoritas._ID).append(" = ? ");
            db.delete(SQLiteObjectsHelper.TLinhasFavoritas.TABLE_NAME, whereClause.toString(), new String[]{linha.getIdSql().toString()});
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void updateMunicipioAtual(Municipio novoMunicipioAtual) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        Municipio municipioAtualOld = getMunicipioAtual();
        ContentValues values = new ContentValues();
        db.beginTransaction();
        values.put(SQLiteObjectsHelper.TMunicipioAtual.COLUMN_IDFIREBASE, novoMunicipioAtual.getId());
        values.put(SQLiteObjectsHelper.TMunicipioAtual.COLUMN_MUNNOME, novoMunicipioAtual.getNome());

        if(municipioAtualOld == null) {
            novoMunicipioAtual.setIdSql(db.insert(SQLiteObjectsHelper.TMunicipioAtual.TABLE_NAME, null, values));
        } else {
            db.update(SQLiteObjectsHelper.TMunicipioAtual.TABLE_NAME, values, null, null);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static Municipio getMunicipioAtual() {
        Municipio municipioAux = null;
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(getSelectAllMunicipioAtual(), null);
        if(cursor != null) {
            cursor.moveToFirst();

            if(cursor.getCount() > 0) {
                municipioAux = new Municipio();
                municipioAux.setIdSql(cursor.getLong(0));
                municipioAux.setId(cursor.getString(1));
                municipioAux.setNome(cursor.getString(2));
                municipioAux.setEhMunicipioAtual(true);
            }

            cursor.close();
        }
        return municipioAux;
    }

    private static String getSelectAllMunicipioAtual() {
        StringBuilder sb = new StringBuilder("SELECT ").append(SQLiteObjectsHelper.TMunicipioAtual.getInstance().getColunasParaSelect()).append(" FROM ");
        sb.append(SQLiteObjectsHelper.TMunicipioAtual.TABLE_NAME).append(" MUN ");
        return sb.toString();
    }
}
