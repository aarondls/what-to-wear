package com.example.whattowear.models;

import android.util.Log;

import com.example.whattowear.R;

public class Footwear {
    public static final String TAG = "Footwear";
    private FootwearType footwearType;

    private static enum FootwearType {
        SNEAKERS, RUBBER_SHOES, BOAT, LOAFER, SANDALS, BOOTS, HEELS
    }

    // Indexing of this is based on the order declaration of the enum
    private static ClothingRanker sneakersRanker;
    private static ClothingRanker rubberShoesRanker;
    private static ClothingRanker boatRanker;
    private static ClothingRanker loaferRanker;
    private static ClothingRanker sandalsRanker;
    private static ClothingRanker bootsRanker;
    private static ClothingRanker heelsRanker;

    // number of footwear rankers as defined above
    public static final int FOOTWEAR_RANKERS_COUNT = 7;

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
        sneakersRanker = new ClothingRanker("Sneakers", R.drawable.rubber_shoes);
        rubberShoesRanker = new ClothingRanker("Rubber shoes", R.drawable.rubber_shoes);
        boatRanker = new ClothingRanker("Boat", R.drawable.rubber_shoes);
        loaferRanker = new ClothingRanker("Loafer", R.drawable.rubber_shoes);
        sandalsRanker = new ClothingRanker("Sandals", R.drawable.rubber_shoes);
        bootsRanker = new ClothingRanker("Boots", R.drawable.rubber_shoes);
        heelsRanker = new ClothingRanker("Heels", R.drawable.rubber_shoes);
    }

    /**
     * @param position the index of the desired footwear ranker, which is based on its declaration within the enum FootwearType
     * @return the footwear ranker at the index position
     */
    public static ClothingRanker getFootwearRankerAtPosition(int position) {
        switch(position) {
            case 0:
                return sneakersRanker;
            case 1:
                return rubberShoesRanker;
            case 2:
                return boatRanker;
            case 3:
                return loaferRanker;
            case 4:
                return sandalsRanker;
            case 5:
                return bootsRanker;
            case 6:
                return heelsRanker;
            default:
                // Initialization inside Clothing will ensure this will not be null
                // Improper use of this function will return null
                return null;
        }
    }
}
