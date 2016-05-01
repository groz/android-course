package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bignerdranch.android.criminalintent.models.CrimeLab;
import com.bignerdranch.android.criminalintent.models.raw.CriminalIntentProtos.Crime;

import org.joda.time.DateTime;

import java.util.UUID;

public class CrimeFragment extends Fragment {
    public final static int DELETE_ACTION_RESULT = 1414;

    private static final int DATE_UPDATE_REQUEST = 111;
    private static final int REQUEST_CONTACT = 222;
    private static final String DATE_DIALOG_TAG = "DATE_DIALOG_TAG";
    private static String TAG = "CrimeFragment";
    private static String CRIME_ID_EXTRA = "CRIME_STATE";

    private Crime.Builder mCrimeRef = null;
    private EditText mCrimeTitle;
    private CheckBox mSolvedCheckbox;
    private Button mDateButton;
    private Button mSuspectButton;
    private ImageButton mCallButton;
    private Button mReportButton;

    private CrimeLab mCrimeLab = CrimeLab.Instance;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID_EXTRA, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String generateCrimeReport() {
        String solvedString = getString(
                mCrimeRef.getSolved()
                        ? R.string.crime_report_solved
                        : R.string.crime_report_unsolved
        );

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrimeRef.getCreatedDate()).toString();

        String suspect = mCrimeRef.getSuspect();
        if (suspect == null || suspect.trim().isEmpty()) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        return getString(R.string.crime_report,
                mCrimeRef.getTitle(), dateString, solvedString, suspect);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        mCrimeRef = loadCrime(savedInstanceState, getArguments());

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                UUID crimeId = UUID.fromString(mCrimeRef.getId());
                mCrimeLab.deleteCrime(crimeId);
                setResult(DELETE_ACTION_RESULT);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Crime.Builder loadCrime(Bundle... bundles) {
        Crime.Builder result;

        for (Bundle bundle : bundles) {
            result = loadFrom(bundle);
            if (result != null)
                return result;
        }

        Log.d(TAG, "Crime wasn't found. Adding new one");

        Crime.Builder newCrime = Crime.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setTitle("")
                .setCreatedDate(DateTime.now().getMillis())
                .setSolved(false);

        mCrimeLab.addCrime(newCrime);

        return newCrime;
    }

    @Nullable
    private Crime.Builder loadFrom(Bundle bundle) {
        if (bundle == null)
            return null;

        UUID crimeId = (UUID) bundle.getSerializable(CRIME_ID_EXTRA);
        return mCrimeLab.getCrimeById(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mCrimeTitle = (EditText) v.findViewById(R.id.crime_title);
        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.solved_checkbox);
        mDateButton = (Button) v.findViewById(R.id.crime_date_button);

        if (mCrimeRef == null) {
            mCrimeRef = loadCrime(savedInstanceState);
        }

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime dt = new DateTime(mCrimeRef.getCreatedDate());
                DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(dt);
                dateDialogFragment.setTargetFragment(CrimeFragment.this, DATE_UPDATE_REQUEST);
                dateDialogFragment.show(getFragmentManager(), DATE_DIALOG_TAG);
            }
        });

        // set handlers
        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrimeRef.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                setResult();
            }
        });

        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrimeRef.setSolved(isChecked);
                setResult();
            }
        });

        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = ShareCompat.IntentBuilder.from(getActivity())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setText(generateCrimeReport())
                        .setType("text/plain")
                        .getIntent();

                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        mCallButton = (ImageButton)v.findViewById(R.id.crime_call);

        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent pickContact = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);

        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        String suspect = mCrimeRef.getSuspect();

        if (suspect != null && !suspect.trim().isEmpty()) {
            mSuspectButton.setText(suspect);
            mCallButton.setEnabled(true);
        } else {
            mCallButton.setEnabled(false);
        }

        PackageManager pm = getActivity().getPackageManager();

        if (pm.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, String.format("onActivityResult: %d, %d", requestCode, resultCode));

        if (requestCode == DATE_UPDATE_REQUEST && resultCode == Activity.RESULT_OK) {
            DateTime result = DateDialogFragment.extractTime(data);
            DateTime currentDateTime = new DateTime(mCrimeRef.getCreatedDate());
            DateTime newDateTime = result.withMillisOfDay(currentDateTime.getMillisOfDay());
            mCrimeRef.setCreatedDate(newDateTime.getMillis());
            setDateButtonText();
        }

        if (requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK) {
            Uri contactUri = data.getData();

            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            Cursor cursor = getActivity().getContentResolver().query(
                    contactUri, queryFields, null, null, null);

            if (cursor == null)
                return;

            try {
                if (cursor.getCount() == 0) {
                    return;
                }

                cursor.moveToFirst();

                String suspect = cursor.getString(0);
                mCrimeRef.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                cursor.close();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
    }

    private void setResult() {
        setResult(Activity.RESULT_OK);
    }

    private void setResult(int result) {
        Log.d(TAG, String.format("setResult: %s, %d", mCrimeRef.getId(), result));
        // remember id for result
        Activity parent = getActivity();
        Intent resultData = new Intent();
        UUID crimeId = UUID.fromString(mCrimeRef.getId());
        resultData.putExtra(CRIME_ID_EXTRA, crimeId);
        parent.setResult(result, resultData);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    private void setDateButtonText() {
        DateTime dt = new DateTime(mCrimeRef.getCreatedDate());
        mDateButton.setText(dt.toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        mCrimeTitle.setText(mCrimeRef.getTitle());
        mSolvedCheckbox.setChecked(mCrimeRef.getSolved());
        setDateButtonText();
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

    @Nullable
    public static UUID getCrimeId(Intent i) {
        return (UUID) i.getSerializableExtra(CRIME_ID_EXTRA);
    }
}
