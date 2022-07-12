package com.example.whattowear.models;

import android.util.Log;

import com.example.whattowear.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Footwear {
    public static final String TAG = "Footwear";
    private FootwearType footwearType;

    private static enum FootwearType {
        SNEAKERS, BOAT, LOAFER, SANDALS, BOOTS, HEELS
    }

    // Indexing of this is based on the order declaration of the enum
    private static List<ClothingRanker> footwearRankers;

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

    /**
     * Initializes all the footwear rankers with default factors
     * @param user the ParseUser owning the footwear
     * @return the list of initialized clothing rankers for footwear with default factors
     */
    public static List<ClothingRanker> initializeFootwearRankers(ParseUser user) {
        // TODO: fix icons
        // TODO: get from parse
        ClothingRanker sneakersRanker = new ClothingRanker();
        sneakersRanker.initializeFactorsToDefault("Sneakers", R.drawable.rubber_shoes, user);
        ClothingRanker boatRanker = new ClothingRanker();
        boatRanker.initializeFactorsToDefault("Boat", R.drawable.rubber_shoes, user);
        ClothingRanker loaferRanker = new ClothingRanker();
        loaferRanker.initializeFactorsToDefault("Loafer", R.drawable.rubber_shoes, user);
        ClothingRanker sandalsRanker = new ClothingRanker();
        sandalsRanker.initializeFactorsToDefault("Sandals", R.drawable.rubber_shoes, user);
        ClothingRanker bootsRanker = new ClothingRanker();
        bootsRanker.initializeFactorsToDefault("Boots", R.drawable.rubber_shoes, user);
        ClothingRanker heelsRanker = new ClothingRanker();
        heelsRanker.initializeFactorsToDefault("Heels", R.drawable.rubber_shoes, user);

        footwearRankers = new ArrayList<>();
        footwearRankers.add(sneakersRanker);
        footwearRankers.add(boatRanker);
        footwearRankers.add(loaferRanker);
        footwearRankers.add(sandalsRanker);
        footwearRankers.add(bootsRanker);
        footwearRankers.add(heelsRanker);

        return footwearRankers;
    }

    /**
     * @return the list of clothing rankers for footwear, in the order of which their type is declared within the FootwearType enum
     */
    public static List<ClothingRanker> getFootwearRankers() {
        return footwearRankers;
    }
}
