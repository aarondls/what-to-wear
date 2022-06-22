package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button goToDashboardButton;
    private Button goToPreferencesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        goToDashboardButton = findViewById(R.id.menu_select_dashboard_button);
        goToPreferencesButton = findViewById(R.id.menu_select_preferences_button);

        goToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to dashboard screen
                Intent i = new Intent(MenuActivity.this, DashboardActivity.class);
                startActivity(i);
            }
        });

        goToPreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to preferences screen
                Intent i = new Intent(MenuActivity.this, PreferencesActivity.class);
                startActivity(i);
            }
        });
    }
}