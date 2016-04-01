package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    private final static String CRIME_ID_EXTRA = "CRIME_ID_EXTRA";

    @Override
    protected String getLogTag() {
        return "CrimeActivity";
    }

    @Override
    protected CrimeFragment createFragment() {
        UUID crimeId = (UUID)getIntent().getSerializableExtra(CRIME_ID_EXTRA);
        return CrimeFragment.newInstance(crimeId);
    }

    public static Intent newIntent(Context ctx, UUID crimeId) {
        Intent intent = new Intent(ctx, CrimeActivity.class);
        intent.putExtra(CRIME_ID_EXTRA, crimeId);
        return intent;
    }
}
