package com.example.whattowear.models;

import com.example.whattowear.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class LowerBodyGarment {
    private static final int DRESS_PANTS_TEMPERATURE_LOWER_RANGE = 0;
    private static final int DRESS_PANTS_TEMPERATURE_UPPER_RANGE = 60;
    private static final int DRESS_PANTS_ACTIVITY_IMPORTANCE = 9;
    private static final int DRESS_PANTS_WORK_ACTIVITY_FACTOR = 10;
    private static final int DRESS_PANTS_SPORTS_ACTIVITY_FACTOR = 0;
    private static final int DRESS_PANTS_CASUAL_ACTIVITY_FACTOR = 0;

    private static final int JEANS_TEMPERATURE_LOWER_RANGE = 0;
    private static final int JEANS_TEMPERATURE_UPPER_RANGE = 70;
    private static final int JEANS_ACTIVITY_IMPORTANCE = 7;
    private static final int JEANS_WORK_ACTIVITY_FACTOR = 4;
    private static final int JEANS_SPORTS_ACTIVITY_FACTOR = 0;
    private static final int JEANS_CASUAL_ACTIVITY_FACTOR = 10;

    private static final int SHORTS_TEMPERATURE_IMPORTANCE = 8;
    private static final int SHORTS_TEMPERATURE_LOWER_RANGE = 70;
    private static final int SHORTS_TEMPERATURE_UPPER_RANGE = 100;
    private static final int SHORTS_ACTIVITY_IMPORTANCE = 9;
    private static final int SHORTS_WORK_ACTIVITY_FACTOR = 2;
    private static final int SHORTS_SPORTS_ACTIVITY_FACTOR = 10;
    private static final int SHORTS_CASUAL_ACTIVITY_FACTOR = 8;

    private static final int SKIRT_TEMPERATURE_IMPORTANCE = 8;
    private static final int SKIRT_TEMPERATURE_LOWER_RANGE = 70;
    private static final int SKIRT_TEMPERATURE_UPPER_RANGE = 100;
    private static final int SKIRT_WORK_ACTIVITY_FACTOR = 7;
    private static final int SKIRT_SPORTS_ACTIVITY_FACTOR = 2;
    private static final int SKIRT_CASUAL_ACTIVITY_FACTOR = 9;

    private static final int SWEATS_TEMPERATURE_LOWER_RANGE = 0;
    private static final int SWEATS_TEMPERATURE_UPPER_RANGE = 60;
    private static final int SWEATS_ACTIVITY_IMPORTANCE = 9;
    private static final int SWEATS_WORK_ACTIVITY_FACTOR = 0;
    private static final int SWEATS_SPORTS_ACTIVITY_FACTOR = 10;
    private static final int SWEATS_CASUAL_ACTIVITY_FACTOR = 8;


    private LowerBodyGarmentType lowerBodyGarmentType;

    private static enum LowerBodyGarmentType {
        DRESS_PANTS, JEANS, SHORTS, SKIRT, SWEATS
    }

    // Indexing of this is based on the order declaration of the enum
    private static List<ClothingRanker> lowerBodyGarmentRankers;

    /**
     * Calculates the optimal lower body garment based on weather,
     * activity, and preferences, then returns it
     * @return the calculated optimal lower body garment
     */
    public static LowerBodyGarment getOptimalLowerBodyGarment() {
        // TODO: update type based on weather, activity, and preferences
        // for testing, default to shorts

        LowerBodyGarment lowerBodyGarment = new LowerBodyGarment();

        lowerBodyGarment.lowerBodyGarmentType = ClothingRanker.getOptimalClothingType(lowerBodyGarmentRankers, LowerBodyGarmentType.values()).first;

        return lowerBodyGarment;
    }

    /**
     * @return the ID of the local file of the lower body garment image
     */
    public Integer getLowerBodyGarmentImage() {
        return getLowerBodyGarmentImage(lowerBodyGarmentType.name());
    }

    /**
     * @param clothingTypeName the clothingTypeName of the desired icon ID to get
     * @return the ID of the local file of the upper body garment image, if
     * it exists, based on the given clothingTypeName. Otherwise, this
     * returns null.
     */
    public static Integer getLowerBodyGarmentImage(String clothingTypeName) {
        if (clothingTypeName.equals(LowerBodyGarmentType.DRESS_PANTS.name())) {
            return R.drawable.clothing_icon_dress_pants;
        } else if (clothingTypeName.equals(LowerBodyGarmentType.JEANS.name())) {
            return R.drawable.clothing_icon_jeans;
        } else if (clothingTypeName.equals(LowerBodyGarmentType.SHORTS.name())) {
            return R.drawable.clothing_icon_shorts;
        } else if (clothingTypeName.equals(LowerBodyGarmentType.SKIRT.name())) {
            return R.drawable.clothing_icon_skirt;
        } else if (clothingTypeName.equals(LowerBodyGarmentType.SWEATS.name())) {
            return R.drawable.clothing_icon_sweatpants;
        }
        // unrecognized return null
        return null;
    }

    /**
     * Sets the lowerBodyGarmentRankers and fetches any missing information from the Parse database, if needed
     * @param lowerBodyGarmentRankers the lowerBodyGarmentRankers to set
     * @throws ParseException the non-null ParseException, if fetching the missing information fails
     */
    public static void setLowerBodyGarmentRankers(List<ClothingRanker> lowerBodyGarmentRankers) throws ParseException {
        LowerBodyGarment.lowerBodyGarmentRankers = lowerBodyGarmentRankers;

        try {
            ClothingRanker.fetchAllIfNeeded(lowerBodyGarmentRankers);
        } catch (ParseException e) {
            throw e;
        }

        for (ClothingRanker clothingRanker : lowerBodyGarmentRankers) {
            clothingRanker.setClothingTypeIconID(getLowerBodyGarmentImage(clothingRanker.getClothingTypeName()));
        }
    }

    /**
     * Initializes all the lower body garment rankers with default factors
     * @param user the ParseUser owning the lower body garments
     * @return the list of initialized clothing rankers for lower body garments with default factors
     */
    public static List<ClothingRanker> initializeLowerBodyGarmentRankers(ParseUser user) {
        ClothingRanker dressPantsRanker = new ClothingRanker();
        dressPantsRanker.initializeFactorsToDefault(LowerBodyGarmentType.DRESS_PANTS.name(), R.drawable.clothing_icon_dress_pants, user);
        ClothingRanker jeansRanker = new ClothingRanker();
        jeansRanker.initializeFactorsToDefault(LowerBodyGarmentType.JEANS.name(), R.drawable.clothing_icon_jeans, user);
        ClothingRanker shortsRanker = new ClothingRanker();
        shortsRanker.initializeFactorsToDefault(LowerBodyGarmentType.SHORTS.name(), R.drawable.clothing_icon_shorts, user);
        ClothingRanker skirtRanker = new ClothingRanker();
        skirtRanker.initializeFactorsToDefault(LowerBodyGarmentType.SKIRT.name(), R.drawable.clothing_icon_skirt, user);
        ClothingRanker sweatsRanker = new ClothingRanker();
        sweatsRanker.initializeFactorsToDefault(LowerBodyGarmentType.SWEATS.name(), R.drawable.clothing_icon_sweatpants, user);

        dressPantsRanker.setTemperatureLowerRange(DRESS_PANTS_TEMPERATURE_LOWER_RANGE);
        dressPantsRanker.setTemperatureUpperRange(DRESS_PANTS_TEMPERATURE_UPPER_RANGE);
        dressPantsRanker.setActivityImportance(DRESS_PANTS_ACTIVITY_IMPORTANCE);
        dressPantsRanker.setWorkActivityFactor(DRESS_PANTS_WORK_ACTIVITY_FACTOR);
        dressPantsRanker.setSportsActivityFactor(DRESS_PANTS_SPORTS_ACTIVITY_FACTOR);
        dressPantsRanker.setCasualActivityFactor(DRESS_PANTS_CASUAL_ACTIVITY_FACTOR);

        jeansRanker.setTemperatureLowerRange(JEANS_TEMPERATURE_LOWER_RANGE);
        jeansRanker.setTemperatureUpperRange(JEANS_TEMPERATURE_UPPER_RANGE);
        jeansRanker.setActivityImportance(JEANS_ACTIVITY_IMPORTANCE);
        jeansRanker.setWorkActivityFactor(JEANS_WORK_ACTIVITY_FACTOR);
        jeansRanker.setSportsActivityFactor(JEANS_SPORTS_ACTIVITY_FACTOR);
        jeansRanker.setCasualActivityFactor(JEANS_CASUAL_ACTIVITY_FACTOR);

        shortsRanker.setTemperatureImportance(SHORTS_TEMPERATURE_IMPORTANCE);
        shortsRanker.setTemperatureLowerRange(SHORTS_TEMPERATURE_LOWER_RANGE);
        shortsRanker.setTemperatureUpperRange(SHORTS_TEMPERATURE_UPPER_RANGE);
        shortsRanker.setActivityImportance(SHORTS_ACTIVITY_IMPORTANCE);
        shortsRanker.setWorkActivityFactor(SHORTS_WORK_ACTIVITY_FACTOR);
        shortsRanker.setSportsActivityFactor(SHORTS_SPORTS_ACTIVITY_FACTOR);
        shortsRanker.setCasualActivityFactor(SHORTS_CASUAL_ACTIVITY_FACTOR);

        skirtRanker.setTemperatureImportance(SKIRT_TEMPERATURE_IMPORTANCE);
        skirtRanker.setTemperatureLowerRange(SKIRT_TEMPERATURE_LOWER_RANGE);
        skirtRanker.setTemperatureUpperRange(SKIRT_TEMPERATURE_UPPER_RANGE);
        skirtRanker.setWorkActivityFactor(SKIRT_WORK_ACTIVITY_FACTOR);
        skirtRanker.setSportsActivityFactor(SKIRT_SPORTS_ACTIVITY_FACTOR);
        skirtRanker.setCasualActivityFactor(SKIRT_CASUAL_ACTIVITY_FACTOR);

        sweatsRanker.setTemperatureLowerRange(SWEATS_TEMPERATURE_LOWER_RANGE);
        sweatsRanker.setTemperatureUpperRange(SWEATS_TEMPERATURE_UPPER_RANGE);
        sweatsRanker.setActivityImportance(SWEATS_ACTIVITY_IMPORTANCE);
        sweatsRanker.setWorkActivityFactor(SWEATS_WORK_ACTIVITY_FACTOR);
        sweatsRanker.setSportsActivityFactor(SWEATS_SPORTS_ACTIVITY_FACTOR);
        sweatsRanker.setCasualActivityFactor(SWEATS_CASUAL_ACTIVITY_FACTOR);

        lowerBodyGarmentRankers = new ArrayList<>();
        lowerBodyGarmentRankers.add(dressPantsRanker);
        lowerBodyGarmentRankers.add(jeansRanker);
        lowerBodyGarmentRankers.add(shortsRanker);
        lowerBodyGarmentRankers.add(skirtRanker);
        lowerBodyGarmentRankers.add(sweatsRanker);

        return lowerBodyGarmentRankers;
    }

    /**
     * @return the list of clothing rankers for lower body garments, in the order of which their type is declared within the lowerBodyGarmentType enum
     */
    public static List<ClothingRanker> getLowerBodyGarmentRankers() {
        return lowerBodyGarmentRankers;
    }

    public LowerBodyGarmentType getLowerBodyGarmentType() {
        return lowerBodyGarmentType;
    }
}
