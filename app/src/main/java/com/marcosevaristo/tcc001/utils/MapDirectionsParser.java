package com.marcosevaristo.tcc001.utils;

import com.marcosevaristo.tcc001.model.DistanceObject;
import com.marcosevaristo.tcc001.model.DurationObject;
import com.marcosevaristo.tcc001.model.StepsObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                for (int j = 0; j < jLegs.length(); j++) {
                    String durationText;
                    Integer durationValue;
                    String distanceText;
                    Integer distanceValue;

                    durationText = (String) ((JSONObject) ((JSONObject) jLegs
                            .get(j)).get("duration")).get("text");
                    durationValue = (Integer) ((JSONObject) ((JSONObject) jLegs
                            .get(j)).get("duration")).get("value");

                    stepsObject.setDuration(new DurationObject(durationText, durationValue));

                    distanceText = (String) ((JSONObject) ((JSONObject) jLegs
                            .get(j)).get("distance")).get("text");
                    distanceValue = (Integer) ((JSONObject) ((JSONObject) jLegs
                            .get(j)).get("distance")).get("value");

                    stepsObject.setDistance(new DistanceObject(distanceText, distanceValue));
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
