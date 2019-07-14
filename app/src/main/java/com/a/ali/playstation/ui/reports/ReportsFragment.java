package com.a.ali.playstation.ui.reports;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.a.ali.playstation.R;
import com.a.ali.playstation.ui.util.AppDialog;
import com.google.android.material.button.MaterialButton;

public class ReportsFragment extends Fragment {
    private ProgressBar mLoadingProgressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        mLoadingProgressBar = view.findViewById(R.id.progressBar);

        showTypeDialog();

        return view;
    }

    private void showTypeDialog() {
        AppDialog.show(getContext(), R.layout.report_type_dialog, (view, dialog) -> {
            MaterialButton goButton = view.findViewById(R.id.mbtn_go);
            goButton.setOnClickListener(view1 -> {
                dialog.dismiss();

                mLoadingProgressBar.setVisibility(View.VISIBLE);
                //TODO: Load data
                new Handler().postDelayed(() ->  mLoadingProgressBar.setVisibility(View.GONE), 1000);
            });
        });
    }
}