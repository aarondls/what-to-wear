package com.example.whattowear.models;

import com.example.whattowear.ClothingInterface;

import java.util.List;

public class Clothing {
    // Clothing is a singleton class

    public static enum ClothingType {
        OVER_BODY_GARMENT, UPPER_BODY_GARMENT, LOWER_BODY_GARMENT, FOOTWEAR, ACCESSORIES
    }

    private static final String OVER_BODY_GARMENT_NAME = "Over body garment";
    private static final String UPPER_BODY_GARMENT_NAME = "Upper body garment";
    private static final String LOWER_BODY_GARMENT_NAME = "Lower body garment";
    private static final String FOOTWEAR_NAME = "Footwear";
    private static final String ACCESSORIES_NAME = "Accessories";

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
    }

    /**
     * Updates clothing based on weather, activity, and preferences
     */
    public static void calculateOptimalClothing(ClothingInterface clothingInterface) {
        // Do not calculate anything if not all data is available
        if (Weather.getLoadedDataLocationName().isEmpty() || selectedActivityType == null) {
            return;
        }

        overBodyGarment = OverBodyGarment.getOptimalOverBodyGarment();
        upperBodyGarment = UpperBodyGarment.getOptimalUpperBodyGarment();
        lowerBodyGarment = LowerBodyGarment.getOptimalLowerBodyGarment();
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

    /**
     * @param clothingType the clothingType of the desired rankers to get
     * @return a list of ClothingRankers containing all rankers of the given ClothingType
     */
    public static List<ClothingRanker> getRankersWithType(ClothingType clothingType) {
        switch (clothingType) {
            case OVER_BODY_GARMENT:
                return OverBodyGarment.getOverBodyGarmentRankers();
            case UPPER_BODY_GARMENT:
                return UpperBodyGarment.getUpperBodyGarmentRankers();
            case LOWER_BODY_GARMENT:
                return LowerBodyGarment.getLowerBodyGarmentRankers();
            case FOOTWEAR:
                return Footwear.getFootwearRankers();
            case ACCESSORIES:
                return Accessories.getAccessoriesRankers();
            default:
                // if unrecognized type, return null
                return null;
        }
    }

    public static String getClothingNameFromType(ClothingType clothingType) {
        switch (clothingType) {
            case OVER_BODY_GARMENT:
                return OVER_BODY_GARMENT_NAME;
            case UPPER_BODY_GARMENT:
                return UPPER_BODY_GARMENT_NAME;
            case LOWER_BODY_GARMENT:
                return LOWER_BODY_GARMENT_NAME;
            case FOOTWEAR:
                return FOOTWEAR_NAME;
            case ACCESSORIES:
                return ACCESSORIES_NAME;
            default:
                // if unrecognized type, return null
                return null;
        }
    }
}
