package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class SignupActivity extends AppCompatActivity {
    public static final String TAG = "SignupActivity";

    private static final String KEY_NAME = "name";

    private EditText signupNameEdittext;
    private EditText signupUsernameEdittext;
    private EditText signupPasswordEdittext;
    private Button signupButton;
    private Button goToLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // have layout be full screen to hide both the top and bottom bars
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        signupNameEdittext = findViewById(R.id.signup_name_edittext);
        signupUsernameEdittext = findViewById(R.id.signup_username_edittext);
        signupPasswordEdittext = findViewById(R.id.signup_password_edittext);
        signupButton = findViewById(R.id.signup_button);
        goToLoginButton = findViewById(R.id.go_to_login_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to login screen
                Intent i = new Intent(SignupActivity.this, LoginUserActivity.class);
                startActivity(i);
            }
        });
    }

    // TODO: as a stretch goal, indicate loading screen while waiting for all these
    /**
     * Handles creating the new user profile, along with their new preferences by calling createUserPreferences
     * once the new user profile is successfully made
     * Leads to the dashboard screen if everything is successful with Parse through createUserPreferences
     */
    public void createUser() {
        Log.i(TAG, "Attempting to create new account");
        ParseUser user = new ParseUser();
        user.setUsername(signupUsernameEdittext.getText().toString());
        user.setPassword(signupPasswordEdittext.getText().toString());
        user.put(KEY_NAME, signupNameEdittext.getText().toString());

        user.signUpInBackground(e -> {
            if (e == null) {
                // user successfully created here, but not fully yet (missing preferences)
                // add preferences
                createUserPreferences(user);
            } else {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // TODO: stretch feature: more robust signup error handling instead of simply letting user resignup maybe do something else
    /**
     * Handles creating the new user preferences locally
     * Calls savePreferences once successful to upload to Parse, which leads to the dashboard screen if successful
     * @param user the newly created user that requires new preferences
     */
    public void createUserPreferences(ParseUser user) {
        // create all the rankers first
        List<ClothingRanker> allGarmentRankers = new ArrayList<>();
        allGarmentRankers.addAll(OverBodyGarment.initializeOverBodyGarmentRankers(user));
        allGarmentRankers.addAll(UpperBodyGarment.initializeUpperBodyGarmentRankers(user));
        allGarmentRankers.addAll(LowerBodyGarment.initializeLowerBodyGarmentRankers(user));
        allGarmentRankers.addAll(Footwear.initializeFootwearRankers(user));
        allGarmentRankers.addAll(Accessories.initializeAccessoriesRankers(user));

        ClothingRanker.saveAllInBackground(allGarmentRankers, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
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
                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void savePreferences(ParseUser user, Preferences preferences) {
        preferences.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // Once the preferences is saved, link it with user account, then move to the dashboard screen
                if (e == null) {
                    linkPreferencesToUser(user, preferences);
                } else {
                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Handles linking the newly created user and preferences together
     * Moves to the dashboard screen once successful
     * @param user
     * @param preferences
     */
    public void linkPreferencesToUser(ParseUser user, Preferences preferences) {
        user.put(Preferences.KEY_PREFERENCES, preferences);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(SignupActivity.this, "Successfully created new account!", Toast.LENGTH_SHORT).show();

                    // Move to dashboard activity
                    Intent i = new Intent(SignupActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish(); // to prevent moving back
                } else {
                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}