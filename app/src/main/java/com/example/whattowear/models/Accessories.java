package com.example.whattowear.models;

import android.util.Log;

import com.example.whattowear.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Accessories {
    private static final String TAG = "Accessories";

    private static final String ACCESSORIES_NAME = "Accessories";

    private static final float UV_ACCESSORY_THRESHOLD = 20;

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

        // TODO: how to rank accessories?
        // just preferences, and its own unique factor?
        // maybe restrict accessories settings to just preferences
        // set all other factors to 0 manually
        // then get preferences factor only, and multiply with UV index factor, then set up a threshold it must be bigger than
        Accessories accessories = new Accessories();

        accessories.accessoriesTypes = new ArrayList<>();

        // go through all rain related accessories
        // umbrella works on rain/drizzle (but not thunderstorm, since that has high winds)
        int dayConditionsIDFirstDigit = Weather.getDayConditionsID()/100;
        if (dayConditionsIDFirstDigit == Conditions.DRIZZLE_CONDITIONS_ID_FIRST_DIGIT || dayConditionsIDFirstDigit == Conditions.RAIN_CONDITIONS_ID_FIRST_DIGIT) {
            // TODO: interate preference factor better by multiplying with drizzle/rain strength
            // TODO: move classification of conditions from animation to conditions class
            if (accessoriesRankers.get(0).getPreferenceFactor() >= 5) {
                accessories.accessoriesTypes.add(AccessoryType.UMBRELLA);
            }
        }

        // go through all UV related accessories
        float dayMaxUVIndex = Weather.getDayMaxUVIndex();
        for (int i=1; i<3; i++) {
            if (dayMaxUVIndex*accessoriesRankers.get(i).getPreferenceFactor() >= UV_ACCESSORY_THRESHOLD) {
                // UV accessory exceeds required threshold to wear
                accessories.accessoriesTypes.add(getAccessoriesTypeAtPosition(i));
            }
        }

        Log.i(TAG, "Finished calculating accessories");
        return accessories;
    }

    /**
     * @return the footwear name describing its type of garment
     */
    public static String getAccessoriesName() {
        return ACCESSORIES_NAME;
    }

    /**
     * @return a list of the names of all accessories types
     */
    public List<String> getAccessoriesTypeNames() {
        List<String> accessoriesNames = new ArrayList<>();
        for (AccessoryType accessory : accessoriesTypes) {
            switch (accessory) {
                case UMBRELLA:
                    accessoriesNames.add("Umbrella");
                case HAT:
                    accessoriesNames.add("Hat");
                case SUNGLASSES:
                    accessoriesNames.add("Sunglasses");
            }
        }
        return accessoriesNames;
    }

    /**
     * @return all the IDs of the local file of the accessories images as
     * a list. If there are no accessories, then the returned list is empty.
     */
    public List<Integer> getAccessoriesImages() {
        List<Integer> accessoriesImages = new ArrayList<>();

        for (AccessoryType accessory : accessoriesTypes) {
            accessoriesImages.add(getAccessoriesImages(accessory.name()));
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
        AccessoryType accessoryType;

        try {
            accessoryType = AccessoryType.valueOf(clothingTypeName);
        } catch (IllegalArgumentException e) {
            // unrecognized return null
            return null;
        }

        switch (accessoryType) {
            case UMBRELLA:
                return R.drawable.clothing_icon_umbrella;
            case HAT:
                return R.drawable.clothing_icon_hat;
            case SUNGLASSES:
                return R.drawable.clothing_icon_sunglasses;
        }
        // The cases above cover all possibilities, so this cannot happen
        return null;
    }

    /**
     * Sets the accessoriesRankers, including the locally generated icon IDs
     *
     * @param accessoriesRankers the accessoriesRankers to set
     */
    public static void setAccessoriesRankers(List<ClothingRanker> accessoriesRankers) {
        Accessories.accessoriesRankers = accessoriesRankers;

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
        ClothingRanker umbrellaRanker = new ClothingRanker();
        umbrellaRanker.initializeFactorsToDefault(AccessoryType.UMBRELLA.name(), R.drawable.clothing_icon_umbrella, user);
        ClothingRanker hatRanker = new ClothingRanker();
        hatRanker.initializeFactorsToDefault(AccessoryType.HAT.name(), R.drawable.clothing_icon_hat, user);
        ClothingRanker sunglassesRanker = new ClothingRanker();
        sunglassesRanker.initializeFactorsToDefault(AccessoryType.SUNGLASSES.name(), R.drawable.clothing_icon_sunglasses, user);

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

    public List<AccessoryType> getAccessoriesTypes() {
        return accessoriesTypes;
    }
}
