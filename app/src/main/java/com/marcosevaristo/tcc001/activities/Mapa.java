package com.marcosevaristo.tcc001.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.tcc001.App;
import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.model.Carro;
import com.marcosevaristo.tcc001.model.Linha;
import com.marcosevaristo.tcc001.model.StepsObject;
import com.marcosevaristo.tcc001.utils.CollectionUtils;
import com.marcosevaristo.tcc001.utils.FirebaseUtils;
import com.marcosevaristo.tcc001.utils.GoogleMapsHelper;
import com.marcosevaristo.tcc001.utils.MapDirectionsParser;
import com.marcosevaristo.tcc001.utils.StringUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener{

    private GoogleMap gMap;
    private List<Marker> lMarker;
    private Linha linha;
    private boolean permitiuLocalizacao;
    private Map<Carro, Marker> mCarrosMarkers;

    private TextView carroInfoDistancia, carroInfoTempo;
    private Animation carroInfoOpen,carroInfoClose;
    private RelativeLayout carroInfoLayout;
    private boolean isCarroInfoOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        linha = (Linha) getIntent().getExtras().get("linha");
        setupToolbar();
        permitiuLocalizacao = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if(!permitiuLocalizacao) {
            verificaPermissoes();
        } else {
            solicitaAtivarLocalizacao();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMarkerClickListener(this);
        gMap.setOnMapClickListener(this);
        try{
            gMap.setMyLocationEnabled(permitiuLocalizacao);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        setaObjetosIniciaisNoMapa();
        moveCameraParaMunicipio();
    }

    private void setupToolbar() {
        ((Toolbar) findViewById(R.id.toolbar_map)).setTitle("");
        TextView textViewLinhaTitulo = (TextView) findViewById(R.id.linhaMapaText);
        TextView textViewLinhaSubTitulo = (TextView) findViewById(R.id.linhaMapaSubText);

        textViewLinhaTitulo.setText(linha.toStringSemSubtitulo());
        textViewLinhaSubTitulo.setText(linha.getSubtitulo());
    }

    private void setaObjetosIniciaisNoMapa() {
        setaCarroInfoObjects();
        if (linha != null) {
            FirebaseUtils.getLinhasReference(App.getMunicipio().getId(), linha.getId()).addValueEventListener(getEventoFirebaseGetLinha());
        }
    }

    private void setaCarroInfoObjects() {
        carroInfoLayout = (RelativeLayout) findViewById(R.id.carroInfoLayout);
        carroInfoOpen = AnimationUtils.loadAnimation(App.getAppContext(), R.anim.carroinfo_open);
        carroInfoClose = AnimationUtils.loadAnimation(App.getAppContext(), R.anim.carroinfo_close);
        carroInfoDistancia = (TextView) findViewById(R.id.textDistancia);
        carroInfoTempo = (TextView) findViewById(R.id.textTempo);
    }

    private void moveCameraParaMunicipio() {
        try {
            Geocoder gc = new Geocoder(this);
            List<Address> addresses = gc.getFromLocationName(linha.getMunicipio().getNome(), 1);

            if(CollectionUtils.isNotEmpty(addresses)) {
                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        gMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                        gMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(a.getLatitude(), a.getLongitude())));
                    }
                }
            }
        } catch (IOException e) {
            App.toast(R.string.nao_achou_municipio_no_mapa, linha.getMunicipio().getNome());
        }
    }

    @SuppressWarnings("unchecked")
    private ValueEventListener getEventoFirebaseGetLinha() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot umDataSnapshot : dataSnapshot.getChildren()) {
                        if(umDataSnapshot.getKey().equals("carros")) {
                            mCarrosMarkers = new HashMap<>();
                            for(DataSnapshot carroDataSnapshot : umDataSnapshot.getChildren()) {
                                String id = carroDataSnapshot.getKey();
                                String latitude = StringUtils.toStringSecure(carroDataSnapshot.child("latitude").getValue());
                                String longitude = StringUtils.toStringSecure(carroDataSnapshot.child("longitude").getValue());
                                String location = StringUtils.toStringSecure(carroDataSnapshot.child("location").getValue());
                                mCarrosMarkers.put(new Carro(id, latitude, longitude, location), null);
                            }
                            linha.setCarros(new ArrayList<>(mCarrosMarkers.keySet()));
                        }
                        if(CollectionUtils.isEmpty(linha.getRota()) && umDataSnapshot.getKey().equals("rota")) {
                            linha.setRota((List<String>) umDataSnapshot.getValue());
                            gMap.addPolyline(GoogleMapsHelper.desenhaRota((ArrayList<LatLng>) GoogleMapsHelper.getListLatLngFromListString(linha.getRota())));
                        }
                    }

                    if(CollectionUtils.isNotEmpty(lMarker)) {
                        removeMarkers();
                    }
                    populaMarkersDoMapaComCarros(linha.getCarros());
                }
                if(isCarroInfoOpen) animateCarroInfo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        };
    }

    private void removeMarkers() {
        for(Marker umMarker : lMarker) {
            umMarker.remove();
        }
    }

    private void populaMarkersDoMapaComCarros(List<Carro> lCarros) {
        lMarker = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(lCarros)) {
            for(Carro umCarro : lCarros) {
                LatLng posicaoUmCarro = new LatLng(Double.parseDouble(umCarro.getLatitude()), Double.parseDouble(umCarro.getLongitude()));
                MarkerOptions umMarker = new MarkerOptions()
                        .position(posicaoUmCarro)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bus_marker));
                lMarker.add(gMap.addMarker(umMarker));
            }
        }
    }

    private void solicitaAtivarLocalizacao() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(App.getAppContext())
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(App.getAppContext().getString(R.string.title_activity_mapa), App.getAppContext().getString(R.string.permissoes_de_localizacao_ja_concedidas));
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(App.getAppContext().getString(R.string.title_activity_mapa), App.getAppContext().getString(R.string.solicitando_permissoes));

                        try {
                            status.startResolutionForResult(Mapa.this, 0x1);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(App.getAppContext().getString(R.string.title_activity_mapa), App.getAppContext().getString(R.string.aguardando_permissao_localizacao));
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(App.getAppContext().getString(R.string.title_activity_mapa), App.getAppContext().getString(R.string.permissoes_de_localizacao_inadequadas));
                        break;
                    case LocationSettingsStatusCodes.CANCELED:
                        App.toast(R.string.localizacao_necessaria);
                }
            }
        });
    }

    public void verificaPermissoes() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Titulo teste")
                        .setMessage("Mensagem teste")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(Mapa.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permitiuLocalizacao = ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                }
                if(permitiuLocalizacao) {
                    solicitaAtivarLocalizacao();
                } else {
                    verificaPermissoes();
                }
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(permitiuLocalizacao) {
            if(!isCarroInfoOpen) {
                animateCarroInfo();
            }
            new CarregaCarroInfo(marker).execute();
        }
        return false;
    }



    public void animateCarroInfo(){
        if(isCarroInfoOpen){
            carroInfoLayout.startAnimation(carroInfoClose);
            carroInfoDistancia.setText(StringUtils.emptyString());
            carroInfoTempo.setText(StringUtils.emptyString());

            isCarroInfoOpen = false;
        } else {
            carroInfoLayout.startAnimation(carroInfoOpen);
            isCarroInfoOpen = true;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(isCarroInfoOpen) {
            animateCarroInfo();
        }
    }

    public class CarregaCarroInfo extends AsyncTask<Void, Void, Void> {

        private Marker carroMarker;

        CarregaCarroInfo(Marker carroMarker) {
            this.carroMarker = carroMarker;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                FusedLocationProviderClient mFusedLocationClient = new FusedLocationProviderClient(Mapa.this);
                mFusedLocationClient.getLastLocation().addOnSuccessListener(Mapa.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            setaCarroInfo(location.getLatitude(), location.getLongitude(), carroMarker);
                        }
                    }
                });
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            App.hideLoadingDialog();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            App.showLoadingDialog(Mapa.this);
        }

        private void setaCarroInfo(double latitudeAtual, double longitudeAtual, final Marker marker) {
            String posicaoCarroStr = String.valueOf(marker.getPosition().latitude) + "," + String.valueOf(marker.getPosition().longitude);
            String posicaoAtualStr = String.valueOf(latitudeAtual) + "," + String.valueOf(longitudeAtual);
            String url = GoogleMapsHelper.getUrlSearchRoute(posicaoCarroStr, posicaoAtualStr);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MapDirectionsParser parser = new MapDirectionsParser();
                            StepsObject stepsObject = parser.parse(response);
                            if(stepsObject != null) {
                                if(stepsObject.getDistance() != null && StringUtils.isNotBlank(stepsObject.getDistance().getText())){
                                    carroInfoDistancia.setText(stepsObject.getDistance().getText());
                                }
                                if(stepsObject.getDuration() != null && StringUtils.isNotBlank(stepsObject.getDuration().getText())) {
                                    carroInfoTempo.setText(stepsObject.getDuration().getText());
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            App.hideLoadingDialog();
                        }
                    });
            App.addToReqQueue(jsonObjectRequest);
        }
    }
}
