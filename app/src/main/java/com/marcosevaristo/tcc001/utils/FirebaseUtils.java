package com.marcosevaristo.tcc001.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    private static FirebaseDatabase database;

    private static final String NODE_MUNICIPIOS = "municipios";
    private static final String NODE_LINHAS = "linhas";

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
}
