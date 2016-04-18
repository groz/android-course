package com.bignerdranch.android.criminalintent;

import android.app.Application;
import android.content.Context;

public class CriminalIntentApp extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
