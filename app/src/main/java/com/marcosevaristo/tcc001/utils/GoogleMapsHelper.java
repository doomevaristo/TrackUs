package com.marcosevaristo.tcc001.utils;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapsHelper {

    private static final String URL_SEARCH_ROUTE = "https://maps.googleapis.com/maps/api/directions/json?origin=*srcParam*&destination=*destParam*&sensor=false&units=metric&mode=driving";

    public static String getUrlSearchRoute(String srcParam, String destParam) {
        return URL_SEARCH_ROUTE.replace("*srcParam*", srcParam).replace("*destParam*", destParam);
    }

    public static PolylineOptions desenhaRota(ArrayList<LatLng> points) {
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        PolylineOptions polylineOpt = new PolylineOptions();
        for (LatLng latlng : points) {
            polylineOpt.add(latlng);
        }
        polylineOpt.color(Color.BLUE);
        polylineOpt.width(12);

        return polylineOpt;
    }

    public static String getLatLngToString(LatLng latLng) {
        return latLng != null ? latLng.latitude + "," + latLng.longitude : "";
    }

    public static List<LatLng> getListLatLngFromListString(List<String> lLatLngStr) {
        List<LatLng> lLatLngAux = new ArrayList<>();
        for(String umLatLngStr : lLatLngStr) {
            lLatLngAux.add(new LatLng(Double.valueOf(umLatLngStr.substring(0,umLatLngStr.indexOf(","))),
                    Double.valueOf(umLatLngStr.substring(umLatLngStr.indexOf(",")+1, umLatLngStr.length()))));
        }
        return lLatLngAux;
    }

    public static Float calculaDistanciaAproximadaMetros(double latitudeAtual, double longitudeAtual, Marker marker) {
        float[] distanciaArr = new float[1];
        Location.distanceBetween(marker.getPosition().latitude, marker.getPosition().longitude, latitudeAtual, longitudeAtual, distanciaArr);
        return distanciaArr[0];
    }


}
