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

public class SignupActivity extends AppCompatActivity {
    public static final String TAG = "SignupActivity";

    private EditText signupNameEdittext;
    private EditText signupUsernameEdittext;
    private EditText signupPasswordEdittext;
    private Button signupButton;
    private Button goToLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    public void createUser() {
        Log.i(TAG, "Attempting to create new account");
        ParseUser user = new ParseUser();
        user.setUsername(signupUsernameEdittext.getText().toString());
        user.setPassword(signupPasswordEdittext.getText().toString());
        user.put("name", signupNameEdittext.getText().toString());

        user.signUpInBackground(e -> {
            if (e == null) {
                Toast.makeText(this, "Successfully created new account!", Toast.LENGTH_SHORT).show();

                // Move to dashboard activity
                Intent i = new Intent(this, DashboardActivity.class);
                startActivity(i);
                finish(); // to prevent moving back
            } else {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}