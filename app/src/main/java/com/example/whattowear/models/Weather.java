package com.example.whattowear.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Weather {
    // Weather is a singleton class

    public static final String TAG = "Weather";

    private static Weather weather = new Weather();

    private static List<Forecast> hourlyForecast;
    private static Forecast currentForecast;
    private static Conditions dayConditions;

    // private to ensure that only the class can instantiate itself
    private Weather() {
        hourlyForecast = new ArrayList<>();
    }

    public static void loadFromJson(JSONObject jsonObject) throws JSONException {
        hourlyForecast.clear();
        hourlyForecast.addAll(Forecast.fromJsonArray(jsonObject.getJSONArray("hourly")));

        // TODO: fill up currentForecast and dayConditions, when needed
        // TODO: if not, delete them
    }

    public static List<Forecast> getHourlyForecast() {
        return hourlyForecast;
    }
}

