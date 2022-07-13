package com.example.whattowear.weatheranimation;

import android.view.ViewGroup;

import com.example.whattowear.models.Conditions;
import com.example.whattowear.models.Weather;
import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles creating the proper weather animation and all the necessary parameters based on Weather data
 */
public class WeatherAnimation {
    private static String TAG = "WeatherAnimation";
    private static WeatherAnimation weatherAnimation = new WeatherAnimation();

    public static enum ConditionsType {
        DRIZZLE, RAIN, SLEET, SNOW
    }

    // TODO: handle dynamic modifiers
    public static enum ConditionsModifier {
        THUNDERSTORM, SHOWER
    }

    public static enum ConditionsIntensity {
        NONE, LIGHT, MODERATE, HEAVY, VERY_HEAVY, EXTREME
    }

    private static String loadedDataLocationName;

    private static List<ConditionsType> conditionsTypes; // used to specify the condition type
    private static List<ConfettoGenerator> confettoGenerators; // used to store the ith confetto generator associated with the ith conditionType
    private static List<WeatherAnimationParameters> weatherAnimationParameters; // used to store the ith WeatherAnimationParameters associated with the ith conditionType

    private static List<ConditionsModifier> conditionsModifiers; // used to specify what dynamic weather animation is used

    private static ConditionsIntensity conditionsIntensity;
    private static ConditionsIntensity alternateConditionsIntensity; // used for the second condition intensity switched during dynamic weather animations

    private WeatherAnimation() {
        conditionsTypes = new ArrayList<>();
        confettoGenerators = new ArrayList<>();
        weatherAnimationParameters = new ArrayList<>();

        conditionsModifiers = new ArrayList<>();

        loadedDataLocationName = "";
    }

    /**
     * @param viewGroup viewGroup the view to display the weather animation
     * @return the ConfettiManagers with the properties associated with the current weather and given viewgroup
     */
    public static List<ConfettiManager> startNewAnimationFromWeatherData(ViewGroup viewGroup) {
        generateConfettoParametersFromWeatherData();

        loadedDataLocationName = Weather.getLoadedDataLocationName();

        return startExistingAnimation(viewGroup);
    }

    /**
     * Starts calculated animation at the given viewGroup
     * @param viewGroup the view to display the weather animation
     * @return the ConfettiManagers with the existing calculated properties and given viewgroup
     */
    public static List<ConfettiManager> startExistingAnimation (ViewGroup viewGroup) {
        int containerWidth = viewGroup.getWidth();
        ConfettiSource confettiSource = new ConfettiSource(0, 0, containerWidth, 10);

        // generate all confetti managers for each condition type
        List<ConfettiManager> confettiManagers = new ArrayList<>();
        for (int i=0; i<conditionsTypes.size(); i++) {
            WeatherAnimationParameters currentWeatherAnimationParameters = weatherAnimationParameters.get(i);
            confettiManagers.add(new ConfettiManager(viewGroup.getContext(), confettoGenerators.get(i), confettiSource, viewGroup)
                    .setEmissionDuration(currentWeatherAnimationParameters.getEmissionDurations())
                    .setEmissionRate(currentWeatherAnimationParameters.getEmissionRates()/ (float) conditionsTypes.size()) // account for multiple weather conditions
                    .setVelocityX(currentWeatherAnimationParameters.getVelocityXs(), currentWeatherAnimationParameters.getVelocityDeviationXs())
                    .setVelocityY(currentWeatherAnimationParameters.getVelocityYs(), currentWeatherAnimationParameters.getVelocityDeviationYs())
                    .setRotationalVelocity(currentWeatherAnimationParameters.getRotationalVelocities(), currentWeatherAnimationParameters.getRotationalVelocityDeviations())
                    .animate());
        }

        return confettiManagers;
    }

