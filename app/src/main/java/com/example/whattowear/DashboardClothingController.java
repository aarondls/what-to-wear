package com.example.whattowear;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whattowear.models.Clothing;
import com.example.whattowear.models.Weather;

import java.util.List;

public class DashboardClothingController {
    public static final String TAG = "DashboardClothingController";

    private Activity activity;

    private ImageView overBodyGarmentImageview;
    private ImageView upperBodyGarmentImageview;
    private ImageView lowerBodyGarmentImageview;
    private ImageView footwearImageview;
    private ImageView accessoriesImageview;
    private TextView excessAccessoriesTextview;

    public DashboardClothingController(Activity activity, DashboardWeatherController dashboardWeatherController) {
        this.activity = activity;

        overBodyGarmentImageview = activity.findViewById(R.id.dashboard_over_body_garment_imageview);
        upperBodyGarmentImageview = activity.findViewById(R.id.dashboard_upper_body_garment_imageview);
        lowerBodyGarmentImageview = activity.findViewById(R.id.dashboard_lower_body_garment_imageview);
        footwearImageview = activity.findViewById(R.id.dashboard_footwear_imageview);
        accessoriesImageview = activity.findViewById(R.id.dashboard_accessories_imageview);
        excessAccessoriesTextview = activity.findViewById(R.id.dashboard_excess_accessories_textview);

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

        dashboardWeatherController.setNewWeatherDataListener(new DashboardWeatherController.WeatherDataListener() {
            @Override
            public void onNewWeatherDataReady() {
                Log.i(TAG, "Weather data is ready from clothing controller");
                onDataSetChanged();
            }
        });
    }

    /**
     * Handles calculating all the clothing information when new weather data has come in,
     * and updates the clothing display as necessary
     */
    public void onDataSetChanged() {
        // TODO: can calculate clothing information here
        Clothing.calculateOptimalClothing(new ClothingInterface() {
            @Override
            public void onFinish() {
                // load clothing images here
                Log.i(TAG, "Finished calculating all clothing info");
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
        }
        upperBodyGarmentImageview.setImageResource(Clothing.getUpperBodyGarment().getUpperBodyGarmentImage());
        lowerBodyGarmentImageview.setImageResource(Clothing.getLowerBodyGarment().getLowerBodyGarmentImage());
        footwearImageview.setImageResource(Clothing.getFootwear().getFootwearImage());

        List<Integer> accessoriesImages = Clothing.getAccessories().getAccessoriesImages();
        if (!accessoriesImages.isEmpty()) {
            accessoriesImageview.setImageResource(accessoriesImages.get(0));

            if (accessoriesImages.size() > 1) {
                String excessAccessoryCount = "+" + String.valueOf(accessoriesImages.size()-1);
                if (accessoriesImages.size() > 2) {
                    excessAccessoryCount += " accessories";
                } else {
                    excessAccessoryCount += " accessory";
                }
                excessAccessoriesTextview.setText(excessAccessoryCount);
            }
        }
    }

    /**
     * Displays placeholder clothing information to visually signal that there is no clothing data yet
     */
    public void displayPlaceholderOnDashboard() {
        overBodyGarmentImageview.setImageDrawable(null);
        upperBodyGarmentImageview.setImageDrawable(null);
        lowerBodyGarmentImageview.setImageDrawable(null);
        footwearImageview.setImageDrawable(null);

        accessoriesImageview.setImageDrawable(null);
        excessAccessoriesTextview.setText("");
    }
}
