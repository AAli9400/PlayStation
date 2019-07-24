package com.a.ali.playstation.ui.console.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import static com.a.ali.playstation.data.model.Console.CONSOLE_STATUS_FINISH;
import static com.a.ali.playstation.data.model.Console.CONSOLE_STATUS_FINISH_TRANSFORMING;
import static com.a.ali.playstation.data.model.Console.CONSOLE_STATUS_MULTI;
import static com.a.ali.playstation.data.model.Console.CONSOLE_STATUS_PLAYING;
import static com.a.ali.playstation.data.model.Console.CONSOLE_STATUS_SINGLE;

public class ConsoleDetailsAdapter extends RecyclerView.Adapter<ConsoleDetailsAdapter.ViewHolder> {
    private Context mContext;

    private List<Console> mConsolesList = null;
    public ConsoleDetailsAdapter(Context context) {
        mContext = context;
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
        holder.detailsButton.setVisibility(View.GONE);
        holder.ordersButton.setVisibility(View.GONE);

        Console console = mConsolesList.get(position);

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
    }

    @Override
    public int getItemCount() {
        return mConsolesList != null ? mConsolesList.size() : 0;
    }

    public void swapData(@NonNull List<Console> consoles) {
        mConsolesList = consoles;

        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialButton ordersButton, detailsButton;
        ConstraintLayout detailsConstraintLayout;
        ImageView consoleStatusImageView, multiImageView;
        TextView consoleNameTextView, singleOrMultiTextView, consoleStartDate, moneyTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ordersButton = itemView.findViewById(R.id.mbtn_orders);
            detailsButton = itemView.findViewById(R.id.mbtn_details);
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
