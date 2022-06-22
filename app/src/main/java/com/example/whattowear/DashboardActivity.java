package com.example.whattowear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.whattowear.models.Forecast;
import com.example.whattowear.models.Weather;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class DashboardActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final String TAG = "DashboardActivity";

    public static final int PERMISSIONS_REQUEST_CODE = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private AsyncHttpClient openWeatherClient;

    private TextView locationTextview;
    private TextView forecastDescriptionTextview;
    private TextView currentTemperatureTextview;
    private ImageView weatherIconImageview;

    private TextView forecast1HrTimeTextview;
    private ImageView forecast1HrWeatherIconImageview;
    private TextView forecast1HrTempTextview;
    private TextView forecast2HrTimeTextview;
    private ImageView forecast2HrWeatherIconImageview;
    private TextView forecast2HrTempTextview;
    private TextView forecast3HrTimeTextview;
    private ImageView forecast3HrWeatherIconImageview;
    private TextView forecast3HrTempTextview;

    private Button detailedClothingButton;
    private Button menuButton;

    private RelativeLayout forecast3HrRelativelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // initialize fused location client
        // does not need permissions
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        openWeatherClient = new AsyncHttpClient();

        locationTextview = findViewById(R.id.dashboard_location_textview);
        forecastDescriptionTextview = findViewById(R.id.dashboard_forecast_description_textview);
        currentTemperatureTextview = findViewById(R.id.dashboard_current_temp_textview);
        weatherIconImageview = findViewById(R.id.dashboard_weather_icon_imageview);

        forecast1HrTimeTextview = findViewById(R.id.dashboard_1hr_time_textview);
        forecast1HrWeatherIconImageview = findViewById(R.id.dashboard_1hr_weather_icon_imageview);
        forecast1HrTempTextview = findViewById(R.id.dashboard_1hr_temp_textview);
        forecast2HrTimeTextview = findViewById(R.id.dashboard_2hr_time_textview);
        forecast2HrWeatherIconImageview = findViewById(R.id.dashboard_2hr_weather_icon_imageview);
        forecast2HrTempTextview = findViewById(R.id.dashboard_2hr_temp_textview);
        forecast3HrTimeTextview = findViewById(R.id.dashboard_3hr_time_textview);
        forecast3HrWeatherIconImageview = findViewById(R.id.dashboard_3hr_weather_icon_imageview);
        forecast3HrTempTextview = findViewById(R.id.dashboard_3hr_temp_textview);

        detailedClothingButton = findViewById(R.id.detailed_clothing_button);
        menuButton = findViewById(R.id.dashboard_to_menu_button);

        forecast3HrRelativelayout = findViewById(R.id.forecast_3hr_relativelayout);

        detailedClothingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to detailed clothing screen
                Intent i = new Intent(DashboardActivity.this, DetailedClothingActivity.class);
                startActivity(i);
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to menu screen
                Intent i = new Intent(DashboardActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        forecast3HrRelativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to detailed weather screen
                Intent i = new Intent(DashboardActivity.this, DetailedWeatherActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // request permissions if no permission given (can be moved to a button click or anywhere)
        if (!hasLocationPermissions()) {
            requestLocationPermissions();
        }
        Log.i(TAG, "Finished asking permissions");

        updateWeather();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Used to check if location permissions have been granted
     * @return  whether the application has locations services permissions
     */
    private boolean hasLocationPermissions() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * Used to request location permissions
     */
    private void requestLocationPermissions() {
        EasyPermissions.requestPermissions(
                this,
                "This application requires location services to auto detect your location.",
                PERMISSIONS_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "Location services permission granted!", Toast.LENGTH_SHORT).show();

        updateWeather();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "Location services permission denied!", Toast.LENGTH_SHORT).show();

        // Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            requestLocationPermissions();
        }
    }

    /**
     * Updates weatherData at lastLocation
     * For safety, checks if location permissions have been granted before checking last location
     */
    @SuppressLint("MissingPermission") // permission is checked with hasLocationPermissions method
    public void updateWeather() {
        // first fetch last location
        if (hasLocationPermissions()) {
            Log.i(TAG, "Location permissions granted");
            // needs permissions to get location
            // uses last location since it gets the location estimate quicker and reduces battery usage
            // the exact current location isn't that important as weather data is local to the general area
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Weather.setLastLocation(location);

                                String locationName = getLocationName(location);
                                Weather.setLastLocationName(locationName);
                                locationTextview.setText(locationName);

                                getWeatherAtLastLocation();
                            } else {
                                // TODO: Handle no location found
                                Log.e(TAG, "No location");
                            }
                        }
                    });
        }
    }

    /**
     * Used to get the city name from a Location
     * @param location  the Location of the city
     * @return  the name of the passed Location
     * the name could be, in order of which exists first:
     * locality, sub admin area, admin area, country name, long/lat coordinates
     */
    private String getLocationName(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        String locationName = "";
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            // set depending on what is known
            if (address.get(0).getLocality() != null) {
                locationName = address.get(0).getLocality();
            } else if (address.get(0).getSubAdminArea() != null) {
                locationName = address.get(0).getSubAdminArea();
            } else if (address.get(0).getAdminArea() != null) {
                locationName = address.get(0).getAdminArea();
            } else if (address.get(0).getCountryName() != null) {
                // Worst case, try to use country name
                locationName = address.get(0).getCountryName();
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException", e);
        }

        // TODO: Fix city being unknown
        // if city still blank, worst case, it is set to long lat coordinates
        if (locationName.isEmpty()) {
            locationName = "Lat: " + latitude + "Long: " + longitude;
        }

        return locationName;
    }

    /**
     * Gets the weather from the OpenWeather API at the saved last_location
     */
    private void getWeatherAtLastLocation() {
        if (Weather.getLastLocation() == null) {
            // TODO: Handle null last location
            Log.e(TAG, "Location is null when requesting weather");
            return;
        }

        double latitude = Weather.getLastLocation().getLatitude();
        double longitude = Weather.getLastLocation().getLongitude();

        // TODO: Exclude non needed data after detailed weather screen is built
        String apiUrl = "https://api.openweathermap.org/data/3.0/onecall?"
                + "lat=" + latitude
                + "&lon=" + longitude
                + "&lon=" + longitude
                + "&units=" + Weather.getWeatherUnits()
                + "&appid=" + BuildConfig.OPENWEATHER_API_KEY;

        Log.i(TAG, "Requesting weather data");
        openWeatherClient.get(apiUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // TODO: Update weather data here
                // Log entire response
                Log.i(TAG, json.jsonObject.toString());

                // Get relevant data
                try {
                    // load the weather data from the received json data
                    // note that weather is a singleton class, so it can be loaded directly
                    Weather.loadFromJson(json.jsonObject);
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to convert JSON data into Weather object", e);
                    e.printStackTrace();
                }

                // TODO: if adding recycle view in this activity, notify adapter here
                // should also set anything after new weather data comes in here
                forecastDescriptionTextview.setText(Weather.getCurrentForecast().getHourCondition().getConditionDescription());
                currentTemperatureTextview.setText(Weather.getCurrentForecast().getFormattedTemp());

                // TODO: can convert all image icons below into using local images based on condition ID, when graphics are available
                List<Forecast> hourlyForecast = Weather.getHourlyForecast();
                // ensure forecasts exist at projected hour
                int numForecast = hourlyForecast.size();
                if (numForecast >= 2) {
                    forecast1HrTimeTextview.setText(hourlyForecast.get(1).getAMPMTime());
                    Glide.with(DashboardActivity.this).load(hourlyForecast.get(1).getHourCondition().getConditionIconLink()).into(forecast1HrWeatherIconImageview);
                    forecast1HrTempTextview.setText(hourlyForecast.get(1).getFormattedTemp());
                }
                if (numForecast >= 3) {
                    forecast2HrTimeTextview.setText(hourlyForecast.get(2).getAMPMTime());
                    Glide.with(DashboardActivity.this).load(hourlyForecast.get(2).getHourCondition().getConditionIconLink()).into(forecast2HrWeatherIconImageview);
                    forecast2HrTempTextview.setText(hourlyForecast.get(2).getFormattedTemp());
                }
                if (numForecast >= 4) {
                    forecast3HrTimeTextview.setText(hourlyForecast.get(3).getAMPMTime());
                    Glide.with(DashboardActivity.this).load(hourlyForecast.get(3).getHourCondition().getConditionIconLink()).into(forecast3HrWeatherIconImageview);
                    forecast3HrTempTextview.setText(hourlyForecast.get(3).getFormattedTemp());
                }

                Glide.with(DashboardActivity.this).load(Weather.getCurrentForecast().getHourCondition().getConditionIconLink()).into(weatherIconImageview);
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Failed to get weather data");
                Log.e(TAG, response);
            }
        });
    }
}