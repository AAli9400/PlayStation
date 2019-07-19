package com.a.ali.playstation.ui.console;

import android.content.Context;
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

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.MainActivity;
import com.a.ali.playstation.ui.console.adapter.ConsoleAdapter;

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

        mLoadingProgressBar = view.findViewById(R.id.progressBar);

        mRoomsRecyclerView = view.findViewById(R.id.recyclerview);

        mConsoleAdapter = new ConsoleAdapter(mContext, mAppNetworkRepository, this);
        mRoomsRecyclerView.setAdapter(mConsoleAdapter);

        loadRooms();

        return view;
    }

    private void loadRooms() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);

        mAppNetworkRepository.loadRooms()
                .observe(this, response -> {
                    if (response != null) {
                        mConsoleAdapter.swapData(response);
                    }
                    mLoadingProgressBar.setVisibility(View.GONE);
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragment);

        if (item.getItemId() == R.id.action_logout) {
            return ((MainActivity) getActivity()).onSupportNavigateUp();
        }

        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);

    }
}