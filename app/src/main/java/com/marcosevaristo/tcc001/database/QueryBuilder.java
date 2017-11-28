package com.marcosevaristo.tcc001.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marcosevaristo.tcc001.App;
import com.marcosevaristo.tcc001.model.Linha;
import com.marcosevaristo.tcc001.model.Municipio;
import com.marcosevaristo.tcc001.utils.CollectionUtils;
import com.marcosevaristo.tcc001.utils.NumberUtils;
import com.marcosevaristo.tcc001.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            linhaAux.setMunicipio(getMunicipios(cursor.getLong(5)).get(0));
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
        sb.append(" INNER JOIN ").append(SQLiteObjectsHelper.TMunicipios.TABLE_NAME).append(" MUN ON MUN.").append(SQLiteObjectsHelper.TMunicipios._ID).append(" = LIN.")
                .append(SQLiteObjectsHelper.TLinhasFavoritas.COLUMN_MUNICIPIO);
        if(StringUtils.isNotBlank(idFirebase)) {
            sb.append(" WHERE ").append(SQLiteObjectsHelper.TLinhasFavoritas.COLUMN_IDFIREBASE).append(" = '").append(idFirebase).append("' ");
        }
        sb.append(" ORDER BY MUN.").append(SQLiteObjectsHelper.TMunicipios.COLUMN_MUNNOME).append(" ASC, ").append(SQLiteObjectsHelper.TLinhasFavoritas.COLUMN_NUMERO);
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
            values.put(SQLiteObjectsHelper.TLinhasFavoritas.COLUMN_MUNICIPIO, App.getMunicipio().getIdSql());
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
        ContentValues values = new ContentValues();
        StringBuilder whereClause = new StringBuilder();
        Municipio municipioAtualOld = getMunicipioAtual();

        values.put(SQLiteObjectsHelper.TMunicipios.COLUMN_EHMUNICIPIOATUAL, NumberUtils.INTEGER_ONE);
        whereClause.append(SQLiteObjectsHelper.TMunicipios._ID).append(" = ?");

        db.beginTransaction();
        db.update(SQLiteObjectsHelper.TMunicipios.TABLE_NAME, values, whereClause.toString(), new String[]{novoMunicipioAtual.getIdSql().toString()});

        if(municipioAtualOld != null) {
            values = new ContentValues();
            values.put(SQLiteObjectsHelper.TMunicipios.COLUMN_EHMUNICIPIOATUAL, NumberUtils.INTEGER_ZERO);

            db.update(SQLiteObjectsHelper.TMunicipios.TABLE_NAME, values, whereClause.toString(), new String[]{municipioAtualOld.getIdSql().toString()});
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
                municipioAux.setEhMunicipioAtual(NumberUtils.INTEGER_ONE.equals(cursor.getInt(3)));
            }

            cursor.close();
        }
        return municipioAux;
    }

    private static String getSelectAllMunicipioAtual() {
        StringBuilder sb = new StringBuilder("SELECT ").append(SQLiteObjectsHelper.TMunicipios.getInstance().getColunasParaSelect()).append(" FROM ");
        sb.append(SQLiteObjectsHelper.TMunicipios.TABLE_NAME).append(" MUN ");
        sb.append("WHERE MUN.").append(SQLiteObjectsHelper.TMunicipios.COLUMN_EHMUNICIPIOATUAL).append(" = 1 ");
        return sb.toString();
    }

    public static void insereMunicipios(List<Municipio> lMunicipiosAux) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        List<Municipio> municipiosGravados = getMunicipios(null);
        Map<String, Municipio> mMunicipiosAux = null;
        if(CollectionUtils.isNotEmpty(municipiosGravados)) {
            mMunicipiosAux = new HashMap<>();
            for(Municipio umMunicipioGravado : municipiosGravados) {
                mMunicipiosAux.put(umMunicipioGravado.getIdSql()+"|"+umMunicipioGravado.getNome(), umMunicipioGravado);
            }
        }

        db.beginTransaction();
        for (Municipio umMunicipio : lMunicipiosAux) {
            if(mMunicipiosAux != null && mMunicipiosAux.get(umMunicipio.getIdSql()+"|"+umMunicipio.getNome()) != null) {
                continue;
            }
            values.put(SQLiteObjectsHelper.TMunicipios.COLUMN_IDFIREBASE, umMunicipio.getId());
            values.put(SQLiteObjectsHelper.TMunicipios.COLUMN_MUNNOME, umMunicipio.getNome());
            values.put(SQLiteObjectsHelper.TMunicipios.COLUMN_EHMUNICIPIOATUAL,
                    umMunicipio.isEhMunicipioAtual() ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO);
            umMunicipio.setIdSql(db.insert(SQLiteObjectsHelper.TMunicipios.TABLE_NAME, null, values));
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static List<Municipio> getMunicipios(Long municipioID) {
        List<Municipio> lLinhas = new ArrayList<>();
        Municipio municipioAux;
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(getSelectAllMunicipios(municipioID), null);
        if(cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                municipioAux = new Municipio();
                municipioAux.setIdSql(cursor.getLong(0));
                municipioAux.setId(cursor.getString(1));
                municipioAux.setNome(cursor.getString(2));
                municipioAux.setEhMunicipioAtual(NumberUtils.INTEGER_ONE.equals(cursor.getInt(3)));
                lLinhas.add(municipioAux);
                cursor.moveToNext();
            }

            cursor.close();
        }
        return lLinhas;
    }

    private static String getSelectAllMunicipios(Long municipioID) {
        StringBuilder sb = new StringBuilder("SELECT ").append(SQLiteObjectsHelper.TMunicipios.getInstance().getColunasParaSelect()).append(" FROM ");
        sb.append(SQLiteObjectsHelper.TMunicipios.TABLE_NAME).append(" MUN ");
        if(municipioID != null) {
            sb.append(" WHERE ").append(SQLiteObjectsHelper.TMunicipios._ID).append(" = ").append(municipioID.toString());
        }
        sb.append(" ORDER BY ").append(SQLiteObjectsHelper.TMunicipios.COLUMN_MUNNOME).append(" ASC ");
        return sb.toString();
    }
}
