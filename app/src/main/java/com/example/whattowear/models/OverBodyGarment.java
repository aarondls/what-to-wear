package com.example.whattowear.models;

import com.example.whattowear.R;

public class OverBodyGarment {
    private OverBodyGarmentType overBodyGarmentType;

    private static enum OverBodyGarmentType {
        ATHLETIC_JACKET, RAIN_JACKET, WINTER_COAT, NONE
    }

    // Indexing of this is based on the order declaration of the enum
    private static ClothingRanker athleticJacketRanker;
    private static ClothingRanker rainJacketRanker;
    private static ClothingRanker winterCoatRanker;

    // number of over body garment rankers as defined above
    public static final int OVER_BODY_GARMENT_RANKERS_COUNT = 3;

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
     * Initializes all the over body garment rankers
     */
    public static void initializeOverBodyGarmentRankers() {
        // TODO: fix icons
        // TODO: get from parse
        athleticJacketRanker = new ClothingRanker("Athletic jacket", R.drawable.hoodie);
        rainJacketRanker = new ClothingRanker("Rain jacket", R.drawable.hoodie);
        winterCoatRanker = new ClothingRanker("Winter coat", R.drawable.hoodie);
    }

    /**
     * @param position the index of the desired over body garment ranker, which is based on its declaration within the enum OverBodyGarmentType
     * @return the over body garment ranker at the index position
     */
    public static ClothingRanker getOverBodyGarmentRankerAtPosition(int position) {
        switch(position) {
            case 0:
                return athleticJacketRanker;
            case 1:
                return rainJacketRanker;
            case 2:
                return winterCoatRanker;
            default:
                // Initialization inside Clothing will ensure this will not be null
                // Improper use of this function will return null
                return null;
        }
    }
}
