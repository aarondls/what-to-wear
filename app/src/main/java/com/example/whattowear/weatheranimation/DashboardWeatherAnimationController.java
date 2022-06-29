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
    private ConfettiManager confettiManager;

    public DashboardWeatherAnimationController(ViewGroup viewGroup, DashboardWeatherController dashboardWeatherController) {
        this.viewGroup = viewGroup;

        // Check if there is data to be displayed
        // this first check of Weather.hasPreloadedDataToDisplay is needed in case WeatherAnimation has yet to calculate the correct animation
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
     * Called when the dashboard activity restart
     * Checks if a dynamic animation exists and restarts it
     */
    public void onDashboardActivityRestart() {
        // only need to check if weather animation has valid data, since this comes from dashboard activity restart
        if (WeatherAnimation.hasPreloadedDataToDisplay() && confettiManager != null && weatherRequiresDynamicAnimation()) {
            handleDynamicWeatherAnimation();
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
                clearVisibleWeatherAnimation();
                confettiManager = WeatherAnimation.startNewAnimationFromWeatherData(viewGroup);

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
                clearVisibleWeatherAnimation();

                confettiManager = WeatherAnimation.startExistingAnimation(viewGroup);

                handleDynamicWeatherAnimation(confettiManager);
            }
        });
    }

    /**
     * Ends the currently playing weather animation on the dashboard, if there is one
     */
    private void clearVisibleWeatherAnimation() {
        // cancel existing animation, if there is one
        if (confettiManager != null) {
            Log.i(TAG, "Cancelling other animation");
            confettiManager.setEmissionDuration(0);
        }
    }

    /**
     * Handles changing weather conditions, eg thunderstorms with changing wind conditions
     */
    private void handleDynamicWeatherAnimation() {
        // TODO: change type of response based on weather data
        moveToNextDelayedResponse(2000);
    }

    private void moveToNextDelayedResponse(float emissionRate) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // stop if view no longer in view
                if (!viewGroup.isShown()) {
                    return;
                }
                Log.i(TAG, "delay kicking in with emission rate " + emissionRate);
                if (emissionRate == 2000) {
                    confettiManager.setEmissionRate(2000);
                    moveToNextDelayedResponse(100);
                } else {
                    confettiManager.setEmissionRate(10);
                    moveToNextDelayedResponse(2000);
                }
            }
        }, 500);
    }
}
