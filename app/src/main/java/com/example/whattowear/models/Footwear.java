package com.example.whattowear.models;

import android.util.Log;

import com.example.whattowear.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Footwear {
    private static final String TAG = "Footwear";

    private static final int SNEAKERS_TEMPERATURE_LOWER_RANGE = 50;
    private static final int SNEAKERS_TEMPERATURE_UPPER_RANGE = 100;
    private static final int SNEAKERS_ACTIVITY_IMPORTANCE = 9;
    private static final int SNEAKERS_WORK_ACTIVITY_FACTOR = 0;
    private static final int SNEAKERS_SPORTS_ACTIVITY_FACTOR = 10;
    private static final int SNEAKERS_CASUAL_ACTIVITY_FACTOR = 6;

    private static final int BOAT_TEMPERATURE_LOWER_RANGE = 70;
    private static final int BOAT_TEMPERATURE_UPPER_RANGE = 100;
    private static final int BOAT_ACTIVITY_IMPORTANCE = 7;
    private static final int BOAT_WORK_ACTIVITY_FACTOR = 7;
    private static final int BOAT_SPORTS_ACTIVITY_FACTOR = 0;
    private static final int BOAT_CASUAL_ACTIVITY_FACTOR = 10;

    private static final int LOAFER_TEMPERATURE_LOWER_RANGE = 0;
    private static final int LOAFER_TEMPERATURE_UPPER_RANGE = 70;
    private static final int LOAFER_ACTIVITY_IMPORTANCE = 7;
    private static final int LOAFER_WORK_ACTIVITY_FACTOR = 9;
    private static final int LOAFER_SPORTS_ACTIVITY_FACTOR = 0;

    private static final int SANDALS_TEMPERATURE_LOWER_RANGE = 80;
    private static final int SANDALS_TEMPERATURE_UPPER_RANGE = 100;
    private static final int SANDALS_WORK_ACTIVITY_FACTOR = 0;
    private static final int SANDALS_SPORTS_ACTIVITY_FACTOR = 0;
    private static final int SANDALS_CASUAL_ACTIVITY_FACTOR = 5;

    private static final int BOOTS_TEMPERATURE_IMPORTANCE = 8;
    private static final int BOOTS_TEMPERATURE_LOWER_RANGE = 0;
    private static final int BOOTS_TEMPERATURE_UPPER_RANGE = 40;
    private static final int BOOTS_SPORTS_ACTIVITY_FACTOR = 0;
    private static final int BOOTS_WEATHER_IMPORTANCE = 10;
    private static final int BOOTS_CONDITION_THUNDERSTORM_FACTOR = 9;
    private static final int BOOTS_CONDITIONS_DRIZZLE_FACTOR = 8;
    private static final int BOOTS_CONDITIONS_RAIN_FACTOR = 9;
    private static final int BOOTS_CONDITIONS_SNOW_FACTOR = 10;

    private static final int HEELS_TEMPERATURE_LOWER_RANGE = 50;
    private static final int HEELS_TEMPERATURE_UPPER_RANGE = 100;
    private static final int HEELS_WORK_ACTIVITY_FACTOR = 9;
    private static final int HEELS_SPORTS_ACTIVITY_FACTOR = 0;
    private static final int HEELS_CASUAL_ACTIVITY_FACTOR = 3;

    private FootwearType footwearType;

    private static enum FootwearType {
        SNEAKERS, BOAT, LOAFER, SANDALS, BOOTS, HEELS
    }

    // Indexing of this is based on the order declaration of the enum
    private static List<ClothingRanker> footwearRankers;

    /**
     * Calculates the optimal footwear based on weather, activity,
     * and preferences, then returns it
     * @return the calculated optimal footwear
     */
    public static Footwear getOptimalFootwear() {
        // TODO: update footwear type based on weather, activity, and preferences
        // for testing, default to sneakers

        Footwear footwear = new Footwear();

        footwear.footwearType = ClothingRanker.getOptimalClothingType(footwearRankers, FootwearType.values()).first;

        Log.i(TAG, "Finished calculating footwear");
        return footwear;
    }

    /**
     * @return the ID of the local file of the footwear image
     */
    public java.lang.Integer getFootwearImage() {
        // TODO: return correct image based on footwear
        return R.drawable.rubber_shoes;
    }

    /**
     * @param clothingTypeName the clothingTypeName of the desired icon ID to get
     * @return the ID of the local file of the upper body garment image, if
     * it exists, based on the given clothingTypeName. Otherwise, this
     * returns null.
     */
    public static Integer getFootwearImage(String clothingTypeName) {
        // TODO: return correct image based on type
        if (clothingTypeName.equals("Sneakers")) {
            return R.drawable.rubber_shoes;
        } else if (clothingTypeName.equals("Boat")) {
            return R.drawable.rubber_shoes;
        } else if (clothingTypeName.equals("Loafer")) {
            return R.drawable.rubber_shoes;
        } else if (clothingTypeName.equals("Sandals")) {
            return R.drawable.rubber_shoes;
        } else if (clothingTypeName.equals("Boots")) {
            return R.drawable.rubber_shoes;
        } else if (clothingTypeName.equals("Heels")) {
            return R.drawable.rubber_shoes;
        }
        // unrecognized return null
        return null;
    }

    /**
     * Sets the footwearRankers and fetches any missing information from the Parse database, if needed
     * @param footwearRankers the footwearRankers to set
     * @throws ParseException the non-null ParseException, if fetching the missing information fails
     */
    public static void setFootwearRankers(List<ClothingRanker> footwearRankers) throws ParseException {
        Footwear.footwearRankers = footwearRankers;

        try {
            ClothingRanker.fetchAllIfNeeded(footwearRankers);
        } catch (ParseException e) {
            throw e;
        }

        for (ClothingRanker clothingRanker : footwearRankers) {
            clothingRanker.setClothingTypeIconID(getFootwearImage(clothingRanker.getClothingTypeName()));
        }
    }

    /**
     * Initializes all the footwear rankers with default factors
     * @param user the ParseUser owning the footwear
     * @return the list of initialized clothing rankers for footwear with default factors
     */
    public static List<ClothingRanker> initializeFootwearRankers(ParseUser user) {
        // TODO: fix icons
        // TODO: get from parse
        ClothingRanker sneakersRanker = new ClothingRanker();
        sneakersRanker.initializeFactorsToDefault("Sneakers", R.drawable.rubber_shoes, user);
        ClothingRanker boatRanker = new ClothingRanker();
        boatRanker.initializeFactorsToDefault("Boat", R.drawable.rubber_shoes, user);
        ClothingRanker loaferRanker = new ClothingRanker();
        loaferRanker.initializeFactorsToDefault("Loafer", R.drawable.rubber_shoes, user);
        ClothingRanker sandalsRanker = new ClothingRanker();
        sandalsRanker.initializeFactorsToDefault("Sandals", R.drawable.rubber_shoes, user);
        ClothingRanker bootsRanker = new ClothingRanker();
        bootsRanker.initializeFactorsToDefault("Boots", R.drawable.rubber_shoes, user);
        ClothingRanker heelsRanker = new ClothingRanker();
        heelsRanker.initializeFactorsToDefault("Heels", R.drawable.rubber_shoes, user);

        sneakersRanker.setTemperatureLowerRange(SNEAKERS_TEMPERATURE_LOWER_RANGE);
        sneakersRanker.setTemperatureUpperRange(SNEAKERS_TEMPERATURE_UPPER_RANGE);
        sneakersRanker.setActivityImportance(SNEAKERS_ACTIVITY_IMPORTANCE);
        sneakersRanker.setWorkActivityFactor(SNEAKERS_WORK_ACTIVITY_FACTOR);
        sneakersRanker.setSportsActivityFactor(SNEAKERS_SPORTS_ACTIVITY_FACTOR);
        sneakersRanker.setCasualActivityFactor(SNEAKERS_CASUAL_ACTIVITY_FACTOR);

        boatRanker.setTemperatureLowerRange(BOAT_TEMPERATURE_LOWER_RANGE);
        boatRanker.setTemperatureUpperRange(BOAT_TEMPERATURE_UPPER_RANGE);
        boatRanker.setActivityImportance(BOAT_ACTIVITY_IMPORTANCE);
        boatRanker.setWorkActivityFactor(BOAT_WORK_ACTIVITY_FACTOR);
        boatRanker.setSportsActivityFactor(BOAT_SPORTS_ACTIVITY_FACTOR);
        boatRanker.setCasualActivityFactor(BOAT_CASUAL_ACTIVITY_FACTOR);

        loaferRanker.setTemperatureLowerRange(LOAFER_TEMPERATURE_LOWER_RANGE);
        loaferRanker.setTemperatureUpperRange(LOAFER_TEMPERATURE_UPPER_RANGE);
        loaferRanker.setActivityImportance(LOAFER_ACTIVITY_IMPORTANCE);
        loaferRanker.setWorkActivityFactor(LOAFER_WORK_ACTIVITY_FACTOR);
        loaferRanker.setSportsActivityFactor(LOAFER_SPORTS_ACTIVITY_FACTOR);

        sandalsRanker.setTemperatureLowerRange(SANDALS_TEMPERATURE_LOWER_RANGE);
        sandalsRanker.setTemperatureUpperRange(SANDALS_TEMPERATURE_UPPER_RANGE);
        sandalsRanker.setWorkActivityFactor(SANDALS_WORK_ACTIVITY_FACTOR);
        sandalsRanker.setSportsActivityFactor(SANDALS_SPORTS_ACTIVITY_FACTOR);
        sandalsRanker.setCasualActivityFactor(SANDALS_CASUAL_ACTIVITY_FACTOR);

        bootsRanker.setTemperatureImportance(BOOTS_TEMPERATURE_IMPORTANCE);
        bootsRanker.setTemperatureLowerRange(BOOTS_TEMPERATURE_LOWER_RANGE);
        bootsRanker.setTemperatureUpperRange(BOOTS_TEMPERATURE_UPPER_RANGE);
        bootsRanker.setSportsActivityFactor(BOOTS_SPORTS_ACTIVITY_FACTOR);
        bootsRanker.setWeatherImportance(BOOTS_WEATHER_IMPORTANCE);
        bootsRanker.setConditionsThunderstormFactor(BOOTS_CONDITION_THUNDERSTORM_FACTOR);
        bootsRanker.setConditionsDrizzleFactor(BOOTS_CONDITIONS_DRIZZLE_FACTOR);
        bootsRanker.setConditionsRainFactor(BOOTS_CONDITIONS_RAIN_FACTOR);
        bootsRanker.setConditionsSnowFactor(BOOTS_CONDITIONS_SNOW_FACTOR);

        heelsRanker.setTemperatureLowerRange(HEELS_TEMPERATURE_LOWER_RANGE);
        heelsRanker.setTemperatureUpperRange(HEELS_TEMPERATURE_UPPER_RANGE);
        heelsRanker.setWorkActivityFactor(HEELS_WORK_ACTIVITY_FACTOR);
        heelsRanker.setSportsActivityFactor(HEELS_SPORTS_ACTIVITY_FACTOR);
        heelsRanker.setCasualActivityFactor(HEELS_CASUAL_ACTIVITY_FACTOR);

        footwearRankers = new ArrayList<>();
        footwearRankers.add(sneakersRanker);
        footwearRankers.add(boatRanker);
        footwearRankers.add(loaferRanker);
        footwearRankers.add(sandalsRanker);
        footwearRankers.add(bootsRanker);
        footwearRankers.add(heelsRanker);

        return footwearRankers;
    }

    /**
     * @return the list of clothing rankers for footwear, in the order of which their type is declared within the FootwearType enum
     */
    public static List<ClothingRanker> getFootwearRankers() {
        return footwearRankers;
    }

    public FootwearType getFootwearType() {
        return footwearType;
    }
}
