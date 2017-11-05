package com.marcosevaristo.tcc001.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.database.DatabaseReference;
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

    public static List<Linha> getLinhas(String nroLinha) {
        List<Linha> lLinhas = new ArrayList<>();
        Linha linhaAux;
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(getSelectAllLinhas(nroLinha), null);
        if(cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                linhaAux = new Linha();
                linhaAux.setIdSql(cursor.getLong(0));
                linhaAux.setNumero(cursor.getString(1));
                linhaAux.setTitulo(cursor.getString(2));
                linhaAux.setSubtitulo(cursor.getString(3));
                linhaAux.setMunicipio(new Municipio(cursor.getLong(4)));
                linhaAux.setEhFavorito(NumberUtils.INTEGER_ONE.equals(cursor.getInt(5)));
                lLinhas.add(linhaAux);
                cursor.moveToNext();
            }

            cursor.close();
        }

        return lLinhas;
    }

    private static String getSelectAllLinhas(String nroLinha) {
        StringBuilder sb = new StringBuilder("SELECT ").append(SQLiteObjectsHelper.TLinhas.getInstance().getColunasParaSelect()).append(" FROM ");
        sb.append(SQLiteObjectsHelper.TLinhas.TABLE_NAME).append(" LIN ");
        if(StringUtils.isNotBlank(nroLinha)) {
            sb.append(" WHERE ").append(SQLiteObjectsHelper.TLinhas.COLUMN_NUMERO).append(" LIKE '%").append(nroLinha).append("%' ");
        }
        sb.append(" ORDER BY ").append(SQLiteObjectsHelper.TLinhas.COLUMN_NUMERO).append(" DESC");
        return sb.toString();
    }

    public static List<Linha> getFavoritos(String nroLinha) {
        List<Linha> lLinhas = new ArrayList<>();
        Linha linhaAux;
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(getSelectAllFavoritos(nroLinha), null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        for (int i = 0; i < cursor.getCount(); i++) {
            linhaAux = new Linha();
            linhaAux.setIdSql(cursor.getLong(0));
            linhaAux.setNumero(cursor.getString(1));
            linhaAux.setTitulo(cursor.getString(2));
            linhaAux.setSubtitulo(cursor.getString(3));
            linhaAux.setMunicipio(new Municipio(cursor.getLong(4)));
            linhaAux.setEhFavorito(true);
            lLinhas.add(linhaAux);
            cursor.moveToNext();
        }

        cursor.close();
        return lLinhas;
    }

    private static String getSelectAllFavoritos(String nroLinha) {
        StringBuilder sb = new StringBuilder("SELECT ").append(SQLiteObjectsHelper.TLinhas.getInstance().getColunasParaSelect()).append(" FROM ");
        sb.append(SQLiteObjectsHelper.TLinhas.TABLE_NAME).append(" LIN ");
        sb.append("WHERE LIN.").append(SQLiteObjectsHelper.TLinhas.COLUMN_EHFAVORITA).append(" = 1 ");
        if(StringUtils.isNotBlank(nroLinha)) {
            sb.append(" AND ").append(SQLiteObjectsHelper.TLinhas.COLUMN_NUMERO).append(" = '").append(nroLinha).append("' ");
        }
        sb.append(" ORDER BY ").append(SQLiteObjectsHelper.TLinhas.COLUMN_NUMERO).append(" DESC");
        return sb.toString();
    }

    public static void updateFavorito(Linha linha) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SQLiteObjectsHelper.TLinhas.COLUMN_EHFAVORITA, linha.ehFavorito() ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO);

        StringBuilder whereClause = new StringBuilder();
        whereClause.append(SQLiteObjectsHelper.TLinhas._ID).append(" = ?");
        db.beginTransaction();
        db.update(SQLiteObjectsHelper.TLinhas.TABLE_NAME, values, whereClause.toString(), new String[]{linha.getIdSql().toString()});

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
                municipioAux.setId(cursor.getLong(0));
                municipioAux.setNome(cursor.getString(1));
            }

            cursor.close();
        }
        return municipioAux;
    }

    private static String getSelectAllMunicipioAtual() {
        StringBuilder sb = new StringBuilder("SELECT ").append(SQLiteObjectsHelper.TMunicipios.getInstance().getColunasParaSelect()).append(" FROM ");
        sb.append(SQLiteObjectsHelper.TMunicipioAtual.TABLE_NAME).append(" MUA ");
        sb.append(" INNER JOIN ").append(SQLiteObjectsHelper.TMunicipios.TABLE_NAME).append(" MUN ON MUN.");
        sb.append(SQLiteObjectsHelper.TMunicipios._ID).append(" = MUA.").append(SQLiteObjectsHelper.TMunicipioAtual.COLUMN_MUNICIPIOID);
        return sb.toString();
    }

    public static void insereLinhas(List<Linha> lLinhas) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        List<Linha> linhasGravadas = getLinhas(null);
        Map<String, Linha> mLinhasAux = null;
        if(CollectionUtils.isNotEmpty(linhasGravadas)) {
            mLinhasAux = new HashMap<>();
            for(Linha umaLinhaGravada : linhasGravadas) {
                mLinhasAux.put(umaLinhaGravada.getNumero()+"|"+umaLinhaGravada.getMunicipio().getId(), umaLinhaGravada);
            }
        }

        ContentValues values = new ContentValues();
        db.beginTransaction();
        for (Linha umaLinha : lLinhas) {
            if(mLinhasAux != null && mLinhasAux.get(umaLinha.getNumero()+"|"+umaLinha.getMunicipio().getId()) != null) {
                continue;
            }
            values.put(SQLiteObjectsHelper.TLinhas.COLUMN_NUMERO, umaLinha.getNumero());
            values.put(SQLiteObjectsHelper.TLinhas.COLUMN_TITULO, umaLinha.getTitulo());
            values.put(SQLiteObjectsHelper.TLinhas.COLUMN_SUBTITULO, umaLinha.getSubtitulo());
            values.put(SQLiteObjectsHelper.TLinhas.COLUMN_MUNICIPIO, App.getMunicipio().getId());
            umaLinha.setIdSql(db.insert(SQLiteObjectsHelper.TLinhas.TABLE_NAME, null, values));
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void insereMunicipios(List<Municipio> lMunicipiosAux) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        List<Municipio> municipiosGravados = getMunicipios(null);
        Map<String, Municipio> mMunicipiosAux = null;
        if(CollectionUtils.isNotEmpty(municipiosGravados)) {
            mMunicipiosAux = new HashMap<>();
            for(Municipio umMunicipioGravado : municipiosGravados) {
                mMunicipiosAux.put(umMunicipioGravado.getId()+"|"+umMunicipioGravado.getNome(), umMunicipioGravado);
            }
        }

        db.beginTransaction();
        for (Municipio umMunicipio : lMunicipiosAux) {
            if(mMunicipiosAux != null && mMunicipiosAux.get(umMunicipio.getId()+"|"+umMunicipio.getNome()) != null) {
                continue;
            }
            values.put(SQLiteObjectsHelper.TMunicipios._ID, umMunicipio.getId());
            values.put(SQLiteObjectsHelper.TMunicipios.COLUMN_MUNNOME, umMunicipio.getNome());
            umMunicipio.setId(db.insert(SQLiteObjectsHelper.TMunicipios.TABLE_NAME, null, values));
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
                municipioAux.setId(cursor.getLong(0));
                municipioAux.setNome(cursor.getString(1));
                if(App.getMunicipio() != null) {
                    municipioAux.setEhMunicipioAtual(App.getMunicipio().getId().equals(municipioAux.getId()));
                }
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
        sb.append(" ORDER BY ").append(SQLiteObjectsHelper.TMunicipios.COLUMN_MUNNOME).append(" DESC");
        return sb.toString();
    }
}
