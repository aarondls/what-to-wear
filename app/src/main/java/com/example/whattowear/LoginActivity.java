package com.example.whattowear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

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

        // Automatically move to dashboard activity if logged in
        if (ParseUser.getCurrentUser() != null) {
            moveToDashboard();
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

    public void loginUser() {
        Log.i(TAG, "Attempting to login user");
        String username = usernameEdittext.getText().toString();
        String password = passwordEdittext.getText().toString();

        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (user != null) {
                Toast.makeText(this, "Successfully logged in.", Toast.LENGTH_SHORT).show();
                moveToDashboard();
            } else {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void moveToDashboard() {
        Intent i = new Intent(this, DashboardActivity.class);
        startActivity(i);
        finish(); // to prevent moving back
    }
}