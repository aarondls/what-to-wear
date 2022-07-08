package com.example.whattowear;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattowear.models.Clothing;
import com.example.whattowear.models.ClothingRanker;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.List;

import static com.example.whattowear.models.Clothing.ClothingType;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.ViewHolder> {
    private static final String TAG = "PreferencesAdapter";
    private Context context;
    private Boolean isAdvancedSettings;
    private List<ClothingRanker> selectedClothingRankers;

    public PreferencesAdapter(Context context, ClothingType clothingType, Boolean isAdvancedSettings) {
        this.context = context;
        this.isAdvancedSettings = isAdvancedSettings;
        selectedClothingRankers = Clothing.getRankersWithType(clothingType);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_preferences_row, parent, false);

        // Return a new holder instance
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get correct item based on position
        ClothingRanker clothingRanker = selectedClothingRankers.get(position);
        // the above can return null, if position is out of bounds of if the clothing type is not recognized
        // this will not happen due to the way clothing type is set up and the way the number of rankers are found from Clothing

        // Bind the ranker data
        holder.bind(clothingRanker);
    }

    @Override
    public int getItemCount() {
        return selectedClothingRankers.size();
    }

    /**
     * Used to notify adapter when the preferences for a new clothing type should be displayed
     * Calls the notifyDataSetChanged method
     * @param clothingType the desired clothing type preferences to display
     */
    public void onNewClothingType(ClothingType clothingType) {
        selectedClothingRankers = Clothing.getRankersWithType(clothingType);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ClothingRanker clothingRanker;

        public TextView preferenceClothingTypeTextview;
        public ImageView clothingIcon;

        public TextView activityImportanceTextview;
        public TextView workFactorTextview;
        public TextView sportsFactorTextview;
        public TextView casualFactorTextview;
        public TextView temperatureImportanceTextview;
        public TextView temperatureRangeTextview;

        public Slider preferenceFactorSlider;
        public Slider activityImportanceSlider;
        public Slider workActivityFactorSlider;
        public Slider sportsActivityFactorSlider;
        public Slider casualActivityFactorSlider;
        public Slider temperatureImportanceSlider;
        public RangeSlider temperatureRangeslider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            preferenceClothingTypeTextview = itemView.findViewById(R.id.preference_clothing_type_textview);
            clothingIcon = itemView.findViewById(R.id.preferences_clothing_icon_imageview);

            activityImportanceTextview = itemView.findViewById(R.id.preferences_activity_importance_textview);
            workFactorTextview = itemView.findViewById(R.id.preferences_work_factor_textview);
            sportsFactorTextview = itemView.findViewById(R.id.preferences_sports_factor_textview);
            casualFactorTextview = itemView.findViewById(R.id.preferences_casual_factor_textview);
            temperatureImportanceTextview = itemView.findViewById(R.id.preferences_temperature_importance_textview);
            temperatureRangeTextview = itemView.findViewById(R.id.preferences_temperature_range_textview);

            preferenceFactorSlider = itemView.findViewById(R.id.preferences_factor_slider);
            activityImportanceSlider = itemView.findViewById(R.id.activity_importance_slider);
            workActivityFactorSlider = itemView.findViewById(R.id.activity_work_factor_slider);
            sportsActivityFactorSlider = itemView.findViewById(R.id.activity_sports_factor_slider);
            casualActivityFactorSlider = itemView.findViewById(R.id.activity_casual_factor_slider);
            temperatureImportanceSlider = itemView.findViewById(R.id.temperature_importance_slider);
            temperatureRangeslider = itemView.findViewById(R.id.temperature_rangeslider);

            // Set up the basic preferences first
            preferenceFactorSlider.addOnChangeListener(new Slider.OnChangeListener() {
                @Override
                public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                    clothingRanker.setPreferenceFactor(Math.round(value));
                }
            });

            // Set up the advanced preferences, if user requested for it
            if (isAdvancedSettings) {
                // set up advanced settings
                activityImportanceSlider.addOnChangeListener(new Slider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                        Log.i(TAG, "Activity importance slider changed to " + value);
                        clothingRanker.setActivityImportance(Math.round(value));
                    }
                });

                workActivityFactorSlider.addOnChangeListener(new Slider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                        clothingRanker.setWorkActivityFactor(Math.round(value));
                    }
                });

                sportsActivityFactorSlider.addOnChangeListener(new Slider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                        clothingRanker.setSportsActivityFactor(Math.round(value));
                    }
                });

                casualActivityFactorSlider.addOnChangeListener(new Slider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                        clothingRanker.setCasualActivityFactor(Math.round(value));
                    }
                });

                temperatureImportanceSlider.addOnChangeListener(new Slider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                        clothingRanker.setTemperatureImportance(Math.round(value));
                    }
                });

                temperatureRangeslider.addOnChangeListener(new RangeSlider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                        // check which change
                        List<Float> endpointValues = slider.getValues();
                        // TODO: change the temp factor of the associated clothing ranker
                        if (endpointValues.get(0) == value) {
                            // change is the first end point
                            Log.i(TAG, "Temp lower range changed to " + value);
                            clothingRanker.setTemperatureLowerRange(Math.round(value));
                        } else {
                            // change is the last end point
                            Log.i(TAG, "Temp upper range changed to " + value);
                            clothingRanker.setTemperatureUpperRange(Math.round(value));
                        }

                    }
                });
            } else {
                // hide advanced settings
                activityImportanceSlider.setVisibility(View.GONE);
                workActivityFactorSlider.setVisibility(View.GONE);
                sportsActivityFactorSlider.setVisibility(View.GONE);
                casualActivityFactorSlider.setVisibility(View.GONE);
                temperatureImportanceSlider.setVisibility(View.GONE);
                temperatureRangeslider.setVisibility(View.GONE);

                activityImportanceTextview.setVisibility(View.GONE);
                workFactorTextview.setVisibility(View.GONE);
                sportsFactorTextview.setVisibility(View.GONE);
                casualFactorTextview.setVisibility(View.GONE);
                temperatureImportanceTextview.setVisibility(View.GONE);
                temperatureRangeTextview.setVisibility(View.GONE);
            }

        }

        public void bind(ClothingRanker clothingRanker) {
            // set elements based on item
            this.clothingRanker = clothingRanker;

            preferenceClothingTypeTextview.setText(clothingRanker.getClothingTypeName());

            clothingIcon.setImageResource(clothingRanker.getClothingTypeIconID());


            preferenceFactorSlider.setValue(clothingRanker.getPreferenceFactor());

            // Check if advanced settings need to be set
            if (isAdvancedSettings) {
                activityImportanceSlider.setValue(clothingRanker.getActivityImportance());
                workActivityFactorSlider.setValue(clothingRanker.getWorkActivityFactor());
                sportsActivityFactorSlider.setValue(clothingRanker.getSportsActivityFactor());
                casualActivityFactorSlider.setValue(clothingRanker.getCasualActivityFactor());
                temperatureImportanceSlider.setValue(clothingRanker.getTemperatureImportance());

                List<Float> tempRange = new ArrayList<>();
                tempRange.add((float) clothingRanker.getTemperatureLowerRange());
                tempRange.add((float) clothingRanker.getTemperatureUpperRange());
                temperatureRangeslider.setValues(tempRange);
            }
        }
    }
}
