package com.example.whattowear;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.whattowear.models.Forecast;
import com.example.whattowear.models.Weather;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class DashboardWeatherController {
    public static final String TAG = "DashboardWeatherController";
    public static final String DEG_SIGN = "\u00B0";
    private static final int FORECAST_3_HOUR_COUNT = 3;
    private static final String OPENWEATHER_API_LINK = "https://api.openweathermap.org/data/3.0/onecall?";
    private static final String OPENWEATHER_LAT = "lat=";
    private static final String OPENWEATHER_LON = "&lon=";
    private static final String OPENWEATHER_UNITS = "&units=";
    private static final String OPENWEATHER_APPID = "&appid=";
    private static final String CANNOT_PARSE_WEATHER_INFO_PROMPT = "Unable to parse weather information.";
    private static final String CANNOT_FETCH_WEATHER_INFO_PROMPT = "Unable to fetch weather information.";
    private static final String NO_LOCATION_PROMPT = "No location";
    private static final String TEMPERATURE_PLACEHOLDER = "--";

    /**
     * Used to let clothing controller know when new weather data is available
     */
    public interface WeatherDataListener {
        public void onNewWeatherDataReady();
    }

    private static final String REQUEST_WEATHER_DATA_UNIT = "imperial";

    private Activity activity;

    private AsyncHttpClient openWeatherClient;

    private List<WeatherDataListener> listeners;

    private TextView locationTextview;
    private TextView forecastDescriptionTextview;
    private TextView currentTemperatureTextview;
    private ImageView weatherIconImageview;

    private List<TextView> forecastHoursTimeTextviews;
    private List<ImageView> forecastHoursWeatherIconImageviews;
    private List<TextView> forecastHoursTempTextviews;


    // need activity to access elements
    public DashboardWeatherController(DashboardActivity activity) {
        this.activity = activity;

        openWeatherClient = new AsyncHttpClient();

        listeners = new ArrayList<>();

        locationTextview = activity.findViewById(R.id.dashboard_location_textview);
        forecastDescriptionTextview = activity.findViewById(R.id.dashboard_forecast_description_textview);
        currentTemperatureTextview = activity.findViewById(R.id.dashboard_current_temp_textview);
        weatherIconImageview = activity.findViewById(R.id.dashboard_weather_icon_imageview);

        forecastHoursTimeTextviews = new ArrayList<>(FORECAST_3_HOUR_COUNT);
        forecastHoursWeatherIconImageviews = new ArrayList<>(FORECAST_3_HOUR_COUNT);
        forecastHoursTempTextviews = new ArrayList<>(FORECAST_3_HOUR_COUNT);

        forecastHoursTimeTextviews.add(activity.findViewById(R.id.dashboard_1hr_time_textview));
        forecastHoursWeatherIconImageviews.add(activity.findViewById(R.id.dashboard_1hr_weather_icon_imageview));
        forecastHoursTempTextviews.add(activity.findViewById(R.id.dashboard_1hr_temp_textview));

        forecastHoursTimeTextviews.add(activity.findViewById(R.id.dashboard_2hr_time_textview));
        forecastHoursWeatherIconImageviews.add(activity.findViewById(R.id.dashboard_2hr_weather_icon_imageview));
        forecastHoursTempTextviews.add(activity.findViewById(R.id.dashboard_2hr_temp_textview));

        forecastHoursTimeTextviews.add(activity.findViewById(R.id.dashboard_3hr_time_textview));
        forecastHoursWeatherIconImageviews.add(activity.findViewById(R.id.dashboard_3hr_weather_icon_imageview));
        forecastHoursTempTextviews.add(activity.findViewById(R.id.dashboard_3hr_temp_textview));

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
                onDataSetChanged();
            }
        });
    }

    public void addNewWeatherDataListener(WeatherDataListener listener) {
        listeners.add(listener);
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
        String apiUrl = OPENWEATHER_API_LINK
                + OPENWEATHER_LAT + latitude
                + OPENWEATHER_LON + longitude
                + OPENWEATHER_UNITS + REQUEST_WEATHER_DATA_UNIT
                + OPENWEATHER_APPID + BuildConfig.OPENWEATHER_API_KEY;

        openWeatherClient.get(apiUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Get relevant data
                try {
                    // load the weather data from the received json data
                    // note that weather is a singleton class, so it can be loaded directly
                    Weather.loadFromJson(json.jsonObject);

                    // on success, set loaded data location name
                    Weather.setLoadedDataLocationName(Weather.getLastLocationName());
                } catch (JSONException e) {
                    // TODO: fix what happens when weather data is not found; perhaps change weather display to show a message that it isn't found
                    Toast.makeText(activity, CANNOT_PARSE_WEATHER_INFO_PROMPT, Toast.LENGTH_SHORT).show();
                }

                updateDashboardDisplay();
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                // TODO: fix what happens when weather data is not found; perhaps change weather display to show a message that it isn't found
                // for now, make a toast
                Toast.makeText(activity, CANNOT_FETCH_WEATHER_INFO_PROMPT, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Updates the dashboard weather controller elements with the available Weather data
     */
    private void updateDashboardDisplay() {
        // notify the listeners (clothing controller and weather animation controller)
        for (WeatherDataListener listener : listeners) {
            listener.onNewWeatherDataReady();
        }

        // set this again so updateDashboardDisplay can be called by itself when loading preexisting weather data
        locationTextview.setText(Weather.getLastLocationName());

        // should set anything to be displayed after new weather data comes in here
        forecastDescriptionTextview.setText(Weather.getCurrentForecast().getHourCondition().getConditionDescription());
        currentTemperatureTextview.setText(Weather.getCurrentForecast().getFormattedTemp());

        // TODO: can convert all image icons below into using local images based on condition ID, when graphics are available

        // Clear the existing forecast display
        clear3HrForecastDisplay();

        List<Forecast> hourlyForecasts = Weather.getHourlyForecast();
        for (int i=0; i<hourlyForecasts.size() && i<3; i++) {
            Forecast hourlyForecast = hourlyForecasts.get(i);
            forecastHoursTimeTextviews.get(i).setText(hourlyForecast.getAMPMTime());
            Glide.with(activity).load(hourlyForecast.getHourCondition().getConditionIconLink()).into(forecastHoursWeatherIconImageviews.get(i));
            forecastHoursTempTextviews.get(i).setText(hourlyForecast.getFormattedTemp());
        }

        Glide.with(activity).load(Weather.getCurrentForecast().getHourCondition().getConditionIconLink()).into(weatherIconImageview);
    }

    /**
     * Displays placeholder weather information to visually signal that there is no weather data yet
     */
    private void displayPlaceholderOnDashboard() {
        locationTextview.setHint(NO_LOCATION_PROMPT);

        forecastDescriptionTextview.setText("");
        currentTemperatureTextview.setText(TEMPERATURE_PLACEHOLDER + DEG_SIGN);

        clear3HrForecastDisplay();
    }

    /**
     * Clears the 3 hour forecast elements in the screen
     */
    private void clear3HrForecastDisplay() {
        for (int i=0; i<3; i++) {
            forecastHoursTimeTextviews.get(i).setText("");
            forecastHoursWeatherIconImageviews.get(i).setImageDrawable(null);
            forecastHoursTempTextviews.get(i).setText("");
        }
    }
}
