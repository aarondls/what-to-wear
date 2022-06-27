package com.example.whattowear.models;

import com.example.whattowear.ClothingInterface;

public class Clothing {
    // Clothing is a singleton class

    private static UpperBodyGarment upperBodyGarment;
    private static LowerBodyGarment lowerBodyGarment;
    private static OverBodyGarment overBodyGarment;
    private static Footwear footwear;
    private static Accessories accessories;

    private static String loadedDataLocationName;

    private Clothing () {
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
}
