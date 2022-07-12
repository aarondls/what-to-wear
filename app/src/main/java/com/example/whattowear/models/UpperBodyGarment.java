package com.example.whattowear.models;

import com.example.whattowear.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UpperBodyGarment {
    private UpperBodyGarmentType upperBodyGarmentType;

    private static enum UpperBodyGarmentType {
        T_SHIRT, POLO_SHIRT, DRESS_SHIRT, BLOUSE,
    }

    // Indexing of this is based on the order declaration of the enum
    private static List<ClothingRanker> upperBodyGarmentRankers;

    /**
     * Calculates the optimal upper body garment based on weather,
     * activity, and preferences, then returns it
     * @return the calculated optimal upper body garment
     */
    public static UpperBodyGarment getOptimalUpperBodyGarment() {
        // TODO: update type based on weather, activity, and preferences
        // for testing, default to T_SHIRT

        UpperBodyGarment upperBodyGarment = new UpperBodyGarment();

        upperBodyGarment.upperBodyGarmentType = UpperBodyGarmentType.T_SHIRT;

        return upperBodyGarment;
    }

    /**
     * @return the ID of the local file of the over body garment image
     */
    public java.lang.Integer getUpperBodyGarmentImage() {
        // TODO: return correct image based on type

        return R.drawable.t_shirt;
    }

    /**
     * Initializes all the upper body garment rankers with default factors
     * @param user the ParseUser owning the upper body garments
     * @return the list of initialized clothing rankers for upper body garments with default factors
     */
    public static List<ClothingRanker> initializeUpperBodyGarmentRankers(ParseUser user) {
        // TODO: fix icons
        // TODO: get from parse
        ClothingRanker tShirtRanker = new ClothingRanker();
        tShirtRanker.initializeFactorsToDefault("T shirt", R.drawable.t_shirt, user);
        ClothingRanker poloShirtRanker = new ClothingRanker();
        poloShirtRanker.initializeFactorsToDefault("Polo shirt", R.drawable.t_shirt, user);
        ClothingRanker dressShirtRanker = new ClothingRanker();
        dressShirtRanker.initializeFactorsToDefault("Dress shirt", R.drawable.t_shirt, user);
        ClothingRanker blouseRanker = new ClothingRanker();
        blouseRanker.initializeFactorsToDefault("Blouse", R.drawable.t_shirt, user);


        upperBodyGarmentRankers = new ArrayList<>();
        upperBodyGarmentRankers.add(tShirtRanker);
        upperBodyGarmentRankers.add(poloShirtRanker);
        upperBodyGarmentRankers.add(dressShirtRanker);
        upperBodyGarmentRankers.add(blouseRanker);

        return upperBodyGarmentRankers;
    }

    /**
     * @return the list of clothing rankers for upper body garments, in the order of which their type is declared within the UpperBodyGarmentType enum
     */
    public static List<ClothingRanker> getUpperBodyGarmentRankers() {
        return upperBodyGarmentRankers;
    }
}
