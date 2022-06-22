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

    private EditText username_edittext;
    private EditText password_edittext;
    private Button login_button;
    private Button go_to_signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Automatically move to dashboard activity if logged in
        if (ParseUser.getCurrentUser() != null) {
            moveToDashboard();
        }

        username_edittext = findViewById(R.id.username_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        login_button = findViewById(R.id.login_button);
        go_to_signup_button = findViewById(R.id.go_to_signup_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        go_to_signup_button.setOnClickListener(new View.OnClickListener() {
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
        String username = username_edittext.getText().toString();
        String password = password_edittext.getText().toString();

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