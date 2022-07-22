package com.example.whattowear;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.whattowear.models.ActivityType;
import com.example.whattowear.models.Clothing;
import com.example.whattowear.models.Weather;

import java.util.List;

public class DashboardClothingController {
    public static final String TAG = "DashboardClothingController";
    private static final String PLUS_SYMBOL = "+";
    private static final String ACCESSORIES_PHRASE = " accessories";
    private static final String ACCESSORY_PHRASE = " accessory";
    private static final String NO_CLOTHING_INFORMATION_TEXT = "No clothing information.";

    private Activity activity;

    private ImageView overBodyGarmentImageview;
    private ImageView upperBodyGarmentImageview;
    private ImageView lowerBodyGarmentImageview;
    private ImageView footwearImageview;
    private ImageView accessoriesImageview;
    private TextView excessAccessoriesTextview;
    private Spinner activitySelectorSpinner;

    public DashboardClothingController(Activity activity, DashboardWeatherController dashboardWeatherController) {
        this.activity = activity;

        overBodyGarmentImageview = activity.findViewById(R.id.dashboard_over_body_garment_imageview);
        upperBodyGarmentImageview = activity.findViewById(R.id.dashboard_upper_body_garment_imageview);
        lowerBodyGarmentImageview = activity.findViewById(R.id.dashboard_lower_body_garment_imageview);
        footwearImageview = activity.findViewById(R.id.dashboard_footwear_imageview);
        accessoriesImageview = activity.findViewById(R.id.dashboard_accessories_imageview);
        excessAccessoriesTextview = activity.findViewById(R.id.dashboard_excess_accessories_textview);
        activitySelectorSpinner = activity.findViewById(R.id.activity_selector_spinner);

        // Check if there is data to be displayed
        if (Weather.hasPreloadedDataToDisplay()) {
            // note that if this data is out of date, then the weather controller updates the weather info and triggers the listener onNewWeatherDataReady
            // check if clothing data is valid based on weather data, and update if necessary
            if (Clothing.hasPreloadedDataToDisplay()) {
                // display existing clothing data
                updateDashboardDisplay();
            } else {
                // recalculate clothing data
                onDataSetChanged();
            }
        } else {
            // show placeholder when no data
            displayPlaceholderOnDashboard();
        }

        SpinnerAdapter spinnerAdapter = new ActivityAdapter(activity, R.layout.item_activity_option, ActivityType.getActivityTypes());
        activitySelectorSpinner.setAdapter(spinnerAdapter);
        activitySelectorSpinner.setSelection(Clothing.getSelectedActivityTypeIndex());

        activitySelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Set clothing type and position in clothing class
                Clothing.setSelectedActivity((ActivityType) parent.getItemAtPosition(position), position);

                onDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No need to do anything
            }
        });

        dashboardWeatherController.addNewWeatherDataListener(new DashboardWeatherController.WeatherDataListener() {
            @Override
            public void onNewWeatherDataReady() {
                onDataSetChanged();
            }
        });
    }

    /**
     * Handles calculating all the clothing information when new weather or activity data has come in,
     * and updates the clothing display as necessary
     */
    public void onDataSetChanged() {
        Clothing.calculateOptimalClothing(new ClothingInterface() {
            @Override
            public void onFinish() {
                // load clothing images here
                Clothing.setLoadedDataLocationName(Weather.getLoadedDataLocationName());
                updateDashboardDisplay();
            }
        });
    }

    /**
     * Updates the dashboard clothing controller elements with the available Clothing data
     */
    public void updateDashboardDisplay() {
        if (Clothing.getOverBodyGarment().getOverBodyGarmentImage() != null) {
            overBodyGarmentImageview.setImageResource(Clothing.getOverBodyGarment().getOverBodyGarmentImage());
        } else {
            overBodyGarmentImageview.setImageDrawable(null);
        }
        upperBodyGarmentImageview.setImageResource(Clothing.getUpperBodyGarment().getUpperBodyGarmentImage());
        lowerBodyGarmentImageview.setImageResource(Clothing.getLowerBodyGarment().getLowerBodyGarmentImage());
        footwearImageview.setImageResource(Clothing.getFootwear().getFootwearImage());

        List<Integer> accessoriesImages = Clothing.getAccessories().getAccessoriesImages();
        if (!accessoriesImages.isEmpty()) {
            accessoriesImageview.setImageResource(accessoriesImages.get(0));

            if (accessoriesImages.size() > 1) {
                String excessAccessoryCount = PLUS_SYMBOL + String.valueOf(accessoriesImages.size()-1);
                if (accessoriesImages.size() > 2) {
                    excessAccessoryCount += ACCESSORIES_PHRASE;
                } else {
                    excessAccessoryCount += ACCESSORY_PHRASE;
                }
                excessAccessoriesTextview.setText(excessAccessoryCount);
            }
        } else {
            accessoriesImageview.setImageDrawable(null);
        }
    }

    /**
     * Displays placeholder clothing information to visually signal that there is no clothing data yet
     */
    public void displayPlaceholderOnDashboard() {
        excessAccessoriesTextview.setText(NO_CLOTHING_INFORMATION_TEXT);
    }
}
