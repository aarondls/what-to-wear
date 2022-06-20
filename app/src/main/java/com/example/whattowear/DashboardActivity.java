package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {
    private Button detailedWeatherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        detailedWeatherButton = findViewById(R.id.detailed_weather_button);

        detailedWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to detailed weather screen
                Intent i = new Intent(DashboardActivity.this, DetailedWeatherActivity.class);
                startActivity(i);
            }
        });
    }
}