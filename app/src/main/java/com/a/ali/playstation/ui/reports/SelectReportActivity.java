package com.a.ali.playstation.ui.reports;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.model.CafeReport;
import com.a.ali.playstation.data.model.OutsReport;
import com.a.ali.playstation.data.model.PlayReport;
import com.a.ali.playstation.data.model.User;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.reports.adapter.CafeReportAdapter;
import com.a.ali.playstation.ui.reports.adapter.OutsReportAdapter;
import com.a.ali.playstation.ui.reports.adapter.PlayReportAdapter;
import com.a.ali.playstation.ui.reports.adapter.SummaryReportAdapter;
import com.a.ali.playstation.ui.reports.pdf.ITextGUtil;
import com.a.ali.playstation.ui.util.AppLoadingViewUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SelectReportActivity extends AppCompatActivity implements ITextGUtil.Helper2 {
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
    private SummaryReportAdapter mSummaryReportAdapter;
    private OutsReportAdapter mOutsReportAdapter;

    private MaterialButton mPDFMaterialButton;

    private boolean mIsReportDataOn = false;

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
            } else {
                mCafeTypeSpinner.setVisibility(View.GONE);
                mOpenSpinnerImageView.setVisibility(View.GONE);
            }
        });

        setupReportDates();

        loadShifts();

        MaterialButton displayButton = findViewById(R.id.mbtn_go);
        displayButton.setOnClickListener(view1 -> validateReportData());

        mPDFMaterialButton = findViewById(R.id.mbtn_pdf);
        mPDFMaterialButton.setOnClickListener(view -> {
            mPDFMaterialButton.setEnabled(false);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            } else generatePDFFile();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generatePDFFile();
            }
        }
    }

    private void generatePDFFile() {
        ITextGUtil.createPdfFile(
                mReportTypeRadioGroup.getCheckedRadioButtonId() ==
                        R.id.rb_cafe ? 8 : 7,
                getApplication(),
                mReportTypeRadioGroup.getCheckedRadioButtonId() ==
                        R.id.rb_cafe ? new CafeReportHelper() : mReportTypeRadioGroup.getCheckedRadioButtonId() ==
                        R.id.rb_outs ? new OutsReportHelper() : new PlayReportHelper()
                , this);
    }

    private void loadShifts() {
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add(getString(R.string.all));
        mAppNetworkRepository.getAllUsers().observe(this, users -> {
            if (users != null) {
                for (User user : users) {
                    usernames.add(user.getUserN());
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usernames);
            mShiftSpinner.setAdapter(adapter);
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
                                            new SimpleDateFormat("YYYY/MM/dd -- HH:mm")
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
                                            new SimpleDateFormat("YYYY/MM/dd -- HH:mm")
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
            mIsReportDataOn = true;
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
                         mPDFMaterialButton.setVisibility(View.VISIBLE);

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
                        mPDFMaterialButton.setVisibility(View.VISIBLE);

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
            } else if (checkedReportTypeRadioButtonId == R.id.rb_summary) {
                mAppNetworkRepository.summaryReport(
                        selectedShiftName,
                        startDate,
                        startHour,
                        startMinute,
                        amPmStart,
                        endDate,
                        endHour,
                        endMinute,
                        amPmEnd
                ).observe(this, summaryReports -> {
                    mPDFMaterialButton.setVisibility(View.INVISIBLE);

                    if (summaryReports != null) {
                        mSummaryReportAdapter = new SummaryReportAdapter();
                        mReportRecyclerView.setAdapter(mSummaryReportAdapter);

                        mSummaryReportAdapter.swapData(summaryReports);

                        if (summaryReports.isEmpty()) {
                            mEmptyView.setVisibility(View.VISIBLE);
                        } else {
                            mEmptyView.setVisibility(View.GONE);
                        }
                    } else {
                        mEmptyView.setVisibility(View.VISIBLE);
                    }
                    mLoadingViewUtil.hide();
                });
            } else if (checkedReportTypeRadioButtonId == R.id.rb_outs) {
                mAppNetworkRepository.outsReport(
                        selectedShiftName,
                        startDate,
                        startHour,
                        startMinute,
                        amPmStart,
                        endDate,
                        endHour,
                        endMinute,
                        amPmEnd
                ).observe(this, outsReports -> {
                    if (outsReports != null) {
                        mPDFMaterialButton.setVisibility(View.VISIBLE);

                        mOutsReportAdapter = new OutsReportAdapter();
                        mReportRecyclerView.setAdapter(mOutsReportAdapter);

                        mOutsReportAdapter.swapData(outsReports);

                        if (outsReports.isEmpty()) {
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mIsReportDataOn) {
            mReportDetailsConstraintLayout.setVisibility(View.VISIBLE);
            mReportRecyclerView.setVisibility(View.GONE);

            mReportRecyclerView.setAdapter(null);

            mCafeReportAdapter = null;
            mPlayReportAdapter = null;
            mSummaryReportAdapter = null;

            mEmptyView.setVisibility(View.GONE);
            mPDFMaterialButton.setVisibility(View.GONE);

            mIsReportDataOn = false;
        } else super.onBackPressed();
    }

    @Override
    public void openFile(@NonNull File file) {
        Intent target = new Intent(Intent.ACTION_VIEW);

        String authority = getPackageName() + ".fileprovider";
        target.setDataAndType(FileProvider.getUriForFile(
                this,
                authority,
                file), "application/pdf");

        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Exceptionn", e.getMessage());
        }

        mPDFMaterialButton.setEnabled(true);
    }

    private class PlayReportHelper implements ITextGUtil.Helper {

        @Override
        public void setData(PdfPTable table, Font arialFont) {
            if (mPlayReportAdapter != null) {
                List<PlayReport> playReports = mPlayReportAdapter.getData();
                if (playReports != null) {
                    Double totalPriceBeforeDiscount = 0.0;
                    Double totalPrice = 0.0;
                    for (PlayReport report : playReports) {
                        table.addCell(new PdfPCell(new Paragraph(report.getPs(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getStart_date(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getFinish_date(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getSingle_multi(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getPreDiscount(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getBillCash(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getShift_name(), arialFont)));

                        totalPriceBeforeDiscount += Double.valueOf(report.getPreDiscount());
                        totalPrice += Double.valueOf(report.getBillCash());
                    }

                    table.addCell(new PdfPCell(new Paragraph("الإجمالي", arialFont)));
                    table.addCell(new PdfPCell(new Paragraph("", arialFont)));
                    table.addCell(new PdfPCell(new Paragraph("", arialFont)));
                    table.addCell(new PdfPCell(new Paragraph("", arialFont)));
                    table.addCell(new PdfPCell(new Paragraph(String.valueOf(totalPriceBeforeDiscount), arialFont)));
                    table.addCell(new PdfPCell(new Paragraph(String.valueOf(totalPrice), arialFont)));
                    table.addCell(new PdfPCell(new Paragraph("", arialFont)));
                }
            }
        }

        @Override
        public boolean setHeader(PdfPTable table, Font arialFont) {
            table.addCell(new PdfPCell(new Paragraph("جهاز", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("وقت البداية", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("وقت النهاية", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("نوع اللعب", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("قبل الخصم", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("نهائي", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("شيفت", arialFont)));
            return true;
        }
    }

    private class CafeReportHelper implements ITextGUtil.Helper {

        @Override
        public void setData(PdfPTable table, Font arialFont) {
            if (mCafeReportAdapter != null) {
                List<CafeReport> cafeReports = mCafeReportAdapter.getData();
                if (cafeReports != null) {
                    Double totalPriceBeforeDiscount = 0.0;
                    Double totalPrice = 0.0;

                    for (CafeReport report : cafeReports) {
                        table.addCell(new PdfPCell(new Paragraph(report.getBillNO(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getRoom_Table(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getBillDate(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getBillItem(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getItemQT(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getPreDiscount(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getBillCash(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getShift_name(), arialFont)));

                        totalPriceBeforeDiscount += Double.valueOf(report.getPreDiscount());
                        totalPrice += Double.valueOf(report.getBillCash());
                    }

                    table.addCell(new PdfPCell(new Paragraph("الإجمالي", arialFont)));
                    table.addCell(new PdfPCell(new Paragraph("", arialFont)));
                    table.addCell(new PdfPCell(new Paragraph("", arialFont)));
                    table.addCell(new PdfPCell(new Paragraph("", arialFont)));
                    table.addCell(new PdfPCell(new Paragraph("", arialFont)));
                    table.addCell(new PdfPCell(new Paragraph(String.valueOf(totalPriceBeforeDiscount), arialFont)));
                    table.addCell(new PdfPCell(new Paragraph(String.valueOf(totalPrice), arialFont)));
                    table.addCell(new PdfPCell(new Paragraph("", arialFont)));
                }
            }
        }

        @Override
        public boolean setHeader(PdfPTable table, Font arialFont) {
            table.addCell(new PdfPCell(new Paragraph("فاتورة", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("رقم الطاولة", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("التاريخ والوقت", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("الأصناف", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("الكمية", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("قبل الخصم", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("السعر", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("شيفت", arialFont)));
            return true;
        }
    }

    private class OutsReportHelper implements ITextGUtil.Helper {

        @Override
        public void setData(PdfPTable table, Font arialFont) {
            if (mOutsReportAdapter != null) {
                List<OutsReport> outsReports = mOutsReportAdapter.getData();
                if (outsReports != null) {
                    Double totalPrice = 0.0;

                    for (OutsReport report : outsReports) {
                        table.addCell(new PdfPCell(new Paragraph(report.getOutsType(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getOutsNote(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getOutsVal(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getOutsDate(), arialFont)));
                        table.addCell(new PdfPCell(new Paragraph(report.getShift_name(), arialFont)));

                        totalPrice += Double.valueOf(report.getOutsVal());
                    }

                    table.addCell(new PdfPCell(new Paragraph("الإجمالي", arialFont)));
                    table.addCell(new PdfPCell(new Paragraph("", arialFont)));
                    table.addCell(new PdfPCell(new Paragraph(String.valueOf(totalPrice), arialFont)));
                    table.addCell(new PdfPCell(new Paragraph("", arialFont)));
                    table.addCell(new PdfPCell(new Paragraph("", arialFont)));
                }
            }
        }

        @Override
        public boolean setHeader(PdfPTable table, Font arialFont) {
            table.addCell(new PdfPCell(new Paragraph("نوع المصروفات", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("المصروفات", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("المبلغ", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("تاريخ الوردية", arialFont)));
            table.addCell(new PdfPCell(new Paragraph("مسؤول الوردية", arialFont)));
            return true;
        }
    }
}