package com.bignerdranch.android.criminalintent.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bignerdranch.android.criminalintent.CriminalIntentApp;
import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCursor;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema;
import com.bignerdranch.android.criminalintent.models.raw.CriminalIntentProtos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static com.bignerdranch.android.criminalintent.database.CrimeDbSchema.*;
import static com.bignerdranch.android.criminalintent.models.raw.CriminalIntentProtos.*;

public class CrimeLab {
    private static final String TAG = "CrimeLab";
    private CriminalIntentProtos.CrimeLab.Builder mCrimeLabRaw;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public final static CrimeLab Instance = new CrimeLab();

    private CrimeLab() {
        Log.d(TAG, "ctor()");

        mCrimeLabRaw = CriminalIntentProtos.CrimeLab.newBuilder();
        mContext = CriminalIntentApp.getContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

        loadAllFromDatabase();
    }

    private ContentValues toContentValues(CrimeOrBuilder crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Columns.UUID, crime.getId());
        values.put(CrimeTable.Columns.TITLE, crime.getTitle());
        values.put(CrimeTable.Columns.DATE, crime.getCreatedDate());
        values.put(CrimeTable.Columns.SOLVED, crime.getSolved() ? 1 : 0);
        return values;
    }

    public void saveAll() {
        for (Crime crime : mCrimeLabRaw.getCrimesList()) {
            ContentValues values = toContentValues(crime);

            int updated = mDatabase.update(CrimeTable.NAME, values,
                    String.format("%s = ?", CrimeTable.Columns.UUID),
                    new String[] {crime.getId()});

            if (updated == 0) {
                Log.d(TAG, "inserting...");
                mDatabase.insert(CrimeTable.NAME, null, values);
            } else {
                Log.d(TAG, String.format("updated: %d", updated));
            }
        }
    }

    private void loadAllFromDatabase() {
        CrimeCursor cursor = new CrimeCursor(mDatabase.query(CrimeTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null
        ));

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Crime.Builder crime = cursor.getCrime();
                mCrimeLabRaw.addCrimes(crime);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    @Nullable
    public Crime.Builder getCrime(int index) {
        return index == -1 ? null : mCrimeLabRaw.getCrimesBuilder(index);
    }

    @Nullable
    public CriminalIntentProtos.Crime.Builder getCrimeById(UUID crimeId) {
        int pos = getCrimePosById(crimeId);
        return getCrime(pos);
    }

    public int getCrimePosById(UUID crimeId) {
        if (crimeId != null) {
            String id = crimeId.toString();
            List<CriminalIntentProtos.Crime.Builder> crimeList = mCrimeLabRaw.getCrimesBuilderList();

            for (int i = 0; i < crimeList.size(); ++i) {
                if (crimeList.get(i).getId().equals(id)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public int getCrimesCount() {
        return mCrimeLabRaw.getCrimesCount();
    }

    public void addCrime(Crime crime) {
        mCrimeLabRaw.addCrimes(crime);
    }

    public void addCrime(Crime.Builder crime) {
        mCrimeLabRaw.addCrimes(crime);
    }
}
