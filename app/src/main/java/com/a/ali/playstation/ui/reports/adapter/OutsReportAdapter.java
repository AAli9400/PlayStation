package com.a.ali.playstation.ui.reports.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.model.OutsReport;

import java.util.List;

public class OutsReportAdapter extends RecyclerView.Adapter<OutsReportAdapter.ViewHolder> {
    private List<OutsReport> mOutsReportsList = null;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.outs_report_list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OutsReport report = mOutsReportsList.get(position);

        holder.shiftNameTextView.setText(report.getShift_name());
        holder.dateTextView.setText(report.getOutsDate());
        holder.typeTextView.setText(report.getOutsType());
        holder.valueTextView.setText(report.getOutsVal());
        holder.notesTextView.setText(report.getOutsNote());
    }

    @Override
    public int getItemCount() {
        return mOutsReportsList == null ? 0 : mOutsReportsList.size();
    }

    public void swapData(@NonNull List<OutsReport> outsReports) {
        mOutsReportsList = outsReports;

        notifyDataSetChanged();
    }

    public List<OutsReport> getData() {
        return mOutsReportsList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView shiftNameTextView, dateTextView, typeTextView, valueTextView, notesTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            shiftNameTextView = itemView.findViewById(R.id.tv_shift_name);
            dateTextView = itemView.findViewById(R.id.tv_date);
            typeTextView = itemView.findViewById(R.id.tv_type);
            valueTextView = itemView.findViewById(R.id.tv_value);
            notesTextView = itemView.findViewById(R.id.tv_notes);
        }
    }
}
