package com.bunny.weather.weago.utils;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class WeaGo extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
