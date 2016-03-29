package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bignerdranch.android.criminalintent.model.CriminalIntentProtos.Crime;
import com.bignerdranch.android.criminalintent.model.CriminalIntentProtos.CrimeLab;

import java.util.Date;
import java.util.UUID;

public class CrimeListFragment extends Fragment {
    private static String TAG = "CrimeListFragment";
    private static String CRIME_LAB_TAG = "CRIME_LAB";
    private static int EDIT_CRIME_REQUEST = 101;
    private CrimeLab.Builder mCrimeLab;
    private RecyclerView.Adapter<CrimeHolder> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View v = inflater.inflate(R.layout.fragment_crimelist, container, false);

        initState(savedInstanceState);

        RecyclerView crimeListRecyclerView = (RecyclerView) v.findViewById(R.id.crime_list);
        crimeListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new RecyclerView.Adapter<CrimeHolder>() {
            @Override
            public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return createCrimeViewHolder(parent, viewType);
            }

            @Override
            public void onBindViewHolder(CrimeHolder holder, int position) {
                populateViewHolder(holder, position);
            }

            @Override
            public int getItemCount() {
                return mCrimeLab.getCrimesCount();
            }
        };

        crimeListRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private CrimeHolder createCrimeViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.crime_list_item, parent, false);
        return new CrimeHolder(v);
    }

    private void populateViewHolder(CrimeHolder holder, int position) {
        Crime.Builder crime = mCrimeLab.getCrimesBuilder(position);
        holder.bindTo(crime);
    }

    private void initState(Bundle savedInstanceState) {
        try {
            byte[] data = savedInstanceState.getByteArray(CRIME_LAB_TAG);
            mCrimeLab = CrimeLab.parseFrom(data).toBuilder();
            Log.d(TAG, "Loaded saved state: " + mCrimeLab.toString());
        } catch (Exception e) {
            Log.d(TAG, "Couldn't load state. Creating anew...", e);

            mCrimeLab = CrimeLab.newBuilder();

            for (int i = 0; i < 100; ++i) {
                Crime.Builder crime = Crime.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setTitle("Crime # " + i)
                        .setSolved(i % 2 == 0)
                        .setCreatedDate(new Date().getTime());
                mCrimeLab.addCrimes(crime);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");

        outState.putByteArray(CRIME_LAB_TAG, mCrimeLab.build().toByteArray());
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

    private class CrimeHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckbox;
        private Crime.Builder mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crimelist_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crimelist_date);
            mSolvedCheckbox = (CheckBox) itemView.findViewById(R.id.crimelist_solved);
            mSolvedCheckbox.setOnCheckedChangeListener(this);
        }

        public void bindTo(Crime.Builder crime) {
            Log.d(TAG, "bindTo(" + crime.toString() + ")");
            mCrime = crime;

            mTitleTextView.setText(crime.getTitle());
            mDateTextView.setText(new Date(crime.getCreatedDate()).toString());
            mSolvedCheckbox.setChecked(crime.getSolved());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mCrime != null) {
                Log.d(TAG, "setSolved(" + mCrime.toString() + ", " + isChecked + ")");
                mCrime.setSolved(isChecked);
            }
        }

        @Override
        public void onClick(View v) {
            Intent i = CrimeActivity.newIntent(getActivity(), mCrime.build());
            CrimeListFragment.this.startActivityForResult(i, EDIT_CRIME_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, String.format("onActivityResult(%d, %d, %s)", requestCode, resultCode, data));

        if (requestCode == EDIT_CRIME_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Result found...");

            Crime crime = CrimeFragment.extractCrime(data);
            if (crime == null)
                return;

            int pos = 0;

            for (Crime.Builder builder : mCrimeLab.getCrimesBuilderList()) {
                if (builder.getId().equals(crime.getId())) {
                    builder.mergeFrom(crime);
                    break;
                }
                ++pos;
            }

            mAdapter.notifyItemChanged(pos);
        }
    }
}
