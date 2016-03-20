package criminalintent.android.bignerdranch.com.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class CriminalActivity extends FragmentActivity {
    private static String TAG = "CriminalActivity";
    private Fragment mCrimeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criminal);
        Log.d(TAG, "onCreate");

        FragmentManager fm = getSupportFragmentManager();
        mCrimeFragment = fm.findFragmentById(R.id.fragment_container);

        if (mCrimeFragment == null) {
            mCrimeFragment = new CrimeFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, new CrimeFragment())
                    .commit();
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
}
