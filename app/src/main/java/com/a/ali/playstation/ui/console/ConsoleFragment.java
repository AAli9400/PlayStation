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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.database.util.ConsoleLogWorkerManager;
import com.a.ali.playstation.data.model.User;
import com.a.ali.playstation.data.repository.AppDatabaseRepository;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.MainActivity;
import com.a.ali.playstation.ui.console.adapter.ConsoleAdapter;
import com.a.ali.playstation.ui.util.AppLoadingViewUtil;

import java.util.concurrent.TimeUnit;

public class ConsoleFragment extends Fragment {
    private RecyclerView mRoomsRecyclerView;

    private ImageView mLoadingImageView;
    private AppLoadingViewUtil mLoadingViewUtil;

    private ConsoleAdapter mConsoleAdapter;

    private AppNetworkRepository mAppNetworkRepository;
    private AppDatabaseRepository mAppDatabaseRepository;
    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room, parent, false);
        setHasOptionsMenu(true);

        mContext = getContext();
        mAppNetworkRepository = AppNetworkRepository.getInstance(getActivity().getApplication());
        mAppDatabaseRepository = AppDatabaseRepository.getInstance(getActivity().getApplication());

        setupLoadingConsolesPeriodically();

        mLoadingImageView = view.findViewById(R.id.iv_loading);
        mLoadingViewUtil = new AppLoadingViewUtil(mContext, mLoadingImageView);
        mLoadingImageView.setImageDrawable(mLoadingViewUtil.getDrawable());

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
        mLoadingViewUtil.show();

        mAppNetworkRepository.loadConsoles()
                .observe(this, response -> {
                    if (response != null) {
                        mConsoleAdapter.swapData(response);
                    }
                    mLoadingViewUtil.hide();
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        String userTitle = getActivity().getSharedPreferences(getString(R.string.user_shared_preferences_name), Context.MODE_PRIVATE)
                .getString(getString(R.string.user_title_key), null);

        if (userTitle != null && userTitle.equals(User.TITLE_SHIFT)) {
            menu.getItem(R.id.selectReportFragment).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragment);

        int itemId = item.getItemId();
        if (itemId == R.id.action_logout) {
            getActivity().getSharedPreferences(getString(R.string.user_shared_preferences_name), Context.MODE_PRIVATE)
                    .edit().clear().apply();

//            getActivity().getSharedPreferences(getString(R.string.ip_shared_preference_name), Context.MODE_PRIVATE)
//                    .edit().clear().apply();

//            mAppDatabaseRepository.deleteAllConsolesData();

            WorkManager.getInstance(mContext).cancelAllWork();

            return ((MainActivity) getActivity()).onSupportNavigateUp();
        } else if (itemId == R.id.action_refresh) {
            loadConsoles();
            return true;
        }

        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }
}