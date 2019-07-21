package com.a.ali.playstation.ui.lastActions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.repository.AppDatabaseRepository;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.console.adapter.ConsoleAdapter;

public class LastActionsActivity extends AppCompatActivity {
    private AppDatabaseRepository mAppDatabaseRepository;

    private ConstraintLayout mEmptyViewConstraintLayout;

    private RecyclerView mConsolesRecyclerView;
    private ConsoleAdapter mConsoleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmptyViewConstraintLayout = findViewById(R.id.cl_empty_view);

        mAppDatabaseRepository = AppDatabaseRepository.getInstance(getApplication());

        mConsolesRecyclerView = findViewById(R.id.recyclerview);

        mConsoleAdapter = new ConsoleAdapter(this,
                AppNetworkRepository.getInstance(getApplication()),
                this, true,
                mAppDatabaseRepository
        );
        mConsolesRecyclerView.setAdapter(mConsoleAdapter);

        loadData();

    }

    private void loadData() {
        mAppDatabaseRepository.selectAllLiveData().observe(this, consoles -> {
            if (consoles == null || consoles.isEmpty()) {
                mEmptyViewConstraintLayout.setVisibility(View.VISIBLE);
            } else {
                mEmptyViewConstraintLayout.setVisibility(View.GONE);

                mConsoleAdapter.swapData(consoles);
            }
        });
    }
}
