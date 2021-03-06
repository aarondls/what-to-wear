package com.example.whattowear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.example.whattowear.models.Weather;
import com.example.whattowear.weatheranimation.DashboardWeatherAnimationController;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

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
    private PlacesClient placesClient;

    private AutocompleteSupportFragment autocompleteFragment;

    private DashboardWeatherController dashboardWeatherController;
    private DashboardClothingController dashboardClothingController;
    private DashboardWeatherAnimationController dashboardWeatherAnimationController;
    private LocationDataListener locationDataListener;

    private Button detailedClothingButton;
    private Button menuButton;
    private RelativeLayout detailedWeatherClickable;

    private Button locationServicesDeniedWarningButton;
    private Button getUserLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        locationDataListener = null;

        // initialize fused location client, which does not need user location permissions
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        openWeatherClient = new AsyncHttpClient();

        // Initialize the Places SDK
        Places.initialize(getApplicationContext(), BuildConfig.GOOGLEPLACES_API_KEY);
        // Create new Places client instance
        placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setHint("");

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));

        // this handles getting new weather data when location data changes
        dashboardWeatherController = new DashboardWeatherController(this);

        // this handles calculating new clothing data when weather data changes
        dashboardClothingController = new DashboardClothingController(this, dashboardWeatherController);

        // this handles the weather animation in the background
        dashboardWeatherAnimationController = new DashboardWeatherAnimationController(findViewById(R.id.dashboard_weather_animation_view), dashboardWeatherController);

        detailedClothingButton = findViewById(R.id.detailed_clothing_button);
        menuButton = findViewById(R.id.dashboard_to_menu_button);
        detailedWeatherClickable = findViewById(R.id.forecast_3hr_relativelayout);

        getUserLocationButton = findViewById(R.id.get_user_location_button);
        getUserLocationButton.setVisibility(View.GONE);
        locationServicesDeniedWarningButton = findViewById(R.id.location_services_denied_warning_button);
        locationServicesDeniedWarningButton.setVisibility(View.GONE);

        // TODO: create diff gradient for morning/afternoon/night
        // Put gradient as background
        // for now, use morning
        View dashboardView = findViewById(R.id.dashboard_relativelayout);
        dashboardView.setBackground(AppCompatResources.getDrawable(this, R.drawable.morning_gradient));

        // have layout be full screen to hide both the top and bottom bars
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Change places search icon to white drawable icon
        ImageView searchIcon = (ImageView)((LinearLayout)autocompleteFragment.getView()).getChildAt(0);
        searchIcon.setImageResource(R.drawable.ic_baseline_search_24);

        detailedClothingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to detailed clothing screen
                Intent i = new Intent(DashboardActivity.this, DetailedClothingActivity.class);
                startActivity(i);
            }
        });

        getUserLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // user permissions is available if this button is displayed
                getUserLocation();
            }
        });

        locationServicesDeniedWarningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display warning snackbar, and allow user to change if they wish by redirecting to settings app
                Snackbar.make(v, "Location services is permanently denied and app functionality is reduced.", Snackbar.LENGTH_LONG)
                        .setAction("Change settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // directing user to enable the permission in app settings.
                                new AppSettingsDialog.Builder(DashboardActivity.this).build().show();
                            }
                        }).show();
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

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // give weather new location
                Weather.setLastLocation(place);

                // let location listener (weather controller) know new location data is available
                if (locationDataListener != null) {
                    locationDataListener.onNewLocationDataReady();
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG,status.toString());

                // the error could be either hitting back or something more serious
                // if it is serious, the status message is not null, so display as a toast
                if (status.getStatusMessage() != null) {
                    Toast.makeText(DashboardActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setNewLocationDataListener(LocationDataListener locationDataListener) {
        this.locationDataListener = locationDataListener;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Weather.getLastLocationName().isEmpty()) { // check if new location needs to be updated or not
            // need to update with new location
            Log.i(TAG, "loc is empty");
            requestUserLocation();
        } else if (hasLocationPermissions()) { // prepare app depending on permissions
            handleLocationServicesAccepted();
        } else {
            handleLocationServicesDenied();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onrestart");

        // restart the weather animation if it exists
        dashboardWeatherAnimationController.onDashboardActivityRestart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onresume");
        super.onResume();

        autocompleteFragment.setText("");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Used to check if location permissions have been granted
     * @return whether the application has locations services permissions
     */
    private boolean hasLocationPermissions() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * Used to request user location if location permissions haven't been given
     * Asks the user for permissions and gets user location if the user provides
     * permission to access location always/once
     * If permission is already given when this is called, then the user location
     * is immediately queried without asking for permission
     */
    private void requestUserLocation() {
        if (hasLocationPermissions()) {
            getUserLocation();
            handleLocationServicesAccepted();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.i(TAG, "permanently denied");
            // TODO: stretch goal to animate button bouncing to highlight whats wrong
            handleLocationServicesDenied();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This application requires location services to auto detect your location.",
                    PERMISSIONS_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "Location services permission granted!", Toast.LENGTH_SHORT).show();

        // immediately get current location
        getUserLocation();
        
        handleLocationServicesAccepted();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "Location services permission denied!", Toast.LENGTH_SHORT).show();

        handleLocationServicesDenied();
    }

    /**
     * Prepares app to work with location services granted
     */
    private void handleLocationServicesAccepted() {
        locationServicesDeniedWarningButton.setVisibility(View.GONE);
        getUserLocationButton.setVisibility(View.VISIBLE);
    }

    /**
     * Prepares app to work even without location services so the user can still use certain functionalities
     * and not get locked out of the app
     */
    private void handleLocationServicesDenied() {
        locationServicesDeniedWarningButton.setVisibility(View.VISIBLE);
        getUserLocationButton.setVisibility(View.GONE);
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
                                Weather.setLastLocation(location, DashboardActivity.this);

                                // let location listener (weather controller) know new location data is available
                                if (locationDataListener != null) {
                                    locationDataListener.onNewLocationDataReady();
                                }

                            } else {
                                Log.e(TAG, "No location found");
                                Toast.makeText(DashboardActivity.this, "No location found. Check your GPS or manually enter in a location.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}