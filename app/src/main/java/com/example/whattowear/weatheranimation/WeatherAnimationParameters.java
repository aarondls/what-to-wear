package com.example.whattowear.weatheranimation;

import com.github.jinatonic.confetti.ConfettiManager;

/**
 * Represents all the weather animation parameters needed to create a confetti manager
 */
public class WeatherAnimationParameters {
    public static final float LIGHT_EMISSION_RATE = 20;
    public static final float MODERATE_EMISSION_RATE = 100;
    public static final float HEAVY_EMISSION_RATE = 500;
    public static final float VERY_HEAVY_EMISSION_RATE = 1000;
    public static final float EXTREME_EMISSION_RATE = 2000;

    public static final long SHOWER_DELAY = 3000; // the delay between changing intensities for the shower dynamic animation
    public static final long SHOWER_DELAY_VARIANCE_BOUND = 1000; // the maximum positive/negative change in the shower delay
    public static final float MAX_DELTA_X = 100; // bound on the maximum change in x velocity for thunderstorm animation
    public static final long THUNDERSTORM_DELAY_FACTOR = 10; // factor for determining delay in changing thunderstorm wind speed based on current wind speed
    public static final long THUNDERSTORM_DELAY_MIN = 300; // absolute min delay for thunderstorm dynamic animation that is always added

    public static final float DRIZZLE_RAINDROP_XVELOCITY = 15;
    public static final float DRIZZLE_RAINDROP_XVELOCITY_DEVIATION = 5;
    public static final float RAIN_RAINDROP_XVELOCITY = 30;
    public static final float RAIN_RAINDROP_XVELOCITY_DEVIATION = 10;
    public static final float SLEET_RAINDROP_XVELOCITY = 10;
    public static final float SLEET_RAINDROP_XVELOCITY_DEVIATION = 5;
    public static final float SNOW_RAINDROP_XVELOCITY = 0;
    public static final float SNOW_RAINDROP_XVELOCITY_DEVIATION = 20;

    public static final float THUNDERSTORM_XVELOCITY = 300f;

    public static final float DRIZZLE_RAINDROP_YVELOCITY = 800;
    public static final float DRIZZLE_RAINDROP_YVELOCITY_DEVIATION = 200;
    public static final float RAIN_RAINDROP_YVELOCITY = 1200;
    public static final float RAIN_RAINDROP_YVELOCITY_DEVIATION = 300;
    public static final float SLEET_RAINDROP_YVELOCITY = 500;
    public static final float SLEET_RAINDROP_YVELOCITY_DEVIATION = 100;
    public static final float SNOW_RAINDROP_YVELOCITY = 200;
    public static final float SNOW_RAINDROP_YVELOCITY_DEVIATION = 100;

    private long emissionDurations;
    private float emissionRates;
    private float velocityXs;
    private float velocityDeviationXs;
    private float velocityYs;
    private float velocityDeviationYs;
    private float rotationalVelocities;
    private float rotationalVelocityDeviations;

    public WeatherAnimationParameters(long emissionDurations, float emissionRates, float velocityXs, float velocityDeviationXs, float velocityYs, float velocityDeviationYs, float rotationalVelocities, float rotationalVelocityDeviations) {
        this.emissionDurations = emissionDurations;
        this.emissionRates = emissionRates;
        this.velocityXs = velocityXs;
        this.velocityDeviationXs = velocityDeviationXs;
        this.velocityYs = velocityYs;
        this.velocityDeviationYs = velocityDeviationYs;
        this.rotationalVelocities = rotationalVelocities;
        this.rotationalVelocityDeviations = rotationalVelocityDeviations;
    }

