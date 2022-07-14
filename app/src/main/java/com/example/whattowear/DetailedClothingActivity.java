package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/** DetailedClothingActivity represents the screen where
 * the user's clothing recommendations are shown in detail
 * using a RecycleView. This is accessible from the main
 * dashboard screen.
 */
public class DetailedClothingActivity extends AppCompatActivity {

    private Button backToDashboardButton;
    private RecyclerView clothingRecyclerview;
    private ClothingAdapter clothingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_clothing);

        backToDashboardButton = findViewById(R.id.clothing_back_to_dashboard_button);
        clothingRecyclerview = findViewById(R.id.clothing_info_recycleview);

        backToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to dashboard screen
                finish();
            }
        });

        // TODO: create diff gradient for morning/afternoon/night
        // Put gradient as background
        // for now, use morning
        View dashboardView = findViewById(R.id.detailed_clothing_relative_layout);
        dashboardView.setBackground(AppCompatResources.getDrawable(this, R.drawable.morning_gradient));

        // have layout be full screen to hide both the top and bottom bars
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Set up the adapter for the recycleview
        clothingAdapter = new ClothingAdapter(this);
        clothingRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        clothingRecyclerview.setAdapter(clothingAdapter);

        clothingAdapter.notifyDataSetChanged();
    }
}