package com.example.whattowear.models;

import android.util.Log;
import android.util.Pair;

import com.example.whattowear.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class OverBodyGarment {
    private static final String TAG = "OverBodyGarment";

    private static final float NONE_TYPE_RANKING = 300;

    private static final int ATHLETIC_JACKET_TEMPERATURE_LOWER_RANGE = 40;
    private static final int ATHLETIC_JACKET_TEMPERATURE_UPPER_RANGE = 60;
    private static final int ATHLETIC_JACKET_WORK_ACTIVITY_FACTOR = 0;
    private static final int ATHLETIC_JACKET_SPORTS_ACTIVITY_FACTOR = 10;
    private static final int ATHLETIC_JACKET_CASUAL_ACTIVITY_FACTOR = 3;

    private static final int RAIN_JACKET_TEMPERATURE_IMPORTANCE = 1;
    private static final int RAIN_JACKET_TEMPERATURE_LOWER_RANGE = 30;
    private static final int RAIN_JACKET_TEMPERATURE_UPPER_RANGE = 90;
    private static final int RAIN_JACKET_ACTIVITY_IMPORTANCE = 1;
    private static final int RAIN_JACKET_WEATHER_IMPORTANCE = 10;
    private static final int RAIN_JACKET_CONDITIONS_THUNDERSTORM_FACTOR = 8;
    private static final int RAIN_JACKET_CONDITIONS_DRIZZLE_FACTOR = 7;
    private static final int RAIN_JACKET_CONDITIONS_RAIN_FACTOR = 10;
    private static final int RAIN_JACKET_CONDITIONS_SNOW_FACTOR = 4;

    private static final int WINTER_COAT_TEMPERATURE_IMPORTANCE = 9;
    private static final int WINTER_COAT_TEMPERATURE_LOWER_RANGE = 0;
    private static final int WINTER_COAT_TEMPERATURE_UPPER_RANGE = 25;
    private static final int WINTER_COAT_WEATHER_IMPORTANCE = 10;
    private static final int WINTER_COAT_CONDITIONS_SNOW_FACTOR = 10;

    private OverBodyGarmentType overBodyGarmentType;
    private float rankingScore; // TODO: store ranking score for each clothing class, so this can be displayed in detailed clothing screen (or some kind of metric)

    private static enum OverBodyGarmentType {
        ATHLETIC_JACKET, RAIN_JACKET, WINTER_COAT, NONE
    }

    // Indexing of this is based on the order declaration of the enum, excluding none
    private static List<ClothingRanker> overBodyGarmentRankers;

    /**
     * Calculates the optimal over body garment based on weather,
     * activity, and preferences, then returns it
     * @return the calculated optimal over body garment
     */
    public static OverBodyGarment getOptimalOverBodyGarment() {
        // TODO: update type based on weather, activity, and preferences
        // for testing, default to none

        OverBodyGarment overBodyGarment = new OverBodyGarment();

        Pair<OverBodyGarmentType, Float> optimalResult = ClothingRanker.getOptimalClothingType(overBodyGarmentRankers, OverBodyGarmentType.values());

        // see if NONE type is better
        if (optimalResult.second > NONE_TYPE_RANKING) {
            overBodyGarment.overBodyGarmentType = optimalResult.first;
            overBodyGarment.rankingScore = optimalResult.second;
        } else {
            // NONE is better
            overBodyGarment.overBodyGarmentType = OverBodyGarmentType.NONE;
            overBodyGarment.rankingScore = NONE_TYPE_RANKING;
        }

        Log.i(TAG, "Returning optimal: " + overBodyGarment.overBodyGarmentType);

        return overBodyGarment;
    }

    /**
     * @return the ID of the local file of the over body garment image, if
     * it exists. If it is defined as OverBodyGarmentType.NONE, then this
     * returns null.
     */
    public java.lang.Integer getOverBodyGarmentImage() {
        // TODO: return correct image based on type
        if (overBodyGarmentType == OverBodyGarmentType.NONE) return null;

        return R.drawable.clothing_over_body_athletic_jacket;
    }

    /**
     * @param clothingTypeName the clothingTypeName of the desired icon ID to get
     * @return the ID of the local file of the over body garment image, if
     * it exists, based on the given clothingTypeName. Otherwise, if it
     * is defined as OverBodyGarmentType.NONE or does not exist, then this
     * returns null.
     */
    public static Integer getOverBodyGarmentImage(String clothingTypeName) {
        // TODO: return correct image based on type
        if (clothingTypeName.equals("Athletic jacket")) {
            return R.drawable.clothing_over_body_athletic_jacket;
        } else if (clothingTypeName.equals("Rain jacket")) {
            return R.drawable.clothing_over_body_athletic_jacket;
        } else if (clothingTypeName.equals("Winter coat")) {
            return R.drawable.clothing_over_body_athletic_jacket;
        }
        // unrecognized return null
        return null;
    }

    /**
     * Sets the overBodyGarmentRankers and fetches any missing information from the Parse database, if needed
     * @param overBodyGarmentRankers the overBodyGarmentRankers to set
     * @throws ParseException the non-null ParseException, if fetching the missing information fails
     */
    public static void setOverBodyGarmentRankers(List<ClothingRanker> overBodyGarmentRankers) throws ParseException {
        OverBodyGarment.overBodyGarmentRankers = overBodyGarmentRankers;

        try {
            ClothingRanker.fetchAllIfNeeded(overBodyGarmentRankers);
        } catch (ParseException e) {
            throw e;
        }

        // this is generated locally at runtime, so cannot get from parse database
        for (ClothingRanker clothingRanker : overBodyGarmentRankers) {
            clothingRanker.setClothingTypeIconID(getOverBodyGarmentImage(clothingRanker.getClothingTypeName()));
        }
    }

    /**
     * Initializes all the over body garment rankers with default factors
     * @param user the ParseUser owning the over body garments
     * @return the list of initialized clothing rankers for over body garments with default factors
     */
    public static List<ClothingRanker> initializeOverBodyGarmentRankers(ParseUser user) {
        // TODO: fix icons
        ClothingRanker athleticJacketRanker  = new ClothingRanker();
        athleticJacketRanker.initializeFactorsToDefault("Athletic jacket", R.drawable.hoodie, user);
        ClothingRanker rainJacketRanker  = new ClothingRanker();
        rainJacketRanker.initializeFactorsToDefault("Rain jacket", R.drawable.hoodie, user);
        ClothingRanker winterCoatRanker  = new ClothingRanker();
        winterCoatRanker.initializeFactorsToDefault("Winter coat", R.drawable.hoodie, user);

        // Set all relevant default factors
        athleticJacketRanker.setTemperatureLowerRange(ATHLETIC_JACKET_TEMPERATURE_LOWER_RANGE);
        athleticJacketRanker.setTemperatureUpperRange(ATHLETIC_JACKET_TEMPERATURE_UPPER_RANGE);
        athleticJacketRanker.setWorkActivityFactor(ATHLETIC_JACKET_WORK_ACTIVITY_FACTOR);
        athleticJacketRanker.setSportsActivityFactor(ATHLETIC_JACKET_SPORTS_ACTIVITY_FACTOR);
        athleticJacketRanker.setCasualActivityFactor(ATHLETIC_JACKET_CASUAL_ACTIVITY_FACTOR);

        rainJacketRanker.setTemperatureImportance(RAIN_JACKET_TEMPERATURE_IMPORTANCE);
        rainJacketRanker.setTemperatureLowerRange(RAIN_JACKET_TEMPERATURE_LOWER_RANGE);
        rainJacketRanker.setTemperatureUpperRange(RAIN_JACKET_TEMPERATURE_UPPER_RANGE);
        rainJacketRanker.setActivityImportance(RAIN_JACKET_ACTIVITY_IMPORTANCE);
        rainJacketRanker.setWeatherImportance(RAIN_JACKET_WEATHER_IMPORTANCE);
        rainJacketRanker.setConditionsThunderstormFactor(RAIN_JACKET_CONDITIONS_THUNDERSTORM_FACTOR);
        rainJacketRanker.setConditionsDrizzleFactor(RAIN_JACKET_CONDITIONS_DRIZZLE_FACTOR);
        rainJacketRanker.setConditionsRainFactor(RAIN_JACKET_CONDITIONS_RAIN_FACTOR);
        rainJacketRanker.setConditionsSnowFactor(RAIN_JACKET_CONDITIONS_SNOW_FACTOR);

        winterCoatRanker.setTemperatureImportance(WINTER_COAT_TEMPERATURE_IMPORTANCE);
        winterCoatRanker.setTemperatureLowerRange(WINTER_COAT_TEMPERATURE_LOWER_RANGE);
        winterCoatRanker.setTemperatureUpperRange(WINTER_COAT_TEMPERATURE_UPPER_RANGE);
        winterCoatRanker.setWeatherImportance(WINTER_COAT_WEATHER_IMPORTANCE);
        winterCoatRanker.setConditionsSnowFactor(WINTER_COAT_CONDITIONS_SNOW_FACTOR);

        // add all rankers to the list
        overBodyGarmentRankers = new ArrayList<>();
        overBodyGarmentRankers.add(athleticJacketRanker);
        overBodyGarmentRankers.add(rainJacketRanker);
        overBodyGarmentRankers.add(winterCoatRanker);

        return overBodyGarmentRankers;
    }

    /**
     * @return the list of clothing rankers for over body garments, in the order of which their type is declared within the OverBodyGarmentType enum
     */
    public static List<ClothingRanker> getOverBodyGarmentRankers() {
        return overBodyGarmentRankers;
    }

    public OverBodyGarmentType getOverBodyGarmentType() {
        return overBodyGarmentType;
    }
}
