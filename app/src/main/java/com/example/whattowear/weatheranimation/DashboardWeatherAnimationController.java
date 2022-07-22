package com.example.whattowear.weatheranimation;

import static com.example.whattowear.weatheranimation.WeatherAnimationParameters.SHOWER_DELAY;
import static com.example.whattowear.weatheranimation.WeatherAnimationParameters.SHOWER_DELAY_VARIANCE_BOUND;
import static com.example.whattowear.weatheranimation.WeatherAnimationParameters.THUNDERSTORM_DELAY_FACTOR;
import static com.example.whattowear.weatheranimation.WeatherAnimationParameters.THUNDERSTORM_DELAY_MIN;
import static com.example.whattowear.weatheranimation.WeatherAnimationParameters.THUNDERSTORM_XVELOCITY;

import android.util.Log;
import android.view.ViewGroup;

import com.example.whattowear.DashboardWeatherController;
import com.example.whattowear.models.Weather;
import com.github.jinatonic.confetti.ConfettiManager;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DashboardWeatherAnimationController {
    private static final String TAG = "DashboardWeatherAnimation";

    private ViewGroup viewGroup;
    private List<ConfettiManager> confettiManagers;

    private float conditionsEmissionRate;
    private float alternateConditionsEmissionRate;

    private Random random;

    public DashboardWeatherAnimationController(ViewGroup viewGroup, DashboardWeatherController dashboardWeatherController) {
        this.viewGroup = viewGroup;

        confettiManagers = new ArrayList<>();

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
        if (WeatherAnimation.hasPreloadedDataToDisplay() && !confettiManagers.isEmpty()) {
            handleDynamicWeatherAnimationIfNeeded();
        }
    }

    /**
     * Starts new weather animation on the dashboard based on new weather data
     */
    private void startNewWeatherAnimation() {
        Log.i(TAG, "Making new weather animation");
        viewGroup.post(new Runnable() {
            @Override
            public void run() {
                clearVisibleWeatherAnimation();
                confettiManagers = WeatherAnimation.startNewAnimationFromWeatherData(viewGroup);

                handleDynamicWeatherAnimationIfNeeded();
            }
        });
    }

    /**
     * Starts weather animation on the dashboard based on existing weather animation data
     */
    private void startExistingWeatherAnimation() {
        Log.i(TAG, "Loading preloaded weather animation");
        viewGroup.post(new Runnable() {
            @Override
            public void run() {
                clearVisibleWeatherAnimation();

                confettiManagers = WeatherAnimation.startExistingAnimation(viewGroup);

                handleDynamicWeatherAnimationIfNeeded();
            }
        });
    }

    /**
     * Ends the currently playing weather animation on the dashboard, if there is one
     */
    private void clearVisibleWeatherAnimation() {
        // cancel existing animations, if there are one
        if (!confettiManagers.isEmpty()) {
            Log.i(TAG, "Cancelling other animations");
            for (ConfettiManager confettiManager : confettiManagers) {
                confettiManager.setEmissionDuration(0);
            }
        }
    }

    /**
     * @return whether the current weather animation requires dynamic control
     */
    private boolean weatherRequiresDynamicAnimation() {
        return !WeatherAnimation.getConditionsModifiers().isEmpty();
    }

    /**
     * Handles dynamic changing weather conditions, eg thunderstorms with changing wind conditions,
     * if needed. This is done by checking the weatherRequiresDynamicAnimation method.
     */
    private void handleDynamicWeatherAnimationIfNeeded() {
        // check if dynamic animation is not needed
        if (!weatherRequiresDynamicAnimation()) {
            return;
        }

        // get the condition modifiers (dynamic condition types) of the weather animation
        List<WeatherAnimation.ConditionsModifier> conditionsModifiers = WeatherAnimation.getConditionsModifiers();

        // initialize randomizer
        random = new Random();

        // conditions modifiers affects all conditions
        for (WeatherAnimation.ConditionsModifier conditionsModifier : conditionsModifiers) {
            if (conditionsModifier == WeatherAnimation.ConditionsModifier.SHOWER) {
                // set emission and alternate emission rates
                conditionsEmissionRate = WeatherAnimationParameters.getEmissionRatesFromConditionIntensity(WeatherAnimation.getConditionsIntensity());
                alternateConditionsEmissionRate = WeatherAnimationParameters.getEmissionRatesFromConditionIntensity(WeatherAnimation.getAlternateConditionsIntensity());

                showerDynamicAnimation(alternateConditionsEmissionRate);
            } else if (conditionsModifier == WeatherAnimation.ConditionsModifier.THUNDERSTORM) {
                thunderstormDynamicAnimation(0);
            }
        }
    }

    /**
     * Handles the shower dynamic animation, which varies the emission rate of whatever ConditionsType (ie drizzle/rain/sleet/snow)
     * is on the screen by switching between the conditionsIntensity and alternateConditionsIntensity as calculated in WeatherAnimations.
     * The delay between switching is randomly centered around SHOWER_DELAY.
     * This method calls itself and continues, until the viewgroup displaying the animation is no longer in view.
     * @param emissionRate the emission rate that the animations will be changed to
     */
    private void showerDynamicAnimation(float emissionRate) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // stop if view no longer in view
                if (!viewGroup.isShown()) {
                    return;
                }
                Log.i(TAG, "delay kicking in with emission rate " + emissionRate);
                // switch between the default and alternate conditons emission rate
                if (emissionRate == conditionsEmissionRate) {
                    for (ConfettiManager confettiManager : confettiManagers) {
                        confettiManager.setEmissionRate(conditionsEmissionRate);
                    }
                    showerDynamicAnimation(alternateConditionsEmissionRate);
                } else {
                    for (ConfettiManager confettiManager : confettiManagers) {
                        confettiManager.setEmissionRate(alternateConditionsEmissionRate);
                    }
                    showerDynamicAnimation(conditionsEmissionRate);
                }
            }
        }, SHOWER_DELAY+ random.nextInt()%SHOWER_DELAY_VARIANCE_BOUND); // vary the delay between changes randomly
    }

    /**
     * Handles the thunderstorm dynamic animation, which varies the xVelocity of whatever ConditionsType (ie drizzle/rain/sleet/snow)
     * is on the screen to simulate changing wind conditions by randomly generating the change in xVelocity.
     * To make the animation more natural, the wind speed is not randomly generated but rather the change in wind speed is. If
     * the change in wind speed causes the wind to change directions (ie, switch signs of xVelocity), then the wind speed is set to 0.
     * This makes the animation more natural as it is very smooth with no abrupt changes in direction.
     * The delay between changing wind speeds is determined as a factor of the current wind speed (xVelocity), as it is not natural
     * to see an immediate shift in wind speed when blowing strongly at a specific direction.
     * This method calls itself and continues, until the viewgroup displaying the animation is no longer in view.
     * @param xVelocity the xVelocity that the animations will be changed to
     */
    private void thunderstormDynamicAnimation(float xVelocity) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // stop if view no longer in view
                if (!viewGroup.isShown()) {
                    return;
                }
                Log.i(TAG, "delay kicking in with x velocity " + xVelocity);
                for (ConfettiManager confettiManager : confettiManagers) {
                    confettiManager.setVelocityX(xVelocity, 0.1f*xVelocity);
                }

                // generate new change of wind direction
                // use random number to change current wind speed, then bound at maximum
                // this is more realistic than simply changing wind direction directly
                // if the change results in sign of x velocity changing, put x velocity to 0 first to avoid abrupt change

                float nextXVelocity = xVelocity + random.nextInt()%WeatherAnimationParameters.MAX_DELTA_X;
                if (nextXVelocity > THUNDERSTORM_XVELOCITY) {
                    nextXVelocity = THUNDERSTORM_XVELOCITY;
                } else if (nextXVelocity < -THUNDERSTORM_XVELOCITY) {
                    nextXVelocity = -THUNDERSTORM_XVELOCITY;
                } else if (xVelocity*nextXVelocity < 0) { // will not overflow since wind x velocities are low enough
                    // opposite signs
                    nextXVelocity = 0;
                }
                thunderstormDynamicAnimation(nextXVelocity);
            }
        }, (long) (xVelocity*THUNDERSTORM_DELAY_FACTOR+THUNDERSTORM_DELAY_MIN)); // have delay a factor of how much wind changed
    }

}
