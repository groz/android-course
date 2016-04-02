package com.bignerdranch.android.criminalintent;

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

import com.bignerdranch.android.criminalintent.models.CrimeLab;
import com.bignerdranch.android.criminalintent.models.raw.CriminalIntentProtos.Crime;

import java.util.Date;
import java.util.UUID;

public class CrimeListFragment extends Fragment {
    private static final int EDIT_CRIME_REQUEST = 101;

    private static String TAG = "CrimeListFragment";
    private CrimeLab mCrimeLab = CrimeLab.Instance;
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
        Crime.Builder crime = mCrimeLab.getCrime(position);
        holder.bindTo(crime);
    }

    private void initState(Bundle savedInstanceState) {
        if (mCrimeLab.getCrimesCount() == 0) {
            Log.d(TAG, "CrimeLab is empty. Filling with data");

            for (int i = 0; i < 100; ++i) {
                Crime.Builder crime = Crime.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setTitle("Crime # " + i)
                        .setSolved(i % 2 == 0)
                        .setCreatedDate(new Date().getTime());
                mCrimeLab.addCrime(crime);
            }
        } else {
            Log.d(TAG, "CrimeLab already filled out.");
        }
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
            Intent i = CrimePagerActivity.newIntent(getActivity(), UUID.fromString(mCrime.getId()));
            startActivityForResult(i, EDIT_CRIME_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");

        switch (requestCode) {
            case EDIT_CRIME_REQUEST:
                UUID editedId = CrimeFragment.getCrimeId(data);
                int pos = mCrimeLab.getCrimePosById(editedId);
                if (pos != -1) {
                    String msg = String.format("onActivityResult: updating item #%d...", pos);
                    Log.d(TAG, msg);
                    mAdapter.notifyItemChanged(pos);
                } else {
                    String msg = String.format("onActivityResult: updated item #%d not found", pos);
                    Log.d(TAG, msg);
                }
                break;
            default:
                String msg = String.format("Invalid request code: %d", requestCode);
                Log.e(TAG, "onActivityResult", new IllegalArgumentException(msg));
        }
    }
}
