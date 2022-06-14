package com.example.whattowear;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Used to initialize Parse according to the API docs
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("JJ1EFtDnaScXZAutZWEb2YkUxVlB6YIgEhfjsb8r")
                .clientKey("UBgkN809q1uKPkuT1y9hGMLUJtKvmp0SObpLlAWA")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

}
