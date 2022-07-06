package com.example.whattowear.models;

import com.example.whattowear.R;

public class LowerBodyGarment {
    private LowerBodyGarmentType lowerBodyGarmentType;

    private static enum LowerBodyGarmentType {
        PANTS, SHORTS, SKIRT, SWEATS
    }

    // Indexing of this is based on the order declaration of the enum
    private static ClothingRanker pantsRanker;
    private static ClothingRanker shortsRanker;
    private static ClothingRanker skirtRanker;
    private static ClothingRanker sweatsRanker;

    // number of lower body garment rankers as defined above
    public static final int LOWER_BODY_GARMENT_RANKERS_COUNT = 4;

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
        pantsRanker = new ClothingRanker("Pants", R.drawable.sweats);
        shortsRanker = new ClothingRanker("Shorts", R.drawable.sweats);
        skirtRanker = new ClothingRanker("Skirt", R.drawable.sweats);
        sweatsRanker = new ClothingRanker("Sweats", R.drawable.sweats);
    }

    /**
     * @param position the index of the desired lower body garment ranker, which is based on its declaration within the enum LowerBodyGarmentType
     * @return the lower body garment ranker at the index position
     */
    public static ClothingRanker getLowerBodyGarmentRankerAtPosition(int position) {
        switch(position) {
            case 0:
                return pantsRanker;
            case 1:
                return shortsRanker;
            case 2:
                return skirtRanker;
            case 3:
                return sweatsRanker;
            default:
                return null;
        }
    }
}
