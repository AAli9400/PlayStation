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
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.util.AppDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import static com.a.ali.playstation.data.network.api.ApiResponseConstants.CONSOLE_STATUS_FINISH;
import static com.a.ali.playstation.data.network.api.ApiResponseConstants.CONSOLE_STATUS_MULTI;
import static com.a.ali.playstation.data.network.api.ApiResponseConstants.CONSOLE_STATUS_PLAYING;
import static com.a.ali.playstation.data.network.api.ApiResponseConstants.CONSOLE_STATUS_SINGLE;

public class ConsoleAdapter extends RecyclerView.Adapter<ConsoleAdapter.ViewHolder> {
    private Context mContext;
    private AppNetworkRepository mAppNetworkRepository;
    private Fragment mOwnerFragment;

    /*
     * [{"DepositCash":0,"dev_code":"ps01","single_multi":"multi","startTime":"18\/07\/2019 02:07:29 م","state":"playing"},{"DepositCash":0,"dev_code":"ps01","single_multi":"single","startTime":"18\/07\/2019 01:58:29 م","state":"finish transforming"},{"DepositCash":0,"dev_code":"ps02","single_multi":"single","startTime":"18\/05\/2019 05:39:57 م","state":"finish"},{"DepositCash":0,"dev_code":"ps03","single_multi":"multi","startTime":"18\/07\/2019 02:13:01 م","state":"playing"},{"DepositCash":0,"dev_code":"ps03","single_multi":"single","startTime":"18\/07\/2019 01:58:26 م","state":"finish transforming"},{"DepositCash":0,"dev_code":"ps04","single_multi":"single","startTime":"18\/07\/2019 01:58:23 م","state":"playing"},{"DepositCash":0,"dev_code":"ps05","single_multi":"multi","startTime":"18\/07\/2019 02:13:07 م","state":"playing"},{"DepositCash":0,"dev_code":"ps07","single_multi":"single","startTime":"12\/04\/2019 04:30:53 م","state":"finish"},{"DepositCash":0,"dev_code":"ps10","single_multi":"single","startTime":"18\/05\/2019 11:19:22 م","state":"finish"}]*/

    private List<Console> mConsoles = null;

    public ConsoleAdapter(Context context, AppNetworkRepository appNetworkRepository, Fragment ownerFragment) {
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
        Console console = mConsoles.get(position);

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


        holder.ordersButton.setOnClickListener(view -> showOrdersDialog(console.getDev_code()));
    }

    private void showOrdersDialog(String consoleCode) {
        AppDialog.show(mContext, R.layout.cafe_orders_dialog, (view, dialog) -> {
            RecyclerView ordersRecyclerView = view.findViewById(R.id.recyclerview);

            OrderAdapter orderAdapter = new OrderAdapter();
            ordersRecyclerView.setAdapter(orderAdapter);

            mAppNetworkRepository.loadOrders(consoleCode).observe(
                    mOwnerFragment, response -> {
                        if (response != null) {
                            double price = orderAdapter.swapData(response);

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
