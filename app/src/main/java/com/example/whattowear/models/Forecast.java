package com.example.whattowear.models;

import static java.lang.Math.min;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Forecast {
    public static final String TAG = "Forecast";
    private static final String DEG_SIGN = "\u00B0";
    private static final String PERCENT_SIGN = "%";
    private static final String FEELS_LIKE_SENTENCE = "But feels like: ";
    private static final String TWO_DECIMAL_PLACES_FORMAT = "%.2f";

    // TODO: Remove fields that may not be used once clothing selection logic is completed
    private Date dt;
    private int temp;
    private int feelsLike;
    private int humidity;
    private double uvIndex;
    private int clouds;
    private double windSpeed;
    private int probOfPrecipitation;
    private int rainHourVol;
    private int snowHourVol;
    private Conditions hourCondition;

    public static Forecast fromJson(JSONObject jsonObject) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.dt = new Date(Long.parseLong(jsonObject.getString("dt")) * 1000);
        forecast.temp = jsonObject.getInt("temp");
        forecast.feelsLike = jsonObject.getInt("feels_like");
        forecast.humidity = jsonObject.getInt("humidity");
        forecast.uvIndex = jsonObject.getDouble("uvi");
        forecast.clouds = jsonObject.getInt("clouds");
        forecast.windSpeed = jsonObject.getDouble("wind_speed");

        if (jsonObject.has("pop")) {
            forecast.probOfPrecipitation = jsonObject.getInt("pop");
        } else {
            forecast.probOfPrecipitation = -1;
        }

        forecast.hourCondition = Conditions.fromJson(jsonObject.getJSONArray("weather").getJSONObject(0));

        if (jsonObject.has("rain")) {
            forecast.rainHourVol = jsonObject.getJSONObject("rain").getInt("1h");
        } else {
            forecast.rainHourVol = 0;
        }

        if (jsonObject.has("snow")) {
            forecast.snowHourVol = jsonObject.getJSONObject("snow").getInt("1h");
        } else {
            forecast.snowHourVol = 0;
        }

        return forecast;
    }

    public static List<Forecast> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Forecast> forecasts = new ArrayList<>();
        // Limit hours to display at most only a day from current hour
        int num_hours_limit = min(jsonArray.length(), 24);
        for (int i=0; i<num_hours_limit; i++) {
            forecasts.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return forecasts;
    }

    public Date getDt() {
        return dt;
    }

    /**
     * Converts Date into hour only AM/PM time
     * @return the String representation of the hour of the Date
     */
    public String getAMPMTime() {
        int hours = dt.getHours();

        if (hours == 0) {
            return "12AM";
        } else if (hours == 12) {
            return hours + "PM";
        } else if (hours < 12) {
            return hours + "AM";
        } else {
            return hours-12 + "PM";
        }
    }

    public int getTemp() {
        return temp;
    }

    public String getFormattedTemp() {
        // TODO: Change specific deg sign based on units
        // "\u2103" for C and "\u2109" for F
        return String.valueOf(temp) + DEG_SIGN;
    }

    public Conditions getHourCondition() {
        return hourCondition;
    }

    public String getFormattedFeelsLikeTemp() {
        return FEELS_LIKE_SENTENCE + feelsLike + DEG_SIGN;
    }

    public String getFormattedHumidity() {
        return humidity + PERCENT_SIGN;
    }

    public String getFormattedUvIndex() {
        return String.format(TWO_DECIMAL_PLACES_FORMAT, uvIndex);
    }

    public String getFormattedProbOfPrecipitation() {
        if (probOfPrecipitation == -1) return "0" + PERCENT_SIGN;
        return probOfPrecipitation + PERCENT_SIGN;
    }

    // TODO: Generate getter functions once they're needed
}
