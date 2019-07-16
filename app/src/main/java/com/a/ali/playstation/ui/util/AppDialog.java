package com.a.ali.playstation.ui.util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.a.ali.playstation.R;

public class AppDialog {
    private AppDialog() {
    }

    public static void show(@NonNull Context context, int resId, @NonNull Helper helper) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(resId, null);
        builder.setView(view).setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        helper.initViews(view, dialog);

        ImageView closeImageView = view.findViewById(R.id.iv_close);
        if (closeImageView != null) {
            closeImageView.setOnClickListener(view1 -> dialog.dismiss());
        }

        dialog.show();
    }

    public interface Helper {
        void initViews(@NonNull View view, @NonNull AlertDialog dialog);
    }
}
