package com.marcosevaristo.tcc001;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

import com.marcosevaristo.tcc001.database.SQLiteHelper;
import com.marcosevaristo.tcc001.model.Municipio;

public class App extends Application {
    private static Context context;
    private static SQLiteHelper sqLiteHelper;
    private static Municipio municipio;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sqLiteHelper = SQLiteHelper.getInstance(context);
    }

    public static Context getAppContext() {
        return App.context;
    }

    public static SQLiteHelper getSqLiteHelper() {
        return sqLiteHelper;
    }

    public static Municipio getMunicipio() {
        return municipio;
    }
    public static void setMunicipio(Municipio municipio) {
        App.municipio = municipio;
    }
}