    /**
     * Calculates the correct confetto generator given current weather conditions and stores it into confettoGenerator and
     * calculates the correct parameters for creating a ConfettiManager to handle the weather animation for the current weather conditions
     */
    private static void generateConfettoParametersFromWeatherData() {
        clearAllAnimationData();

        int weatherConditionsID = Weather.getCurrentForecast().getHourCondition().getWeatherConditionsID();

        int conditionsIDFirstDigit = weatherConditionsID/100;
        int conditionsIDMiddleDigit = (weatherConditionsID/10)%10;
        int conditionsIDLastDigit = weatherConditionsID%10;

        // set intensity and type
        // use chart at: https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
        // interpret shower/ragged as the same

        // notice that for some of the weather IDs:
        // the first digit indicates the main condition type
        // the last digit indicates intensity

        switch (conditionsIDFirstDigit) {
            case Conditions.THUNDERSTORM_CONDITIONS_ID_FIRST_DIGIT:
                classifyThunderstormConditionsForConfettoParameters(conditionsIDMiddleDigit, conditionsIDLastDigit);
                break;
            case Conditions.DRIZZLE_CONDITIONS_ID_FIRST_DIGIT:
                classifyDrizzleConditionsForConfettoParameters(conditionsIDMiddleDigit, conditionsIDLastDigit);
                break;
            case Conditions.RAIN_CONDITIONS_ID_FIRST_DIGIT:
                classifyRainConditionsForConfettoParameters(conditionsIDMiddleDigit, conditionsIDLastDigit);
                break;
            case Conditions.SNOW_CONDITIONS_ID_FIRST_DIGIT:
                classifySnowConditionsForConfettoParameters(conditionsIDMiddleDigit, conditionsIDLastDigit);
                break;
        }

        generateConfettoParametersFromClassifiedConditions();
    }

    /**
     * Classifies the current thunderstorm conditions into the correct condition types and intensities.
     * This adds the correct:
     *      ConditionsModifier(s) to the conditionsModifiers list
     *      ConditionsType(s) to conditionsTypes list
     *      conditionsIntensity and alternateConditionsIntensity
     * If the current thunderstorm conditions requires no animation, then nothing is added or set.
     * @param conditionsIDMiddleDigit the middle digit of the current thunderstorm condition ID
     * @param conditionsIDLastDigit the last digit of the current thunderstorm condition ID
     */
    private static void classifyThunderstormConditionsForConfettoParameters(int conditionsIDMiddleDigit, int conditionsIDLastDigit) {
        // main weather condition is thunderstorm

        // check for the correct ID
        if (conditionsIDMiddleDigit == 0) {
            // type is thunderstorm with rain
            conditionsModifiers.add(ConditionsModifier.THUNDERSTORM);
            conditionsTypes.add(ConditionsType.RAIN);
            setConditionIntensityFromIDLastDigit(conditionsIDLastDigit);
        } else if (conditionsIDMiddleDigit == 3) {
            // type is thunderstorm with drizzle
            conditionsModifiers.add(ConditionsModifier.THUNDERSTORM);
            conditionsTypes.add(ConditionsType.DRIZZLE);
            setConditionIntensityFromIDLastDigit(conditionsIDLastDigit);
        }
        // if ID does not match the above conditions, then no need for animation, so keep conditionsTypes empty
    }

