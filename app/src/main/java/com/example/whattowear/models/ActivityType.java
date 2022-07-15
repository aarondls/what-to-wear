package com.example.whattowear.models;

import com.example.whattowear.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single activity that can be chosen by the user. A single object of this
 * ActivityType class can represent both the primary activity and the secondary after
 * that, ie go to work then go workout.
 */
public class ActivityType {
    private String primaryActivityType;
    private String secondaryActivityType; // represents the activity to be done after the primary activity; can be set to null if not used

    public ActivityType(String primaryActivityType, String secondaryActivityType) {
        this.primaryActivityType = primaryActivityType;
        this.secondaryActivityType = secondaryActivityType;
    }

    /**
     * @return a list of ActivityTypes representing the possible activity types that can be chosen by the user
     * this list is guaranteed to be non-empty
     */
    public static List<ActivityType> getActivityTypes() {
        List<ActivityType> activitiesArrayList = new ArrayList<>();

        // add the activities
        activitiesArrayList.add(new ActivityType("Work", null));
        activitiesArrayList.add(new ActivityType("Sports", null));
        activitiesArrayList.add(new ActivityType("Casual", null));

        // TODO: add more activities as needed here

        return activitiesArrayList;
    }

    public String getPrimaryActivityType() {
        return primaryActivityType;
    }

    public String getSecondaryActivityType() {
        return secondaryActivityType;
    }

    public String getDescription() {
        String activityDescription = primaryActivityType;
        if (secondaryActivityType != null) {
            activityDescription += " + " + secondaryActivityType;
        }
        return activityDescription;
    }

    public int getImageID() {
        switch (primaryActivityType) {
            case "Work":
                return R.drawable.activity_type_work;
            case "Sports":
                return R.drawable.activity_type_sport;
            case "Casual":
                return R.drawable.activity_type_casual;
            default:
                // for types without an icon that naturally describes them, use custom icon
                return R.drawable.activity_type_custom;
        }
    }
}
