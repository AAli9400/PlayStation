package com.a.ali.playstation.ui.rooms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.a.ali.playstation.R;
import com.a.ali.playstation.ui.MainActivity;
import com.a.ali.playstation.ui.rooms.adapter.RoomAdapter;

public class RoomFragment extends Fragment {
    private RecyclerView mRoomsRecyclerView;
    private ProgressBar mLoadingProgressBar;

    private RoomAdapter mRoomAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room, parent, false);
        setHasOptionsMenu(true);

        mLoadingProgressBar = view.findViewById(R.id.progressBar);

        mRoomsRecyclerView = view.findViewById(R.id.recyclerview);

        mRoomAdapter = new RoomAdapter(getContext());

        mLoadingProgressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(
                () -> {
                    mRoomsRecyclerView.setAdapter(mRoomAdapter);
                    mLoadingProgressBar.setVisibility(View.GONE);
                },
                3000
        );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
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