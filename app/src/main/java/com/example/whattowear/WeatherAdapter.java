package com.example.whattowear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whattowear.models.Forecast;
import com.example.whattowear.models.Weather;

import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    public static final String TAG = "WeatherAdapter";

    Context context;

    // Pass context and weather info
    public WeatherAdapter(Context context) {
        this.context = context;
    }

    // For each row, inflate the correct layout type
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hourly_weather, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on pos of elem
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the forecast at pos
        Forecast hourlyForecast = Weather.getHourlyForecast().get(position);

        // Bind the forecast data
        holder.bind(hourlyForecast);
    }

    @Override
    public int getItemCount() {
        // TODO: Calculate size from weather_data
        // TODO: Change to size of needed hourly forecast + other types displayed
        return Weather.getHourlyForecast().size();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // follow layout
        TextView hourly_temp_textview;
        TextView hourly_time_textview;
        ImageView hourly_weather_imageview;

        public ViewHolder(@NonNull View itemView) {
            // itemview is representation of one row
            super(itemView);
            hourly_temp_textview = itemView.findViewById(R.id.hourly_temp_textview);
            hourly_time_textview = itemView.findViewById(R.id.hourly_time_textview);
            hourly_weather_imageview = itemView.findViewById(R.id.hourly_weather_imageview);
        }

        public void bind(Forecast hourly_forecast) {
            hourly_temp_textview.setText(hourly_forecast.getFormattedTemp());
            hourly_time_textview.setText(hourly_forecast.getAMPMTime());

            // TODO: can convert into using local images based on condition ID
            Glide.with(context).load(hourly_forecast.getHourCondition().getConditionIconLink()).into(hourly_weather_imageview);

            // TODO: load all other data here
        }


    }
}
