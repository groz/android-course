package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

public class CriminalActivity extends SingleFragmentActivity {
    @Override
    protected String getLogTag() {
        return "CriminalActivity";
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
