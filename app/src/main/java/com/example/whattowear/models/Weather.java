package com.example.whattowear.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Weather {
    public static final String TAG = "Weather";

    private List<Forecast> hourlyForecast;
    private Forecast currentForecast;
    private Conditions dayConditions;

    public Weather() {
        hourlyForecast = new ArrayList<>();
    }

    public void loadFromJson(JSONObject jsonObject) throws JSONException {
        hourlyForecast.clear();
        hourlyForecast.addAll(Forecast.fromJsonArray(jsonObject.getJSONArray("hourly")));

        // TODO: fill up currentForecast and dayConditions, when needed
        // TODO: if not, delete them
    }

    public List<Forecast> getHourlyForecast() {
        return hourlyForecast;
    }
}

