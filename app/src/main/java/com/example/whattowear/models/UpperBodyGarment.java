package com.example.whattowear.models;

import com.example.whattowear.R;

import java.util.ArrayList;
import java.util.List;

public class UpperBodyGarment {
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
        ClothingRanker tShirtRanker = new ClothingRanker("T shirt", R.drawable.t_shirt);
        ClothingRanker poloShirtRanker = new ClothingRanker("Polo shirt", R.drawable.t_shirt);
        ClothingRanker dressShirtRanker = new ClothingRanker("Dress shirt", R.drawable.t_shirt);
        ClothingRanker blouseRanker = new ClothingRanker("Blouse", R.drawable.t_shirt);

        upperBodyGarmentRankers = new ArrayList<>();
        upperBodyGarmentRankers.add(tShirtRanker);
        upperBodyGarmentRankers.add(poloShirtRanker);
        upperBodyGarmentRankers.add(dressShirtRanker);
        upperBodyGarmentRankers.add(blouseRanker);
    }

    /**
     * @return the list of clothing rankers for upper body garments, in the order of which their type is declared within the UpperBodyGarmentType enum
     */
    public static List<ClothingRanker> getUpperBodyGarmentRankers() {
        return upperBodyGarmentRankers;
    }

    /**
     * @param position the index of the upper body garment within the list upperBodyGarmentRankers
     * @return the UpperBodyGarmentType of the ClothingRanker at the index position within the list upperBodyGarmentRankers
     */
    public static UpperBodyGarmentType getUpperBodyGarmentTypeAtPosition(int position) {
        switch(position) {
            case 0:
                return UpperBodyGarmentType.T_SHIRT;
            case 1:
                return UpperBodyGarmentType.POLO_SHIRT;
            case 2:
                return UpperBodyGarmentType.DRESS_SHIRT;
            case 3:
                return UpperBodyGarmentType.BLOUSE;
            default:
                return null;
        }
    }
}
