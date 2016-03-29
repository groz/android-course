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

import com.bignerdranch.android.criminalintent.model.CriminalIntentProtos.Crime;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private static String TAG = "CrimeFragment";
    private static String CRIME_STATE_TAG = "CRIME_STATE";

    private Crime.Builder mCrime = null;
    private EditText mCrimeTitle;
    private CheckBox mSolvedCheckbox;
    private Button mDateButton;

    public static CrimeFragment newInstance(Crime crime) {
        Bundle args = new Bundle();
        args.putByteArray(CRIME_STATE_TAG, crime.toByteArray());

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        mCrime = loadCrime(savedInstanceState, getArguments());
    }

    private static Crime.Builder loadCrime(Bundle... bundles) {
        Crime.Builder result;

        for (Bundle bundle : bundles) {
            result = loadFrom(bundle);
            if (result != null)
                return result;
        }

        return Crime.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setTitle("")
                .setCreatedDate(new Date().getTime())
                .setSolved(false);
    }

    @Nullable
    private static Crime.Builder loadFrom(Bundle bundle) {
        if (bundle == null)
            return null;

        Crime.Builder result = null;

        byte[] data = bundle.getByteArray(CRIME_STATE_TAG);

        if (data != null) {
            try {
                result = Crime.parseFrom(data).toBuilder();
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mCrimeTitle = (EditText) v.findViewById(R.id.crime_title);
        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.solved_checkbox);
        mDateButton = (Button) v.findViewById(R.id.crime_date_button);

        if (mCrime == null) {
            mCrime = loadCrime(savedInstanceState);
        }

        // set handlers
        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                saveResult();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                saveResult();
            }
        });

        return v;
    }

    private void saveResult() {
        // TODO: so ugly, determine a better way
        Intent resultData = new Intent();
        resultData.putExtra(CRIME_STATE_TAG, mCrime.build().toByteArray());

        Activity parent = getActivity();
        if (parent != null) {
            parent.setResult(Activity.RESULT_OK, resultData);
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

    @Nullable
    public static Crime extractCrime(Intent i) {
        if (i == null)
            return null;

        byte[] data = i.getByteArrayExtra(CRIME_STATE_TAG);

        try {
            return Crime.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
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
