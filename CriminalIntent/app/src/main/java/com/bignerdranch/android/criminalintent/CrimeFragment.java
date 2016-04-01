package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.bignerdranch.android.criminalintent.models.CrimeLab;
import com.bignerdranch.android.criminalintent.models.raw.CriminalIntentProtos.Crime;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private static String TAG = "CrimeFragment";
    private static String CRIME_ID_EXTRA = "CRIME_STATE";

    private Crime.Builder mCrimeRef = null;
    private EditText mCrimeTitle;
    private CheckBox mSolvedCheckbox;
    private Button mDateButton;
    private CrimeLab mCrimeLab = CrimeLab.Instance;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID_EXTRA, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        mCrimeRef = loadCrime(savedInstanceState, getArguments());
    }

    private Crime.Builder loadCrime(Bundle... bundles) {
        Crime.Builder result;

        for (Bundle bundle : bundles) {
            result = loadFrom(bundle);
            if (result != null)
                return result;
        }

        Log.d(TAG, "Crime wasn't found. Adding new one");

        Crime.Builder newCrime = Crime.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setTitle("")
                .setCreatedDate(new Date().getTime())
                .setSolved(false);

        mCrimeLab.addCrime(newCrime);

        return newCrime;
    }

    @Nullable
    private Crime.Builder loadFrom(Bundle bundle) {
        if (bundle == null)
            return null;

        UUID crimeId = (UUID) bundle.getSerializable(CRIME_ID_EXTRA);
        return mCrimeLab.getCrimeById(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mCrimeTitle = (EditText) v.findViewById(R.id.crime_title);
        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.solved_checkbox);
        mDateButton = (Button) v.findViewById(R.id.crime_date_button);

        if (mCrimeRef == null) {
            mCrimeRef = loadCrime(savedInstanceState);
        }

        // set handlers
        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrimeRef.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrimeRef.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");


        // remember id for result
        Activity parent = getActivity();
        Intent resultData = new Intent();
        UUID crimeId = UUID.fromString(mCrimeRef.getId());
        resultData.putExtra(CRIME_ID_EXTRA, crimeId);
        parent.setResult(Activity.RESULT_OK, resultData);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        mCrimeTitle.setText(mCrimeRef.getTitle());
        mSolvedCheckbox.setChecked(mCrimeRef.getSolved());
        mDateButton.setText(new Date(mCrimeRef.getCreatedDate()).toString());
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

    @Nullable
    public static UUID getCrimeId(Intent i) {
        return (UUID)i.getSerializableExtra(CRIME_ID_EXTRA);
    }
}
