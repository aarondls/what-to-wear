package com.example.whattowear.models;

import com.example.whattowear.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class LowerBodyGarment {
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

        lowerBodyGarment.lowerBodyGarmentType = LowerBodyGarmentType.SWEATS;

        return lowerBodyGarment;
    }

    /**
     * @return the ID of the local file of the lower body garment image
     */
    public java.lang.Integer getLowerBodyGarmentImage() {
        // TODO: return correct image based on lower body garment
        return R.drawable.sweats;
    }

    /**
     * @param clothingTypeName the clothingTypeName of the desired icon ID to get
     * @return the ID of the local file of the upper body garment image, if
     * it exists, based on the given clothingTypeName. Otherwise, this
     * returns null.
     */
    public static Integer getLowerBodyGarmentImage(String clothingTypeName) {
        // TODO: return correct image based on type
        if (clothingTypeName.equals("Dress pants")) {
            return R.drawable.sweats;
        } else if (clothingTypeName.equals("Jeans")) {
            return R.drawable.sweats;
        } else if (clothingTypeName.equals("Shorts")) {
            return R.drawable.sweats;
        } else if (clothingTypeName.equals("Skirt")) {
            return R.drawable.sweats;
        } else if (clothingTypeName.equals("Sweats")) {
            return R.drawable.sweats;
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
        // TODO: fix icons
        // TODO: get from parse
        ClothingRanker dressPantsRanker = new ClothingRanker();
        dressPantsRanker.initializeFactorsToDefault("Dress pants", R.drawable.sweats, user);
        ClothingRanker jeansRanker = new ClothingRanker();
        jeansRanker.initializeFactorsToDefault("Jeans", R.drawable.sweats, user);
        ClothingRanker shortsRanker = new ClothingRanker();
        shortsRanker.initializeFactorsToDefault("Shorts", R.drawable.sweats, user);
        ClothingRanker skirtRanker = new ClothingRanker();
        skirtRanker.initializeFactorsToDefault("Skirt", R.drawable.sweats, user);
        ClothingRanker sweatsRanker = new ClothingRanker();
        sweatsRanker.initializeFactorsToDefault("Sweats", R.drawable.sweats, user);

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
}
