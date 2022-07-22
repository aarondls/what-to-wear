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

    private static final String PERCENT_SIGN = "%";
    private static final String FEELS_LIKE_SENTENCE = "But feels like: ";
    private static final String TWO_DECIMAL_PLACES_FORMAT = "%.2f";

    private static final String DATE_TIME_ID = "dt";
    private static final String TEMP_ID = "temp";
    private static final String FEELS_LIKE_ID = "feels_like";
    private static final String HUMIDITY_ID = "humidity";
    private static final String UV_INDEX_ID = "uvi";
    private static final String CLOUDS_ID = "clouds";
    private static final String WIND_SPEED_ID = "wind_speed";
    private static final String PROBABILITY_OF_PRECIPITATION_ID = "pop";
    private static final String WEATHER_ID = "weather";
    private static final String HOUR_1_ID = "1h";
    private static final String RAIN_ID = "rain";
    private static final String SNOW_ID = "snow";
    private static final String AM_12 = "12am";
    private static final String AM = "am";
    private static final String PM = "pm";
    private static final int ZERO_HOUR = 0;
    private static final int TWELVE_HOUR = 12;


    // TODO: Remove fields that may not be used once clothing selection logic is completed
    private Date dt;
    private int fahrenheitTemp;
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

        forecast.dt = new Date(Long.parseLong(jsonObject.getString(DATE_TIME_ID)) * 1000);
        forecast.fahrenheitTemp = jsonObject.getInt(TEMP_ID);
        forecast.feelsLike = jsonObject.getInt(FEELS_LIKE_ID);
        forecast.humidity = jsonObject.getInt(HUMIDITY_ID);
        forecast.uvIndex = jsonObject.getDouble(UV_INDEX_ID);
        forecast.clouds = jsonObject.getInt(CLOUDS_ID);
        forecast.windSpeed = jsonObject.getDouble(WIND_SPEED_ID);

        if (jsonObject.has(PROBABILITY_OF_PRECIPITATION_ID)) {
            forecast.probOfPrecipitation = jsonObject.getInt(PROBABILITY_OF_PRECIPITATION_ID);
        } else {
            forecast.probOfPrecipitation = -1;
        }

        forecast.hourCondition = Conditions.fromJson(jsonObject.getJSONArray(WEATHER_ID).getJSONObject(0));

        if (jsonObject.has(RAIN_ID)) {
            forecast.rainHourVol = jsonObject.getJSONObject(RAIN_ID).getInt(HOUR_1_ID);
        } else {
            forecast.rainHourVol = 0;
        }

        if (jsonObject.has(SNOW_ID)) {
            forecast.snowHourVol = jsonObject.getJSONObject(SNOW_ID).getInt("1h");
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

        if (hours == ZERO_HOUR) {
            return AM_12;
        } else if (hours == TWELVE_HOUR) {
            return hours + PM;
        } else if (hours < TWELVE_HOUR) {
            return hours + AM;
        } else {
            return hours-TWELVE_HOUR + PM;
        }
    }

    public Conditions getHourCondition() {
        return hourCondition;
    }

    /**
     * @return the Forecast temperature formatted in the correct unit according to Weather.weatherUnits with the degree symbol
     */
    public String getFormattedTemp() {
        return Weather.getFormattedTemp(fahrenheitTemp);
    }

    /**
     * @return the Forecast feels like temperature formatted in the correct unit according to Weather.weatherUnits with the degree symbol
     * and the phrase "But feels like"
     */
    public String getFormattedFeelsLikeTemp() {
        return FEELS_LIKE_SENTENCE + Weather.getFormattedTemp(feelsLike);
    }

    /**
     * @return the Forecast humidity with the percent sign
     */
    public String getFormattedHumidity() {
        return humidity + PERCENT_SIGN;
    }

    public String getFormattedUvIndex() {
        return String.format(TWO_DECIMAL_PLACES_FORMAT, uvIndex);
    }

    /**
     * @return the Forecast probability of precipitation with the percent sign
     */
    public String getFormattedProbOfPrecipitation() {
        if (probOfPrecipitation == -1) return "0" + PERCENT_SIGN;
        return probOfPrecipitation + PERCENT_SIGN;
    }
}