    /**
     * @param conditionsIntensity the condition intensity of the drizzle
     * @return a new WeatherAnimationParameters object with the appropriate drizzle parameters
     */
    public static WeatherAnimationParameters drizzleParameters(WeatherAnimation.ConditionsIntensity conditionsIntensity) {
        return new WeatherAnimationParameters(
                ConfettiManager.INFINITE_DURATION,
                getEmissionRatesFromConditionIntensity(conditionsIntensity),
                DRIZZLE_RAINDROP_XVELOCITY,
                DRIZZLE_RAINDROP_XVELOCITY_DEVIATION,
                DRIZZLE_RAINDROP_YVELOCITY,
                DRIZZLE_RAINDROP_YVELOCITY_DEVIATION,
                0f,
                0f);
    }

    /**
     * @param conditionsIntensity the condition intensity of the rain
     * @return a new WeatherAnimationParameters object with the appropriate rain parameters
     */
    public static WeatherAnimationParameters rainParameters(WeatherAnimation.ConditionsIntensity conditionsIntensity) {
        return new WeatherAnimationParameters(
                ConfettiManager.INFINITE_DURATION,
                getEmissionRatesFromConditionIntensity(conditionsIntensity),
                RAIN_RAINDROP_XVELOCITY,
                RAIN_RAINDROP_XVELOCITY_DEVIATION,
                RAIN_RAINDROP_YVELOCITY,
                RAIN_RAINDROP_YVELOCITY_DEVIATION,
                0f,
                0f);
    }

    /**
     * @param conditionsIntensity the condition intensity of the rain
     * @return a new WeatherAnimationParameters object with the appropriate sleet parameters
     */
    public static WeatherAnimationParameters sleetParameters(WeatherAnimation.ConditionsIntensity conditionsIntensity) {
        return new WeatherAnimationParameters(
                ConfettiManager.INFINITE_DURATION,
                getEmissionRatesFromConditionIntensity(conditionsIntensity),
                SLEET_RAINDROP_XVELOCITY,
                SLEET_RAINDROP_XVELOCITY_DEVIATION,
                SLEET_RAINDROP_YVELOCITY,
                SLEET_RAINDROP_YVELOCITY_DEVIATION,
                0f,
                0f);
    }

    /**
     * @param conditionsIntensity the condition intensity of the rain
     * @return a new WeatherAnimationParameters object with the appropriate snow parameters
     */
    public static WeatherAnimationParameters snowParameters(WeatherAnimation.ConditionsIntensity conditionsIntensity) {
        return new WeatherAnimationParameters(
                ConfettiManager.INFINITE_DURATION,
                getEmissionRatesFromConditionIntensity(conditionsIntensity),
                SNOW_RAINDROP_XVELOCITY,
                SNOW_RAINDROP_XVELOCITY_DEVIATION,
                SNOW_RAINDROP_YVELOCITY,
                SNOW_RAINDROP_YVELOCITY_DEVIATION,
                0f,
                0f);
    }

    public static float getEmissionRatesFromConditionIntensity(WeatherAnimation.ConditionsIntensity conditionsIntensity) {
        switch(conditionsIntensity) {
            case NONE:
                return 0f;
            case LIGHT:
                return LIGHT_EMISSION_RATE;
            case MODERATE:
                return MODERATE_EMISSION_RATE;
            case HEAVY:
                return HEAVY_EMISSION_RATE;
            case VERY_HEAVY:
                return VERY_HEAVY_EMISSION_RATE;
            case EXTREME:
                return EXTREME_EMISSION_RATE;
            default:
                // Have no emissions if not matching any known conditions
                return 0f;
        }
    }

    public long getEmissionDurations() {
        return emissionDurations;
    }

    public float getEmissionRates() {
        return emissionRates;
    }

    public float getVelocityXs() {
        return velocityXs;
    }

    public float getVelocityDeviationXs() {
        return velocityDeviationXs;
    }

    public float getVelocityYs() {
        return velocityYs;
    }

    public float getVelocityDeviationYs() {
        return velocityDeviationYs;
    }

    public float getRotationalVelocities() {
        return rotationalVelocities;
    }

    public float getRotationalVelocityDeviations() {
        return rotationalVelocityDeviations;
    }
}
