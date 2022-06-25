package com.example.whattowear.models;

import android.util.Log;

import com.example.whattowear.R;

public class Footwear {
    public static final String TAG = "Footwear";
    private FootwearType footwearType;

    private static enum FootwearType {
        SNEAKERS, RUBBER_SHOES, BOAT, LOAFER, SANDALS, BOOTS, HEELS
    }

    /**
     * Calculates the optimal footwear based on weather, activity,
     * and preferences, then returns it
     * @return the calculated optimal footwear
     */
    public static Footwear getOptimalFootwear() {
        // TODO: update footwear type based on weather, activity, and preferences
        // for testing, default to sneakers

        Footwear footwear = new Footwear();

        footwear.footwearType = FootwearType.SNEAKERS;

        Log.i(TAG, "Finished calculating footwear");
        return footwear;
    }

    /**
     * @return the ID of the local file of the footwear image
     */
    public java.lang.Integer getFootwearImage() {
        // TODO: return correct image based on footwear
        return R.drawable.rubber_shoes;
    }
}
