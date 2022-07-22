package com.example.whattowear.models;

import android.util.Pair;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Class responsible for ranking an individual piece of clothing based on weather and user given factors
 */
@ParseClassName("ClothingRanker")
public class ClothingRanker extends ParseObject {
    // Parse keys
    public static final String KEY_USER = "user";
    public static final String KEY_CLOTHING_TYPE_NAME = "clothingTypeName";
    public static final String KEY_PREFERENCE_FACTOR = "preferenceFactor";
    public static final String KEY_TEMPERATURE_IMPORTANCE = "temperatureImportance";
    public static final String KEY_TEMPERATURE_LOWER_RANGE = "temperatureLowerRange";
    public static final String KEY_TEMPERATURE_UPPER_RANGE = "temperatureUpperRange";
    public static final String KEY_ACTIVITY_IMPORTANCE = "activityImportance";
    public static final String KEY_WORK_ACTIVITY_FACTOR = "workActivityFactor";
    public static final String KEY_SPORTS_ACTIVITY_FACTOR = "sportsActivityFactor";
    public static final String KEY_CASUAL_ACTIVITY_FACTOR = "casualActivityFactor";
    public static final String KEY_WEATHER_IMPORTANCE = "weatherImportance";
    private static final String KEY_CONDITIONS_THUNDERSTORM_FACTOR = "conditionsThunderstormFactor";
    private static final String KEY_CONDITIONS_DRIZZLE_FACTOR = "conditionsDrizzleFactor";
    private static final String KEY_CONDITIONS_RAIN_FACTOR = "conditionsRainFactor";
    private static final String KEY_CONDITIONS_SNOW_FACTOR = "conditionsSnowFactor";


    private static final int DEFAULT_PREFERENCE_FACTOR = 5;
    private static final int DEFAULT_TEMPERATURE_IMPORTANCE = 5;
    private static final int DEFAULT_TEMPERATURE_LOWER_RANGE = 0;
    private static final int DEFAULT_TEMPERATURE_UPPER_RANGE = 100;
    private static final int DEFAULT_ACTIVITY_IMPORTANCE = 5;
    private static final int DEFAULT_WORK_ACTIVITY_FACTOR = 5;
    private static final int DEFAULT_SPORTS_ACTIVITY_FACTOR = 5;
    private static final int DEFAULT_CASUAL_ACTIVITY_FACTOR = 5;
    private static final int DEFAULT_WEATHER_IMPORTANCE = 5;
    private static final int DEFAULT_CONDITIONS_THUNDERSTORM_FACTOR = 0;
    private static final int DEFAULT_CONDITIONS_DRIZZLE_FACTOR = 0;
    private static final int DEFAULT_CONDITIONS_RAIN_FACTOR = 0;
    private static final int DEFAULT_CONDITIONS_SNOW_FACTOR = 0;

    private int clothingTypeIconID; // do not store in parse as generated during runtime


    /**
     *  All the variables available at the Parse database:
     *
     *  Clothing name
     *  String clothingTypeName
     *
     *  Preference factors
     *  int preferenceFactor: how much the user likes this piece of clothing
     *
     *  Temperature factors
     *  int temperatureImportance: how sensitive this clothing is to the temp range
     *  int temperatureLowerRange
     *  int temperatureUpperRange
     *
     *  Activity factors
     *  int activityImportance: how relevant this clothing is to the activity
     *  int workActivityFactor: how relevant this clothing is for work
     *  int sportsActivityFactor: how relevant this clothing is for sports
     *  int casualActivityFactor: how relevant this clothing is for casual
     *
     *  Weather type factors
     *  int weatherImportance: how relevant this clothing is for the weather
     *  int conditionsThunderstormFactor: weather factors based on the condition ID first digit
     *  int conditionsDrizzleFactor
     *  int conditionsRainFactor
     *  int conditionsSnowFactor
     */

    /**
     * Needed for parse
     */
    public ClothingRanker() {}

