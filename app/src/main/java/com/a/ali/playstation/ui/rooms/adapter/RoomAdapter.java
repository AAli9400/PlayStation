package com.a.ali.playstation.ui.rooms.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.a.ali.playstation.R;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        View view = LayoutInflater.from(mContext).inflate(R.layout.orders_dialog, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RecyclerView ordersRecyclerView = view.findViewById(R.id.recyclerview);
        ordersRecyclerView.setAdapter(new OrderAdapter());

        ImageView closeImageView = view.findViewById(R.id.iv_close);
        closeImageView.setOnClickListener(view1 -> dialog.dismiss());

        dialog.show();
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
