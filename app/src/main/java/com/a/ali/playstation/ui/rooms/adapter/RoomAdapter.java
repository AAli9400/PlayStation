package com.a.ali.playstation.ui.rooms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.util.AppDialog;
import com.google.android.material.button.MaterialButton;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    private Context mContext;
    private AppNetworkRepository mAppNetworkRepository;
    private Fragment mOwnerFragment;

    public RoomAdapter(Context context, AppNetworkRepository appNetworkRepository, Fragment ownerFragment) {
        mContext = context;
        mAppNetworkRepository = appNetworkRepository;
        mOwnerFragment = ownerFragment;
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
        if (position > 10) {
            holder.detailsConstraintLayout.setVisibility(View.GONE);
            holder.roomStatusImageView.setImageDrawable(
                    VectorDrawableCompat.create(
                            mContext.getResources(),
                            R.drawable.empty_background,
                            null
                    )
            );
        }
    }

    private void showOrdersDialog(int position) {
        AppDialog.show(mContext, R.layout.orders_dialog, (view, dialog) -> {
            RecyclerView ordersRecyclerView = view.findViewById(R.id.recyclerview);
            ordersRecyclerView.setAdapter(new OrderAdapter());

            mAppNetworkRepository.loadOrders(position/*TODO: RoomID*/).observe(
                    mOwnerFragment, response -> {
                        if (response != null) {
                            //TODO:
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialButton ordersButton;
        ConstraintLayout detailsConstraintLayout;
        ImageView roomStatusImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ordersButton = itemView.findViewById(R.id.mbtn_orders);
            detailsConstraintLayout = itemView.findViewById(R.id.cl_room_details);
            roomStatusImageView = itemView.findViewById(R.id.iv_room_status);
        }
    }
}
