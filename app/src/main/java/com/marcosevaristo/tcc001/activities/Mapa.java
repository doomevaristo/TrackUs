package com.marcosevaristo.tcc001.activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.model.Carro;
import com.marcosevaristo.tcc001.model.Linha;
import com.marcosevaristo.tcc001.utils.FirebaseUtils;

import java.util.Map;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Linha linha;
    Query queryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setaCarrosNoMapa(getIntent().getExtras(), googleMap);
    }

    private void setaCarrosNoMapa(Bundle params, GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        linha = (Linha) params.get("linha");
        if(linha != null) {
            queryRef = FirebaseUtils.getLinhasReference().child(linha.getNumero()).child("carros");
            ValueEventListener evento = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map mapCarros = (Map) dataSnapshot.getValue();
                    if(mapCarros != null) {
                        for(Object umaKey : mapCarros.keySet()) {
                            Carro umCarro = (Carro) mapCarros.get(umaKey);
                            LatLng posicaoUmCarro = new LatLng(Integer.getInteger(umCarro.getLatitude()), Integer.getInteger(umCarro.getLongitude()));
                            mMap.addMarker(new MarkerOptions().position(posicaoUmCarro).title(linha.toString()));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            queryRef.addValueEventListener(evento);
        }
    }
}
