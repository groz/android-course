package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.bignerdranch.android.criminalintent.model.CriminalIntentProtos.Crime;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private static String TAG = "CrimeFragment";
    private static String CRIME_STATE_TAG = "CrimeState";

    private Crime.Builder mCrime;
    private EditText mCrimeTitle;
    private CheckBox mSolvedCheckbox;
    private Button mDateButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mCrimeTitle = (EditText) v.findViewById(R.id.crime_title);
        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.solved_checkbox);
        mDateButton = (Button) v.findViewById(R.id.crime_date_button);

        initState(savedInstanceState);

        // set handlers
        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }

    private void initState(Bundle savedInstanceState) {
        try {
            byte[] data = savedInstanceState.getByteArray(CRIME_STATE_TAG);
            mCrime = Crime.parseFrom(data).toBuilder();
            Log.d(TAG, "Loaded saved state: " + mCrime.toString());
        } catch (Exception e) {
            Log.d(TAG, "Couldn't load state. Creating anew...", e);

            mCrime = Crime.newBuilder()
                    .setId(UUID.randomUUID().toString())
                    .setTitle("")
                    .setCreatedDate(new Date().getTime())
                    .setSolved(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");

        outState.putByteArray(CRIME_STATE_TAG, mCrime.build().toByteArray());
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


        mCrimeTitle.setText(mCrime.getTitle());
        mSolvedCheckbox.setChecked(mCrime.getSolved());
        mDateButton.setText(new Date(mCrime.getCreatedDate()).toString());
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
