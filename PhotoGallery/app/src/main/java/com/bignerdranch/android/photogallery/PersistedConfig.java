package com.bignerdranch.android.photogallery;

import android.content.Context;
import android.preference.PreferenceManager;

public final class PersistedConfig {

    private static final String QUERY_STRING_KEY = "QueryString";

    public static String getQuery(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(QUERY_STRING_KEY, null);
    }

    public static void setQuery(Context ctx, String query) {
        PreferenceManager.getDefaultSharedPreferences(ctx)
                .edit()
                .putString(QUERY_STRING_KEY, query)
                .apply();
    }
}


