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
    public static final String DEG_SIGN = "\u00B0";

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
            // TODO: Change specific deg sign based on units
            // "\u2103" for C and "\u2109" for F
            String temp_with_deg = String.valueOf(hourly_forecast.getTemp()) + DEG_SIGN;
            hourly_temp_textview.setText(temp_with_deg);
            hourly_time_textview.setText(getAMPMTime(hourly_forecast.getDt()));

            // TODO: can convert into using local images based on condition ID
            Glide.with(context).load(hourly_forecast.getHourCondition().getConditionIconLink()).into(hourly_weather_imageview);

            // TODO: load all other data here
        }

        /**
         * Converts Date into hour only AM/PM time
         * @param dt the Date to be converted
         * @return  the String representation of the hour of the Date
         */
        private String getAMPMTime(Date dt) {
            int hours = dt.getHours();

            if (hours == 0) {
                return "12AM";
            } else if (hours == 12) {
                return hours + "PM";
            } else if (hours < 12) {
                return hours + "AM";
            } else {
                return hours-12 + "PM";
            }
        }
    }
}
