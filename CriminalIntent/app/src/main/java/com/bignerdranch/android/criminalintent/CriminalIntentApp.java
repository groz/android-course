package com.bignerdranch.android.criminalintent;

import android.app.Application;
import android.content.Context;

public class CriminalIntentApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    // possible race condition
    public static Context getContext() {
        return context;
    }
}
