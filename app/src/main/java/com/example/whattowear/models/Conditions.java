package com.example.whattowear.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Conditions {
    public static final int THUNDERSTORM_CONDITIONS_ID_FIRST_DIGIT = 2;
    public static final int DRIZZLE_CONDITIONS_ID_FIRST_DIGIT = 3;
    public static final int RAIN_CONDITIONS_ID_FIRST_DIGIT = 5;
    public static final int SNOW_CONDITIONS_ID_FIRST_DIGIT = 6;

    private static final String WEATHER_CONDITIONS_ID_NAME = "id";
    private static final String WEATHER_CONDITIONS_SUMMARY_NAME = "main";
    private static final String WEATHER_CONDITIONS_DESCRIPTION_NAME = "description";
    private static final String WEATHER_CONDITIONS_ICON_NAME = "icon";
    private static final String IMG_URL_BEGIN = "https://openweathermap.org/img/wn/";
    private static final String IMG_URL_END = "@2x.png";

    private int weatherConditionsID;
    private String conditionSummary;
    private String conditionDescription;
    private String conditionIconLink;

    public static Conditions fromJson(JSONObject jsonObject) throws JSONException {
        Conditions conditions = new Conditions();

        conditions.weatherConditionsID = jsonObject.getInt(WEATHER_CONDITIONS_ID_NAME);
        conditions.conditionSummary = jsonObject.getString(WEATHER_CONDITIONS_SUMMARY_NAME);
        conditions.conditionDescription = jsonObject.getString(WEATHER_CONDITIONS_DESCRIPTION_NAME);
        conditions.conditionIconLink = IMG_URL_BEGIN + jsonObject.getString(WEATHER_CONDITIONS_ICON_NAME) + IMG_URL_END;

        return conditions;
    }

    public int getWeatherConditionsID() {
        return weatherConditionsID;
    }

    public String getConditionSummary() {
        return conditionSummary;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

    public String getConditionIconLink() {
        return conditionIconLink;
    }
}
