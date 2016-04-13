package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bignerdranch.android.criminalintent.models.CrimeLab;
import com.bignerdranch.android.criminalintent.models.raw.CriminalIntentProtos;

import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private final static String TAG = "CrimePagerActivity";
    private final static String CRIME_ID_EXTRA = "CRIME_ID_EXTRA";
    private final CrimeLab mCrimeLab = CrimeLab.Instance;
    private ViewPager mViewPager;

    public static Intent newIntent(Context ctx, UUID crimeId) {
        Intent i = new Intent(ctx, CrimePagerActivity.class);
        i.putExtra(CRIME_ID_EXTRA, crimeId);
        return i;
    }

    public UUID getCrimeId() {
        return (UUID)getIntent().getSerializableExtra(CRIME_ID_EXTRA);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = getCrimeId();
        Log.d(TAG, String.format("crimeId: %s", crimeId));

        FragmentManager fm = getSupportFragmentManager();

        mViewPager = (ViewPager)findViewById(R.id.crime_pager_view_pager);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                CriminalIntentProtos.Crime.Builder crime = mCrimeLab.getCrime(position);

                if (crime == null) {
                    String msg = String.format("There's no crime #%d", position);
                    throw new IndexOutOfBoundsException(msg);
                }

                return CrimeFragment.newInstance(UUID.fromString(crime.getId()));
            }

            @Override
            public int getCount() {
                return mCrimeLab.getCrimesCount();
            }
        });

        int pos = mCrimeLab.getCrimePosById(crimeId);
        mViewPager.setCurrentItem(pos);
    }
}