    /**
     * Used to initialize a clothing ranker with default preference, temperature, activity, and weather type factors
     * Must provide the clothingTypeName, clothingTypeIconID, and ParseUser to be associated with this clothing ranker
     *
     * @param clothingTypeName the type name of the clothing represented by this clothing ranker
     * @param clothingTypeIconID the ID (generated at runtime) of the icon associated with this clothing
     * @param user the user who owns this clothing
     */
    public void initializeFactorsToDefault(String clothingTypeName, int clothingTypeIconID, ParseUser user) {
        // use the required given information
        setClothingTypeName(clothingTypeName);
        this.clothingTypeIconID = clothingTypeIconID;
        setUser(user);

        // use these defaults
        setPreferenceFactor(DEFAULT_PREFERENCE_FACTOR);
        setTemperatureImportance(DEFAULT_TEMPERATURE_IMPORTANCE);
        setTemperatureLowerRange(DEFAULT_TEMPERATURE_LOWER_RANGE);
        setTemperatureUpperRange(DEFAULT_TEMPERATURE_UPPER_RANGE);
        setActivityImportance(DEFAULT_ACTIVITY_IMPORTANCE);
        setWorkActivityFactor(DEFAULT_WORK_ACTIVITY_FACTOR);
        setSportsActivityFactor(DEFAULT_SPORTS_ACTIVITY_FACTOR);
        setCasualActivityFactor(DEFAULT_CASUAL_ACTIVITY_FACTOR);
        setWeatherImportance(DEFAULT_WEATHER_IMPORTANCE);
        setConditionsThunderstormFactor(DEFAULT_CONDITIONS_THUNDERSTORM_FACTOR);
        setConditionsDrizzleFactor(DEFAULT_CONDITIONS_DRIZZLE_FACTOR);
        setConditionsRainFactor(DEFAULT_CONDITIONS_RAIN_FACTOR);
        setConditionsSnowFactor(DEFAULT_CONDITIONS_SNOW_FACTOR);
    }

    public float getCurrentPrimaryRating() {
        return getPreferenceFactor()*(getTemperatureFactor()*getTemperatureImportance() + getPrimaryActivityFactor()*getActivityImportance() + getWeatherConditionsFactor()*getWeatherImportance());
    }

    public float getCurrentSecondaryRating() {
        return getPreferenceFactor()*(getTemperatureFactor()*getTemperatureImportance() + getSecondaryActivityFactor()*getActivityImportance() + getWeatherConditionsFactor()*getWeatherImportance());
    }

    /**
     * Generic function to calculate the optimal clothing type from a list of clothing rankers
     *
     * @param clothingRankers the list of clothing rankers from which the optimal one will be selected
     * @param clothingTypesInOrder the associated clothing types of the list clothingRankers, in the same order (ie, the enum value RAIN_JACKET)
     * @param <T> the enum type of clothing to rank (ie, the enum OverBodyGarment or Accessories)
     * @return the clothing type (ie, enum value) and ranking score of the optimal clothing
     */
    public static <T> Pair<T, Float> getOptimalClothingType(List<ClothingRanker> clothingRankers, T[] clothingTypesInOrder) {
        float maxRankingSoFar = 0;

        T optimalType = null;
        for (int i=0; i<clothingRankers.size(); i++) {
            ClothingRanker currentRanker = clothingRankers.get(i);
            float currentRank = currentRanker.getCurrentPrimaryRating();
            if (currentRank > maxRankingSoFar) {
                optimalType = clothingTypesInOrder[i];
                maxRankingSoFar = currentRank;
            }
        }
        return new Pair<>(optimalType, maxRankingSoFar);
    }

    /**
     * @return the current temperature factor based on the current day mean temperature
     */
    private float getTemperatureFactor() {
        // must be >0 inside of defined temperature range, <0 outside
        // model using quadratic function
        // use temperatureLowerRange and temperatureUpperRange as the zeros
        // constrain stretch factor by defining maximum temperature factor as 10

        // form of function at desired maximum is: -f(x-temperatureLowerRange)(x-temperatureUpperRange)=10
        // need to solve for scaling factor f:
        // -f(x^2 + (-temperatureLowerRange-temperatureUpperRange)x + temperatureLowerRange*temperatureUpperRange)=10
        // Now denote the variables as: -f(x^2+bx+c)=10, where f is the scaling factor
        // Maximum happens at x_max =-b/2

        int temperatureLowerRange = getTemperatureLowerRange();
        int temperatureUpperRange = getTemperatureUpperRange();
        float b = -(temperatureLowerRange+temperatureUpperRange);
        float c = temperatureLowerRange*temperatureUpperRange;
        float xMax = -b/2f;
        float f = (-10f)/(xMax*xMax + b*xMax + c);

        // Now calculate current temperature factor given all variables and the current temperature
        float dayMeanTemperature = Weather.getDayMeanFahrenheitTemperature();
        // TODO: instead of day mean temp use next xx hours mean
        float temperatureFactor = -f*(dayMeanTemperature-temperatureLowerRange)*(dayMeanTemperature-temperatureUpperRange);

        return temperatureFactor;
    }

    /**
     * Gets the icon ID representing the clothing type
     * Do not get this from Parse as the ID is generated at runtime
     *
     * @return the icon ID representing the clothing type
     */
    public int getClothingTypeIconID() {
        return clothingTypeIconID;
    }

    /**
     * @param clothingTypeIconID the icon ID representing the clothing type
     */
    public void setClothingTypeIconID(int clothingTypeIconID) {
        this.clothingTypeIconID = clothingTypeIconID;
    }


