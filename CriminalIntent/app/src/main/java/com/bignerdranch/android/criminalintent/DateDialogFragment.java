package com.bignerdranch.android.criminalintent;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import org.joda.time.DateTime;

public class DateDialogFragment extends AppCompatDialogFragment {
    private static String TAG = "DateDialogFragment";
    private static String DATE_ARG = "DATE_ARG";

    public static DateDialogFragment newInstance(DateTime dateTime) {
        Bundle args = new Bundle();
        args.putSerializable(DATE_ARG, dateTime);

        DateDialogFragment dialogFragment = new DateDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dateDialogView = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.date_dialog, null);

        final DatePicker dp = (DatePicker) dateDialogView.findViewById(R.id.date_picker_view);

        DateTime dateTime = (DateTime) getArguments().getSerializable(DATE_ARG);

        if (dateTime == null) {
            throw new RuntimeException("Provide date to dialog");
        }

        dp.updateDate(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Choose date")
                .setView(dateDialogView)
                .setPositiveButton("Choose", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            DateTime dt = new DateTime(
                                    dp.getYear(),
                                    dp.getMonth(),
                                    dp.getDayOfMonth(),
                                    0, 0
                            );
                            Log.d(TAG, dt.toString());
                        }
                    }
                })
                .create();
    }
}
