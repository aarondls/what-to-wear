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

    private EditText signup_name_edittext;
    private EditText signup_username_edittext;
    private EditText signup_password_edittext;
    private Button signup_button;
    private Button go_to_login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_name_edittext = findViewById(R.id.signup_name_edittext);
        signup_username_edittext = findViewById(R.id.signup_username_edittext);
        signup_password_edittext = findViewById(R.id.signup_password_edittext);
        signup_button = findViewById(R.id.signup_button);
        go_to_login_button = findViewById(R.id.go_to_login_button);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        go_to_login_button.setOnClickListener(new View.OnClickListener() {
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
        user.setUsername(signup_username_edittext.getText().toString());
        user.setPassword(signup_password_edittext.getText().toString());
        user.put("name", signup_name_edittext.getText().toString());

        user.signUpInBackground(e -> {
            if (e == null) {
                Toast.makeText(this, "Successfully created new account!", Toast.LENGTH_SHORT).show();

                // Move to main activity
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish(); // to prevent moving back
            } else {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}