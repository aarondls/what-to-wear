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

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.example.whattowear.models.Weather;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * DashboardActivity represents the main dashboard, which is the first screen that
 * the user sees upon logging into the app. From the main dashboard, the user
 * can see the weather and clothing overview, and navigate towards
 * the detailed weather/clothing screens, as well as the menu screen.
 */
public class DashboardActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final String TAG = "DashboardActivity";

    public static final int PERMISSIONS_REQUEST_CODE = 1;

    /**
     * Used to let weather controller know when new location data is available
     */
    public interface LocationDataListener {
        public void onNewLocationDataReady();
    }

    private FusedLocationProviderClient fusedLocationClient;
    private AsyncHttpClient openWeatherClient;

    private DashboardWeatherController dashboardWeatherController;
    private DashboardClothingController dashboardClothingController;

    private LocationDataListener locationDataListener;

    private Button detailedClothingButton;
    private Button menuButton;
    private RelativeLayout detailedWeatherClickable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        locationDataListener = null;

        // initialize fused location client, which does not need user location permissions
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        openWeatherClient = new AsyncHttpClient();

        // this handles getting new weather data when location data changes
        dashboardWeatherController = new DashboardWeatherController(this);

        // this handles calculating new clothing data when weather data changes
        dashboardClothingController = new DashboardClothingController(this, dashboardWeatherController);

        detailedClothingButton = findViewById(R.id.detailed_clothing_button);
        menuButton = findViewById(R.id.dashboard_to_menu_button);
        detailedWeatherClickable = findViewById(R.id.forecast_3hr_relativelayout);

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

        detailedWeatherClickable.setOnClickListener(new View.OnClickListener() {
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
            getUserLocation();
        }
        Log.i(TAG, "Finished asking permissions");

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

        getUserLocation();
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
     * Updates lastLocation to be the user's last location
     * Notifies the weather controller if location data changes
     */
    @SuppressLint("MissingPermission") // permission is checked with hasLocationPermissions method
    private void getUserLocation() {
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
                                // give weather new location
                                Weather.setLastLocationName(getLocationName(location));
                                Weather.setLastLocationLatitude(location.getLatitude());
                                Weather.setLastLocationLongitude(location.getLongitude());

                                // let location listener (weather controller) know new location data is available
                                if (locationDataListener != null) {
                                    locationDataListener.onNewLocationDataReady();
                                }

                            } else {
                                // TODO: Handle no location found
                                Log.e(TAG, "No location");
                                Toast.makeText(DashboardActivity.this, "No location found. No weather or clothing information will be displayed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // TODO: handle case where location services permission is not granted
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
}