package com.bignerdranch.android.criminalintent;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected String getLogTag() {
        return "CrimeListActivity";
    }

    @Override
    protected CrimeListFragment createFragment() {
        return new CrimeListFragment();
    }
}