    /**
     * All the following getter and setters utilize the Parse database
     */

    /**
     * @return the ParseUser owning the clothing ranker
     */
    public ParseUser getUser() {
       return getParseUser(KEY_USER);
    }

    /**
     * @param user the ParseUser owning the clothing ranker
     */
    public void setUser(ParseUser user) {
        put (KEY_USER, user);
    }

    /**
     * @return the String representing the name of the current clothing type
     */
    public String getClothingTypeName() {
        return getString(KEY_CLOTHING_TYPE_NAME);
    }

    /**
     * @param clothingTypeName the String representing the name of the current clothing type
     */
    public void setClothingTypeName(String clothingTypeName) {
        put(KEY_CLOTHING_TYPE_NAME, clothingTypeName);
    }

    /**
     * @return the preference factor of the current clothing
     */
    public int getPreferenceFactor() {
        return getInt(KEY_PREFERENCE_FACTOR);
    }

    /**
     * @param preferenceFactor the preference factor of the current clothing
     *                         must be between 0 and 10
     */
    public void setPreferenceFactor(int preferenceFactor) {
        put(KEY_PREFERENCE_FACTOR, preferenceFactor);
    }

    /**
     * @return the temperature importance of the current clothing
     */
    public int getTemperatureImportance() {
        return getInt(KEY_TEMPERATURE_IMPORTANCE);
    }

    /**
     * @param temperatureImportance the temperature importance of the current clothing
     *                              must be between 0 and 10
     */
    public void setTemperatureImportance(int temperatureImportance) {
        put(KEY_TEMPERATURE_IMPORTANCE, temperatureImportance);
    }

    /**
     * @return the lower temperature range of the current clothing
     */
    public int getTemperatureLowerRange() {
        return getInt(KEY_TEMPERATURE_LOWER_RANGE);
    }

    /**
     * @param temperatureLowerRange the lower temperature range of the current clothing
     *                              must be between 0 and 110
     */
    public void setTemperatureLowerRange(int temperatureLowerRange) {
        put(KEY_TEMPERATURE_LOWER_RANGE, temperatureLowerRange);
    }

    /**
     * @return the upper temperature range of the current clothing
     */
    public int getTemperatureUpperRange() {
        return getInt(KEY_TEMPERATURE_UPPER_RANGE);
    }

    /**
     * @param temperatureUpperRange the upper temperature range of the current clothing
     *                              must be between 0 and 110
     */
    public void setTemperatureUpperRange(int temperatureUpperRange) {
        put(KEY_TEMPERATURE_UPPER_RANGE, temperatureUpperRange);
    }

    /**
     * @return the activity importance of the current clothing
     */
    public int getActivityImportance() {
        return getInt(KEY_ACTIVITY_IMPORTANCE);
    }

    /**
     * @param activityImportance the activity importance of the current clothing
     *                           must be between 1 and 10, inclusive
     */
    public void setActivityImportance(int activityImportance) {
        put(KEY_ACTIVITY_IMPORTANCE, activityImportance);
    }

    /**
     * @return the primary activity factor of the current clothing
     */
    private int getPrimaryActivityFactor() {
        return getActivityFactorFromType(Clothing.getSelectedActivityType().getPrimaryActivityType());
    }

    /**
     * @return the secondary activity factor of the current clothing
     */
    private int getSecondaryActivityFactor() {
        return getActivityFactorFromType(Clothing.getSelectedActivityType().getSecondaryActivityType());
    }

    /**
     * Returns the activity factor of the given activity type
     * Allowed values are: "Work", "Sports", and "Casual"
     *
     * @param activityType the activity type to check
     * @return the activity factor of the given activity type
     */
    private int getActivityFactorFromType(ActivityType.ActivityTypeName activityType) {
        // if no activity, return 0 so the calculation is not affected by it
        if (activityType == null) return 0;

        switch (activityType) {
            case WORK:
                return getInt(KEY_WORK_ACTIVITY_FACTOR);
            case SPORTS:
                return getInt(KEY_SPORTS_ACTIVITY_FACTOR);
            case CASUAL:
                return getInt(KEY_CASUAL_ACTIVITY_FACTOR);
            default:
                // if no known factor, return 0 so the calculation is not affected by it
                return 0;
        }
    }

    /**
     * @return the work activity factor of the current clothing
     */
    public int getWorkActivityFactor() {
        return getInt(KEY_WORK_ACTIVITY_FACTOR);
    }

    /**
     * @return the sports activity factor of the current clothing
     */
    public int getSportsActivityFactor() {
        return getInt(KEY_SPORTS_ACTIVITY_FACTOR);
    }

