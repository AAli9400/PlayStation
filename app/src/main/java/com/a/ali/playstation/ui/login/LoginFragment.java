package com.a.ali.playstation.ui.login;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a.ali.playstation.R;
import com.google.android.material.button.MaterialButton;

public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, parent, false);

        MaterialButton loginButton = view.findViewById(R.id.mbtn_login);
        loginButton.setOnClickListener(
                (Navigation.createNavigateOnClickListener(
                        R.id.action_login_to_home, null)));

        return view;
    }
}
