package com.example.whattowear.models;

/**
 * Class responsible for ranking an individual piece of clothing based on weather and user given factors
 */
public class ClothingRanker {
    private String clothingTypeName;
    private int clothingTypeIconID;

    private int preferenceFactor; // how much the user likes this piece of clothing

    // Temperature factors
    private int temperatureImportance; // how sensitive this clothing is to the temp range
    private int temperatureUpperRange;
    private int temperatureLowerRange;

    // Activity factors
    private int activityImportance; // how relevant this clothing is to the activity
    private int workActivityFactor; // how relevant this clothing is for work
    private int sportsActivityFactor; // how relevant this clothing is for sports
    private int casualActivityFactor; // how relevant this clothing is for casual

    public ClothingRanker(String clothingTypeName, int clothingTypeIconID) {
        this.clothingTypeName = clothingTypeName;
        this.clothingTypeIconID = clothingTypeIconID;

        // TODO: Get these from parse
        temperatureImportance = 50;
        temperatureUpperRange = 10;
        temperatureLowerRange = 50;
        preferenceFactor = 10;
        activityImportance = 50;
        workActivityFactor = 10;
        sportsActivityFactor = 10;
        casualActivityFactor = 10;
    }

    // TODO: create a model to calculate rating of clothes based on factor
    // these use placeholders for now
    public int getCurrentPrimaryRating() {
        return preferenceFactor*(getTemperatureFactor()*temperatureImportance + getPrimaryActivityFactor()*activityImportance);
    }

    public int getCurrentSecondaryRating() {
        return preferenceFactor*(getTemperatureFactor()*temperatureImportance + getSecondaryActivityFactor()*activityImportance);
    }

    private int getTemperatureFactor() {
        // must be >0 inside of defined temperature range, <0 outside
        // model using quadratic function
        // use temperatureLowerRange and temperatureLowerRange as the zeros
        // constrain stretch factor by having function maximum at 10

        if (Weather.getCurrentForecast().getTemp() >= temperatureLowerRange && Weather.getCurrentForecast().getTemp() <= temperatureUpperRange) {
            return 1;
        } else {
            return 0;
        }
    }

    private int getPrimaryActivityFactor() {
        return getActivityFactorFromType(Clothing.getSelectedActivityType().getPrimaryActivityType());
    }

    private int getSecondaryActivityFactor() {
        return getActivityFactorFromType(Clothing.getSelectedActivityType().getSecondaryActivityType());
    }

    private int getActivityFactorFromType(String activityType) {
        // if no activity, return 0 so the calculation is not affected by it
        if (activityType == null) return 0;

        switch (activityType) {
            case "Work":
                return workActivityFactor;
            case "Sports":
                return sportsActivityFactor;
            case "Casual":
                return casualActivityFactor;
            default:
                // if no known factor, return 0 so the calculation is not affected by it
                return 0;
        }
    }

    public String getClothingTypeName() {
        return clothingTypeName;
    }

    public int getClothingTypeIconID() {
        return clothingTypeIconID;
    }

    public int getTemperatureImportance() {
        return temperatureImportance;
    }

    public int getTemperatureUpperRange() {
        return temperatureUpperRange;
    }

    public int getTemperatureLowerRange() {
        return temperatureLowerRange;
    }

    public int getPreferenceFactor() {
        return preferenceFactor;
    }

    public int getWorkActivityFactor() {
        return workActivityFactor;
    }

    public int getSportsActivityFactor() {
        return sportsActivityFactor;
    }

    public int getCasualActivityFactor() {
        return casualActivityFactor;
    }

    public int getActivityImportance() {
        return activityImportance;
    }

    public void setActivityImportance(int activityImportance) {
        this.activityImportance = activityImportance;
    }

    public void setTemperatureImportance(int temperatureImportance) {
        this.temperatureImportance = temperatureImportance;
    }

    public void setTemperatureUpperRange(int temperatureUpperRange) {
        this.temperatureUpperRange = temperatureUpperRange;
    }

    public void setTemperatureLowerRange(int temperatureLowerRange) {
        this.temperatureLowerRange = temperatureLowerRange;
    }

    public void setWorkActivityFactor(int workActivityFactor) {
        this.workActivityFactor = workActivityFactor;
    }

    public void setSportsActivityFactor(int sportsActivityFactor) {
        this.sportsActivityFactor = sportsActivityFactor;
    }

    public void setCasualActivityFactor(int casualActivityFactor) {
        this.casualActivityFactor = casualActivityFactor;
    }

    public void setPreferenceFactor(int preferenceFactor) {
        this.preferenceFactor = preferenceFactor;
    }
}
