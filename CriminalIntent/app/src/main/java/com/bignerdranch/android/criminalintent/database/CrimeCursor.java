package com.bignerdranch.android.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import static com.bignerdranch.android.criminalintent.models.raw.CriminalIntentProtos.Crime;

public class CrimeCursor extends CursorWrapper {

    public CrimeCursor(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuid = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Columns.UUID));
        String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Columns.TITLE));
        long date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Columns.DATE));
        boolean solved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Columns.SOLVED)) == 1;

        return Crime.newBuilder()
                .setId(uuid)
                .setTitle(title)
                .setCreatedDate(date)
                .setSolved(solved)
                .build();
    }

}
