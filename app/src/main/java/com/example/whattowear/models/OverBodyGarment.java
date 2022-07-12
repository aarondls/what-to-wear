package com.example.whattowear.models;

import com.example.whattowear.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class OverBodyGarment {
    private OverBodyGarmentType overBodyGarmentType;

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

        overBodyGarment.overBodyGarmentType = OverBodyGarmentType.NONE;

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

        return R.drawable.hoodie;
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
        // TODO: get from parse
        ClothingRanker athleticJacketRanker  = new ClothingRanker();
        athleticJacketRanker.initializeFactorsToDefault("Athletic jacket", R.drawable.hoodie, user);
        ClothingRanker rainJacketRanker  = new ClothingRanker();
        rainJacketRanker.initializeFactorsToDefault("Rain jacket", R.drawable.hoodie, user);
        ClothingRanker winterCoatRanker  = new ClothingRanker();
        winterCoatRanker.initializeFactorsToDefault("Winter coat", R.drawable.hoodie, user);

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
