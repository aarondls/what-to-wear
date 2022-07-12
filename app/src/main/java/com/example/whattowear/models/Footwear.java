package com.example.whattowear.models;

import android.util.Log;

import com.example.whattowear.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Footwear {
    public static final String TAG = "Footwear";
    private FootwearType footwearType;

    private static enum FootwearType {
        SNEAKERS, BOAT, LOAFER, SANDALS, BOOTS, HEELS
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
     * @param clothingTypeName the clothingTypeName of the desired icon ID to get
     * @return the ID of the local file of the upper body garment image, if
     * it exists, based on the given clothingTypeName. Otherwise, this
     * returns null.
     */
    public static Integer getFootwearImage(String clothingTypeName) {
        // TODO: return correct image based on type
        if (clothingTypeName.equals("Sneakers")) {
            return R.drawable.rubber_shoes;
        } else if (clothingTypeName.equals("Boat")) {
            return R.drawable.rubber_shoes;
        } else if (clothingTypeName.equals("Loafer")) {
            return R.drawable.rubber_shoes;
        } else if (clothingTypeName.equals("Sandals")) {
            return R.drawable.rubber_shoes;
        } else if (clothingTypeName.equals("Boots")) {
            return R.drawable.rubber_shoes;
        } else if (clothingTypeName.equals("Heels")) {
            return R.drawable.rubber_shoes;
        }
        // unrecognized return null
        return null;
    }

    /**
     * Sets the footwearRankers and fetches any missing information from the Parse database, if needed
     * @param footwearRankers the footwearRankers to set
     * @throws ParseException the non-null ParseException, if fetching the missing information fails
     */
    public static void setFootwearRankers(List<ClothingRanker> footwearRankers) throws ParseException {
        Footwear.footwearRankers = footwearRankers;

        try {
            ClothingRanker.fetchAllIfNeeded(footwearRankers);
        } catch (ParseException e) {
            throw e;
        }

        for (ClothingRanker clothingRanker : footwearRankers) {
            clothingRanker.setClothingTypeIconID(getFootwearImage(clothingRanker.getClothingTypeName()));
        }
    }

    /**
     * Initializes all the footwear rankers with default factors
     * @param user the ParseUser owning the footwear
     * @return the list of initialized clothing rankers for footwear with default factors
     */
    public static List<ClothingRanker> initializeFootwearRankers(ParseUser user) {
        // TODO: fix icons
        // TODO: get from parse
        ClothingRanker sneakersRanker = new ClothingRanker();
        sneakersRanker.initializeFactorsToDefault("Sneakers", R.drawable.rubber_shoes, user);
        ClothingRanker boatRanker = new ClothingRanker();
        boatRanker.initializeFactorsToDefault("Boat", R.drawable.rubber_shoes, user);
        ClothingRanker loaferRanker = new ClothingRanker();
        loaferRanker.initializeFactorsToDefault("Loafer", R.drawable.rubber_shoes, user);
        ClothingRanker sandalsRanker = new ClothingRanker();
        sandalsRanker.initializeFactorsToDefault("Sandals", R.drawable.rubber_shoes, user);
        ClothingRanker bootsRanker = new ClothingRanker();
        bootsRanker.initializeFactorsToDefault("Boots", R.drawable.rubber_shoes, user);
        ClothingRanker heelsRanker = new ClothingRanker();
        heelsRanker.initializeFactorsToDefault("Heels", R.drawable.rubber_shoes, user);

        footwearRankers = new ArrayList<>();
        footwearRankers.add(sneakersRanker);
        footwearRankers.add(boatRanker);
        footwearRankers.add(loaferRanker);
        footwearRankers.add(sandalsRanker);
        footwearRankers.add(bootsRanker);
        footwearRankers.add(heelsRanker);

        return footwearRankers;
    }

    /**
     * @return the list of clothing rankers for footwear, in the order of which their type is declared within the FootwearType enum
     */
    public static List<ClothingRanker> getFootwearRankers() {
        return footwearRankers;
    }
}
