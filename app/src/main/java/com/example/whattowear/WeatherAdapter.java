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
        return Weather.getHourlyForecast().size();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // follow layout
        TextView hourlyTempTextview;
        TextView hourlyTimeTextview;
        ImageView hourlyWeatherImageview;
        TextView hourlyFeelsLikeTextview;

        public ViewHolder(@NonNull View itemView) {
            // itemview is representation of one row
            super(itemView);
            hourlyTempTextview = itemView.findViewById(R.id.hourly_temp_textview);
            hourlyTimeTextview = itemView.findViewById(R.id.hourly_time_textview);
            hourlyWeatherImageview = itemView.findViewById(R.id.hourly_weather_imageview);
            hourlyFeelsLikeTextview = itemView.findViewById(R.id.hourly_feels_like_textview);
        }

        public void bind(Forecast hourly_forecast) {
            hourlyTempTextview.setText(hourly_forecast.getFormattedTemp());
            hourlyTimeTextview.setText(hourly_forecast.getAMPMTime());

            Glide.with(context).load(hourly_forecast.getHourCondition().getConditionIconLink()).into(hourlyWeatherImageview);

            hourlyFeelsLikeTextview.setText(hourly_forecast.getFormattedFeelsLikeTemp());
        }


    }
}
