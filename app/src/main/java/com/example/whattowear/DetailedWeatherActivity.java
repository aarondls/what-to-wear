package com.example.whattowear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
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

public class DetailedWeatherActivity extends AppCompatActivity {
    public static final String TAG = "DetailedWeatherActivity";

    private TextView locationTextview;
    private Button backToDashboardButton;
    private RecyclerView weatherRecyclerview;
    private WeatherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_weather);

        locationTextview = findViewById(R.id.location_textview);
        backToDashboardButton = findViewById(R.id.dw_back_to_dashboard_button);
        weatherRecyclerview = findViewById(R.id.weather_info_recycleview);

        backToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to dashboard screen
                finish();
            }
        });

        // initialize recycle view
        adapter = new WeatherAdapter(this);
        // Recycleview setup: layout manager and adapter
        weatherRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        weatherRecyclerview.setAdapter(adapter);

        // notify adapter that weather data is ready
        adapter.notifyDataSetChanged();

        locationTextview.setText(Weather.getLastLocationName());
    }
}
