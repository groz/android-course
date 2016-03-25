package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected String getLogTag() {
        return "CrimeListActivity";
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
