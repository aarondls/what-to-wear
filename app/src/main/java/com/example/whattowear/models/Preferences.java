package com.example.whattowear.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Represents all user preferences, which will be synced with the Parse database
 */
@ParseClassName("Preferences")
public class Preferences extends ParseObject {
    public static final String KEY_PREFERENCES = "preferences";
    public static final String KEY_USER = "user";
    public static final String KEY_WEATHER_UNIT = "weatherUnit";
    public static final String KEY_OVER_BODY_GARMENT_RANKERS = "overBodyGarmentRankers";
    public static final String KEY_UPPER_BODY_GARMENT_RANKERS = "upperBodyGarmentRankers";
    public static final String KEY_LOWER_BODY_GARMENT_RANKERS = "lowerBodyGarmentRankers";
    public static final String KEY_FOOTWEAR_RANKERS = "footwearRankers";
    public static final String KEY_ACCESSORIES_RANKERS = "accessoriesRankers";

    /**
     * Required default constructor for parse
     */
    public Preferences() {
    }

    /**
     * @return the ParseUser associated with (who owns) the preferences class
     */
    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    /**
     * Sets the owner of this preferences class
     *
     * @param user the owner of the preferences class
     */
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    /**
     * Possible values: Weather.WeatherUnit.FAHRENHEIT, Weather.WeatherUnit.CELSIUS
     *
     * @return the weather unit to be used
     */
    public String getWeatherUnit() {
        return getString(KEY_WEATHER_UNIT);
    }

    /**
     * Sets the weather unit
     * Possible values: Weather.WeatherUnit.FAHRENHEIT, Weather.WeatherUnit.CELSIUS
     *
     * @param weatherUnit the weather unit to be used
     */
    public void setWeatherUnit(String weatherUnit) {
        put(KEY_WEATHER_UNIT, weatherUnit);
    }

    /**
     * @return the list of overBodyGarmentRankers contained in this preferences
     */
    public List<ClothingRanker> getOverBodyGarmentRankers() {
        return getList(KEY_OVER_BODY_GARMENT_RANKERS);
    }

    /**
     * Sets the list of overBodyGarmentRankers contained in this preferences
     *
     * @param overBodyGarmentRankers the list of overBodyGarmentRankers to be set
     */
    public void setOverBodyGarmentRankers(List<ClothingRanker> overBodyGarmentRankers) {
        put(KEY_OVER_BODY_GARMENT_RANKERS, overBodyGarmentRankers);
    }

    /**
     * @return the list of upperBodyGarmentRankers contained in this preferences
     */
    public List<ClothingRanker> getUpperBodyGarmentRankers() {
        return getList(KEY_UPPER_BODY_GARMENT_RANKERS);
    }

    /**
     * Sets the list of upperBodyGarmentRankers contained in this preferences
     *
     * @param upperBodyGarmentRankers the list of upperBodyGarmentRankers to be set
     */
    public void setUpperBodyGarmentRankers(List<ClothingRanker> upperBodyGarmentRankers) {
        put(KEY_UPPER_BODY_GARMENT_RANKERS, upperBodyGarmentRankers);
    }

    /**
     * @return the list of lowerBodyGarmentRankers contained in this preferences
     */
    public List<ClothingRanker> getLowerBodyGarmentRankers() {
        return getList(KEY_LOWER_BODY_GARMENT_RANKERS);
    }

    /**
     * Sets the list of lowerBodyGarmentRankers contained in this preferences
     *
     * @param lowerBodyGarmentRankers the list of lowerBodyGarmentRankers to be set
     */
    public void setLowerBodyGarmentRankers(List<ClothingRanker> lowerBodyGarmentRankers) {
        put(KEY_LOWER_BODY_GARMENT_RANKERS, lowerBodyGarmentRankers);
    }

    /**
     * @return the list of footwearRankers contained in this preferences
     */
    public List<ClothingRanker> getFootwearRankers() {
        return getList(KEY_FOOTWEAR_RANKERS);
    }

    /**
     * Sets the list of footwearRankers contained in this preferences
     *
     * @param footwearRankers the list of footwearRankers to be set
     */
    public void setFootwearRankers(List<ClothingRanker> footwearRankers) {
        put(KEY_FOOTWEAR_RANKERS, footwearRankers);
    }

    /**
     * @return the list of accessoriesRankers contained in this preferences
     */
    public List<ClothingRanker> getAccessoriesRankers() {
        return getList(KEY_ACCESSORIES_RANKERS);
    }

    /**
     * Sets the list of accessoriesRankers contained in this preferences
     *
     * @param accessoriesRankers the list of accessoriesRankers to be set
     */
    public void setAccessoriesRankers(List<ClothingRanker> accessoriesRankers) {
        put(KEY_ACCESSORIES_RANKERS, accessoriesRankers);
    }
}
