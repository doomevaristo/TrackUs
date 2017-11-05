package com.marcosevaristo.tcc001.utils;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.tcc001.App;
import com.marcosevaristo.tcc001.model.Linha;

import java.util.List;

public class FirebaseUtils {

    private static FirebaseDatabase database;
    private static DatabaseReference databaseReferenceMunicipios;
    private static DatabaseReference databaseReferenceLinhas;

    private static final String NODE_MUNICIPIOS = "municipios";
    private static final String NODE_LINHAS = "linhas";

    public static void startReferenceLinhas() {
        databaseReferenceLinhas = getDatabase().getReference().child(NODE_MUNICIPIOS)
                .child(App.getMunicipio().getId().toString()).child(NODE_LINHAS);
    }

    public static void startReferenceMunicipios() {
        databaseReferenceMunicipios = getDatabase().getReference().child(NODE_MUNICIPIOS);
    }

    public static FirebaseDatabase getDatabase() {
        if(database == null) {
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }

    public static DatabaseReference getLinhasReference() {
        if(databaseReferenceLinhas == null) startReferenceLinhas();
        return databaseReferenceLinhas;
    }

    public static DatabaseReference getMunicipiosReference() {
        if(databaseReferenceMunicipios == null) startReferenceMunicipios();
        return databaseReferenceMunicipios;
    }
}
