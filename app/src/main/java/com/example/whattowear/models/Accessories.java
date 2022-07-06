package com.example.whattowear.models;

import android.util.Log;

import com.example.whattowear.R;

import java.util.ArrayList;
import java.util.List;

public class Accessories {
    public static final String TAG = "Accessories";

    private List<AccessoryType> accessoriesTypes;

    private static enum AccessoryType {
        UMBRELLA, HAT, SUNGLASSES
    }

    // Indexing of this is based on the order declaration of the enum
    private static ClothingRanker umbrellaRanker;
    private static ClothingRanker hatRanker;
    private static ClothingRanker sunglassesRanker;

    // number of accessory rankers as defined above
    public static final int ACCESSORIES_RANKERS_COUNT = 3;

    /**
     * Calculates the optimal accessories based on weather,
     * activity, and preferences, then returns it
     * @return the calculated accessories
     */
    public static Accessories getOptimalAccessories()  {
        // TODO: update type based on weather, activity, and preferences
        // for testing, default to empty

        Accessories accessories = new Accessories();

        accessories.accessoriesTypes = new ArrayList<>();

        accessories.accessoriesTypes.add(AccessoryType.SUNGLASSES);
        accessories.accessoriesTypes.add(AccessoryType.HAT);

        Log.i(TAG, "Finished calculating accessories");
        return accessories;
    }

    /**
     * @return all the IDs of the local file of the accessories images as
     * a list. If there are no accessories, then the returned list is empty.
     */
    public List<java.lang.Integer> getAccessoriesImages() {
        // TODO: return correct image based on type
        List<java.lang.Integer> accessoriesImages = new ArrayList<>();

        for (AccessoryType accessory : accessoriesTypes) {
            accessoriesImages.add(R.drawable.sunglasses);
        }

        return accessoriesImages;

    }

    /**
     * Initializes all the accessory rankers
     */
    public static void initializeAccessoriesRankers() {
        // TODO: fix icons
        // TODO: get from parse
        umbrellaRanker = new ClothingRanker("Umbrella", R.drawable.sunglasses);
        hatRanker = new ClothingRanker("Hat", R.drawable.sunglasses);
        sunglassesRanker = new ClothingRanker("Sunglasses", R.drawable.sunglasses);
    }

    /**
     * @param position the index of the desired accessory ranker, which is based on its declaration within the enum AccessoryType
     * @return the accessory ranker at the index position
     */
    public static ClothingRanker getAccessoriesRankerAtPosition(int position) {
        switch(position) {
            case 0:
                return umbrellaRanker;
            case 1:
                return hatRanker;
            case 2:
                return sunglassesRanker;
            default:
                return null;
        }
    }
}
