package com.example.whattowear.models;

import com.example.whattowear.R;

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
     * Initializes all the over body garment rankers
     */
    public static void initializeOverBodyGarmentRankers() {
        // TODO: fix icons
        // TODO: get from parse
        ClothingRanker athleticJacketRanker  = new ClothingRanker("Athletic jacket", R.drawable.hoodie);
        ClothingRanker rainJacketRanker  = new ClothingRanker("Rain jacket", R.drawable.hoodie);
        ClothingRanker winterCoatRanker  = new ClothingRanker("Winter coat", R.drawable.hoodie);

        // For testing only
        athleticJacketRanker.setTemperatureLowerRange(60);
        athleticJacketRanker.setTemperatureUpperRange(90);
        rainJacketRanker.setTemperatureLowerRange(60);
        rainJacketRanker.setTemperatureUpperRange(90);
        winterCoatRanker.setTemperatureLowerRange(60);
        winterCoatRanker.setTemperatureUpperRange(90);

        // add everything to the list
        overBodyGarmentRankers = new ArrayList<>();
        overBodyGarmentRankers.add(athleticJacketRanker);
        overBodyGarmentRankers.add(rainJacketRanker);
        overBodyGarmentRankers.add(winterCoatRanker);
    }

    /**
     * @return the list of clothing rankers for over body garments, in the order of which their type is declared within the OverBodyGarmentType enum
     */
    public static List<ClothingRanker> getOverBodyGarmentRankers() {
        return overBodyGarmentRankers;
    }

    /**
     * @param position the index of the over body garment within the list overBodyGarmentRankers
     * @return the OverBodyGarmentType of the ClothingRanker at the index position within the list overBodyGarmentRankers
     */
    public static OverBodyGarmentType getOverBodyGarmentTypeAtPosition(int position) {
        switch(position) {
            case 0:
                return OverBodyGarmentType.ATHLETIC_JACKET;
            case 1:
                return OverBodyGarmentType.RAIN_JACKET;
            case 2:
                return OverBodyGarmentType.WINTER_COAT;
            default:
                // Improper use of this function will return null
                return null;
        }
    }

    public OverBodyGarmentType getOverBodyGarmentType() {
        return overBodyGarmentType;
    }
}