    /**
     * Classifies the current drizzle conditions into the correct condition types and intensities.
     * This adds the correct:
     *      ConditionsModifier(s) to the conditionsModifiers list
     *      ConditionsType(s) to conditionsTypes list
     *      conditionsIntensity and alternateConditionsIntensity
     * If the current drizzle conditions requires no animation, then nothing is added or set.
     * @param conditionsIDMiddleDigit the middle digit of the current drizzle condition ID
     * @param conditionsIDLastDigit the last digit of the current drizzle condition ID
     */
    private static void classifyDrizzleConditionsForConfettoParameters(int conditionsIDMiddleDigit, int conditionsIDLastDigit) {
        // main weather condition is drizzle

        // check for correct ID
        if (conditionsIDMiddleDigit == 0) {
            // type is drizzle
            conditionsTypes.add(ConditionsType.DRIZZLE);
            setConditionIntensityFromIDLastDigit(conditionsIDLastDigit);
        } else if (conditionsIDMiddleDigit == 1) {
            // type is drizzle rain
            conditionsTypes.add(ConditionsType.DRIZZLE);
            conditionsTypes.add(ConditionsType.RAIN);

            // condition intensity deviates from the last digit rule
            if (conditionsIDLastDigit == 1 || conditionsIDLastDigit == 3) {
                conditionsIntensity = ConditionsIntensity.MODERATE;
                alternateConditionsIntensity = ConditionsIntensity.LIGHT;
            } else {
                conditionsIntensity = ConditionsIntensity.HEAVY;
                alternateConditionsIntensity = ConditionsIntensity.LIGHT;
            }
        } else if (conditionsIDMiddleDigit == 2) {
            // type is shower drizzle
            conditionsModifiers.add(ConditionsModifier.SHOWER);
            conditionsTypes.add(ConditionsType.DRIZZLE);
            conditionsIntensity = ConditionsIntensity.MODERATE;
            alternateConditionsIntensity = ConditionsIntensity.LIGHT;
        }
        // if ID does not match the above conditions, then no need for animation, so keep conditionsTypes empty
    }

    /**
     * Classifies the current rain conditions into the correct condition types and intensities.
     * This adds the correct:
     *      ConditionsModifier(s) to the conditionsModifiers list
     *      ConditionsType(s) to conditionsTypes list
     *      conditionsIntensity and alternateConditionsIntensity
     * If the current rain conditions requires no animation, then nothing is added or set.
     * @param conditionsIDMiddleDigit the middle digit of the current rain condition ID
     * @param conditionsIDLastDigit the last digit of the current rain condition ID
     */
    private static void classifyRainConditionsForConfettoParameters(int conditionsIDMiddleDigit, int conditionsIDLastDigit) {
        // main weather is rain

        // check for correct ID
        if (conditionsIDMiddleDigit == 0 || conditionsIDMiddleDigit == 1) {
            // type is rain
            conditionsTypes.add(ConditionsType.RAIN);
            setConditionIntensityFromIDLastDigit(conditionsIDLastDigit);
        } else if (conditionsIDMiddleDigit == 2 || conditionsIDMiddleDigit == 3) {
            // type is shower rain
            conditionsModifiers.add(ConditionsModifier.SHOWER);
            conditionsTypes.add(ConditionsType.RAIN);
            setConditionIntensityFromIDLastDigit(conditionsIDLastDigit);
        }
        // if ID does not match the above conditions, then no need for animation, so keep conditionsTypes empty
    }

