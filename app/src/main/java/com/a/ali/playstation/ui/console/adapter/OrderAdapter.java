package com.a.ali.playstation.ui.console.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.model.CafeOrders;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<CafeOrders> mCafeOrders;

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
    }

    @Override
    public int getItemCount() {
        return mCafeOrders == null ? 0 : mCafeOrders.size();
    }

    public double swapData(List<CafeOrders> cafeOrders) {
        mCafeOrders = cafeOrders;

        notifyDataSetChanged();

        double price = 0;
        for (CafeOrders orders : cafeOrders) {
            price += Double.valueOf(orders.getItemPrice());
        }

        return price;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}