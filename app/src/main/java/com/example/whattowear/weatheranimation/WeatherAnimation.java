package com.example.whattowear.weatheranimation;

import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;

import com.example.whattowear.models.Weather;
import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.Random;

/**
 * Handles creating the proper weather animation based on Weather data
 */
public class WeatherAnimation {
    private static WeatherAnimation weatherAnimation = new WeatherAnimation();

    private static String loadedDataLocationName;
    private static ConfettoGenerator confettoGenerator;
    private static long emissionDuration;
    private static float emissionRate;
    private static float velocityX;
    private static float velocityDeviationX;
    private static float velocityY;
    private static float velocityDeviationY;
    private static float rotationalVelocity;
    private static float rotationalVelocityDeviation;


    public WeatherAnimation() {
        confettoGenerator = null;
        loadedDataLocationName = "";
    }

    /**
     *
     * @param viewGroup viewGroup the view to display the weather animation
     * @return the ConfettiManager with the properties associated with the current weather and given viewgroup
     */
    public static ConfettiManager startNewAnimationFromWeatherData(ViewGroup viewGroup) {
        generateConfettoTypeFromWeatherData();
        calculateWeatherAnimationParameters();

        loadedDataLocationName = Weather.getLoadedDataLocationName();

        return startExistingAnimation(viewGroup);
    }

    /**
     * Starts calculated animation at the given viewGroup
     * @param viewGroup the view to display the weather animation
     * @return the ConfettiManager with the existing calculated properties and given viewgroup
     */
    public static ConfettiManager startExistingAnimation (ViewGroup viewGroup) {
        int containerWidth = viewGroup.getWidth();
        // TODO: may want to generate confetti source at some other points/lines
        ConfettiSource confettiSource = new ConfettiSource(0, 0, containerWidth, 10);

        return new ConfettiManager(viewGroup.getContext(), confettoGenerator, confettiSource, viewGroup)
                .setEmissionDuration(emissionDuration)
                .setEmissionRate(emissionRate)
                .setVelocityX(velocityX, velocityDeviationX)
                .setVelocityY(velocityY, velocityDeviationY)
                .setRotationalVelocity(rotationalVelocity, rotationalVelocityDeviation)
                .animate();
    }

    /**
     * Calculates the correct confetto type given current weather data and stores it
     */
    private static void generateConfettoTypeFromWeatherData() {
        // TODO: check if rain or snow, or mixture of both
        // for now, force raining even when it is not for testing
        int currentWeatherConditionsID = Weather.getCurrentForecast().getHourCondition().getWeatherConditionsID();
        if (currentWeatherConditionsID >= 500 && currentWeatherConditionsID <= 531) {
            // then it is raining
        } else {
            // this is only for testing now
            confettoGenerator = new ConfettoGenerator() {
                @Override
                public Confetto generateConfetto(Random random) {
                    return new RaindropConfetto(0.5f, Color.BLACK);
                }
            };
        }
    }

    /**
     * Calculates the parameters for creating a ConfettiManager to handle the weather animation
     * for the current weather data
     */
    private static void calculateWeatherAnimationParameters() {
        // TODO: calculate from weather data
        // For now, keep constant
        emissionDuration = ConfettiManager.INFINITE_DURATION;
        emissionRate = 2000;
        velocityX = 20;
        velocityDeviationX = 10;
        velocityY = 1000;
        velocityDeviationY = 200;
        rotationalVelocity = 180;
        rotationalVelocityDeviation = 180;
    }

    /**
     * @return whether there is valid weather animation data at the location Weather.lastLocationName
     */
    public static boolean hasPreloadedDataToDisplay() {
        return Weather.hasPreloadedDataToDisplay() && loadedDataLocationName.equals(Weather.getLoadedDataLocationName());
    }
}
