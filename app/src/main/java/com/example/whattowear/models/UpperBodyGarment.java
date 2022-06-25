package com.example.whattowear.models;

import com.example.whattowear.R;

public class UpperBodyGarment {
    private UpperBodyGarmentType upperBodyGarmentType;

    private static enum UpperBodyGarmentType {
        T_SHIRT, POLO_SHIRT, DRESS_SHIRT, BLOUSE,
    }

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
}
