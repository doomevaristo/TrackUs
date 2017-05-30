package com.marcosevaristo.tcc001;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.marcosevaristo.tcc001.utils.SQLiteHelper;

public class App extends Application {
    private static Context context;
    private static SQLiteHelper sqLiteHelper;

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
}
