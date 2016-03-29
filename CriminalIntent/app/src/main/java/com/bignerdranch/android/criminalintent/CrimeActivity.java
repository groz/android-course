package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.model.CriminalIntentProtos.Crime;
import com.google.protobuf.InvalidProtocolBufferException;

public class CrimeActivity extends SingleFragmentActivity {
    @Override
    protected String getLogTag() {
        return "CrimeActivity";
    }

    @Override
    protected CrimeFragment createFragment() {
        byte[] data = getIntent().getByteArrayExtra(CRIME_DATA_EXTRA);
        Crime crime = null;

        try {
            crime = (data != null) ? Crime.parseFrom(data) : null;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            // TODO: init crime or fail here?
        }

        return CrimeFragment.newInstance(crime);
    }

    private final static String CRIME_DATA_EXTRA = "CRIME_DATA_EXTRA";

    public static Intent newIntent(Context ctx, Crime crime) {
        Intent intent = new Intent(ctx, CrimeActivity.class);
        intent.putExtra(CRIME_DATA_EXTRA, crime.toByteArray());
        return intent;
    }
}
