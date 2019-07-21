package com.a.ali.playstation.ui.ip;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class IPActivity extends AppCompatActivity {
    private AppNetworkRepository mNetworkRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNetworkRepository = AppNetworkRepository.getInstance(getApplication());

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.ip_shared_preference_name), Context.MODE_PRIVATE);

        TextInputLayout ipAddressTextInputLayout = findViewById(R.id.til_ip_address);
        String ipAddress = sharedPreferences.getString(getString(R.string.ip_key), null);
        if (ipAddress != null) {
            ipAddressTextInputLayout.getEditText().setText(ipAddress);
        }

        MaterialButton saveMaterialButton = findViewById(R.id.mbtn_save);

        saveMaterialButton.setOnClickListener(view1 -> {
            String newIpAddress = ipAddressTextInputLayout.getEditText().getText().toString();
            if (newIpAddress.isEmpty()) {
                ipAddressTextInputLayout.setError(getString(R.string.empty_ip_address_error));
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.ip_key), newIpAddress).apply();

                Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();

                mNetworkRepository.resetIp(newIpAddress);

                this.finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
