package com.a.ali.playstation.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.model.User;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.console.ConsoleActivity;
import com.a.ali.playstation.ui.ip.IPActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mUserNameTextInputLayout, mPasswordTextInputLayout;
    private MaterialButton mForgotPasswordButton, mLoginButton;

//    private Animatable2Compat mLogoAnimatable2Compat;

    private AppNetworkRepository mAppNetworkRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSharedPreferences(
                getString(R.string.user_shared_preferences_name), Context.MODE_PRIVATE
        ).getString(getString(R.string.username_key), null) != null) {
            startActivity(new Intent(this, ConsoleActivity.class));
            this.finish();
        }

        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        mAppNetworkRepository = AppNetworkRepository.getInstance(getApplication());


        mUserNameTextInputLayout = findViewById(R.id.til_user_name);
        mPasswordTextInputLayout = findViewById(R.id.til_password);

        mForgotPasswordButton = findViewById(R.id.mbtn_forget_password);
        mLoginButton = findViewById(R.id.mbtn_login);

//        ImageView imageView = findViewById(R.id.imageView);
//        AnimatedVectorDrawableCompat drawableCompat = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_controller_animator);
//        imageView.setImageDrawable(drawableCompat)
//        mLogoAnimatable2Compat = drawableCompat;

        mLoginButton = findViewById(R.id.mbtn_login);

        mLoginButton.setOnClickListener(view1 -> validateUserInfo());

        MaterialButton resetIpAddressMaterialButton = findViewById(R.id.mbtn_reset_ip);
        resetIpAddressMaterialButton.setOnClickListener(view ->
                startActivity(new Intent(this, IPActivity.class)));

    }

    @Override
    public void onStart() {
        super.onStart();

        isIPEntered();
    }

    private void isIPEntered() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.ip_shared_preference_name), Context.MODE_PRIVATE);
        String ipAddress = sharedPreferences.getString(getString(R.string.ip_key), null);
        if (ipAddress == null) {
            startActivity(new Intent(this, IPActivity.class));
        }
    }

    private boolean validateUserInfo() {
//        mLogoAnimatable2Compat.start();

        String userName = mUserNameTextInputLayout.getEditText().getText().toString();
        if (userName.isEmpty()) {
            mUserNameTextInputLayout.setError(getString(R.string.username_required));
//            mLogoAnimatable2Compat.stop();
            return false;
        } else {
            mUserNameTextInputLayout.setError(null);

            String password = mPasswordTextInputLayout.getEditText().getText().toString();
            if (password.isEmpty()) {
                mPasswordTextInputLayout.setError(getString(R.string.password_required));
//                mLogoAnimatable2Compat.stop();

                return false;
            } else {
                mPasswordTextInputLayout.setError(null);

                login(userName, password);
            }
        }

        return true;
    }

    private void login(@NonNull String userName, @NonNull String password) {
        mAppNetworkRepository.getAllUsers()
                .observe(this, response -> {
                    if (response != null) {
                        boolean isUserExists = false;
                        for (User user : response) {
                            if (user.getUserN().equals(userName)) {
                                mUserNameTextInputLayout.setError(null);
                                isUserExists = true;
                                if (user.getPW().equals(password)) {
                                    mPasswordTextInputLayout.setError(null);

                                    SharedPreferences sharedPreferences = getSharedPreferences(
                                            getString(R.string.user_shared_preferences_name), Context.MODE_PRIVATE
                                    );

                                    sharedPreferences.edit()
                                            .putString(getString(R.string.username_key), userName)
                                            .putString(getString(R.string.user_title_key), user.getUserTitle())
                                            .apply();

                                    startActivity(new Intent(this, ConsoleActivity.class));
                                    this.finish();
                                } else {
                                    mPasswordTextInputLayout.setError(getString(R.string.wrong_password));
                                }
                            }
                        }
                        if (!isUserExists) {
                            mUserNameTextInputLayout.setError(getString(R.string.username_not_found));
                        }
                    }

//                    mLogoAnimatable2Compat.stop();
                });
    }
}
