package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;

public class LoginUserActivity extends AppCompatActivity {
    private static final String TAG = "LoginUserActivity";

    private EditText usernameEdittext;
    private EditText passwordEdittext;
    private Button loginButton;
    private Button goToSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        // have layout be full screen to hide both the top and bottom bars
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Automatically move to LoginBackendActivity to initialize preferences if logged in
        if (ParseUser.getCurrentUser() != null) {
            Intent i = new Intent(LoginUserActivity.this, LoginBackendActivity.class);
            i.putExtra(LoginBackendActivity.IS_LOGGED_IN_KEY, true);
            startActivity(i);
        }

        usernameEdittext = findViewById(R.id.login_username_edittext);
        passwordEdittext = findViewById(R.id.login_password_edittext);
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
                Intent i = new Intent(LoginUserActivity.this, SignupUserActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * Handles logging in the user
     * Goes to the LoginBackendActivity which handles the interaction with the database to login
     */
    private void loginUser() {
        String username = usernameEdittext.getText().toString();
        String password = passwordEdittext.getText().toString();

        Intent i = new Intent(LoginUserActivity.this, LoginBackendActivity.class);
        i.putExtra(LoginBackendActivity.IS_LOGGED_IN_KEY, false);
        i.putExtra(LoginBackendActivity.USERNAME_KEY, username);
        i.putExtra(LoginBackendActivity.PASSWORD_KEY, password);
        startActivity(i);
    }
}