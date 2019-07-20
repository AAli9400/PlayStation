package com.a.ali.playstation.ui.reports;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.model.User;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.reports.adapter.CafeReportAdapter;
import com.a.ali.playstation.ui.reports.adapter.PlayReportAdapter;
import com.a.ali.playstation.ui.util.AppLoadingViewUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SelectReportFragment extends Fragment {
    private ConstraintLayout mEmptyView;
    private ImageView mLoadingImageView;
    private AppLoadingViewUtil mLoadingViewUtil;

    private Date mReportDateFrom, mReportDateTo;
    private Calendar mReportCalenderFrom, mReportCalenderTo;

    private Spinner mShiftSpinner;
    private RadioGroup mReportTypeRadioGroup;

    private RecyclerView mReportRecyclerView;

    private ConstraintLayout mReportDetailsConstraintLayout;

    private Context mContext;
    private AppNetworkRepository mAppNetworkRepository;

    private PlayReportAdapter mPlayReportAdapter;
    private CafeReportAdapter mCafeReportAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_report, container, false);

        mContext = getContext();
        mAppNetworkRepository = AppNetworkRepository.getInstance(getActivity().getApplication());

        mShiftSpinner = view.findViewById(R.id.s_shift);
        mReportTypeRadioGroup = view.findViewById(R.id.rg_report_type);

        mLoadingImageView = view.findViewById(R.id.iv_loading);
        mLoadingViewUtil = new AppLoadingViewUtil(mContext, mLoadingImageView);
        mLoadingImageView.setImageDrawable(mLoadingViewUtil.getDrawable());

        mEmptyView = view.findViewById(R.id.cl_empty_view);

        mReportRecyclerView = view.findViewById(R.id.recyclerview);
        mReportDetailsConstraintLayout = view.findViewById(R.id.cl_report_details);

        setupReportDates(view);

        loadShifts();

        MaterialButton displayButton = view.findViewById(R.id.mbtn_go);
        displayButton.setOnClickListener(view1 -> validateReportData());

        return view;
    }

    private void loadShifts() {
        mAppNetworkRepository.getAllUsers().observe(this, users -> {
            if (users != null) {
                ArrayList<String> usernames = new ArrayList<>();
                usernames.add(getString(R.string.all));
                for (User user : users) {
                    usernames.add(user.getUserN());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, usernames);
                mShiftSpinner.setAdapter(adapter);
            }
        });
    }

    private void setupReportDates(@NonNull View view) {
        TextInputLayout dateFromTextInputLayout = view.findViewById(R.id.til_from_date);
        View dateFromView = view.findViewById(R.id.view_from_date);
        dateFromView.setOnClickListener(view1 -> {
            mReportCalenderFrom = Calendar.getInstance();
            mReportCalenderFrom.setTimeInMillis(System.currentTimeMillis());
            new DatePickerDialog(
                    mContext,
                    R.style.Theme_MaterialComponents_Light_Dialog_Alert,
                    (datePicker, year, month, day) -> {
                        mReportCalenderFrom.set(Calendar.YEAR, year);
                        mReportCalenderFrom.set(Calendar.MONTH, month);
                        mReportCalenderFrom.set(Calendar.DAY_OF_MONTH, day);
                        new TimePickerDialog(
                                mContext,
                                R.style.Theme_MaterialComponents_Light_Dialog_Alert,
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
                    R.style.Theme_MaterialComponents_Light_Dialog_Alert,
                    (datePicker, year, month, day) -> {
                        mReportCalenderTo.set(Calendar.YEAR, year);
                        mReportCalenderTo.set(Calendar.MONTH, month);
                        mReportCalenderTo.set(Calendar.DAY_OF_MONTH, day);
                        new TimePickerDialog(
                                mContext,
                                R.style.Theme_MaterialComponents_Light_Dialog_Alert,
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

    private void validateReportData() {
        String selectedShiftName = (String) mShiftSpinner.getSelectedItem();
        int checkedReportTypeRadioButtonId = mReportTypeRadioGroup.getCheckedRadioButtonId();

        if (selectedShiftName == null) {
            Toast.makeText(mContext, getString(R.string.select_shift), Toast.LENGTH_SHORT).show();
        } else if (mReportDateFrom == null) {
            Toast.makeText(mContext, getString(R.string.date_from_required), Toast.LENGTH_LONG).show();
        } else if (mReportDateTo == null) {
            Toast.makeText(mContext, getString(R.string.date_to_required), Toast.LENGTH_LONG).show();
        } else {
            mLoadingViewUtil.show();

            mReportDetailsConstraintLayout.setVisibility(View.GONE);
            mReportRecyclerView.setVisibility(View.VISIBLE);

            if (selectedShiftName.equals(getString(R.string.all))) {
                selectedShiftName = "allall";
            }

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmaa");

            String strDateString = sdfDate.format(mReportDateFrom.getTime());
            String startDate = strDateString.substring(0, 8);
            String startHour = strDateString.substring(8, 10);
            String startMinute = strDateString.substring(10, 12);
            String amPmStart = strDateString.substring(12, 14);

            String endDateString = sdfDate.format(mReportDateTo.getTime());
            String endDate = endDateString.substring(0, 8);
            String endHour = endDateString.substring(8, 10);
            String endMinute = endDateString.substring(10, 12);
            String amPmEnd = endDateString.substring(12, 14);

            if (checkedReportTypeRadioButtonId == R.id.rb_playstation) {
                mAppNetworkRepository.playReport(
                        selectedShiftName,
                        startDate,
                        startHour,
                        startMinute,
                        amPmStart,
                        endDate,
                        endHour,
                        endMinute,
                        amPmEnd
                ).observe(this, playReports -> {
                    if (playReports != null) {
                        mPlayReportAdapter = new PlayReportAdapter();
                        mReportRecyclerView.setAdapter(mPlayReportAdapter);

                        mPlayReportAdapter.swapData(playReports);

                        if (playReports.isEmpty()) {
                            mEmptyView.setVisibility(View.VISIBLE);
                        } else {
                            mEmptyView.setVisibility(View.GONE);
                        }
                    } else {
                        mEmptyView.setVisibility(View.VISIBLE);
                    }

                    mLoadingViewUtil.hide();
                });
            } else if (checkedReportTypeRadioButtonId == R.id.rb_cafe) {
                mAppNetworkRepository.cafeReport(
                        selectedShiftName,
                        startDate,
                        startHour,
                        startMinute,
                        amPmStart,
                        endDate,
                        endHour,
                        endMinute,
                        amPmEnd
                ).observe(this, cafeReports -> {
                    if (cafeReports != null) {
                        mCafeReportAdapter = new CafeReportAdapter();
                        mReportRecyclerView.setAdapter(mCafeReportAdapter);

                        mCafeReportAdapter.swapData(cafeReports);

                        if (cafeReports.isEmpty()) {
                            mEmptyView.setVisibility(View.VISIBLE);
                        } else {
                            mEmptyView.setVisibility(View.GONE);
                        }
                    } else {
                        mEmptyView.setVisibility(View.VISIBLE);
                    }
                    mLoadingViewUtil.hide();
                });
            }
        }
    }
}