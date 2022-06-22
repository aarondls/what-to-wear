package com.example.whattowear.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Conditions {
    private static final String IMG_URL_BEGIN = "https://openweathermap.org/img/wn/";
    private static final String IMG_URL_END = "@2x.png";

    private int weatherConditionsID;
    private String conditionSummary;
    private String conditionDescription;
    private String conditionIconLink;

    public static Conditions fromJson(JSONObject jsonObject) throws JSONException {
        Conditions conditions = new Conditions();

        conditions.weatherConditionsID = jsonObject.getInt("id");
        conditions.conditionSummary = jsonObject.getString("main");
        conditions.conditionDescription = jsonObject.getString("description");
        conditions.conditionIconLink = IMG_URL_BEGIN + jsonObject.getString("icon") + IMG_URL_END;

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
