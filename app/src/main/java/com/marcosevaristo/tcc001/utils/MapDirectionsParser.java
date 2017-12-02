package com.marcosevaristo.tcc001.utils;

import com.marcosevaristo.tcc001.model.DistanceObject;
import com.marcosevaristo.tcc001.model.DurationObject;
import com.marcosevaristo.tcc001.model.StepsObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapDirectionsParser {

    private static MapDirectionsParser instance;

    public static MapDirectionsParser getInstance() {
        if(instance == null) {
            instance = new MapDirectionsParser();
        }
        return instance;
    }

    public StepsObject parse(JSONObject jObject) {
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        StepsObject stepsObject = new StepsObject();
        try {
            jRoutes = jObject.getJSONArray("routes");

            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                for (int j = 0; j < jLegs.length(); j++) {
                    String durationText = "";
                    String durationValue = "";
                    String distanceText = "";
                    String distanceValue = "";

                    durationText = (String) ((JSONObject) ((JSONObject) jLegs
                            .get(j)).get("duration")).get("text");
                    durationValue = (String) ((JSONObject) ((JSONObject) jLegs
                            .get(j)).get("duration")).get("value");

                    stepsObject.setDuration(new DurationObject(durationText, Integer.parseInt(durationValue)));

                    distanceText = (String) ((JSONObject) ((JSONObject) jLegs
                            .get(j)).get("distance")).get("text");
                    distanceValue = (String) ((JSONObject) ((JSONObject) jLegs
                            .get(j)).get("distance")).get("value");

                    stepsObject.setDistance(new DistanceObject(distanceText, Integer.parseInt(distanceValue)));
                    stepsObject.setLocation((String) ((JSONObject) jLegs.get(j)).get("end_address"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stepsObject;
    }
}
