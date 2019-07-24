package com.a.ali.playstation.ui.console;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.database.util.ConsoleLogService;
import com.a.ali.playstation.data.database.util.ConsoleLogWorkerManager;
import com.a.ali.playstation.data.model.User;
import com.a.ali.playstation.data.repository.AppDatabaseRepository;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.console.adapter.ConsoleAdapter;
import com.a.ali.playstation.ui.ip.IPActivity;
import com.a.ali.playstation.ui.lastActions.LastActionsActivity;
import com.a.ali.playstation.ui.login.LoginActivity;
import com.a.ali.playstation.ui.reports.SelectReportActivity;
import com.a.ali.playstation.ui.util.AppLoadingViewUtil;

import java.util.concurrent.TimeUnit;

public class ConsoleActivity extends AppCompatActivity {
    private RecyclerView mConsolesRecyclerView;

    private ImageView mLoadingImageView;
    private ConstraintLayout mEmptyView;
    private AppLoadingViewUtil mLoadingViewUtil;

    private ConsoleAdapter mConsoleAdapter;

    private AppNetworkRepository mAppNetworkRepository;
    private AppDatabaseRepository mAppDatabaseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);

        getSupportActionBar().show();

        mAppNetworkRepository = AppNetworkRepository.getInstance(getApplication());
        mAppDatabaseRepository = AppDatabaseRepository.getInstance(getApplication());

        setupLoadingConsolesPeriodically();

        mLoadingImageView = findViewById(R.id.iv_loading);
        mLoadingViewUtil = new AppLoadingViewUtil(this, mLoadingImageView);
        mLoadingImageView.setImageDrawable(mLoadingViewUtil.getDrawable());

        mEmptyView = findViewById(R.id.cl_empty_view);

        mConsolesRecyclerView = findViewById(R.id.recyclerview);

        mConsoleAdapter = new ConsoleAdapter(this, mAppNetworkRepository, this, false, AppDatabaseRepository.getInstance(getApplication()));
        mConsolesRecyclerView.setAdapter(mConsoleAdapter);

        loadConsoles();
    }

    private void setupLoadingConsolesPeriodically() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                ConsoleLogWorkerManager.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);
    }

    private void loadConsoles() {
        mLoadingViewUtil.show();
        mEmptyView.setVisibility(View.GONE);

        mAppNetworkRepository.loadConsoles()
                .observe(this, response -> {
                    if (response != null) {

                        startService(new Intent(this, ConsoleLogService.class));

                        mConsoleAdapter.swapData(response);

                        mEmptyView.setVisibility(View.GONE);
                    } else {
                        mEmptyView.setVisibility(View.VISIBLE);
                    }
                    mLoadingViewUtil.hide();
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        String userTitle = getSharedPreferences(getString(R.string.user_shared_preferences_name), Context.MODE_PRIVATE)
                .getString(getString(R.string.user_title_key), null);

        if (userTitle != null && userTitle.equals(User.TITLE_SHIFT)) {
            menu.getItem(R.id.action_report).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_report:
                startActivity(new Intent(this, SelectReportActivity.class));
                return true;
            case R.id.action_ip:
                startActivity(new Intent(this, IPActivity.class));
                return true;

            case R.id.action_last_actions:
                startActivity(new Intent(this, LastActionsActivity.class));
                return true;

            case R.id.action_logout:
                getSharedPreferences(getString(R.string.user_shared_preferences_name), Context.MODE_PRIVATE)
                        .edit().clear().apply();
                WorkManager.getInstance(this).cancelAllWork();

                startActivity(new Intent(this, LoginActivity.class));

                this.finish();
                return true;

            case R.id.action_refresh:
                loadConsoles();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
