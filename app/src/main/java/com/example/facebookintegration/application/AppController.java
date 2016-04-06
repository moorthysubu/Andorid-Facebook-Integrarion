package com.example.facebookintegration.application;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by moorthy on 3/23/2016.
 */
public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
    }
}
