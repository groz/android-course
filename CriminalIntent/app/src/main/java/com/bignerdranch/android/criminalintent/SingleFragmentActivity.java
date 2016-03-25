package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract String getLogTag();
    protected abstract Fragment createFragment();
    protected Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getLogTag(), "onCreate");

        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        mFragment = fm.findFragmentById(R.id.fragment_container);

        if (mFragment == null) {
            mFragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, new CrimeFragment())
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(getLogTag(), "onSaveInstanceState");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getLogTag(), "onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(getLogTag(), "onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(getLogTag(), "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getLogTag(), "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(getLogTag(), "onDestroy");
    }

}
