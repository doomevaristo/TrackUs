package com.marcosevaristo.tcc001.activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

import java.util.List;
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
        linha = (Linha) params.get("linha");
        if(linha != null) {
            queryRef = FirebaseUtils.getLinhasReference().child(linha.getNumero()).child("carros");
            ValueEventListener evento = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map mapCarros = (Map) dataSnapshot.getValue();
                    if(mapCarros != null) {
                        List<Carro> lCarros = Carro.converteMapParaListCarros(mapCarros);
                        for(Carro umCarro : lCarros) {
                            Double latitude = Double.parseDouble(umCarro.getLatitude());
                            Double longitude = Double.parseDouble(umCarro.getLongitude());
                            Double latitudeBlumenau = -26.9053897;
                            Double longitudeBlumenau = -49.0935486;
                            LatLng posicaoUmCarro = new LatLng(latitude, longitude);
                            MarkerOptions marker = new MarkerOptions().position(posicaoUmCarro).title(linha.toString());
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bus_marker));
                            mMap.addMarker(marker);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudeBlumenau, longitudeBlumenau), 14.0f));
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
