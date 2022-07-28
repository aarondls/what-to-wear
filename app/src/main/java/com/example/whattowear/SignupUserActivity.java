package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class SignupUserActivity extends AppCompatActivity {
    public static final String TAG = "SignupActivity";

    private EditText signupNameEdittext;
    private EditText signupUsernameEdittext;
    private EditText signupPasswordEdittext;
    private Button signupButton;
    private Button goToLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_user);

        // have layout be full screen to hide both the top and bottom bars
        FullscreenLayout.hideTopBottomBars(getWindow());

        signupNameEdittext = findViewById(R.id.signup_name_edittext);
        signupUsernameEdittext = findViewById(R.id.signup_username_edittext);
        signupPasswordEdittext = findViewById(R.id.signup_password_edittext);
        signupButton = findViewById(R.id.signup_button);
        goToLoginButton = findViewById(R.id.go_to_login_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupUser();
            }
        });

        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to login screen
                Intent i = new Intent(SignupUserActivity.this, LoginUserActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * Handles signing up the new user given the filled up text fields
     * Goes to the SignupBackendActivity which handles the interaction with the database
     */
    private void signupUser() {
        String username = signupUsernameEdittext.getText().toString();
        String password = signupPasswordEdittext.getText().toString();
        String userFullName = signupNameEdittext.getText().toString();

        Intent i = new Intent(SignupUserActivity.this, SignupBackendActivity.class);
        i.putExtra(SignupBackendActivity.USERNAME_KEY, username);
        i.putExtra(SignupBackendActivity.PASSWORD_KEY, password);
        i.putExtra(SignupBackendActivity.USER_FULL_NAME_KEY, userFullName);
        startActivity(i);
    }
}
