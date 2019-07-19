package com.a.ali.playstation.ui.console;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.database.util.ConsoleLogWorkerManager;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.MainActivity;
import com.a.ali.playstation.ui.console.adapter.ConsoleAdapter;

import java.util.concurrent.TimeUnit;

public class ConsoleFragment extends Fragment {
    private RecyclerView mRoomsRecyclerView;
    private ProgressBar mLoadingProgressBar;

    private ConsoleAdapter mConsoleAdapter;

    private AppNetworkRepository mAppNetworkRepository;
    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room, parent, false);
        setHasOptionsMenu(true);

        mContext = getContext();
        mAppNetworkRepository = AppNetworkRepository.getInstance(getActivity().getApplication());

        setupLoadingConsolesPeriodically();

        mLoadingProgressBar = view.findViewById(R.id.progressBar);

        mRoomsRecyclerView = view.findViewById(R.id.recyclerview);

        mConsoleAdapter = new ConsoleAdapter(mContext, mAppNetworkRepository, this);
        mRoomsRecyclerView.setAdapter(mConsoleAdapter);

        loadConsoles();

        return view;
    }

    private void setupLoadingConsolesPeriodically() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                ConsoleLogWorkerManager.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(mContext).enqueue(workRequest);

    }

    private void loadConsoles() {
        if (isIPEntered())
            mLoadingProgressBar.setVisibility(View.VISIBLE);

        mAppNetworkRepository.loadConsoles()
                .observe(this, response -> {
                    if (response != null) {
                        mConsoleAdapter.swapData(response);
                    }
                    mLoadingProgressBar.setVisibility(View.GONE);
                });
    }

    private boolean isIPEntered() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.ip_shared_preference_name), Context.MODE_PRIVATE);
        String ipAddress = sharedPreferences.getString(getString(R.string.ip_key), null);
        if (ipAddress != null) {
            return true;
        } else {
            Navigation.findNavController(getActivity(), R.id.navHostFragment)
                    .navigate(R.id.action_console_to_IP);
            return false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragment);

        int itemId = item.getItemId();
        if (itemId == R.id.action_logout) {
            return ((MainActivity) getActivity()).onSupportNavigateUp();
        } else if (itemId == R.id.action_refresh) {
            loadConsoles();
            return true;
        }

        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);

    }
}