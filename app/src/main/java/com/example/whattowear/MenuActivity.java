package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * MenuActivity represents the menu screen, where the user can move towards
 * the preferences screen and the dashboard screen. The menu screen is accessible
 * by the three-line symbol on the dashboard and preferences screen. For the
 * public feed stretch goal, it would also be accessible within this menu screen.
 */
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
                i.putExtra(PreferencesActivity.IS_ADVANCED_SETTINGS_KEY, false);
                startActivity(i);
            }
        });

        goToPreferencesButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(MenuActivity.this, PreferencesActivity.class);
                i.putExtra(PreferencesActivity.IS_ADVANCED_SETTINGS_KEY, true);
                startActivity(i);
                return true;
            }
        });
    }
}