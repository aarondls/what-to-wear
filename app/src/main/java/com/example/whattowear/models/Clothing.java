package com.example.whattowear.models;

import com.example.whattowear.ClothingInterface;

public class Clothing {
    // Clothing is a singleton class

    private static Clothing clothing = new Clothing();

    private static UpperBodyGarment upperBodyGarment;
    private static LowerBodyGarment lowerBodyGarment;
    private static OverBodyGarment overBodyGarment;
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
}
