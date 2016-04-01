package com.bignerdranch.android.criminalintent.models;

import android.support.annotation.Nullable;

import com.bignerdranch.android.criminalintent.models.raw.CriminalIntentProtos;

import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private CriminalIntentProtos.CrimeLab.Builder mCrimeLabRaw;

    public final static CrimeLab Instance = new CrimeLab();

    private CrimeLab() {
        mCrimeLabRaw = CriminalIntentProtos.CrimeLab.newBuilder();
    }

    @Nullable
    public CriminalIntentProtos.Crime.Builder getCrime(int index) {
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

    public void addCrime(CriminalIntentProtos.Crime crime) {
        mCrimeLabRaw.addCrimes(crime);
    }

    public void addCrime(CriminalIntentProtos.Crime.Builder crime) {
        mCrimeLabRaw.addCrimes(crime);
    }
}
