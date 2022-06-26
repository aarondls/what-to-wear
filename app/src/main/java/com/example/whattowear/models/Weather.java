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

    private static double lastLocationLatitude;
    private static double lastLocationLongitude;
    private static String lastLocationName;
    private static String weatherUnits;

    private static List<Forecast> hourlyForecast;
    private static Forecast currentForecast;
    private static Conditions dayConditions;

    // private to ensure that only the class can instantiate itself
    private Weather() {
        hourlyForecast = new ArrayList<>();

        // Set last location name to be empty, which can be used to check if there exists a current location
        lastLocationName = "";

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

    public static void setLastLocationLatitude(double lastLocationLatitude) {
        Weather.lastLocationLatitude = lastLocationLatitude;
    }

    public static void setLastLocationLongitude(double lastLocationLongitude) {
        Weather.lastLocationLongitude = lastLocationLongitude;
    }

    public static void setLastLocationName(String lastLocationName) {
        Weather.lastLocationName = lastLocationName;
    }

    public static double getLastLocationLatitude() {
        return lastLocationLatitude;
    }

    public static double getLastLocationLongitude() {
        return lastLocationLongitude;
    }

    public static String getLastLocationName() {
        return lastLocationName;
    }

    public static String getWeatherUnits() {
        return weatherUnits;
    }
}

