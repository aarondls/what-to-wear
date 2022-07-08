package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.button.MaterialButtonToggleGroup;

import static com.example.whattowear.models.Clothing.ClothingType;

public class PreferencesActivity extends AppCompatActivity {
    public static final String IS_ADVANCED_SETTINGS_KEY = "isAdvancedSettings";
    private Button menuButton;

    private RecyclerView preferencesReyclerview;
    private PreferencesAdapter preferencesAdapter;
    private MaterialButtonToggleGroup preferencesButtonToggleGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        menuButton = findViewById(R.id.preferences_to_menu_button);
        preferencesReyclerview = findViewById(R.id.preferences_recycleview);
        preferencesButtonToggleGroup = findViewById(R.id.preferences_button_toggle_group);
        preferencesButtonToggleGroup.check(R.id.over_body_preferences_button);

        // have layout be full screen to hide both the top and bottom bars
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to menu screen
                finish();
            }
        });

        preferencesButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId == R.id.over_body_preferences_button && isChecked) {
                    preferencesAdapter.onNewClothingType(ClothingType.OVER_BODY_GARMENT);
                } else if (checkedId == R.id.upper_body_preferences_button && isChecked) {
                    preferencesAdapter.onNewClothingType(ClothingType.UPPER_BODY_GARMENT);
                } else if (checkedId == R.id.lower_body_preferences_button && isChecked) {
                    preferencesAdapter.onNewClothingType(ClothingType.LOWER_BODY_GARMENT);
                } else if (checkedId == R.id.footwear_preferences_button && isChecked) {
                    preferencesAdapter.onNewClothingType(ClothingType.FOOTWEAR);
                } else if (checkedId == R.id.accessories_preferences_button && isChecked) {
                    preferencesAdapter.onNewClothingType(ClothingType.ACCESSORIES);
                }
                // always scroll to top
                preferencesReyclerview.scrollToPosition(0);
            }
        });

        // set up recycler view with adapter
        // pass in whether advanced settings need to be displayed
        preferencesAdapter = new PreferencesAdapter(this, ClothingType.OVER_BODY_GARMENT, getIntent().getExtras().getBoolean(IS_ADVANCED_SETTINGS_KEY)); // starts with over body
        preferencesReyclerview.setLayoutManager(new LinearLayoutManager(this));
        preferencesReyclerview.setAdapter(preferencesAdapter);
    }
}