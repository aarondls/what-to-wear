package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.whattowear.models.Preferences;
import com.example.whattowear.models.Weather;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * MenuActivity represents the menu screen, where the user can move towards
 * the preferences screen and the dashboard screen. The menu screen is accessible
 * by the three-line symbol on the dashboard and preferences screen. For the
 * public feed stretch goal, it would also be accessible within this menu screen.
 */
public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";
    private static final String PLACEHOLDER_LOCATION = "No location";
    private static final String PLUS_SIGN = "+";
    private static final String MORE_ACCESSORIES_PHRASE = " more accessories";
    private static final String PLACEHOLDER_ACCESSORIES_COUNT = "No clothing information.";

    private RelativeLayout dashboardClickable;
    private RelativeLayout overBodyGarmentPreferencesClickable;
    private RelativeLayout upperBodyGarmentPreferencesClickable;
    private RelativeLayout lowerBodyGarmentPreferencesClickable;
    private RelativeLayout footwearPreferencesClickable;
    private RelativeLayout accessoriesPreferencesClickable;

    private TextView dashboardLocationTextview;
    private TextView dashboardCurrentTemperatureTextview;
    private TextView dashboardForecastDescriptionTextview;
    private TextView dashboardCurrentActivityTextview;
    private ImageView dashboardWeatherIconImageview;
    private ImageView dashboardOverBodyGarmentIconImageview;
    private ImageView dashboardUpperBodyGarmentIconImageview;
    private ImageView dashboardLowerBodyGarmentIconImageview;
    private ImageView dashboardFootwearIconImageview;
    private TextView dashboardAccessoriesCountTextview;

    private LabeledSwitch advancedPreferencesSwitch;
    private LabeledSwitch weatherUnitsSwitch;

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // have layout be full screen to hide both the top and bottom bars
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        dashboardClickable = findViewById(R.id.menu_dashboard_option_relative_layout);
        overBodyGarmentPreferencesClickable = findViewById(R.id.menu_over_body_preferences_option_relative_layout);
        upperBodyGarmentPreferencesClickable = findViewById(R.id.menu_upper_body_preferences_option_relative_layout);
        lowerBodyGarmentPreferencesClickable = findViewById(R.id.menu_lower_body_preferences_option_relative_layout);
        footwearPreferencesClickable = findViewById(R.id.menu_footwear_preferences_option_relative_layout);
        accessoriesPreferencesClickable = findViewById(R.id.menu_accessories_preferences_option_relative_layout);

        dashboardLocationTextview = findViewById(R.id.menu_dashboard_location_textview);
        dashboardCurrentTemperatureTextview = findViewById(R.id.menu_dashboard_current_temp_textview);
        dashboardForecastDescriptionTextview = findViewById(R.id.menu_dashboard_forecast_description_textview);
        dashboardCurrentActivityTextview = findViewById(R.id.menu_dashboard_activity_textview);
        dashboardWeatherIconImageview = findViewById(R.id.menu_dashboard_weather_icon_imageview);
        dashboardOverBodyGarmentIconImageview = findViewById(R.id.menu_dashboard_over_body_garment_imageview);
        dashboardUpperBodyGarmentIconImageview = findViewById(R.id.menu_dashboard_upper_body_garment_imageview);
        dashboardLowerBodyGarmentIconImageview = findViewById(R.id.menu_dashboard_lower_body_garment_imageview);
        dashboardFootwearIconImageview = findViewById(R.id.menu_dashboard_footwear_imageview);
        dashboardAccessoriesCountTextview = findViewById(R.id.menu_dashboard_accessories_textview);

        advancedPreferencesSwitch = findViewById(R.id.menu_enable_advanced_preferences_switch);
        weatherUnitsSwitch = findViewById(R.id.menu_weather_unit_switch);

        logoutButton = findViewById(R.id.menu_logout_button);

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

        // Get advanced preferences and weather units switch from Parse
        Preferences preferences = (Preferences) ParseUser.getCurrentUser().getParseObject(Preferences.KEY_PREFERENCES);

        advancedPreferencesSwitch.setOn(preferences.getShowAdvancedPreferences());

        advancedPreferencesSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                // Save to parse
                preferences.setShowAdvancedPreferences(isOn);
                preferences.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            // Let user know something wrong happened
                            Toast.makeText(MenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Boolean isCelsius = (preferences.getWeatherUnit().equals(Weather.WeatherUnit.CELSIUS.name()));
        weatherUnitsSwitch.setOn(isCelsius);

        weatherUnitsSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                // Save to parse and update weather units
                Weather.WeatherUnit weatherUnit;
                if (isOn) {
                    weatherUnit = Weather.WeatherUnit.CELSIUS;
                } else {
                    weatherUnit = Weather.WeatherUnit.FAHRENHEIT;
                }

                // Set the weather units inside preferences and save to parse
                preferences.setWeatherUnit(weatherUnit.name());
                preferences.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            // Let user know something wrong happened
                            Toast.makeText(MenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Update dashboard widget display
                loadDashboardOverviewWidget();
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
                            Intent i = new Intent(MenuActivity.this, LoginUserActivity.class);
                            // clear task and make new one so user cannot go back to activities that require being logged in
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else {
                            // Let user know of error
                            Toast.makeText(MenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loadDashboardOverviewWidget();
    }

    /**
     * Loads the menu dashboard overview widget with the current weather and clothing information, if available
     * If weather or clothing information is unavailable, then placeholder data is displayed
     */
    private void loadDashboardOverviewWidget() {
        // Only display data if it is valid
        if (Weather.hasPreloadedDataToDisplay()) {
            // fill with the weather data
            loadDashboardWeatherOverviewWidget();
        } else {
            // use placeholder
            loadPlaceholderDashboardWeatherOverviewWidget();
        }
        if (Clothing.hasPreloadedDataToDisplay()) {
            // fill with clothing data
            loadDashboardClothingOverviewWidget();
        } else {
            // use placeholder
            loadPlaceholderDashboardClothingOverviewWidget();
        }
    }

    /**
     * Loads the meny dashboard overview widget with weather data
     */
    private void loadDashboardWeatherOverviewWidget() {
        dashboardLocationTextview.setText(Weather.getLastLocationName());

        Forecast currentForecast = Weather.getCurrentForecast();
        Conditions currentConditions = currentForecast.getHourCondition();
        dashboardCurrentTemperatureTextview.setText(currentForecast.getFormattedTemp());
        dashboardForecastDescriptionTextview.setText(currentConditions.getConditionDescription());
        Glide.with(this).load(currentConditions.getConditionIconLink()).into(dashboardWeatherIconImageview);
    }

    /**
     * Loads the meny dashboard overview widget with clothing data
     */
    private void loadDashboardClothingOverviewWidget() {
        dashboardCurrentActivityTextview.setText(Clothing.getSelectedActivityType().getDescription().toLowerCase());
        if (Clothing.getOverBodyGarment().getOverBodyGarmentImage() != null) {
            dashboardOverBodyGarmentIconImageview.setImageResource(Clothing.getOverBodyGarment().getOverBodyGarmentImage());
        } else {
            dashboardOverBodyGarmentIconImageview.setImageDrawable(null);
        }
        dashboardUpperBodyGarmentIconImageview.setImageResource(Clothing.getUpperBodyGarment().getUpperBodyGarmentImage());
        dashboardLowerBodyGarmentIconImageview.setImageResource(Clothing.getLowerBodyGarment().getLowerBodyGarmentImage());
        dashboardFootwearIconImageview.setImageResource(Clothing.getFootwear().getFootwearImage());
        int accessoriesSize = Clothing.getAccessories().getAccessoriesImages().size();
        if (accessoriesSize > 0) {
            String accessoriesCountPhrase = PLUS_SIGN + accessoriesSize + MORE_ACCESSORIES_PHRASE;
            dashboardAccessoriesCountTextview.setText(accessoriesCountPhrase);
        }
    }

    /**
     * Loads the menu dashboard overview widget with placeholder weather data
     */
    private void loadPlaceholderDashboardWeatherOverviewWidget() {
        dashboardLocationTextview.setText(PLACEHOLDER_LOCATION);
    }

    /**
     * Loads the menu dashboard overview widget with placeholder clothing data
     */
    private void loadPlaceholderDashboardClothingOverviewWidget() {
        dashboardAccessoriesCountTextview.setText(PLACEHOLDER_ACCESSORIES_COUNT);
    }

    /**
     * Goes to the preferences screen with the desired clothing type factors
     * @param clothingType the clothingType of the factors to display
     */
    private void moveToPreferences(Clothing.ClothingType clothingType) {
        // Move to preferences screen
        Intent i = new Intent(MenuActivity.this, PreferencesActivity.class);
        i.putExtra(PreferencesActivity.IS_ADVANCED_SETTINGS_KEY, advancedPreferencesSwitch.isOn());
        i.putExtra(PreferencesActivity.PREFERENCES_TYPE_KEY, clothingType);
        startActivity(i);
    }
}