    /**
     * Classifies the current snow conditions into the correct condition types and intensities.
     * This adds the correct:
     *      ConditionsModifier(s) to the conditionsModifiers list
     *      ConditionsType(s) to conditionsTypes list
     *      conditionsIntensity and alternateConditionsIntensity
     * If the current rain conditions requires no animation, then nothing is added or set.
     * @param conditionsIDMiddleDigit the middle digit of the current snow condition ID
     * @param conditionsIDLastDigit the last digit of the current snow condition ID
     */
    private static void classifySnowConditionsForConfettoParameters(int conditionsIDMiddleDigit, int conditionsIDLastDigit) {
        // main weather is snow

        // check for correct ID
        if (conditionsIDMiddleDigit == 0) {
            // type is snow
            conditionsTypes.add(ConditionsType.SNOW);
            setConditionIntensityFromIDLastDigit(conditionsIDLastDigit);
        } else if (conditionsIDMiddleDigit == 1) {
            // type is dependent
            if (conditionsIDLastDigit == 1) {
                // type is sleet
                conditionsTypes.add(ConditionsType.SLEET);
                conditionsIntensity = ConditionsIntensity.MODERATE;
                alternateConditionsIntensity = ConditionsIntensity.LIGHT;
            } else if (conditionsIDLastDigit == 2) {
                // type is light shower sleet
                conditionsModifiers.add(ConditionsModifier.SHOWER);
                conditionsTypes.add(ConditionsType.SLEET);
                conditionsIntensity = ConditionsIntensity.LIGHT;
                alternateConditionsIntensity = ConditionsIntensity.NONE;
            } else if (conditionsIDLastDigit == 3) {
                // type is shower sleet
                conditionsModifiers.add(ConditionsModifier.SHOWER);
                conditionsTypes.add(ConditionsType.SLEET);
                conditionsIntensity = ConditionsIntensity.MODERATE;
                alternateConditionsIntensity = ConditionsIntensity.LIGHT;
            } else if (conditionsIDLastDigit == 5) {
                // type is light rain and snow
                conditionsTypes.add(ConditionsType.RAIN);
                conditionsTypes.add(ConditionsType.SNOW);
                conditionsIntensity = ConditionsIntensity.LIGHT;
                alternateConditionsIntensity = ConditionsIntensity.NONE;
            } else if (conditionsIDLastDigit == 6) {
                // type is rain and snow
                conditionsTypes.add(ConditionsType.RAIN);
                conditionsTypes.add(ConditionsType.SNOW);
                conditionsIntensity = ConditionsIntensity.MODERATE;
                alternateConditionsIntensity = ConditionsIntensity.LIGHT;
            }
        } else if (conditionsIDMiddleDigit == 2) {
            // type is shower snow
            conditionsModifiers.add(ConditionsModifier.SHOWER);
            conditionsTypes.add(ConditionsType.SNOW);
            setConditionIntensityFromIDLastDigit(conditionsIDLastDigit);
        }
        // if ID does not match the above conditions, then no need for animation, so keep conditionsTypes empty
    }

    /**
     * Generates the weather animation parameters and confetto generators given the classified conditions
     * Requires conditions to be classified already, ie conditionsModifiers, conditionsTypes, conditionsIntesity, and alternateConditionsIntensity to be set
     */
    private static void generateConfettoParametersFromClassifiedConditions() {
        // for each confetto type, create the appropriate generator
        for (int i=0; i<conditionsTypes.size(); i++) {
            ConditionsType currentConfettoType = conditionsTypes.get(i);
            switch (currentConfettoType) {
                case DRIZZLE:
                    weatherAnimationParameters.add(WeatherAnimationParameters.drizzleParameters(conditionsIntensity));

                    confettoGenerators.add(new ConfettoGenerator() {
                        @Override
                        public Confetto generateConfetto(Random random) {
                            return new RaindropConfetto(RaindropConfetto.DRIZZLE_RAINDROP_THICKNESS, RaindropConfetto.RAINDROP_COLOR);
                        }
                    });
                    break;
                case RAIN:
                    weatherAnimationParameters.add(WeatherAnimationParameters.rainParameters(conditionsIntensity));

                    confettoGenerators.add(new ConfettoGenerator() {
                        @Override
                        public Confetto generateConfetto(Random random) {
                            return new RaindropConfetto(RaindropConfetto.RAIN_RAINDROP_THICKNESS, RaindropConfetto.RAINDROP_COLOR);
                        }
                    });
                    break;
                case SLEET:
                    weatherAnimationParameters.add(WeatherAnimationParameters.sleetParameters(conditionsIntensity));

                    confettoGenerators.add(new ConfettoGenerator() {
                        @Override
                        public Confetto generateConfetto(Random random) {
                            return new SnowflakeConfetto(SnowflakeConfetto.SLEET_RADIUS, SnowflakeConfetto.SLEET_COLOR, SnowflakeConfetto.SLEET_ALPHA);
                        }
                    });
                    break;
                case SNOW:
                    weatherAnimationParameters.add(WeatherAnimationParameters.snowParameters(conditionsIntensity));

                    confettoGenerators.add(new ConfettoGenerator() {
                        @Override
                        public Confetto generateConfetto(Random random) {
                            return new SnowflakeConfetto(SnowflakeConfetto.SNOWFLAKE_RADIUS, SnowflakeConfetto.SNOWFLAKE_COLOR, SnowflakeConfetto.SNOWFLAKE_ALPHA);
                        }
                    });
                    break;
                default:
                    // default to raindrop
                    weatherAnimationParameters.add(WeatherAnimationParameters.rainParameters(conditionsIntensity));

                    confettoGenerators.add(new ConfettoGenerator() {
                        @Override
                        public Confetto generateConfetto(Random random) {
                            return new RaindropConfetto(RaindropConfetto.RAIN_RAINDROP_THICKNESS, RaindropConfetto.RAINDROP_COLOR);
                        }
                    });
                    break;
            }
        }
    }

