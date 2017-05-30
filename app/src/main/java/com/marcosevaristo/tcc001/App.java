package com.marcosevaristo.tcc001;

import android.app.Application;
import android.content.Context;

import com.marcosevaristo.tcc001.database.SQLiteHelper;

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
