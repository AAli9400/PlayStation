package com.a.ali.playstation.ui.console.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.model.CafeOrder;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<CafeOrder> mCafeOrders;

    private Context mContext;

    public OrderAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cafe_orders_list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CafeOrder orders = mCafeOrders.get(position);

        holder.amountTextView.setText(orders.getItemQT());

        holder.categoryTextView.setText(orders.getCItem());

        holder.priceTextView.setText(orders.getItemPrice());
    }

    @Override
    public int getItemCount() {
        return mCafeOrders == null ? 0 : mCafeOrders.size();
    }

    public double swapData(List<CafeOrder> cafeOrders) {
        mCafeOrders = cafeOrders;

        notifyDataSetChanged();

        double price = 0;
        for (CafeOrder orders : cafeOrders) {
            price += Double.valueOf(orders.getItemPrice());
        }

        return price;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView amountTextView, categoryTextView, priceTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            amountTextView = itemView.findViewById(R.id.tv_amount);
            categoryTextView = itemView.findViewById(R.id.tv_category);
            priceTextView = itemView.findViewById(R.id.tv_price);
        }
    }
}