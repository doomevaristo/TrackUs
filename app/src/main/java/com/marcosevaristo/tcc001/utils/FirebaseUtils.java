package com.marcosevaristo.tcc001.utils;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.tcc001.model.Linha;

import java.util.List;

public class FirebaseUtils {

    private static FirebaseDatabase database;
    private static DatabaseReference databaseReferenceLinhas;

    public static void startReferenceLinhas() {
        if(databaseReferenceLinhas == null) {
            databaseReferenceLinhas = getDatabase().getReference().child("linhas");
        }
}

    public static FirebaseDatabase getDatabase() {
        if(database == null) {
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }

    public static DatabaseReference getLinhasReference() {
        return databaseReferenceLinhas;
    }
}
