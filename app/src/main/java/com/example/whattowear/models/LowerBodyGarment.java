package com.example.whattowear.models;

import com.example.whattowear.R;

import java.util.ArrayList;
import java.util.List;

public class LowerBodyGarment {
    private LowerBodyGarmentType lowerBodyGarmentType;

    private static enum LowerBodyGarmentType {
        PANTS, SHORTS, SKIRT, SWEATS
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
     * Initializes all the lower body garment rankers
     */
    public static void initializeLowerBodyGarmentRankers() {
        // TODO: fix icons
        // TODO: get from parse
        ClothingRanker pantsRanker = new ClothingRanker("Pants", R.drawable.sweats);
        ClothingRanker shortsRanker = new ClothingRanker("Shorts", R.drawable.sweats);
        ClothingRanker skirtRanker = new ClothingRanker("Skirt", R.drawable.sweats);
        ClothingRanker sweatsRanker = new ClothingRanker("Sweats", R.drawable.sweats);

        lowerBodyGarmentRankers = new ArrayList<>();
        lowerBodyGarmentRankers.add(pantsRanker);
        lowerBodyGarmentRankers.add(shortsRanker);
        lowerBodyGarmentRankers.add(skirtRanker);
        lowerBodyGarmentRankers.add(sweatsRanker);
    }

    /**
     * @return the list of clothing rankers for lower body garments, in the order of which their type is declared within the lowerBodyGarmentType enum
     */
    public static List<ClothingRanker> getLowerBodyGarmentRankers() {
        return lowerBodyGarmentRankers;
    }

    /**
     * @param position the index of the lower body garment within the list lowerBodyGarmentRankers
     * @return the LowerBodyGarmentType of the ClothingRanker at the index position within the list lowerBodyGarmentRankers
     */
    public static LowerBodyGarmentType getLowerBodyGarmentTypeAtPosition(int position) {
        switch(position) {
            case 0:
                return LowerBodyGarmentType.PANTS;
            case 1:
                return LowerBodyGarmentType.SHORTS;
            case 2:
                return LowerBodyGarmentType.SKIRT;
            case 3:
                return LowerBodyGarmentType.SWEATS;
            default:
                return null;
        }
    }
}