    /**
     * Sets the conditionsIntensity and alternateConditionsIntensity variables based from the given last digit of
     * the weather conditions ID. conditionsIntensity follows the rule where the ID is:
     * 0 --> light
     * 1 --> moderate
     * 2 --> heavy
     * 3 --> very heavy
     * 4 --> extreme
     * alternateConditionsIntensity is then set based on conditionsIntensity. It is set to NONE when
     * conditionsIntensity is LIGHT, MODERATE when conditionsIntensity is EXTREME, and LIGHT otherwise.
     * @param conditionsIDLastDigit the last digit of the current conditions ID
     */
    private static void setConditionIntensityFromIDLastDigit(int conditionsIDLastDigit) {
        switch(conditionsIDLastDigit) {
            case 0:
                conditionsIntensity = ConditionsIntensity.LIGHT;
                alternateConditionsIntensity = ConditionsIntensity.NONE;
                break;
            case 1:
                conditionsIntensity = ConditionsIntensity.MODERATE;
                alternateConditionsIntensity = ConditionsIntensity.LIGHT;
                break;
            case 2:
                conditionsIntensity = ConditionsIntensity.HEAVY;
                alternateConditionsIntensity = ConditionsIntensity.LIGHT;
                break;
            case 3:
                conditionsIntensity = ConditionsIntensity.VERY_HEAVY;
                alternateConditionsIntensity = ConditionsIntensity.LIGHT;
                break;
            case 4:
                conditionsIntensity = ConditionsIntensity.EXTREME;
                alternateConditionsIntensity = ConditionsIntensity.MODERATE;
                break;
            default:
                // set none for unidentifiable ID
                conditionsIntensity = ConditionsIntensity.NONE;
                alternateConditionsIntensity = ConditionsIntensity.NONE;
        }
    }

    /**
     * Clears all lists related to animation information, specifically:
     * confettoGenerators, conditionsTypes, conditionsModifiers, and weatherAnimationParameters
     */
    private static void clearAllAnimationData() {
        // clear existing confetto generators
        confettoGenerators.clear();

        // clear existing condition types and modifiers
        conditionsTypes.clear();
        conditionsModifiers.clear();

        // clear all animation parameters
        weatherAnimationParameters.clear();
    }

    /**
     * @return whether there is valid weather animation data at the location Weather.lastLocationName
     */
    public static boolean hasPreloadedDataToDisplay() {
        return Weather.hasPreloadedDataToDisplay() && loadedDataLocationName.equals(Weather.getLoadedDataLocationName());
    }

    public static List<ConditionsModifier> getConditionsModifiers() {
        return conditionsModifiers;
    }

    public static ConditionsIntensity getConditionsIntensity() {
        return conditionsIntensity;
    }

    public static ConditionsIntensity getAlternateConditionsIntensity() {
        return alternateConditionsIntensity;
    }
}
