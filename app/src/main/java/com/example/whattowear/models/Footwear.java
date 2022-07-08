package com.example.whattowear.models;

import android.util.Log;

import com.example.whattowear.R;

import java.util.ArrayList;
import java.util.List;

public class Footwear {
    public static final String TAG = "Footwear";
    private FootwearType footwearType;

    private static enum FootwearType {
        SNEAKERS, RUBBER_SHOES, BOAT, LOAFER, SANDALS, BOOTS, HEELS
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
     * Initializes all the footwear rankers
     */
    public static void initializeFootwearRankers() {
        // TODO: fix icons
        // TODO: get from parse
        ClothingRanker sneakersRanker = new ClothingRanker("Sneakers", R.drawable.rubber_shoes);
        ClothingRanker rubberShoesRanker = new ClothingRanker("Rubber shoes", R.drawable.rubber_shoes);
        ClothingRanker boatRanker = new ClothingRanker("Boat", R.drawable.rubber_shoes);
        ClothingRanker loaferRanker = new ClothingRanker("Loafer", R.drawable.rubber_shoes);
        ClothingRanker sandalsRanker = new ClothingRanker("Sandals", R.drawable.rubber_shoes);
        ClothingRanker bootsRanker = new ClothingRanker("Boots", R.drawable.rubber_shoes);
        ClothingRanker heelsRanker = new ClothingRanker("Heels", R.drawable.rubber_shoes);

        footwearRankers = new ArrayList<>();
        footwearRankers.add(sneakersRanker);
        footwearRankers.add(rubberShoesRanker);
        footwearRankers.add(boatRanker);
        footwearRankers.add(loaferRanker);
        footwearRankers.add(sandalsRanker);
        footwearRankers.add(bootsRanker);
        footwearRankers.add(heelsRanker);
    }

    /**
     * @return the list of clothing rankers for footwear, in the order of which their type is declared within the FootwearType enum
     */
    public static List<ClothingRanker> getFootwearRankers() {
        return footwearRankers;
    }

    /**
     * @param position the index of the footwear within the list footwearRankers
     * @return the FootwearType of the ClothingRanker at the index position within the list footwearRankers
     */
    public static FootwearType getFootwearTypeAtPosition(int position) {
        switch(position) {
            case 0:
                return FootwearType.SNEAKERS;
            case 1:
                return FootwearType.RUBBER_SHOES;
            case 2:
                return FootwearType.BOAT;
            case 3:
                return FootwearType.LOAFER;
            case 4:
                return FootwearType.SANDALS;
            case 5:
                return FootwearType.BOOTS;
            case 6:
                return FootwearType.HEELS;
            default:
                return null;
        }
    }
}
