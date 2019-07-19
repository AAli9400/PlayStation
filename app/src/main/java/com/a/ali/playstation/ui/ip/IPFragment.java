package com.a.ali.playstation.ui.ip;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class IPFragment extends Fragment {
    private AppNetworkRepository mNetworkRepository;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ip, container, false);

        mNetworkRepository = AppNetworkRepository.getInstance(getActivity().getApplication());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.ip_shared_preference_name), Context.MODE_PRIVATE);

        TextInputLayout ipAddressTextInputLayout = view.findViewById(R.id.til_ip_address);
        String ipAddress = sharedPreferences.getString(getString(R.string.ip_key), null);
        if (ipAddress != null) {
            ipAddressTextInputLayout.getEditText().setText(ipAddress);
        }

        MaterialButton saveMaterialButton = view.findViewById(R.id.mbtn_save);

        saveMaterialButton.setOnClickListener(view1 -> {
            String newIpAddress = ipAddressTextInputLayout.getEditText().getText().toString();
            if (newIpAddress.isEmpty()) {
                ipAddressTextInputLayout.setError(getString(R.string.empty_ip_address_error));
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.ip_key), newIpAddress).apply();

                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();

                mNetworkRepository.resetIp(newIpAddress);

                ((MainActivity) getActivity()).onSupportNavigateUp();
            }
        });

        return view;
    }
}