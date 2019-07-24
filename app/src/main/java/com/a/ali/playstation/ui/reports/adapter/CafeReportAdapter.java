package com.a.ali.playstation.ui.reports.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.model.CafeReport;

import java.util.ArrayList;
import java.util.List;

public class CafeReportAdapter extends RecyclerView.Adapter<CafeReportAdapter.ViewHolder> {
    private List<CafeReport> mCafeReports = null;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cafe_report_list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CafeReport cafeReport = mCafeReports.get(position);

        holder.billItemTextView.setText(cafeReport.getBillItem());

        holder.billNumberTextView.setText(cafeReport.getBillNO());

        holder.shiftNameTextView.setText(cafeReport.getShift_name());

        holder.preDiscountTextView.setText(cafeReport.getPreDiscount());

        holder.billCashTextView.setText(cafeReport.getBillCash());

        holder.roomTableTextView.setText(cafeReport.getRoom_Table());

        holder.itemQuantityTextView.setText(cafeReport.getItemQT());

        holder.startDateTextView.setText(cafeReport.getBillDate());
    }

    @Override
    public int getItemCount() {
        return mCafeReports == null ? 0 : mCafeReports.size();
    }

    public void swapData(@NonNull List<CafeReport> cafeOrders) {
        mCafeReports = cafeOrders;

        notifyDataSetChanged();
    }

    public List<CafeReport> getData() {
        return mCafeReports;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView billItemTextView, billNumberTextView, shiftNameTextView, preDiscountTextView, billCashTextView, roomTableTextView,
                itemQuantityTextView, startDateTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            billItemTextView = itemView.findViewById(R.id.tv_bill_item);
            billNumberTextView = itemView.findViewById(R.id.tv_bill_number);
            shiftNameTextView = itemView.findViewById(R.id.tv_shift_name);
            preDiscountTextView = itemView.findViewById(R.id.tv_pre_discount);
            billCashTextView = itemView.findViewById(R.id.tv_bill_cash);
            roomTableTextView = itemView.findViewById(R.id.tv_room_table);
            itemQuantityTextView = itemView.findViewById(R.id.tv_item_quantity);
            startDateTextView = itemView.findViewById(R.id.tv_bill_date);
        }
    }
}
