package com.a.ali.playstation.ui.console.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.model.Console;
import com.a.ali.playstation.data.repository.AppDatabaseRepository;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.util.AppDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import static com.a.ali.playstation.data.model.Console.*;

public class ConsoleAdapter extends RecyclerView.Adapter<ConsoleAdapter.ViewHolder> {
    private Context mContext;
    private AppNetworkRepository mAppNetworkRepository;
    private AppDatabaseRepository mAppDatabaseRepository;
    private Fragment mOwnerFragment;

    private List<Console> mConsoles = null;

    private boolean mForLog;

    public ConsoleAdapter(Context context, AppNetworkRepository appNetworkRepository, Fragment ownerFragment, boolean forLog, AppDatabaseRepository instance) {
        mContext = context;
        mAppNetworkRepository = appNetworkRepository;
        mOwnerFragment = ownerFragment;

        mForLog = forLog;
        mAppDatabaseRepository = instance;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.console_list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Console console = mConsoles.get(position);

        mAppNetworkRepository.loadOrders(console.getDev_code())
                .observe(mOwnerFragment, cafeOrders -> {
                    if (cafeOrders != null && !cafeOrders.isEmpty()) {
                        holder.ordersButton.setVisibility(View.VISIBLE);
                    } else {
                        holder.ordersButton.setVisibility(View.GONE);
                    }
                });

        holder.consoleNameTextView.setText(console.getDev_code());
        holder.consoleStartDate.setText(console.getStartTime());

        String singleOrMulti = console.getSingle_multi();
        if (singleOrMulti.equals(CONSOLE_STATUS_SINGLE)) {
            holder.multiImageView.setVisibility(View.GONE);
        } else if (singleOrMulti.equals(CONSOLE_STATUS_MULTI)) {
            holder.multiImageView.setVisibility(View.VISIBLE);
        }

        holder.singleOrMultiTextView.setText(console.getSingle_multi());

        String state = console.getState();
        if (state.equals(CONSOLE_STATUS_FINISH)) {
            holder.consoleStatusImageView.setImageDrawable(
                    VectorDrawableCompat.create(mContext.getResources(), R.drawable.empty_background, null)
            );
            holder.detailsConstraintLayout.setVisibility(View.GONE);
        } else if (state.equals(CONSOLE_STATUS_PLAYING)) {
            holder.consoleStatusImageView.setImageDrawable(
                    VectorDrawableCompat.create(mContext.getResources(), R.drawable.busy_background, null)
            );
            holder.detailsConstraintLayout.setVisibility(View.VISIBLE);
        }

        holder.moneyTextView.setText(console.getDepositCash());


        holder.ordersButton.setOnClickListener(view -> {
            if (!mForLog) {
                showOrdersDialog(console.getDev_code());
            } else {
                loadOrdersFromDatabase(console.getDev_code());
            }
        });
    }

    private void loadOrdersFromDatabase(String dev_code) {
        AppDialog.show(mContext, R.layout.cafe_orders_dialog, (view, dialog) -> {

            TextView consoleCodeTextView = view.findViewById(R.id.tv_console_name);
            consoleCodeTextView.setText(dev_code);

            RecyclerView ordersRecyclerView = view.findViewById(R.id.recyclerview);

            OrderAdapter orderAdapter = new OrderAdapter(mContext);
            ordersRecyclerView.setAdapter(orderAdapter);

            mAppDatabaseRepository.getConsoleId(dev_code)
                    .observe(mOwnerFragment, integer -> {
                        if(integer != null){
                            mAppDatabaseRepository.selectOrders(integer)
                                    .observe(mOwnerFragment, cafeOrders -> {
                                        if (cafeOrders != null) {
                                            double price = orderAdapter.swapData(cafeOrders);
                                            ((TextView) view.findViewById(R.id.tv_total_cafe_order))
                                                    .setText(String.valueOf(price));

                                        } else {
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    });
        });

    }

    private void showOrdersDialog(String consoleCode) {
        AppDialog.show(mContext, R.layout.cafe_orders_dialog, (view, dialog) -> {

            TextView consoleCodeTextView = view.findViewById(R.id.tv_console_name);
            consoleCodeTextView.setText(consoleCode);

            RecyclerView ordersRecyclerView = view.findViewById(R.id.recyclerview);

            OrderAdapter orderAdapter = new OrderAdapter(mContext);
            ordersRecyclerView.setAdapter(orderAdapter);

            mAppNetworkRepository.loadOrders(consoleCode).observe(
                    mOwnerFragment, response -> {
                        if (response != null) {
                            double price = orderAdapter.swapData(response);
                            ((TextView) view.findViewById(R.id.tv_total_cafe_order))
                                    .setText(String.valueOf(price));

                        } else {
                            dialog.dismiss();
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return mConsoles != null ? mConsoles.size() : 0;
    }

    public void swapData(@NonNull List<Console> consoles) {
        ArrayList<Console> consoleArrayList = new ArrayList<>();
        for (Console console : consoles) {
            if (console.getState().equals(CONSOLE_STATUS_PLAYING)) consoleArrayList.add(console);
        }

        for (Console console : consoles) {
            if (console.getState().equals(CONSOLE_STATUS_FINISH)) consoleArrayList.add(console);
        }

        mConsoles = consoleArrayList;

        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialButton ordersButton;
        ConstraintLayout detailsConstraintLayout;
        ImageView consoleStatusImageView, multiImageView;
        TextView consoleNameTextView, singleOrMultiTextView, consoleStartDate, moneyTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ordersButton = itemView.findViewById(R.id.mbtn_orders);
            detailsConstraintLayout = itemView.findViewById(R.id.cl_console_details);
            consoleStatusImageView = itemView.findViewById(R.id.iv_console_status);
            multiImageView = itemView.findViewById(R.id.iv_multi_icon);

            consoleNameTextView = itemView.findViewById(R.id.tv_console_name);
            singleOrMultiTextView = itemView.findViewById(R.id.tv_single_or_multi);
            consoleStartDate = itemView.findViewById(R.id.tv_start_date);
            moneyTextView = itemView.findViewById(R.id.tv_money);
        }
    }
}
