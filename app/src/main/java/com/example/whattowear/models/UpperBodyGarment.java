package com.example.whattowear.models;

import com.example.whattowear.R;

public class UpperBodyGarment {
    private UpperBodyGarmentType upperBodyGarmentType;

    private static enum UpperBodyGarmentType {
        T_SHIRT, POLO_SHIRT, DRESS_SHIRT, BLOUSE,
    }

    // Indexing of this is based on the order declaration of the enum
    private static ClothingRanker tShirtRanker;
    private static ClothingRanker poloShirtRanker;
    private static ClothingRanker dressShirtRanker;
    private static ClothingRanker blouseRanker;

    // number of upper body garment rankers as defined above
    public static final int UPPER_BODY_GARMENT_RANKERS_COUNT = 4;

    /**
     * Calculates the optimal upper body garment based on weather,
     * activity, and preferences, then returns it
     * @return the calculated optimal upper body garment
     */
    public static UpperBodyGarment getOptimalUpperBodyGarment() {
        // TODO: update type based on weather, activity, and preferences
        // for testing, default to T_SHIRT

        UpperBodyGarment upperBodyGarment = new UpperBodyGarment();

        upperBodyGarment.upperBodyGarmentType = UpperBodyGarmentType.T_SHIRT;

        return upperBodyGarment;
    }

    /**
     * @return the ID of the local file of the over body garment image
     */
    public java.lang.Integer getUpperBodyGarmentImage() {
        // TODO: return correct image based on type

        return R.drawable.t_shirt;
    }

    /**
     * Initializes all the upper body garment rankers
     */
    public static void initializeUpperBodyGarmentRankers() {
        // TODO: fix icons
        // TODO: get from parse
        tShirtRanker = new ClothingRanker("T shirt", R.drawable.t_shirt);
        poloShirtRanker = new ClothingRanker("Polo shirt", R.drawable.t_shirt);
        dressShirtRanker = new ClothingRanker("Dress shirt", R.drawable.t_shirt);
        blouseRanker = new ClothingRanker("Blouse", R.drawable.t_shirt);
    }

    /**
     * @param position the index of the desired upper body garment ranker, which is based on its declaration within the enum UpperBodyGarmentType
     * @return the upper body garment ranker at the index position
     */
    public static ClothingRanker getUpperBodyGarmentRankerAtPosition(int position) {
        switch(position) {
            case 0:
                return tShirtRanker;
            case 1:
                return poloShirtRanker;
            case 2:
                return dressShirtRanker;
            case 3:
                return blouseRanker;
            default:
                return null;
        }
    }
}
