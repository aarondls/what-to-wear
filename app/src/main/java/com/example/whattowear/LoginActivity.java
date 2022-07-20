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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    private EditText usernameEdittext;
    private EditText passwordEdittext;
    private Button loginButton;
    private Button goToSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // have layout be full screen to hide both the top and bottom bars
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Automatically move to dashboard activity if logged in
        if (ParseUser.getCurrentUser() != null) {
            // initialize preferences, which takes care of moving to the dashboard screen once successful
            initializePreferences();
        }

        usernameEdittext = findViewById(R.id.username_edittext);
        passwordEdittext = findViewById(R.id.password_edittext);
        loginButton = findViewById(R.id.login_button);
        goToSignupButton = findViewById(R.id.go_to_signup_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        goToSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to signup screen
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * Handles logging in the user
     * Calls initializePreferences after user is logged in, which moves to the dashboard screen
     */
    public void loginUser() {
        Log.i(TAG, "Attempting to login user");
        String username = usernameEdittext.getText().toString();
        String password = passwordEdittext.getText().toString();

        // TODO: as a stretch goal, the loading screen can be added here, which contains the loginbackground (and all following methods) since this takes a while

        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (user != null) {
                // load preferences from parse
                initializePreferences();
            } else {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // TODO: stretch goal to add loading activity screen for this
    /**
     * Initializes user preferences (all the clothing rankers and associated factors)
     * of the currently logged in user
     * Moves to the dashboard screen once successful
     */
    public void initializePreferences() {
        Preferences preferences = (Preferences) ParseUser.getCurrentUser().getParseObject(Preferences.KEY_PREFERENCES);

        preferences.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                Preferences newPreferences = (Preferences) object;
                // make sure all lists are not null by enclosing in try catch
                try {
                    List<ClothingRanker> overBodyGarmentRankers = newPreferences.getList(Preferences.KEY_OVER_BODY_GARMENT_RANKERS);
                    List<ClothingRanker> upperBodyGarmentRankers = newPreferences.getList(Preferences.KEY_UPPER_BODY_GARMENT_RANKERS);
                    List<ClothingRanker> lowerBodyGarmentRankers = newPreferences.getList(Preferences.KEY_LOWER_BODY_GARMENT_RANKERS);
                    List<ClothingRanker> footwearRankers = newPreferences.getList(Preferences.KEY_FOOTWEAR_RANKERS);
                    List<ClothingRanker> accessoriesRankers = newPreferences.getList(Preferences.KEY_ACCESSORIES_RANKERS);

                    if (overBodyGarmentRankers == null || upperBodyGarmentRankers == null || lowerBodyGarmentRankers == null || footwearRankers == null || accessoriesRankers == null) {
                        throw new NullPointerException(); // this will be caught below along with the parse errors
                    }

                    OverBodyGarment.setOverBodyGarmentRankers(overBodyGarmentRankers);
                    UpperBodyGarment.setUpperBodyGarmentRankers(upperBodyGarmentRankers);
                    LowerBodyGarment.setLowerBodyGarmentRankers(lowerBodyGarmentRankers);
                    Footwear.setFootwearRankers(footwearRankers);
                    Accessories.setAccessoriesRankers(accessoriesRankers);
                } catch (ParseException | NullPointerException ex) {
                    // Let user know of the error
                    Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                    // end function, so the screen remains in login activity and the user can re-login
                    return;
                }

                // move to login screen
                Toast.makeText(LoginActivity.this, "Successfully logged in.", Toast.LENGTH_SHORT).show();
                moveToDashboard();
            }
        });
    }

    void moveToDashboard() {
        Intent i = new Intent(this, DashboardActivity.class);
        startActivity(i);
        finish(); // to prevent moving back
    }
}