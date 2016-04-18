package com.bignerdranch.android.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.bignerdranch.android.criminalintent.database.CrimeDbSchema.*;

public class CrimeBaseHelper extends SQLiteOpenHelper{
    private static final String TAG = "CrimeBaseHelper";
    private static final String DATABASE_NAME = "crimes";
    private static final int VERSION = 1;

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        Log.d(TAG, "ctor()");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating database");

        String cmd = String.format(
                "CREATE TABLE %s (_id integer primary key autoincrement, %s, %s, %s, %s)",
                CrimeTable.NAME,
                CrimeTable.Columns.UUID,
                CrimeTable.Columns.TITLE,
                CrimeTable.Columns.DATE,
                CrimeTable.Columns.SOLVED
        );

        db.execSQL(cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int current, int next) {

    }
}
