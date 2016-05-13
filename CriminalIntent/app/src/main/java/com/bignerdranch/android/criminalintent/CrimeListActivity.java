package com.bignerdranch.android.criminalintent;

import android.util.Log;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected String getLogTag() {
        return "CrimeListActivity";
    }

    @Override
    protected CrimeListFragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }
}
