package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.whattowear.models.Clothing;

import static com.example.whattowear.models.Clothing.ClothingType;

public class PreferencesActivity extends AppCompatActivity {
    public static final String IS_ADVANCED_SETTINGS_KEY = "isAdvancedSettings";
    public static final String PREFERENCES_TYPE_KEY = "preferencesType";

    private Button menuButton;
    private TextView clothingTypeTextview;
    private RecyclerView preferencesReyclerview;
    private PreferencesAdapter preferencesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // have layout be full screen to hide both the top and bottom bars
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        menuButton = findViewById(R.id.preferences_to_menu_button);
        clothingTypeTextview = findViewById(R.id.preferences_type_textview);
        preferencesReyclerview = findViewById(R.id.preferences_recycleview);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to menu screen
                finish();
            }
        });

        // Get passed clothing type
        ClothingType clothingType = (ClothingType) getIntent().getSerializableExtra(PREFERENCES_TYPE_KEY);
        clothingTypeTextview.setText(Clothing.getClothingNameFromType(clothingType));

        // TODO: store and get whether preferences advanced settings was checked from parse

        // set up recycler view with adapter
        // pass in correct clothing type
        // pass in whether advanced settings need to be displayed
        preferencesAdapter = new PreferencesAdapter(this, clothingType, getIntent().getExtras().getBoolean(IS_ADVANCED_SETTINGS_KEY));
        preferencesReyclerview.setLayoutManager(new LinearLayoutManager(this));
        preferencesReyclerview.setAdapter(preferencesAdapter);
    }
}