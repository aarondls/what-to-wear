package com.example.whattowear.models;

import android.location.Location;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Weather {
    // Weather is a singleton class

    public static final String TAG = "Weather";

    private static Weather weather = new Weather();

    private static Location lastLocation;
    private static String lastLocationName;
    private static String weatherUnits;

    private static List<Forecast> hourlyForecast;
    private static Forecast currentForecast;
    private static Conditions dayConditions;

    // private to ensure that only the class can instantiate itself
    private Weather() {
        hourlyForecast = new ArrayList<>();

        // set default location to null
        lastLocation = null;

        // TODO: Change this to get from Parse database
        weatherUnits = "imperial";
    }

    public static void loadFromJson(JSONObject jsonObject) throws JSONException {
        hourlyForecast.clear();
        hourlyForecast.addAll(Forecast.fromJsonArray(jsonObject.getJSONArray("hourly")));

        currentForecast = Forecast.fromJson(jsonObject.getJSONObject("current"));

        // TODO: fill up currentForecast and dayConditions, when needed
        // TODO: if not, delete them
    }

    public static List<Forecast> getHourlyForecast() {
        return hourlyForecast;
    }

    public static Forecast getCurrentForecast() {
        return currentForecast;
    }

    public static void setLastLocation(Location lastLocation) {
        Weather.lastLocation = lastLocation;
    }

    public static void setLastLocationName(String lastLocationName) {
        Weather.lastLocationName = lastLocationName;
    }

    public static Location getLastLocation() {
        return lastLocation;
    }

    public static String getLastLocationName() {
        return lastLocationName;
    }

    public static String getWeatherUnits() {
        return weatherUnits;
    }
}

