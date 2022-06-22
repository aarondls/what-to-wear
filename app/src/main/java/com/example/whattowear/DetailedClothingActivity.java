package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DetailedClothingActivity extends AppCompatActivity {

    private Button backToDashboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_clothing);

        backToDashboardButton = findViewById(R.id.clothing_back_to_dashboard_button);

        backToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to dashboard screen
                finish();
            }
        });
    }
}