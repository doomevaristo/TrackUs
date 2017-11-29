package com.marcosevaristo.tcc001.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marcosevaristo.tcc001.App;

public class FirebaseUtils {

    private static FirebaseDatabase database;

    public static final String NODE_MUNICIPIOS = "municipios";
    public static final String NODE_LINHAS = "linhas";
    public static final String NODE_CARROS = "carros";

    public static FirebaseDatabase getDatabase() {
        if(database == null) {
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }

    public static DatabaseReference getMunicipiosReference(String municipioID) {
        DatabaseReference databaseReferenceMunicipios = getDatabase().getReference().child(NODE_MUNICIPIOS);
        if(StringUtils.isNotBlank(municipioID)) {
            databaseReferenceMunicipios = databaseReferenceMunicipios.child(municipioID);
        }
        return databaseReferenceMunicipios;
    }

    public static DatabaseReference getLinhasReference(String municipioID, String linhaID) {
        DatabaseReference databaseReferenceLinhas = getMunicipiosReference(municipioID).child(NODE_LINHAS);
        if(StringUtils.isNotBlank(linhaID)) {
            databaseReferenceLinhas = databaseReferenceLinhas.child(linhaID);
        }
        return databaseReferenceLinhas;
    }

    public static DatabaseReference getCarrosReference(String municipioID, String linhaID, String carroID) {
        DatabaseReference databaseReferenceCarros = null;
        if(StringUtils.isNotBlank(linhaID)) {
            databaseReferenceCarros = getLinhasReference(municipioID, linhaID).child(NODE_CARROS);
            if(StringUtils.isNotBlank(carroID)) {
                databaseReferenceCarros = databaseReferenceCarros.child(carroID);
            }
        }
        return databaseReferenceCarros;
    }
}
