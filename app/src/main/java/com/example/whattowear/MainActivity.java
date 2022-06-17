package com.example.whattowear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.asynchttpclient.callback.TextHttpResponseHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final String TAG = "MainActivity";
    public static final int PERMISSIONS_REQUEST_CODE = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private AsyncHttpClient openweatherClient;
    private Location lastLocation;
    private TextView locationTextview;
    private String units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize fused location client
        // does not need permissions
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        openweatherClient = new AsyncHttpClient();

        // TODO: Change this to get from Parse database
        units = "imperial";

        locationTextview = findViewById(R.id.location_textview);

        // set default location as null
        lastLocation = null;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // request permissions if no permission given (can be moved to a button click or anywhere)
        // can also use permanently denied
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
     * Updates the weather at the last location
     * For safety, checks if location permission have been granted before checking last location
     */
    public void updateWeather() {
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
                                lastLocation = location;

                                String city_name = getLocationName(location);
                                locationTextview.setText(city_name);
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
        String loc_name = "";
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            // set depending on what is known
            if (address.get(0).getLocality() != null) {
                loc_name = address.get(0).getLocality();
            } else if (address.get(0).getSubAdminArea() != null) {
                loc_name = address.get(0).getSubAdminArea();
            } else if (address.get(0).getAdminArea() != null) {
                loc_name = address.get(0).getAdminArea();
            } else if (address.get(0).getCountryName() != null) {
                // Worst case, try to use country name
                loc_name = address.get(0).getCountryName();
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException", e);
        }

        // TODO: Fix city being unknown
        // if city still blank, worst case, set to long lat?
        if (loc_name.isEmpty()) {
            loc_name = "Lat: " + latitude + "Long: " + longitude;
        }

        return loc_name;
    }

    /**
     * Gets the weather from the OpenWeather API at the saved last_location
     */
    private void getWeatherAtLastLocation() {
        if (lastLocation == null) {
            // TODO: Handle null last location
            Log.e(TAG, "Location is null when requesting weather");
            return;
        }

        double latitude = lastLocation.getLatitude();
        double longitude = lastLocation.getLongitude();

        // TODO: Exclude non needed data after detailed weather screen is built
        String api_url = "https://api.openweathermap.org/data/3.0/onecall?"
                + "lat=" + latitude
                + "&lon=" + longitude
                + "&lon=" + longitude
                + "&units=" + units
                + "&appid=" + BuildConfig.OPENWEATHER_API_KEY;

        openweatherClient.get(api_url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // TODO: Update weather data here
                // For now, log response and check if getting specific fields work

                // Log entire response
                Log.i(TAG, json.jsonObject.toString());

                // Check specific response
                try {
                    Log.i(TAG, json.jsonObject.getJSONArray("hourly").toString());
                } catch (JSONException e) {
                    Log.e(TAG, "Could not get requested field", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "Failed to get weather data");
                Log.i(TAG, response);
            }
        });
    }
}
