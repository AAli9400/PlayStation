package com.a.ali.playstation.ui.rooms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a.ali.playstation.R;
import com.a.ali.playstation.ui.util.AppDialog;
import com.google.android.material.button.MaterialButton;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    private Context mContext;

    public RoomAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.room_list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ordersButton.setOnClickListener(view -> showOrdersDialog(position));
    }

    private void showOrdersDialog(int position) {
        AppDialog.show(mContext, R.layout.orders_dialog, (view, dialog) -> {
            RecyclerView ordersRecyclerView = view.findViewById(R.id.recyclerview);
            ordersRecyclerView.setAdapter(new OrderAdapter());
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialButton ordersButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ordersButton = itemView.findViewById(R.id.mbtn_orders);
        }
    }
}
