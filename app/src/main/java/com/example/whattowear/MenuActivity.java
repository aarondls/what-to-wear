package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whattowear.models.Clothing;
import com.example.whattowear.models.Conditions;
import com.example.whattowear.models.Forecast;
import com.example.whattowear.models.Weather;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * MenuActivity represents the menu screen, where the user can move towards
 * the preferences screen and the dashboard screen. The menu screen is accessible
 * by the three-line symbol on the dashboard and preferences screen. For the
 * public feed stretch goal, it would also be accessible within this menu screen.
 */
public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";
    private static final String PLACEHOLDER_LOCATION = "No location";

    private RelativeLayout dashboardClickable;
    private RelativeLayout overBodyGarmentPreferencesClickable;
    private RelativeLayout upperBodyGarmentPreferencesClickable;
    private RelativeLayout lowerBodyGarmentPreferencesClickable;
    private RelativeLayout footwearPreferencesClickable;
    private RelativeLayout accessoriesPreferencesClickable;
    private Button logoutButton;
    private TextView dashboardLocationTextview;
    private TextView dashboardCurrentTemperatureTextview;
    private TextView dashboardForecastDescriptionTextview;
    private ImageView dashboardWeatherIconImageview;
    private SwitchMaterial advancedPreferencesSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        dashboardClickable = findViewById(R.id.menu_dashboard_option_relative_layout);
        overBodyGarmentPreferencesClickable = findViewById(R.id.menu_over_body_preferences_option_relative_layout);
        upperBodyGarmentPreferencesClickable = findViewById(R.id.menu_upper_body_preferences_option_relative_layout);
        lowerBodyGarmentPreferencesClickable = findViewById(R.id.menu_lower_body_preferences_option_relative_layout);
        footwearPreferencesClickable = findViewById(R.id.menu_footwear_preferences_option_relative_layout);
        accessoriesPreferencesClickable = findViewById(R.id.menu_accessories_preferences_option_relative_layout);
        advancedPreferencesSwitch = findViewById(R.id.menu_enable_advanced_preferences_switch);
        logoutButton = findViewById(R.id.menu_logout_button);

        dashboardLocationTextview = findViewById(R.id.menu_dashboard_location_textview);
        dashboardCurrentTemperatureTextview = findViewById(R.id.menu_dashboard_current_temp_textview);
        dashboardForecastDescriptionTextview = findViewById(R.id.menu_dashboard_forecast_description_textview);
        dashboardWeatherIconImageview = findViewById(R.id.menu_dashboard_weather_icon_imageview);

        // TODO: create diff gradient for morning/afternoon/night
        // Put gradient as background
        // for now, use morning
        View dashboardView = findViewById(R.id.menu_relative_layout);
        dashboardView.setBackground(AppCompatResources.getDrawable(this, R.drawable.dark_gradient));

        // have layout be full screen to hide both the top and bottom bars
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        dashboardClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to dashboard screen
                Intent i = new Intent(MenuActivity.this, DashboardActivity.class);
                startActivity(i);
            }
        });

        overBodyGarmentPreferencesClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToPreferences(Clothing.ClothingType.OVER_BODY_GARMENT);
            }
        });

        upperBodyGarmentPreferencesClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToPreferences(Clothing.ClothingType.UPPER_BODY_GARMENT);
            }
        });

        lowerBodyGarmentPreferencesClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToPreferences(Clothing.ClothingType.LOWER_BODY_GARMENT);
            }
        });

        footwearPreferencesClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToPreferences(Clothing.ClothingType.FOOTWEAR);
            }
        });

        accessoriesPreferencesClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToPreferences(Clothing.ClothingType.ACCESSORIES);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // Successfully logged out
                            Toast.makeText(MenuActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                            // Move to login screen
                            Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                            // clear task and make new one so user cannot go back to activities that require being logged in
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else {
                            // Let user know of error
                            Toast.makeText(MenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });
            }
        });

        // TODO: get whether switch is toggled from parse

        // Only display data if it is valid
        if (Weather.hasPreloadedDataToDisplay()) {
            // fill with the weather data
            loadDashboardOverviewWidget();
        } else {
            // use placeholder
            loadPlaceholderDashboardOverviewWidget();
        }
    }

    /**
     * Loads the menu dashboard overview widget with the current weather information
     */
    private void loadDashboardOverviewWidget() {
        dashboardLocationTextview.setText(Weather.getLastLocationName());

        Forecast currentForecast = Weather.getCurrentForecast();
        Conditions currentConditions = currentForecast.getHourCondition();
        dashboardCurrentTemperatureTextview.setText(currentForecast.getFormattedTemp());
        dashboardForecastDescriptionTextview.setText(currentConditions.getConditionDescription());
        Glide.with(this).load(currentConditions.getConditionIconLink()).into(dashboardWeatherIconImageview);
    }

    /**
     * Loads the menu dashboard overview widget with placeholder data
     */
    private void loadPlaceholderDashboardOverviewWidget() {
        // TODO: fix better placeholder
        dashboardLocationTextview.setText(PLACEHOLDER_LOCATION);
    }

    /**
     * Goes to the preferences screen with the desired clothing type factors
     * @param clothingType the clothingType of the factors to display
     */
    private void moveToPreferences(Clothing.ClothingType clothingType) {
        // Move to preferences screen
        Intent i = new Intent(MenuActivity.this, PreferencesActivity.class);
        i.putExtra(PreferencesActivity.IS_ADVANCED_SETTINGS_KEY, advancedPreferencesSwitch.isChecked());
        i.putExtra(PreferencesActivity.PREFERENCES_TYPE_KEY, clothingType);
        startActivity(i);
    }
}