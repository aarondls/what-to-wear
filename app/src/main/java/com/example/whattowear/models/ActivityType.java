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
    private ActivityTypeName primaryActivityType;
    private ActivityTypeName secondaryActivityType; // represents the activity to be done after the primary activity; can be set to null if not used

    // When adding new activity types, add it here, and fix its description inside getDescription
    public static enum ActivityTypeName {
        WORK, SPORTS, CASUAL
    }

    private static final String WORK_TYPE_DESCRIPTION = "Work";
    private static final String SPORTS_TYPE_DESCRIPTION = "Sports";
    private static final String CASUAL_TYPE_DESCRIPTION = "Casual";

    public ActivityType(ActivityTypeName primaryActivityType, ActivityTypeName secondaryActivityType) {
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
        // for now, only add primaries
        for (ActivityTypeName activityType : ActivityTypeName.values()) {
            activitiesArrayList.add(new ActivityType(activityType, null));
        }

        return activitiesArrayList;
    }

    public ActivityTypeName getPrimaryActivityType() {
        return primaryActivityType;
    }

    public ActivityTypeName getSecondaryActivityType() {
        return secondaryActivityType;
    }

    public String getDescription() {
        String activityDescription = getDescription(primaryActivityType);
        if (secondaryActivityType != null) {
            activityDescription += " + " + getDescription(primaryActivityType);
        }
        return activityDescription;
    }

    private String getDescription(ActivityTypeName activityType) {
        switch (activityType) {
            case WORK:
                return WORK_TYPE_DESCRIPTION;
            case SPORTS:
                return SPORTS_TYPE_DESCRIPTION;
            case CASUAL:
                return CASUAL_TYPE_DESCRIPTION;
            default:
                // this will never happen if properly defined as described above
                return null;
        }
    }

    public int getImageID() {
        switch (primaryActivityType) {
            case WORK:
                return R.drawable.activity_type_work;
            case SPORTS:
                return R.drawable.activity_type_sport;
            case CASUAL:
                return R.drawable.activity_type_casual;
            default:
                // for types without an icon that naturally describes them, use custom icon
                return R.drawable.activity_type_custom;
        }
    }
}
