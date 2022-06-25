package com.example.whattowear.models;

import com.example.whattowear.R;

public class OverBodyGarment {
    private OverBodyGarmentType overBodyGarmentType;

    private static enum OverBodyGarmentType {
        ATHLETIC_JACKET, RAIN_JACKET, WINTER_COAT, NONE
    }

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

        return R.drawable.ic_baseline_arrow_back_ios_24;
    }
}
