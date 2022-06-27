package com.example.whattowear;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.whattowear.BuildConfig;
import com.example.whattowear.ClothingInterface;
import com.example.whattowear.R;
import com.example.whattowear.models.Clothing;
import com.example.whattowear.models.Forecast;
import com.example.whattowear.models.Weather;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

public class DashboardWeatherController {
    public static final String TAG = "DashboardWeatherController";
    public static final String DEG_SIGN = "\u00B0";

    /**
     * Used to let clothing controller know when new weather data is available
     */
    public interface WeatherDataListener {
        public void onNewWeatherDataReady();
    }

    private Activity activity;

    private AsyncHttpClient openWeatherClient;

    private WeatherDataListener listener;

    private TextView locationTextview;
    private TextView forecastDescriptionTextview;
    private TextView currentTemperatureTextview;
    private ImageView weatherIconImageview;

    private TextView forecast1HrTimeTextview;
    private ImageView forecast1HrWeatherIconImageview;
    private TextView forecast1HrTempTextview;
    private TextView forecast2HrTimeTextview;
    private ImageView forecast2HrWeatherIconImageview;
    private TextView forecast2HrTempTextview;
    private TextView forecast3HrTimeTextview;
    private ImageView forecast3HrWeatherIconImageview;
    private TextView forecast3HrTempTextview;


    // need activity to access elements
    public DashboardWeatherController(DashboardActivity activity) {
        this.activity = activity;

        openWeatherClient = new AsyncHttpClient();

        listener = null;

        locationTextview = activity.findViewById(R.id.dashboard_location_textview);
        forecastDescriptionTextview = activity.findViewById(R.id.dashboard_forecast_description_textview);
        currentTemperatureTextview = activity.findViewById(R.id.dashboard_current_temp_textview);
        weatherIconImageview = activity.findViewById(R.id.dashboard_weather_icon_imageview);

        forecast1HrTimeTextview = activity.findViewById(R.id.dashboard_1hr_time_textview);
        forecast1HrWeatherIconImageview = activity.findViewById(R.id.dashboard_1hr_weather_icon_imageview);
        forecast1HrTempTextview = activity.findViewById(R.id.dashboard_1hr_temp_textview);
        forecast2HrTimeTextview = activity.findViewById(R.id.dashboard_2hr_time_textview);
        forecast2HrWeatherIconImageview = activity.findViewById(R.id.dashboard_2hr_weather_icon_imageview);
        forecast2HrTempTextview = activity.findViewById(R.id.dashboard_2hr_temp_textview);
        forecast3HrTimeTextview = activity.findViewById(R.id.dashboard_3hr_time_textview);
        forecast3HrWeatherIconImageview = activity.findViewById(R.id.dashboard_3hr_weather_icon_imageview);
        forecast3HrTempTextview = activity.findViewById(R.id.dashboard_3hr_temp_textview);

        // Check if there is data to be displayed
        if (Weather.hasPreloadedDataToDisplay()) {
            updateDashboardDisplay();
            // if out of date, then refresh weather info
            if (Weather.isPreloadedDataOutOfDate()) {
                getWeatherAtLastLocation();
            }
        } else {
            // Show placeholder when no data
            displayPlaceholderOnDashboard();
        }

        activity.setNewLocationDataListener(new DashboardActivity.LocationDataListener() {
            @Override
            public void onNewLocationDataReady() {
                Log.i(TAG, "Location data changed ready now");
                onDataSetChanged();
            }
        });
    }

    public void setNewWeatherDataListener(WeatherDataListener listener) {
        this.listener = listener;
    }

    /**
     * Handles calculating all weather information when called when new location data has come in,
     * and updates the dashboard display as necessary
     */
    public void onDataSetChanged() {
        // set location name
        String locationName = Weather.getLastLocationName();
        locationTextview.setText(locationName);

        getWeatherAtLastLocation();
    }

