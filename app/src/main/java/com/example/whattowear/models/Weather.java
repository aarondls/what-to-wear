package com.example.whattowear.models;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Weather {
    // Weather is a singleton class

    public static final String TAG = "Weather";
    public static final String DEFAULT_WEATHER_UNIT = WeatherUnit.FAHRENHEIT.name();

    public static enum WeatherUnit {
        FAHRENHEIT, CELSIUS
    }

    private static final String LOW_TEMP_CHAR = "L ";
    private static final String HIGH_TEMP_CHAR = "H ";
    private static final String DEG_SIGN = "\u00B0";
    private static final double FAHRENHEIT_TO_CELSIUS_FACTOR = 5.0/9.0;
    private static final int FAHRENHEIT_TO_CELSIUS_DIFFERENCE_TERM = 32;

    private final static int SECONDS_FACTOR = 1000;
    private final static int MINUTES_FACTOR = 60;

    private static Weather weather = new Weather();

    private static double lastLocationLatitude;
    private static double lastLocationLongitude;
    private static String lastLocationName;
    private static String loadedDataLocationName;

    private static List<Forecast> hourlyForecast;
    private static Forecast currentForecast;
    private static Conditions dayConditions;
    private static int dayConditionsID;
    private static int dayMinTemperatureFahrenheit;
    private static int dayMaxTemperatureFahrenheit;
    private static float dayMaxUVIndex;

    // private to ensure that only the class can instantiate itself
    private Weather() {
        hourlyForecast = new ArrayList<>();

        // Set last location name to be empty, which can be used to check if there exists a current location
        lastLocationName = "";
        loadedDataLocationName = "";
    }

    public static void loadFromJson(JSONObject jsonObject) throws JSONException {
        hourlyForecast.clear();
        hourlyForecast.addAll(Forecast.fromJsonArray(jsonObject.getJSONArray("hourly")));

        currentForecast = Forecast.fromJson(jsonObject.getJSONObject("current"));

        JSONObject dayWeatherData = jsonObject.getJSONArray("daily").getJSONObject(0);
        dayConditionsID = dayWeatherData.getJSONArray("weather").getJSONObject(0).getInt("id");
        JSONObject dayTemperatureData = dayWeatherData.getJSONObject("temp");
        dayMinTemperatureFahrenheit = dayTemperatureData.getInt("min");
        dayMaxTemperatureFahrenheit = dayTemperatureData.getInt("max");
        dayMaxUVIndex = (float) dayWeatherData.getDouble("uvi");

        // TODO: fill up currentForecast and dayConditions, when needed
        // TODO: if not, delete them
    }

    /**
     * Does not account if data is out of date, and only tells if there is existing valid weather data at the set last location
     * @return whether there is existing weather data at the last location
     */
    public static boolean hasPreloadedDataToDisplay() {
        return !lastLocationName.isEmpty() && lastLocationName.equals(loadedDataLocationName);
    }

    /**
     * Assumes that there is preloaded data to display
     * @return whether the preloaded data is within 30 minutes of current time
     */
    public static boolean isPreloadedDataOutOfDate() {
        long minuteDiff = (Calendar.getInstance().getTime().getTime() - currentForecast.getDt().getTime())/(SECONDS_FACTOR*MINUTES_FACTOR);
        return minuteDiff > 30;
    }

    public static List<Forecast> getHourlyForecast() {
        return hourlyForecast;
    }

    public static Forecast getCurrentForecast() {
        return currentForecast;
    }

    public static void setLoadedDataLocationName(String loadedDataLocationName) {
        Weather.loadedDataLocationName = loadedDataLocationName;
    }

    /**
     * Sets lastLocationName, lastLocationLatitude, and lastLocationLongitude using the provided lastLocation
     * @param lastLocation the Location representing the lastLocation
     */
    public static void setLastLocation(Location lastLocation, Activity activity) {
        lastLocationName = getLocationName(lastLocation, activity);
        lastLocationLatitude = lastLocation.getLatitude();
        lastLocationLongitude = lastLocation.getLongitude();
    }

    /**
     * Sets lastLocationName, lastLocationLatitude, and lastLocationLongitude using the provided lastLocation
     * @param lastLocation the Place representing the lastLocation
     */
    public static void setLastLocation(Place lastLocation) {
        lastLocationName = lastLocation.getName();
        LatLng latLng = lastLocation.getLatLng();
        lastLocationLatitude = latLng.latitude;
        lastLocationLongitude = latLng.longitude;
    }

    public static double getLastLocationLatitude() {
        return lastLocationLatitude;
    }

    public static double getLastLocationLongitude() {
        return lastLocationLongitude;
    }

    public static String getLastLocationName() {
        return lastLocationName;
    }

    public static String getLoadedDataLocationName() {
        return loadedDataLocationName;
    }

    /**
     * Used to get the city name from a Location
     * @param location  the Location of the city
     * @return  the name of the passed Location
     * the name could be, in order of which exists first:
     * locality, sub admin area, admin area, country name, long/lat coordinates
     */
    private static String getLocationName(Location location, Activity activity) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Geocoder geoCoder = new Geocoder(activity, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        String locationName = "";
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            // set depending on what is known
            if (address.get(0).getLocality() != null) {
                locationName = address.get(0).getLocality();
            } else if (address.get(0).getSubAdminArea() != null) {
                locationName = address.get(0).getSubAdminArea();
            } else if (address.get(0).getAdminArea() != null) {
                locationName = address.get(0).getAdminArea();
            } else if (address.get(0).getCountryName() != null) {
                // Worst case, try to use country name
                locationName = address.get(0).getCountryName();
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException", e);
        }

        // TODO: Fix city being unknown
        // if city still blank, worst case, it is set to long lat coordinates
        if (locationName.isEmpty()) {
            locationName = "Lat: " + latitude + "Long: " + longitude;
        }

        return locationName;
    }

    public static int getDayConditionsID() {
        return dayConditionsID;
    }

    /**
     * This method is used to convert all the stored temperature data into the appropriate format, according to the
     * weatherUnits stored inside this Weather class and with the degree symbol. All temperature data from the
     * OpenWeather API is stored as Fahrenheit, which is why the input temperature is in Fahrenheit.
     *
     * @param fahrenheitTemperature the temperature in Fahrenheit to format
     * @return the formatted temperature in the appropriate units according to the defined weatherUnits and with the degree sign
     */
    public static String getFormattedTemp(int fahrenheitTemperature) {
        Preferences preferences = (Preferences) ParseUser.getCurrentUser().getParseObject(Preferences.KEY_PREFERENCES);

        String formattedTemp = "";
        if (preferences.getWeatherUnit().equals(WeatherUnit.FAHRENHEIT.name())) {
            formattedTemp += fahrenheitTemperature;
        } else {
            formattedTemp += convertFahrenheitToCelsius(fahrenheitTemperature);
        }
        return formattedTemp + DEG_SIGN;
    }

    /**
     * @param fahrenheitTemperature the temperature in Fahrenheit to convert to Celsius
     * @return the converted temperature in Celsius, rounded to the nearest integer
     */
    private static int convertFahrenheitToCelsius(int fahrenheitTemperature) {
        return (int) Math.round(FAHRENHEIT_TO_CELSIUS_FACTOR * (fahrenheitTemperature - FAHRENHEIT_TO_CELSIUS_DIFFERENCE_TERM));
    }

    /**
     * @return the day minimum temperature formatted with the "L" character and degree sign
     */
    public static String getDayMinFormattedTemperature() {
        return LOW_TEMP_CHAR + getFormattedTemp(dayMinTemperatureFahrenheit);
    }

    /**
     * @return the day maximum temperature formatted with the "H" character and degree sign
     */
    public static String getDayMaxFormattedTemperature() {
        return HIGH_TEMP_CHAR + getFormattedTemp(dayMaxTemperatureFahrenheit);
    }

    public static int getDayMeanFahrenheitTemperature() {
        return (dayMinTemperatureFahrenheit + dayMaxTemperatureFahrenheit)/2; // round down to int
    }

    public static float getDayMaxUVIndex() {
        return dayMaxUVIndex;
    }
}

