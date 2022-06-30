package com.example.whattowear.models;

import com.example.whattowear.R;

public class LowerBodyGarment {
    private LowerBodyGarmentType lowerBodyGarmentType;

    private static enum LowerBodyGarmentType {
        PANTS, SHORTS, SKIRT, SWEATS
    }

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
}
