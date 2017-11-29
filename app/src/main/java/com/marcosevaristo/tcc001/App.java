package com.marcosevaristo.tcc001;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.marcosevaristo.tcc001.database.SQLiteHelper;
import com.marcosevaristo.tcc001.model.Municipio;

public class App extends Application {
    private static Context context;
    private static SQLiteHelper sqLiteHelper;
    private static Municipio municipio;
    private static ProgressDialog progressDialog;

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

    public static void toast(int stringID, String... params) {
        Toast.makeText(context, context.getString(stringID, params), Toast.LENGTH_SHORT).show();
    }

    public static void showLoadingDialog(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(App.getAppContext().getString(R.string.carregando));
        progressDialog.show();
    }

    public static void hideLoadingDialog() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
