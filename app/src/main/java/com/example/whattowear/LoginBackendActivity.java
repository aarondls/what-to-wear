package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class LoginBackendActivity extends AppCompatActivity {
    public static final String IS_LOGGED_IN_KEY = "isLoggedIn";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";

    private static final String NULL_CLOTHING_RANKERS_PROMPT = "Fetched clothing rankers are null";
    private static final String LOGGING_IN_PROMPT = "Logging in";
    private static final String LOG_IN_SUCCESS_PROMPT = "Successfully logged in";
    private static final String INITIALIZING_PREFERENCES_PROMPT = "Initializing preferences";
    private static final String INITIALIZED_PREFERENCES_SUCCESS_PROMPT = "Successfully initialized preferences";
    private static final String INITIALIZING_RANKERS_PROMPT = "Initializing clothing ranking system";
    private static final String INITIALIZED_RANKERS_SUCCESS_PROMPT = "Successfully initialized clothing ranking system";

    private static final int FADE_OUT_ANIMATION_TIME = 1500;

    private TextView informationTextview;
    private LottieAnimationView loadingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_backend);

        // have layout be full screen to hide both the top and bottom bars
        FullscreenLayout.hideTopBottomBars(getWindow());

        informationTextview = findViewById(R.id.login_backend_information_textview);
        loadingAnimation = findViewById(R.id.login_backend_loading_animationview);

        Intent intent = getIntent();
        Boolean isLoggedIn = intent.getBooleanExtra(IS_LOGGED_IN_KEY, false);

        if (isLoggedIn) {
            // initialize preferences, which takes care of moving to the dashboard screen once successful
            initializePreferences();
            return;
        }

        // Since not logged in, log in now
        informationTextview.setText(LOGGING_IN_PROMPT);
        String username = intent.getStringExtra(USERNAME_KEY);
        String password = intent.getStringExtra(PASSWORD_KEY);

        // Start the login process
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (user != null) {
                // Let user know log in was successful
                informationTextview.setText(LOG_IN_SUCCESS_PROMPT);
                // load preferences from parse
                initializePreferences();
            } else {
                // Let user know something wrong happened
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // Go back to login screen to let user re-login
                moveToLoginScreen();
            }
        });
    }

    /**
     * Initializes user preferences (all the clothing rankers and associated factors)
     * of the currently logged in user
     * Moves to the dashboard screen once successful
     */
    private void initializePreferences() {
        // Let user know preferences is being initialized
        informationTextview.setText(INITIALIZING_PREFERENCES_PROMPT);

        Preferences preferences = (Preferences) ParseUser.getCurrentUser().getParseObject(Preferences.KEY_PREFERENCES);

        preferences.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // Let user know preferences was successfully initialized
                    informationTextview.setText(INITIALIZED_PREFERENCES_SUCCESS_PROMPT);

                    initializeClothingRankers((Preferences) object);
                } else {
                    // Let user know something wrong happened
                    Toast.makeText(LoginBackendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Go back to login screen to let user re-login
                    moveToLoginScreen();
                }
            }
        });
    }

    /**
     * Initializes the clothing rankers given the user preferences
     * Moves to the dashboard screen once successful
     *
     * @param preferences the preferences that the clothing rankers will be initialized from
     */
    private void initializeClothingRankers(Preferences preferences) {
        List<ClothingRanker> overBodyGarmentRankers = preferences.getList(Preferences.KEY_OVER_BODY_GARMENT_RANKERS);
        List<ClothingRanker> upperBodyGarmentRankers = preferences.getList(Preferences.KEY_UPPER_BODY_GARMENT_RANKERS);
        List<ClothingRanker> lowerBodyGarmentRankers = preferences.getList(Preferences.KEY_LOWER_BODY_GARMENT_RANKERS);
        List<ClothingRanker> footwearRankers = preferences.getList(Preferences.KEY_FOOTWEAR_RANKERS);
        List<ClothingRanker> accessoriesRankers = preferences.getList(Preferences.KEY_ACCESSORIES_RANKERS);

        if (overBodyGarmentRankers == null || upperBodyGarmentRankers == null || lowerBodyGarmentRankers == null || footwearRankers == null || accessoriesRankers == null) {
            // Let user know something wrong happened
            Toast.makeText(LoginBackendActivity.this, NULL_CLOTHING_RANKERS_PROMPT, Toast.LENGTH_SHORT).show();
            // Go back to login screen to let user re-login
            moveToLoginScreen();
            return;
        }

        // Collect all rankers to load them all together
        List<ClothingRanker> allRankers = new ArrayList<>();
        allRankers.addAll(overBodyGarmentRankers);
        allRankers.addAll(upperBodyGarmentRankers);
        allRankers.addAll(lowerBodyGarmentRankers);
        allRankers.addAll(footwearRankers);
        allRankers.addAll(accessoriesRankers);

        // Let user know clothing rankers are being initialized
        informationTextview.setText(INITIALIZING_RANKERS_PROMPT);

        ClothingRanker.fetchAllIfNeededInBackground(allRankers, new FindCallback<ClothingRanker>() {
            @Override
            public void done(List<ClothingRanker> objects, ParseException e) {
                if (e == null) {
                    // Let user know clothing rankers are successfully initialized
                    informationTextview.setText(INITIALIZED_RANKERS_SUCCESS_PROMPT);

                    // Set clothing rankers
                    OverBodyGarment.setOverBodyGarmentRankers(overBodyGarmentRankers);
                    UpperBodyGarment.setUpperBodyGarmentRankers(upperBodyGarmentRankers);
                    LowerBodyGarment.setLowerBodyGarmentRankers(lowerBodyGarmentRankers);
                    Footwear.setFootwearRankers(footwearRankers);
                    Accessories.setAccessoriesRankers(accessoriesRankers);

                    // Fade out the loading animation and information text view
                    loadingAnimation.animate().alpha(0f).setDuration(FADE_OUT_ANIMATION_TIME);
                    informationTextview.animate().alpha(0f).setDuration(FADE_OUT_ANIMATION_TIME);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // move to dashboard screen after fadeout finishes
                            moveToDashboard();
                        }
                    }, FADE_OUT_ANIMATION_TIME);
                } else {
                    // Let user know of the error
                    Toast.makeText(LoginBackendActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    // Go back to login screen to let user re-login
                    moveToLoginScreen();
                }
            }
        });
    }

    private void moveToDashboard() {
        Intent i = new Intent(LoginBackendActivity.this, DashboardActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish(); // to prevent moving back
    }

    private void moveToLoginScreen() {
        finish(); // to go back to user login screen without automatically logging in and preventing moving back
    }
}
