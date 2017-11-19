package com.marcosevaristo.tcc001.utils;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapsUtils {

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

    public static List<LatLng> getListLatLngFromListString(List<String> lLatLngStr) {
        List<LatLng> lLatLngAux = new ArrayList<>();
        for(String umLatLngStr : lLatLngStr) {
            lLatLngAux.add(new LatLng(Double.valueOf(umLatLngStr.substring(0,umLatLngStr.indexOf(","))),
                    Double.valueOf(umLatLngStr.substring(umLatLngStr.indexOf(",")+1, umLatLngStr.length()))));
        }
        return lLatLngAux;
    }
}
