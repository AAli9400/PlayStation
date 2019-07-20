package com.a.ali.playstation.ui.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.repository.AppDatabaseRepository;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.console.adapter.ConsoleAdapter;

public class LogFragment extends Fragment {
    private AppDatabaseRepository mAppDatabaseRepository;

    private ConstraintLayout mEmptyViewConstraintLayout;

    private RecyclerView mConsolessRecyclerView;
    private ConsoleAdapter mConsoleAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        mEmptyViewConstraintLayout = view.findViewById(R.id.cl_empty_view);

        mAppDatabaseRepository = AppDatabaseRepository.getInstance(getActivity().getApplication());

        mConsolessRecyclerView = view.findViewById(R.id.recyclerview);

        mConsoleAdapter = new ConsoleAdapter(getContext(),
                AppNetworkRepository.getInstance(getActivity().getApplication()),
                this, true,
                AppDatabaseRepository.getInstance(getActivity().getApplication())
                );
        mConsolessRecyclerView.setAdapter(mConsoleAdapter);

        loadData();

        return view;
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