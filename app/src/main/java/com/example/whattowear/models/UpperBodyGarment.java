package com.example.whattowear.models;

import com.example.whattowear.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UpperBodyGarment {
    private static final int T_SHIRT_TEMPERATURE_IMPORTANCE = 3;
    private static final int T_SHIRT_WORK_ACTIVITY_FACTOR = 0;
    private static final int T_SHIRT_CASUAL_ACTIVITY_FACTOR = 9;

    private static final int POLO_SHIRT_TEMPERATURE_LOWER_RANGE = 0;
    private static final int POLO_SHIRT_TEMPERATURE_UPPER_RANGE = 70;
    private static final int POLO_SHIRT_ACTIVITY_IMPORTANCE = 9;
    private static final int POLO_SHIRT_WORK_ACTIVITY_FACTOR = 10;
    private static final int POLO_SHIRT_SPORTS_ACTIVITY_FACTOR = 0;

    private static final int DRESS_SHIRT_TEMPERATURE_LOWER_RANGE = 0;
    private static final int DRESS_SHIRT_TEMPERATURE_UPPER_RANGE = 60;
    private static final int DRESS_SHIRT_ACTIVITY_IMPORTANCE = 9;
    private static final int DRESS_SHIRT_WORK_ACTIVITY_FACTOR = 10;
    private static final int DRESS_SHIRT_SPORTS_ACTIVITY_FACTOR = 0;
    private static final int DRESS_SHIRT_CASUAL_ACTIVITY_FACTOR = 0;

    private static final int BLOUSE_TEMPERATURE_IMPORTANCE = 3;
    private static final int BLOUSE_ACTIVITY_IMPORTANCE = 9;
    private static final int BLOUSE_WORK_ACTIVITY_FACTOR = 10;
    private static final int BLOUSE_SPORTS_ACTIVITY_FACTOR = 0;

    private UpperBodyGarmentType upperBodyGarmentType;

    private static enum UpperBodyGarmentType {
        T_SHIRT, POLO_SHIRT, DRESS_SHIRT, BLOUSE,
    }

    // Indexing of this is based on the order declaration of the enum
    private static List<ClothingRanker> upperBodyGarmentRankers;

    /**
     * Calculates the optimal upper body garment based on weather,
     * activity, and preferences, then returns it
     * @return the calculated optimal upper body garment
     */
    public static UpperBodyGarment getOptimalUpperBodyGarment() {
        // TODO: update type based on weather, activity, and preferences
        // for testing, default to T_SHIRT

        UpperBodyGarment upperBodyGarment = new UpperBodyGarment();

        upperBodyGarment.upperBodyGarmentType = ClothingRanker.getOptimalClothingType(upperBodyGarmentRankers, UpperBodyGarmentType.values()).first;

        return upperBodyGarment;
    }

    /**
     * @return the ID of the local file of the over body garment image
     */
    public Integer getUpperBodyGarmentImage() {
        return getUpperBodyGarmentImage(upperBodyGarmentType.name());
    }

    /**
     * @param clothingTypeName the clothingTypeName of the desired icon ID to get
     * @return the ID of the local file of the upper body garment image, if
     * it exists, based on the given clothingTypeName. Otherwise, this
     * returns null.
     */
    public static Integer getUpperBodyGarmentImage(String clothingTypeName) {
        if (clothingTypeName.equals(UpperBodyGarmentType.T_SHIRT.name())) {
            return R.drawable.clothing_icon_t_shirt;
        } else if (clothingTypeName.equals(UpperBodyGarmentType.POLO_SHIRT.name())) {
            return R.drawable.clothing_icon_polo_shirt;
        } else if (clothingTypeName.equals(UpperBodyGarmentType.DRESS_SHIRT.name())) {
            return R.drawable.clothing_icon_dress_shirt;
        } else if (clothingTypeName.equals(UpperBodyGarmentType.BLOUSE.name())) {
            return R.drawable.clothing_icon_blouse;
        }
        // unrecognized return null
        // this is unreachable since if defined correctly, all of the enum types have been enumerated
        return null;
    }

    /**
     * Sets the upperBodyGarmentRankers and fetches any missing information from the Parse database, if needed
     * @param upperBodyGarmentRankers the upperBodyGarmentRankers to set
     * @throws ParseException the non-null ParseException, if fetching the missing information fails
     */
    public static void setUpperBodyGarmentRankers(List<ClothingRanker> upperBodyGarmentRankers) throws ParseException {
        UpperBodyGarment.upperBodyGarmentRankers = upperBodyGarmentRankers;

        try {
            ClothingRanker.fetchAllIfNeeded(upperBodyGarmentRankers);
        } catch (ParseException e) {
            throw e;
        }

        for (ClothingRanker clothingRanker : upperBodyGarmentRankers) {
            clothingRanker.setClothingTypeIconID(getUpperBodyGarmentImage(clothingRanker.getClothingTypeName()));
        }
    }

    /**
     * Initializes all the upper body garment rankers with default factors
     * @param user the ParseUser owning the upper body garments
     * @return the list of initialized clothing rankers for upper body garments with default factors
     */
    public static List<ClothingRanker> initializeUpperBodyGarmentRankers(ParseUser user) {
        ClothingRanker tShirtRanker = new ClothingRanker();
        tShirtRanker.initializeFactorsToDefault(UpperBodyGarmentType.T_SHIRT.name(), R.drawable.clothing_icon_t_shirt, user);
        ClothingRanker poloShirtRanker = new ClothingRanker();
        poloShirtRanker.initializeFactorsToDefault(UpperBodyGarmentType.POLO_SHIRT.name(), R.drawable.clothing_icon_polo_shirt, user);
        ClothingRanker dressShirtRanker = new ClothingRanker();
        dressShirtRanker.initializeFactorsToDefault(UpperBodyGarmentType.DRESS_SHIRT.name(), R.drawable.clothing_icon_dress_shirt, user);
        ClothingRanker blouseRanker = new ClothingRanker();
        blouseRanker.initializeFactorsToDefault(UpperBodyGarmentType.BLOUSE.name(), R.drawable.clothing_icon_blouse, user);

        tShirtRanker.setTemperatureImportance(T_SHIRT_TEMPERATURE_IMPORTANCE);
        tShirtRanker.setWorkActivityFactor(T_SHIRT_WORK_ACTIVITY_FACTOR);
        tShirtRanker.setCasualActivityFactor(T_SHIRT_CASUAL_ACTIVITY_FACTOR);

        poloShirtRanker.setTemperatureLowerRange(POLO_SHIRT_TEMPERATURE_LOWER_RANGE);
        poloShirtRanker.setTemperatureUpperRange(POLO_SHIRT_TEMPERATURE_UPPER_RANGE);
        poloShirtRanker.setActivityImportance(POLO_SHIRT_ACTIVITY_IMPORTANCE);
        poloShirtRanker.setWorkActivityFactor(POLO_SHIRT_WORK_ACTIVITY_FACTOR);
        poloShirtRanker.setSportsActivityFactor(POLO_SHIRT_SPORTS_ACTIVITY_FACTOR);

        dressShirtRanker.setTemperatureLowerRange(DRESS_SHIRT_TEMPERATURE_LOWER_RANGE);
        dressShirtRanker.setTemperatureUpperRange(DRESS_SHIRT_TEMPERATURE_UPPER_RANGE);
        dressShirtRanker.setActivityImportance(DRESS_SHIRT_ACTIVITY_IMPORTANCE);
        dressShirtRanker.setWorkActivityFactor(DRESS_SHIRT_WORK_ACTIVITY_FACTOR);
        dressShirtRanker.setSportsActivityFactor(DRESS_SHIRT_SPORTS_ACTIVITY_FACTOR);
        dressShirtRanker.setCasualActivityFactor(DRESS_SHIRT_CASUAL_ACTIVITY_FACTOR);

        blouseRanker.setTemperatureImportance(BLOUSE_TEMPERATURE_IMPORTANCE);
        blouseRanker.setActivityImportance(BLOUSE_ACTIVITY_IMPORTANCE);
        blouseRanker.setWorkActivityFactor(BLOUSE_WORK_ACTIVITY_FACTOR);
        blouseRanker.setSportsActivityFactor(BLOUSE_SPORTS_ACTIVITY_FACTOR);

        upperBodyGarmentRankers = new ArrayList<>();
        upperBodyGarmentRankers.add(tShirtRanker);
        upperBodyGarmentRankers.add(poloShirtRanker);
        upperBodyGarmentRankers.add(dressShirtRanker);
        upperBodyGarmentRankers.add(blouseRanker);

        return upperBodyGarmentRankers;
    }

    /**
     * @return the list of clothing rankers for upper body garments, in the order of which their type is declared within the UpperBodyGarmentType enum
     */
    public static List<ClothingRanker> getUpperBodyGarmentRankers() {
        return upperBodyGarmentRankers;
    }

    public UpperBodyGarmentType getUpperBodyGarmentType() {
        return upperBodyGarmentType;
    }
}
