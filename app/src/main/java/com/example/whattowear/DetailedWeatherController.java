package com.example.whattowear;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whattowear.models.Conditions;
import com.example.whattowear.models.Forecast;
import com.example.whattowear.models.Weather;

public class DetailedWeatherController {
    private static final String PLACEHOLDER_DESCRIPTION = "No weather data";
    private static final String PLACEHOLDER_VALUE = "- -";

    private Activity activity;

    private TextView locationTextview;
    private TextView forecastDescriptionTextview;
    private TextView currentTemperatureTextview;
    private ImageView weatherIconImageview;
    private TextView lowTemperatureTextview;
    private TextView highTemperatureTextview;
    private TextView feelsLikeTextview;
    private TextView uvIndexTextview;
    private TextView humidityTextview;
    private TextView chanceOfRainTextview;


    public DetailedWeatherController(DetailedWeatherActivity activity) {
        this.activity = activity;

        locationTextview = activity.findViewById(R.id.detailed_weather_location_textview);
        forecastDescriptionTextview = activity.findViewById(R.id.detailed_weather_forecast_description_textview);
        currentTemperatureTextview = activity.findViewById(R.id.detailed_weather_current_temp_textview);
        weatherIconImageview = activity.findViewById(R.id.detailed_weather_icon_imageview);
        lowTemperatureTextview = activity.findViewById(R.id.detailed_weather_low_temp_textview);
        highTemperatureTextview = activity.findViewById(R.id.detailed_weather_high_temp_textview);
        feelsLikeTextview = activity.findViewById(R.id.detailed_weather_feels_like_textview);
        uvIndexTextview = activity.findViewById(R.id.detailed_weather_uv_index_value_textview);
        humidityTextview = activity.findViewById(R.id.detailed_weather_humidity_value_textview);
        chanceOfRainTextview = activity.findViewById(R.id.detailed_weather_chance_of_rain_value_textview);

        // Only display data if it is valid
        if (Weather.hasPreloadedDataToDisplay()) {
            // fill with the weather data
            fillDetailedWeatherDisplay();
        } else {
            // use placeholder
            displayPlaceholder();
        }
    }

    private void fillDetailedWeatherDisplay() {
        Forecast currentForecast = Weather.getCurrentForecast();
        Conditions hourCondition = currentForecast.getHourCondition();

        locationTextview.setText(Weather.getLastLocationName());
        forecastDescriptionTextview.setText(hourCondition.getConditionDescription());
        currentTemperatureTextview.setText(currentForecast.getFormattedTemp());
        Glide.with(activity).load(hourCondition.getConditionIconLink()).into(weatherIconImageview);
        lowTemperatureTextview.setText(Weather.getDayMinFormattedTemperature());
        highTemperatureTextview.setText(Weather.getDayMaxFormattedTemperature());
        feelsLikeTextview.setText(currentForecast.getFormattedFeelsLikeTemp());
        uvIndexTextview.setText(currentForecast.getFormattedUvIndex());
        humidityTextview.setText(currentForecast.getFormattedHumidity());
        chanceOfRainTextview.setText(currentForecast.getFormattedProbOfPrecipitation());
    }

    private void displayPlaceholder() {
        // TODO: use better placeholders
        forecastDescriptionTextview.setText(PLACEHOLDER_DESCRIPTION);
        uvIndexTextview.setText(PLACEHOLDER_VALUE);
        humidityTextview.setText(PLACEHOLDER_VALUE);
        chanceOfRainTextview.setText(PLACEHOLDER_VALUE);
    }
}
