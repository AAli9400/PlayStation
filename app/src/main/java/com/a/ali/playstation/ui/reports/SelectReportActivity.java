package com.a.ali.playstation.ui.reports;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

public class SelectReportActivity extends AppCompatActivity {
    private ConstraintLayout mEmptyView;
    private ImageView mLoadingImageView;
    private AppLoadingViewUtil mLoadingViewUtil;

    private Date mReportDateFrom, mReportDateTo;
    private Calendar mReportCalenderFrom, mReportCalenderTo;

    private Spinner mShiftSpinner;
    private RadioGroup mReportTypeRadioGroup;

    private ImageView mOpenSpinnerImageView;
    private Spinner mCafeTypeSpinner;

    private RecyclerView mReportRecyclerView;

    private ConstraintLayout mReportDetailsConstraintLayout;

    private AppNetworkRepository mAppNetworkRepository;

    private PlayReportAdapter mPlayReportAdapter;
    private CafeReportAdapter mCafeReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_report);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAppNetworkRepository = AppNetworkRepository.getInstance(getApplication());

        mShiftSpinner = findViewById(R.id.s_shift);
        mReportTypeRadioGroup = findViewById(R.id.rg_report_type);

        mLoadingImageView = findViewById(R.id.iv_loading);
        mLoadingViewUtil = new AppLoadingViewUtil(this, mLoadingImageView);
        mLoadingImageView.setImageDrawable(mLoadingViewUtil.getDrawable());

        mEmptyView = findViewById(R.id.cl_empty_view);

        mReportRecyclerView = findViewById(R.id.recyclerview);
        mReportDetailsConstraintLayout = findViewById(R.id.cl_report_details);

        mCafeTypeSpinner = findViewById(R.id.s_cafe_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"mini", "cafe"});
        mCafeTypeSpinner.setAdapter(adapter);
        mOpenSpinnerImageView = findViewById(R.id.iv_open_spinner2);

        mReportTypeRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_cafe) {
                mCafeTypeSpinner.setVisibility(View.VISIBLE);
                mOpenSpinnerImageView.setVisibility(View.VISIBLE);
            } else if (i == R.id.rb_playstation) {
                mCafeTypeSpinner.setVisibility(View.GONE);
                mOpenSpinnerImageView.setVisibility(View.GONE);
            }
        });

        setupReportDates();

        loadShifts();

        MaterialButton displayButton = findViewById(R.id.mbtn_go);
        displayButton.setOnClickListener(view1 -> validateReportData());
    }

    private void loadShifts() {
        mAppNetworkRepository.getAllUsers().observe(this, users -> {
            if (users != null) {
                ArrayList<String> usernames = new ArrayList<>();
                usernames.add(getString(R.string.all));
                for (User user : users) {
                    usernames.add(user.getUserN());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usernames);
                mShiftSpinner.setAdapter(adapter);
            }
        });
    }

    private void setupReportDates() {
        TextInputLayout dateFromTextInputLayout = findViewById(R.id.til_from_date);
        View dateFromView = findViewById(R.id.view_from_date);
        dateFromView.setOnClickListener(view1 -> {
            mReportCalenderFrom = Calendar.getInstance();
            mReportCalenderFrom.setTimeInMillis(System.currentTimeMillis());
            new DatePickerDialog(
                    this,
                    R.style.Theme_MaterialComponents_Light_Dialog_Alert,
                    (datePicker, year, month, day) -> {
                        mReportCalenderFrom.set(Calendar.YEAR, year);
                        mReportCalenderFrom.set(Calendar.MONTH, month);
                        mReportCalenderFrom.set(Calendar.DAY_OF_MONTH, day);
                        new TimePickerDialog(
                                this,
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

        TextInputLayout dateToTextInputLayout = findViewById(R.id.til_to_date);
        View dateToView = findViewById(R.id.view_to_date);
        dateToView.setOnClickListener(view1 -> {
            mReportCalenderTo = Calendar.getInstance();
            mReportCalenderTo.setTimeInMillis(System.currentTimeMillis());
            new DatePickerDialog(
                    this,
                    R.style.Theme_MaterialComponents_Light_Dialog_Alert,
                    (datePicker, year, month, day) -> {
                        mReportCalenderTo.set(Calendar.YEAR, year);
                        mReportCalenderTo.set(Calendar.MONTH, month);
                        mReportCalenderTo.set(Calendar.DAY_OF_MONTH, day);
                        new TimePickerDialog(
                                this,
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
        String selectedCafeType = (String) mCafeTypeSpinner.getSelectedItem();
        int checkedReportTypeRadioButtonId = mReportTypeRadioGroup.getCheckedRadioButtonId();

        if (selectedShiftName == null) {
            Toast.makeText(this, getString(R.string.select_shift), Toast.LENGTH_SHORT).show();
        } else if (mReportDateFrom == null) {
            Toast.makeText(this, getString(R.string.date_from_required), Toast.LENGTH_LONG).show();
        } else if (mReportDateTo == null) {
            Toast.makeText(this, getString(R.string.date_to_required), Toast.LENGTH_LONG).show();
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
                        selectedCafeType,
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