    /**
     * Gets the weather from the OpenWeather API at the saved last_location and calls
     * updateDashboardDisplay to update the dashboard to show the new weather data
     */
    private void getWeatherAtLastLocation() {
        double latitude = Weather.getLastLocationLatitude();
        double longitude = Weather.getLastLocationLongitude();

        // TODO: Exclude non needed data after detailed weather screen is built
        String apiUrl = "https://api.openweathermap.org/data/3.0/onecall?"
                + "lat=" + latitude
                + "&lon=" + longitude
                + "&units=" + Weather.getWeatherUnits()
                + "&appid=" + BuildConfig.OPENWEATHER_API_KEY;

        Log.i(TAG, "Requesting weather data");
        openWeatherClient.get(apiUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Log entire response
                Log.i(TAG, json.jsonObject.toString());

                // Get relevant data
                try {
                    // load the weather data from the received json data
                    // note that weather is a singleton class, so it can be loaded directly
                    Weather.loadFromJson(json.jsonObject);

                    // on success, set loaded data location name
                    Weather.setLoadedDataLocationName(Weather.getLastLocationName());
                } catch (JSONException e) {
                    // TODO: fix what happens when weather data is not found; perhaps change weather display to show a message that it isn't found
                    Toast.makeText(activity, "Unable to parse weather information.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to convert JSON data into Weather object", e);
                    e.printStackTrace();
                }
                Log.i(TAG, "finished updating all weather info");

                updateDashboardDisplay();
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                // TODO: fix what happens when weather data is not found; perhaps change weather display to show a message that it isn't found
                // for now, make a toast
                Toast.makeText(activity, "Unable to fetch weather information.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to get weather data");
                Log.e(TAG, response);
            }
        });
    }

    /**
     * Updates the dashboard weather controller elements with the available Weather data
     */
    private void updateDashboardDisplay() {
        // notify the listener (clothing controller)
        if (listener != null) {
            Log.i(TAG, "calling listener");
            listener.onNewWeatherDataReady();
        }

        // set this again so updateDashboardDisplay can be called by itself when loading preexisting weather data
        locationTextview.setText(Weather.getLastLocationName());

        // should set anything to be displayed after new weather data comes in here
        forecastDescriptionTextview.setText(Weather.getCurrentForecast().getHourCondition().getConditionDescription());
        currentTemperatureTextview.setText(Weather.getCurrentForecast().getFormattedTemp());

        // TODO: can convert all image icons below into using local images based on condition ID, when graphics are available
        List<Forecast> hourlyForecast = Weather.getHourlyForecast();
        // ensure forecasts exist at projected hour
        int numForecast = hourlyForecast.size();
        if (numForecast >= 2) {
            forecast1HrTimeTextview.setText(hourlyForecast.get(1).getAMPMTime());
            Glide.with(activity).load(hourlyForecast.get(1).getHourCondition().getConditionIconLink()).into(forecast1HrWeatherIconImageview);
            forecast1HrTempTextview.setText(hourlyForecast.get(1).getFormattedTemp());
        }
        if (numForecast >= 3) {
            forecast2HrTimeTextview.setText(hourlyForecast.get(2).getAMPMTime());
            Glide.with(activity).load(hourlyForecast.get(2).getHourCondition().getConditionIconLink()).into(forecast2HrWeatherIconImageview);
            forecast2HrTempTextview.setText(hourlyForecast.get(2).getFormattedTemp());
        }
        if (numForecast >= 4) {
            forecast3HrTimeTextview.setText(hourlyForecast.get(3).getAMPMTime());
            Glide.with(activity).load(hourlyForecast.get(3).getHourCondition().getConditionIconLink()).into(forecast3HrWeatherIconImageview);
            forecast3HrTempTextview.setText(hourlyForecast.get(3).getFormattedTemp());
        }

        Glide.with(activity).load(Weather.getCurrentForecast().getHourCondition().getConditionIconLink()).into(weatherIconImageview);
    }

    /**
     * Displays placeholder weather information to visually signal that there is no weather data yet
     */
    private void displayPlaceholderOnDashboard() {
        locationTextview.setHint("No location");

        forecastDescriptionTextview.setText("");
        currentTemperatureTextview.setText("--" + DEG_SIGN);

        forecast1HrTimeTextview.setText("");
        forecast1HrWeatherIconImageview.setImageDrawable(null);
        forecast1HrTempTextview.setText("");

        forecast2HrTimeTextview.setText("");
        forecast2HrWeatherIconImageview.setImageDrawable(null);
        forecast2HrTempTextview.setText("");

        forecast3HrTimeTextview.setText("");
        forecast3HrWeatherIconImageview.setImageDrawable(null);
        forecast3HrTempTextview.setText("");

        weatherIconImageview.setImageDrawable(null);
    }
}
