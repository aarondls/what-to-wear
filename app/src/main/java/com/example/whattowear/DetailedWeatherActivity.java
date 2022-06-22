package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.whattowear.models.Weather;

/**
 * DetailedWeatherActivity represents the screen where
 * the current weather information is shown in detail
 * using a RecycleView. This screen is accessible from
 * the main dashboard screen.
 */
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

        locationTextview = findViewById(R.id.detailed_weather_location_textview);
        backToDashboardButton = findViewById(R.id.weather_back_to_dashboard_button);
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
