package com.a.ali.playstation.ui.reports;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.a.ali.playstation.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelectReportFragment extends Fragment {
    private Date mReportDateFrom, mReportDateTo;
    private Calendar mReportCalenderFrom, mReportCalenderTo;
    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_report, container, false);

        mContext = getContext();

        setupReportDates(view);

        MaterialButton goButton = view.findViewById(R.id.mbtn_go);
        goButton.setOnClickListener(
                (Navigation.createNavigateOnClickListener(R.id.action_selectReport_to_reports, null)));

        return view;
    }

    private void setupReportDates(@NonNull View view) {
        TextInputLayout dateFromTextInputLayout = view.findViewById(R.id.til_from_date);
        View dateFromView = view.findViewById(R.id.view_from_date);
        dateFromView.setOnClickListener(view1 -> {
            mReportCalenderFrom = Calendar.getInstance();
            mReportCalenderFrom.setTimeInMillis(System.currentTimeMillis());
            new DatePickerDialog(
                    mContext,
                    (datePicker, year, month, day) -> {
                        mReportCalenderFrom.set(Calendar.YEAR, year);
                        mReportCalenderFrom.set(Calendar.MONTH, month);
                        mReportCalenderFrom.set(Calendar.DAY_OF_MONTH, day);
                        new TimePickerDialog(
                                mContext,
                                (timePicker, hour, minute) -> {
                                    mReportCalenderFrom.set(Calendar.HOUR, hour);
                                    mReportCalenderFrom.set(Calendar.MINUTE, minute);
                                    mReportDateFrom = new Date(mReportCalenderFrom.getTimeInMillis());

                                    dateFromTextInputLayout.getEditText().setText(
                                            new SimpleDateFormat("YYYY/MM/DD -- HH:mm")
                                                    .format(mReportCalenderFrom.getTimeInMillis()));
                                },
                                mReportCalenderFrom.get(Calendar.HOUR),
                                mReportCalenderFrom.get(Calendar.MINUTE),
                                false
                        ).show();
                    },
                    mReportCalenderFrom.get(Calendar.YEAR),
                    mReportCalenderFrom.get(Calendar.MONTH),
                    mReportCalenderFrom.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        TextInputLayout dateToTextInputLayout = view.findViewById(R.id.til_to_date);
        View dateToView = view.findViewById(R.id.view_to_date);
        dateToView.setOnClickListener(view1 -> {
            mReportCalenderTo = Calendar.getInstance();
            mReportCalenderTo.setTimeInMillis(System.currentTimeMillis());
            new DatePickerDialog(
                    mContext,
                    (datePicker, year, month, day) -> {
                        mReportCalenderTo.set(Calendar.YEAR, year);
                        mReportCalenderTo.set(Calendar.MONTH, month);
                        mReportCalenderTo.set(Calendar.DAY_OF_MONTH, day);
                        new TimePickerDialog(
                                mContext,
                                (timePicker, hour, minute) -> {
                                    mReportCalenderTo.set(Calendar.HOUR, hour);
                                    mReportCalenderTo.set(Calendar.MINUTE, minute);
                                    mReportDateTo = new Date(mReportCalenderTo.getTimeInMillis());

                                    dateToTextInputLayout.getEditText().setText(
                                            new SimpleDateFormat("YYYY/MM/DD -- HH:mm")
                                                    .format(mReportCalenderTo.getTimeInMillis()));
                                },
                                mReportCalenderTo.get(Calendar.HOUR),
                                mReportCalenderTo.get(Calendar.MINUTE),
                                false
                        ).show();
                    },
                    mReportCalenderTo.get(Calendar.YEAR),
                    mReportCalenderTo.get(Calendar.MONTH),
                    mReportCalenderTo.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }
}