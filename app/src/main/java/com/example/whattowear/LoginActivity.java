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
    private Button signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username_edittext = findViewById(R.id.username_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        login_button = findViewById(R.id.login_button);
        signup_button = findViewById(R.id.signup_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(username_edittext.getText().toString(), password_edittext.getText().toString());
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user");

        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (user != null) {
                Toast.makeText(this, "Successfully logged in.", Toast.LENGTH_SHORT).show();
                // Move to main activity
                moveToMainActivity();
            } else {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}