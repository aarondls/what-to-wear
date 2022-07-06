package com.example.whattowear.models;

import com.example.whattowear.ClothingInterface;

public class Clothing {
    // Clothing is a singleton class

    public static enum ClothingType {
        OVER_BODY_GARMENT, UPPER_BODY_GARMENT, LOWER_BODY_GARMENT, FOOTWEAR, ACCESSORIES
    }

    private static Clothing clothing = new Clothing();

    private static OverBodyGarment overBodyGarment;
    private static UpperBodyGarment upperBodyGarment;
    private static LowerBodyGarment lowerBodyGarment;
    private static Footwear footwear;
    private static Accessories accessories;

    private static ActivityType selectedActivityType;
    private static int selectedActivityTypeIndex;
    private static String loadedDataLocationName;

    private Clothing () {
        loadedDataLocationName = "";
        selectedActivityType = null;
        selectedActivityTypeIndex = 0;

        // Load all the rankers
        OverBodyGarment.initializeOverBodyGarmentRankers();
        UpperBodyGarment.initializeUpperBodyGarmentRankers();
        LowerBodyGarment.initializeLowerBodyGarmentRankers();
        Footwear.initializeFootwearRankers();
        Accessories.initializeAccessoriesRankers();
    }

    /**
     * Updates clothing based on weather, activity, and preferences
     */
    public static void calculateOptimalClothing(ClothingInterface clothingInterface) {
        upperBodyGarment = UpperBodyGarment.getOptimalUpperBodyGarment();
        lowerBodyGarment = LowerBodyGarment.getOptimalLowerBodyGarment();
        overBodyGarment = OverBodyGarment.getOptimalOverBodyGarment();
        footwear = Footwear.getOptimalFootwear();
        accessories = Accessories.getOptimalAccessories();

        loadedDataLocationName = Weather.getLoadedDataLocationName();

        // Call callback since done
        clothingInterface.onFinish();
    }

    /**
     * @return whether there is valid clothing data at the location Weather.lastLocationName
     */
    public static boolean hasPreloadedDataToDisplay() {
       return Weather.hasPreloadedDataToDisplay() && loadedDataLocationName.equals(Weather.getLoadedDataLocationName());
    }

    public static void setLoadedDataLocationName(String loadedDataLocationName) {
        Clothing.loadedDataLocationName = loadedDataLocationName;
    }

    /**
     * Given that the user has selected an activity, sets the provided selectedActivityType and selectedActivityTypeIndex
     * @param selectedActivityType the chosen activity type
     * @param selectedActivityTypeIndex the index of the chosen activity type in the generated list by the getActivityTypes method
     */
    public static void setSelectedActivity(ActivityType selectedActivityType, int selectedActivityTypeIndex) {
        Clothing.selectedActivityType = selectedActivityType;
        Clothing.selectedActivityTypeIndex = selectedActivityTypeIndex;

    }

    public static UpperBodyGarment getUpperBodyGarment() {
        return upperBodyGarment;
    }

    public static LowerBodyGarment getLowerBodyGarment() {
        return lowerBodyGarment;
    }

    public static OverBodyGarment getOverBodyGarment() {
        return overBodyGarment;
    }

    public static Footwear getFootwear() {
        return footwear;
    }

    public static Accessories getAccessories() {
        return accessories;
    }

    public static String getLoadedDataLocationName() {
        return loadedDataLocationName;
    }

    public static ActivityType getSelectedActivityType() {
        return selectedActivityType;
    }

    /**
     * @return the index of the selected activity type in the generated list by the getActivityTypes method
     * If no activity was selected, this returns 0 to choose the first element of the list. The list
     * is guaranteed to be non-empty by the getActivityTypes method.
     */
    public static int getSelectedActivityTypeIndex() {
        return selectedActivityTypeIndex;
    }

    public static int getRankersSize(ClothingType clothingType) {
        // TODO: calculate total size from all ranker lists
        switch (clothingType) {
            case OVER_BODY_GARMENT:
                return OverBodyGarment.OVER_BODY_GARMENT_RANKERS_COUNT;
            case UPPER_BODY_GARMENT:
                return UpperBodyGarment.UPPER_BODY_GARMENT_RANKERS_COUNT;
            case LOWER_BODY_GARMENT:
                return LowerBodyGarment.LOWER_BODY_GARMENT_RANKERS_COUNT;
            case FOOTWEAR:
                return Footwear.FOOTWEAR_RANKERS_COUNT;
            case ACCESSORIES:
                return Accessories.ACCESSORIES_RANKERS_COUNT;
            default:
                // if unrecognized type, have "list" size recognized as empty
                return 0;
        }
    }

    public static ClothingRanker getRankerWithTypeAtPosition(ClothingType clothingType, int position) {
        // TODO: calculate proper indices from clothing type and known ranker counts
        switch (clothingType) {
            case OVER_BODY_GARMENT:
                return OverBodyGarment.getOverBodyGarmentRankerAtPosition(position);
            case UPPER_BODY_GARMENT:
                return UpperBodyGarment.getUpperBodyGarmentRankerAtPosition(position);
            case LOWER_BODY_GARMENT:
                return LowerBodyGarment.getLowerBodyGarmentRankerAtPosition(position);
            case FOOTWEAR:
                return Footwear.getFootwearRankerAtPosition(position);
            case ACCESSORIES:
                return Accessories.getAccessoriesRankerAtPosition(position);
            default:
                // if unrecognized type, return null
                return null;
        }
    }
}
