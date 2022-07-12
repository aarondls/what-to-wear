package com.example.whattowear.models;

import android.util.Log;

import com.example.whattowear.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Accessories {
    public static final String TAG = "Accessories";

    private List<AccessoryType> accessoriesTypes;

    private static enum AccessoryType {
        UMBRELLA, HAT, SUNGLASSES
    }

    // Indexing of this is based on the order declaration of the enum
    private static List<ClothingRanker> accessoriesRankers;

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
     * @param clothingTypeName the clothingTypeName of the desired icon ID to get
     * @return the ID of the local file of the upper body garment image, if
     * it exists, based on the given clothingTypeName. Otherwise, this
     * returns null.
     */
    public static Integer getAccessoriesImages(String clothingTypeName) {
        // TODO: return correct image based on type
        if (clothingTypeName.equals("Umbrella")) {
            return R.drawable.sunglasses;
        } else if (clothingTypeName.equals("Hat")) {
            return R.drawable.sunglasses;
        } else if (clothingTypeName.equals("Sunglasses")) {
            return R.drawable.sunglasses;
        }
        // unrecognized return null
        return null;
    }

    /**
     * Sets the accessoriesRankers and fetches any missing information from the Parse database, if needed
     * @param accessoriesRankers the accessoriesRankers to set
     * @throws ParseException the non-null ParseException, if fetching the missing information fails
     */
    public static void setAccessoriesRankers(List<ClothingRanker> accessoriesRankers) throws ParseException {
        Accessories.accessoriesRankers = accessoriesRankers;

        try {
            ClothingRanker.fetchAllIfNeeded(accessoriesRankers);
        } catch (ParseException e) {
            throw e;
        }

        for (ClothingRanker clothingRanker : accessoriesRankers) {
            clothingRanker.setClothingTypeIconID(getAccessoriesImages(clothingRanker.getClothingTypeName()));
        }
    }

    /**
     * Initializes all the accessory rankers with default factors
     * @param user the ParseUser owning the accessory
     * @return the list of initialized clothing rankers for accessory with default factors
     */
    public static List<ClothingRanker> initializeAccessoriesRankers(ParseUser user) {
        // TODO: fix icons
        // TODO: get from parse
        ClothingRanker umbrellaRanker = new ClothingRanker();
        umbrellaRanker.initializeFactorsToDefault("Umbrella", R.drawable.sunglasses, user);
        ClothingRanker hatRanker = new ClothingRanker();
        hatRanker.initializeFactorsToDefault("Hat", R.drawable.sunglasses, user);
        ClothingRanker sunglassesRanker = new ClothingRanker();
        sunglassesRanker.initializeFactorsToDefault("Sunglasses", R.drawable.sunglasses, user);

        accessoriesRankers = new ArrayList<>();
        accessoriesRankers.add(umbrellaRanker);
        accessoriesRankers.add(hatRanker);
        accessoriesRankers.add(sunglassesRanker);

        return accessoriesRankers;
    }

    /**
     * @return the list of clothing rankers for accessories, in the order of which their type is declared within the AccessoryType enum
     */
    public static List<ClothingRanker> getAccessoriesRankers() {
        return accessoriesRankers;
    }

    /**
     * @param position the index of the accessory within the list accessoriesRankers
     * @return the AccessoryType of the ClothingRanker at the index position within the list accessoriesRankers
     */
    public static AccessoryType getAccessoriesTypeAtPosition(int position) {
        switch(position) {
            case 0:
                return AccessoryType.UMBRELLA;
            case 1:
                return AccessoryType.HAT;
            case 2:
                return AccessoryType.SUNGLASSES;
            default:
                return null;
        }
    }
}
