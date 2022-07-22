package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * DetailedWeatherActivity represents the screen where
 * the current weather information is shown in detail
 * using a RecycleView. This screen is accessible from
 * the main dashboard screen.
 */
public class DetailedWeatherActivity extends AppCompatActivity {
    public static final String TAG = "DetailedWeatherActivity";

    private Button backToDashboardButton;

    private DetailedWeatherController detailedWeatherController;

    private RecyclerView weatherRecyclerview;
    private WeatherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_weather);

        // have layout be full screen to hide both the top and bottom bars
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        backToDashboardButton = findViewById(R.id.weather_back_to_dashboard_button);

        detailedWeatherController = new DetailedWeatherController(this);

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
    }
}
