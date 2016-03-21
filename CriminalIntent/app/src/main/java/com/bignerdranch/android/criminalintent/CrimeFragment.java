package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bignerdranch.android.criminalintent.model.CriminalIntentProtos.Crime;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private static String TAG = "CrimeFragment";
    private static String CRIME_STATE_TAG = "CrimeState";

    private Crime mCrime;
    private EditText mCrimeTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        initState(savedInstanceState);

        mCrimeTitle = (EditText) v.findViewById(R.id.crime_title);
        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime = Crime.newBuilder(mCrime).setTitle(s.toString()).build();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return v;
    }

    private void initState(Bundle savedInstanceState) {
        try {
            byte[] data = savedInstanceState.getByteArray(CRIME_STATE_TAG);
            mCrime = Crime.parseFrom(data);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't load state. Creating anew...", e);

            mCrime = Crime.newBuilder()
                    .setId(UUID.randomUUID().toString())
                    .setCreatedDate(new Date().getTime())
                    .setSolved(false)
                    .build();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");

        outState.putByteArray(CRIME_STATE_TAG, mCrime.toByteArray());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