    /**
     * @return the casual activity factor of the current clothing
     */
    public int getCasualActivityFactor() {
        return getInt(KEY_CASUAL_ACTIVITY_FACTOR);
    }

    /**
     * @param workActivityFactor the work activity factor of the current clothing
     *                           must be between 1 and 10, inclusive
     */
    public void setWorkActivityFactor(int workActivityFactor) {
        put(KEY_WORK_ACTIVITY_FACTOR, workActivityFactor);
    }

    /**
     * @param sportsActivityFactor the sports activity factor of the current clothing
     *                             must be between 1 and 10, inclusive
     */
    public void setSportsActivityFactor(int sportsActivityFactor) {
        put(KEY_SPORTS_ACTIVITY_FACTOR, sportsActivityFactor);
    }

    /**
     * @param casualActivityFactor the casual activity factor of the current clothing
     *                             must be between 1 and 10, inclusive
     */
    public void setCasualActivityFactor(int casualActivityFactor) {
        put(KEY_CASUAL_ACTIVITY_FACTOR, casualActivityFactor);
    }

    /**
     * @return the weather importance of the current clothing
     */
    public int getWeatherImportance() {
        return getInt(KEY_WEATHER_IMPORTANCE);
    }

    /**
     * @param weatherImportance the weather importance of the current clothing
     *                          must be between 1 and 10, inclusive
     */
    public void setWeatherImportance(int weatherImportance) {
        put(KEY_WEATHER_IMPORTANCE, weatherImportance);
    }

    /**
     * @return the current weather conditions factor of the current clothing
     */
    private int getWeatherConditionsFactor() {
        // check day weather condition
        int currentConditionIDFirstDigit = Weather.getDayConditionsID();

        switch (currentConditionIDFirstDigit) {
            case Conditions.THUNDERSTORM_CONDITIONS_ID_FIRST_DIGIT:
                return getNumber(KEY_CONDITIONS_THUNDERSTORM_FACTOR).intValue();
            case Conditions.DRIZZLE_CONDITIONS_ID_FIRST_DIGIT:
                return getNumber(KEY_CONDITIONS_DRIZZLE_FACTOR).intValue();
            case Conditions.RAIN_CONDITIONS_ID_FIRST_DIGIT:
                return getNumber(KEY_CONDITIONS_RAIN_FACTOR).intValue();
            case Conditions.SNOW_CONDITIONS_ID_FIRST_DIGIT:
                return getNumber(KEY_CONDITIONS_SNOW_FACTOR).intValue();
            default:
                // if unrecognized weather condition, return 0 so calculation is unaffected
                return 0;
        }
    }

    /**
     * @return the thunderstorm conditions factor of the current clothing
     */
    public int getConditionsThunderstormFactor() {
        return getNumber(KEY_CONDITIONS_THUNDERSTORM_FACTOR).intValue();
    }

    /**
     * @return the drizzle conditions factor of the current clothing
     */
    public int getConditionsDrizzleFactor() {
        return getNumber(KEY_CONDITIONS_DRIZZLE_FACTOR).intValue();
    }

    /**
     * @return the rain conditions factor of the current clothing
     */
    public int getConditionsRainFactor() {
        return getNumber(KEY_CONDITIONS_RAIN_FACTOR).intValue();
    }

    /**
     * @return the snow conditions factor of the current clothing
     */
    public int getConditionsSnowFactor() {
        return getNumber(KEY_CONDITIONS_SNOW_FACTOR).intValue();
    }

    /**
     * @param conditionsThunderstormFactor the thunderstorm conditions factor of the current clothing
     *                                     must be between 1 and 10, inclusive
     */
    public void setConditionsThunderstormFactor(int conditionsThunderstormFactor) {
        put(KEY_CONDITIONS_THUNDERSTORM_FACTOR, conditionsThunderstormFactor);
    }

    /**
     * @param conditionsDrizzleFactor the drizzle conditions factor of the current clothing
     *                                must be between 1 and 10, inclusive
     */
    public void setConditionsDrizzleFactor(int conditionsDrizzleFactor) {
        put(KEY_CONDITIONS_DRIZZLE_FACTOR, conditionsDrizzleFactor);
    }

    /**
     * @param conditionsRainFactor the rain conditions factor of the current clothing
     *                             must be between 1 and 10, inclusive
     */
    public void setConditionsRainFactor(int conditionsRainFactor) {
        put(KEY_CONDITIONS_RAIN_FACTOR, conditionsRainFactor);
    }

    /**
     * @param conditionsSnowFactor the snow conditions factor of the current clothing
     *                             must be between 1 and 10, inclusive
     */
    public void setConditionsSnowFactor(int conditionsSnowFactor) {
        put(KEY_CONDITIONS_SNOW_FACTOR, conditionsSnowFactor);
    }
}
