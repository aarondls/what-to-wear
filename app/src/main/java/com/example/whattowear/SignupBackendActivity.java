package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.whattowear.models.Accessories;
import com.example.whattowear.models.ClothingRanker;
import com.example.whattowear.models.Footwear;
import com.example.whattowear.models.LowerBodyGarment;
import com.example.whattowear.models.OverBodyGarment;
import com.example.whattowear.models.Preferences;
import com.example.whattowear.models.UpperBodyGarment;
import com.example.whattowear.models.Weather;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class SignupBackendActivity extends AppCompatActivity {
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String USER_FULL_NAME_KEY = "userFullName";
    public static final String PARSE_USER_NAME_KEY = "name";

    private static final String SIGNING_UP_PROMPT = "Signing up";
    private static final String SIGNING_UP_SUCCESS_PROMPT = "Successfully created new user";
    private static final String CREATING_RANKERS_PROMPT = "Creating clothing rankers";
    private static final String CREATING_RANKERS_SUCCESS_PROMPT = "Successfully created clothing rankers";
    private static final String CREATING_PREFERENCES_PROMPT = "Creating preferences";
    private static final String CREATING_PREFERENCES_SUCCESS_PROMPT = "Successfully created preferences";
    private static final String LINKING_ACCOUNT_WITH_PREFERENCES_PROMPT = "Linking user account with preferences";
    private static final String LINKING_ACCOUNT_WITH_PREFERENCES_SUCCESS_PROMPT = "Successfully linked user account with preferences";
    private static final String NEW_USER_WELCOME_PROMPT = "Welcome to WhatToWear";

    private static final int FADE_OUT_ANIMATION_TIME = 1500;
    private static final int FADE_IN_ANIMATION_TIME = 1200;

    private TextView informationTextview;
    private LottieAnimationView loadingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_backend);

        // have layout be full screen to hide both the top and bottom bars
        FullscreenLayout.hideTopBottomBars(getWindow());

        informationTextview = findViewById(R.id.signup_backend_information_textview);
        loadingAnimation = findViewById(R.id.signup_backend_loading_animationview);

        createUser();
    }

    /**
     * Handles creating the new user profile, along with their new preferences by calling createUserPreferences
     * This leads to the dashboard screen if everything is successful with Parse through createUserPreferences
     */
    private void createUser() {
        Intent intent = getIntent();
        String username = intent.getStringExtra(USERNAME_KEY);
        String password = intent.getStringExtra(PASSWORD_KEY);
        String userFullName = intent.getStringExtra(USER_FULL_NAME_KEY);

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.put(PARSE_USER_NAME_KEY, userFullName);

        informationTextview.setText(SIGNING_UP_PROMPT);
        user.signUpInBackground(e -> {
            if (e == null) {
                // user successfully created here, but not fully yet (missing preferences)
                // let user know new user account was made
                informationTextview.setText(SIGNING_UP_SUCCESS_PROMPT);

                // add preferences
                createUserPreferences(user);
            } else {
                // Let user know something wrong happened
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // Go back to login screen to let user re-login
                moveToLoginScreen();
            }
        });
    }


    /**
     * Handles creating the new user preferences locally
     * Calls savePreferences once successful to upload to Parse, which leads to the dashboard screen if successful
     * @param user the newly created user that requires new preferences
     */
    private void createUserPreferences(ParseUser user) {
        // create all the rankers first
        List<ClothingRanker> allGarmentRankers = new ArrayList<>();
        allGarmentRankers.addAll(OverBodyGarment.initializeOverBodyGarmentRankers(user));
        allGarmentRankers.addAll(UpperBodyGarment.initializeUpperBodyGarmentRankers(user));
        allGarmentRankers.addAll(LowerBodyGarment.initializeLowerBodyGarmentRankers(user));
        allGarmentRankers.addAll(Footwear.initializeFootwearRankers(user));
        allGarmentRankers.addAll(Accessories.initializeAccessoriesRankers(user));

        // let user know clothing rankers are being created
        informationTextview.setText(CREATING_RANKERS_PROMPT);

        ClothingRanker.saveAllInBackground(allGarmentRankers, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // let user know clothing rankers are successfully created
                    informationTextview.setText(CREATING_RANKERS_SUCCESS_PROMPT);

                    // rankers successfully created, so create preferences then update user
                    Preferences preferences = new Preferences();
                    preferences.setUser(user);
                    preferences.setWeatherUnit(Weather.DEFAULT_WEATHER_UNIT); // set default
                    preferences.setShowAdvancedPreferences(Preferences.DEFAULT_SHOW_ADVANCED_PREFERENCES); // set default
                    preferences.setOverBodyGarmentRankers(OverBodyGarment.getOverBodyGarmentRankers());
                    preferences.setUpperBodyGarmentRankers(UpperBodyGarment.getUpperBodyGarmentRankers());
                    preferences.setLowerBodyGarmentRankers(LowerBodyGarment.getLowerBodyGarmentRankers());
                    preferences.setFootwearRankers(Footwear.getFootwearRankers());
                    preferences.setAccessoriesRankers(Accessories.getAccessoriesRankers());

                    savePreferences(user, preferences);
                } else {
                    // Let user know something wrong happened
                    Toast.makeText(SignupBackendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Go back to login screen to let user re-login
                    moveToLoginScreen();
                }
            }
        });
    }
    /**
     * Saves the newly created preferences to Parse
     * Calls linkPreferencesToUser once successful to link it with the user profile, which leads to the
     * dashboard screen if successful
     * @param user the user owning the preferences
     * @param preferences the newly locally created preferences object
     */
    private void savePreferences(ParseUser user, Preferences preferences) {
        // let user know preferences are being created
        informationTextview.setText(CREATING_PREFERENCES_PROMPT);

        preferences.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // let user know preferences is successfully created
                    informationTextview.setText(CREATING_PREFERENCES_SUCCESS_PROMPT);

                    // Once the preferences is saved, link it with user account, then move to the dashboard screen
                    linkPreferencesToUser(user, preferences);
                } else {
                    // Let user know something wrong happened
                    Toast.makeText(SignupBackendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Go back to login screen to let user re-login
                    moveToLoginScreen();
                }
            }
        });
    }

    /**
     * Handles linking the newly created user and preferences together
     * Moves to the dashboard screen once successful
     * @param user the user owning the preferences
     * @param preferences the newly saved preferences object within the database
     */
    private void linkPreferencesToUser(ParseUser user, Preferences preferences) {
        // let user know preferences is being linked with user account
        informationTextview.setText(LINKING_ACCOUNT_WITH_PREFERENCES_PROMPT);

        user.put(Preferences.KEY_PREFERENCES, preferences);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // let user know preferences was successfully linked with user account
                    informationTextview.setText(LINKING_ACCOUNT_WITH_PREFERENCES_SUCCESS_PROMPT);

                    // Fade out the loading animation and information text view
                    informationTextview.animate().alpha(0f).setDuration(FADE_OUT_ANIMATION_TIME);
                    loadingAnimation.animate().alpha(0f).setDuration(FADE_OUT_ANIMATION_TIME);

                    // Greet user after animation and information ext view have faded out
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            welcomeUser();
                        }
                    }, FADE_OUT_ANIMATION_TIME);
                } else {
                    // Let user know something wrong happened
                    Toast.makeText(SignupBackendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Go back to login screen to let user re-login
                    moveToLoginScreen();
                }
            }
        });
    }

    /**
     * Displays a welcome message to the user then fades in gently to the dashboard screen
     */
    private void welcomeUser() {
        // Fade in welcome message
        loadingAnimation.setVisibility(View.INVISIBLE);
        informationTextview.setText(NEW_USER_WELCOME_PROMPT);
        informationTextview.animate().alpha(1f).setDuration(FADE_IN_ANIMATION_TIME);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // move to dashboard screen after fadein finishes
                moveToDashboard();
            }
        }, FADE_IN_ANIMATION_TIME);
    }

    private void moveToDashboard() {
        Intent i = new Intent(SignupBackendActivity.this, DashboardActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish(); // to prevent moving back
    }

    private void moveToLoginScreen() {
        finish(); // to go back to user login screen and prevent moving back
    }
}
