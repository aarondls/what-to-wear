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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final String TAG = "MainActivity";
    public static final int PERMISSIONS_REQUEST_CODE = 1;

    private FusedLocationProviderClient fusedLocationClient;

    private TextView location_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize fused location client
        // does not need permissions
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        location_textview = findViewById(R.id.location_textview);
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

        updateLocationToLastLocation();
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

        updateLocationToLastLocation();
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
     * Gets the last location
     * For safety, checks if location permission have been granted
     */
    public void updateLocationToLastLocation() {
        if (hasLocationPermissions()) {
            Log.i(TAG, "Location permissions granted");
            // needs permissions to get location
            // use get last location. can also do current location
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.i(TAG, "Got location");
                                Log.i(TAG, Double.toString(location.getLatitude()));
                                Log.i(TAG, Double.toString(location.getLongitude()));
                                String city_name = getCityName(location);
                                Log.i(TAG, city_name);

                                location_textview.setText(city_name);
                            } else {
                                Log.i(TAG, "No location");
                            }
                        }
                    });
        }
    }

    /**
     * Used to get the city name from a Location
     * @param location  the Location of the city
     * @return  the city name of the passed Location
     */
    private String getCityName(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        String city = "";
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            city = address.get(0).getLocality();
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException", e);
        }

        return city;
    }
}