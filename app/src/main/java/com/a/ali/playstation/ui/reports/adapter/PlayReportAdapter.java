package com.a.ali.playstation.ui.reports.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.model.Console;
import com.a.ali.playstation.data.model.PlayReport;

import java.util.List;

public class PlayReportAdapter extends RecyclerView.Adapter<PlayReportAdapter.ViewHolder> {
    private List<PlayReport> mPlayReports;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.play_report_list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayReport playReport = mPlayReports.get(position);

        holder.codeTextView.setText(playReport.getPs());

        holder.statusTextView.setText(playReport.getSingle_multi());
        if (playReport.getSingle_multi().equals(Console.CONSOLE_STATUS_MULTI)) {
            holder.multiImageView.setVisibility(View.VISIBLE);
        } else if (playReport.getSingle_multi().equals(Console.CONSOLE_STATUS_SINGLE)) {
            holder.multiImageView.setVisibility(View.INVISIBLE);
        }

        holder.shiftNameTextView.setText(playReport.getShift_name());

        holder.prediscountTextView.setText(playReport.getPreDiscount());

        holder.billCashTextView.setText(playReport.getBillCash());

        holder.startDateTextView.setText(playReport.getStart_date());

        holder.endDateTextView.setText(playReport.getFinish_date());
    }

    @Override
    public int getItemCount() {
        return mPlayReports == null ? 0 : mPlayReports.size();
    }

    public void swapData(List<PlayReport> playReports) {
        mPlayReports = playReports;

        notifyDataSetChanged();
    }

    public List<PlayReport> getData() {
        return mPlayReports;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView codeTextView, statusTextView, shiftNameTextView, prediscountTextView, billCashTextView, startDateTextView, endDateTextView;
        ImageView multiImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            codeTextView = itemView.findViewById(R.id.tv_console_name);
            statusTextView = itemView.findViewById(R.id.tv_single_or_multi);
            shiftNameTextView = itemView.findViewById(R.id.tv_shift_name);
            prediscountTextView = itemView.findViewById(R.id.tv_pre_discount);
            billCashTextView = itemView.findViewById(R.id.tv_bill_cash);
            startDateTextView = itemView.findViewById(R.id.tv_start_date);
            endDateTextView = itemView.findViewById(R.id.tv_end_date);

            multiImageView = itemView.findViewById(R.id.iv_multi_icon);
        }
    }
}
