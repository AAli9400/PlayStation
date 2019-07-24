package com.a.ali.playstation.ui.reports.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.model.SummaryReport;

import java.util.List;

public class SummaryReportAdapter extends RecyclerView.Adapter<SummaryReportAdapter.ViewHolder> {

    private List<SummaryReport> mSummaryReports = null;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.summary_report_list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SummaryReport report = mSummaryReports.get(position);

        holder.cafeValTextView.setText(report.getCafeVal());
        holder.miniValTextView.setText(report.getMiniVal());
        holder.netProfitTextView.setText(report.getNetProfit());
        holder.playValTextView.setText(report.getPlayVal());
        holder.outsValTextView.setText(report.getOutsVal());
    }

    @Override
    public int getItemCount() {
        return mSummaryReports == null ? 0 : mSummaryReports.size();
    }

    public void swapData(List<SummaryReport> summaryReports) {
        mSummaryReports = summaryReports;

        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView cafeValTextView, miniValTextView, netProfitTextView, playValTextView, outsValTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            cafeValTextView = itemView.findViewById(R.id.tv_cafe_val);
            miniValTextView = itemView.findViewById(R.id.tv_mini_val);
            netProfitTextView = itemView.findViewById(R.id.tv_net_profit);
            playValTextView = itemView.findViewById(R.id.tv_play_value);
            outsValTextView = itemView.findViewById(R.id.tv_outs_val);
        }
    }
}
