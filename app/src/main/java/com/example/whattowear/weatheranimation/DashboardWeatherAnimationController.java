package com.example.whattowear.weatheranimation;

import android.util.Log;
import android.view.ViewGroup;

import com.example.whattowear.DashboardWeatherController;
import com.example.whattowear.models.Weather;
import com.github.jinatonic.confetti.ConfettiManager;

import android.os.Handler;


public class DashboardWeatherAnimationController {
    private static final String TAG = "DashboardWeatherAnimation";

    private ViewGroup viewGroup;

    public DashboardWeatherAnimationController(ViewGroup viewGroup, DashboardWeatherController dashboardWeatherController) {
        this.viewGroup = viewGroup;

        // Check if there is data to be displayed
        if (Weather.hasPreloadedDataToDisplay()) {
            // note that if this data is out of date, then the weather controller updates the weather info and triggers the listener onNewWeatherDataReady
            // check if weather animation data is valid based on weather data, and update if necessary
            if (WeatherAnimation.hasPreloadedDataToDisplay()) {
                // display existing weather animation data
                startExistingWeatherAnimation();
            } else {
                // recalculate weather animation
                startNewWeatherAnimation();
            }
        } else {
            // TODO: maybe show placeholder when no data to look nice
        }

        dashboardWeatherController.addNewWeatherDataListener(new DashboardWeatherController.WeatherDataListener() {
            @Override
            public void onNewWeatherDataReady() {
                startNewWeatherAnimation();
            }
        });
    }

    /**
     * Called when the dashboard activity resumes
     * Checks if an animation exists and draws it
     */
    public void onDashboardActivityResume() {
        // only need to check if weather animation has valid data, since this comes from dashboard activity resume
        if (WeatherAnimation.hasPreloadedDataToDisplay()) {
            startExistingWeatherAnimation();
        }
    }

    /**
     * Starts new weather animation based on new weather data
     */
    private void startNewWeatherAnimation() {
        Log.i(TAG, "Making new weather animation");
        viewGroup.post(new Runnable() {
            @Override
            public void run() {
                ConfettiManager confettiManager = WeatherAnimation.startNewAnimationFromWeatherData(viewGroup);

                // TODO: check for when dynamic weather animation (changing conditions) is needed
                handleDynamicWeatherAnimation(confettiManager);
            }
        });
    }

    /**
     * Starts weather animation based on existing weather animation data
     */
    private void startExistingWeatherAnimation() {
        Log.i(TAG, "Loading preloaded weather animation");
        viewGroup.post(new Runnable() {
            @Override
            public void run() {
                ConfettiManager confettiManager = WeatherAnimation.startExistingAnimation(viewGroup);

                handleDynamicWeatherAnimation(confettiManager);
            }
        });
    }

    /**
     * Handles changing weather conditions, eg thunderstorms with changing wind conditions
     * @param confettiManager the confettiManager of the desired weather animation to control
     */
    private void handleDynamicWeatherAnimation(ConfettiManager confettiManager) {
        // TODO: change type of response based on weather data
        moveToNextDelayedResponse(confettiManager, 2000);
    }

    private void moveToNextDelayedResponse(ConfettiManager confettiManager, float emissionRate) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // stop if view no longer in view
                if (!viewGroup.isShown()) {
                    return;
                }
                Log.i(TAG, "delay kicking in with emission rate " + emissionRate);
                if (emissionRate == 2000) {
                    confettiManager.setEmissionRate(2000);
                    moveToNextDelayedResponse(confettiManager, 100);
                } else {
                    confettiManager.setEmissionRate(10);
                    moveToNextDelayedResponse(confettiManager, 2000);
                }
            }
        }, 3000);
    }
}
