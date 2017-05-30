package com.marcosevaristo.tcc001.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.marcosevaristo.tcc001.database.DatabaseObjectsHelper;
import com.marcosevaristo.tcc001.database.QueryBuilder;
import com.marcosevaristo.tcc001.model.Linha;

import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static SQLiteHelper sInstance;
    private static int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FAVORITOS";

    public static synchronized SQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseObjectsHelper.TFavoritos.getCreateEntry());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {

    }
}
