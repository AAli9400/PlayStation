package com.a.ali.playstation.ui.login;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.model.User;
import com.a.ali.playstation.data.repository.AppNetworkRepository;
import com.a.ali.playstation.ui.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private TextInputLayout mUserNameTextInputLayout, mPasswordTextInputLayout;
    private MaterialButton mForgotPasswordButton, mLoginButton;

    private Animatable2Compat mLogoAnimatable2Compat;

    private AppNetworkRepository mAppNetworkRepository;
    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        if (getActivity().getSharedPreferences(
                getString(R.string.user_shared_preferences_name), Context.MODE_PRIVATE
        ).getString(getString(R.string.username_key), null) != null) {
            ((MainActivity) getActivity()).navigateToLogin();
        }

        View view = inflater.inflate(R.layout.fragment_login, parent, false);

        mContext = getContext();
        mAppNetworkRepository = AppNetworkRepository.getInstance(getActivity().getApplication());


        mUserNameTextInputLayout = view.findViewById(R.id.til_user_name);
        mPasswordTextInputLayout = view.findViewById(R.id.til_password);

        mForgotPasswordButton = view.findViewById(R.id.mbtn_forget_password);
        mLoginButton = view.findViewById(R.id.mbtn_login);

        ImageView imageView = view.findViewById(R.id.imageView);

        AnimatedVectorDrawableCompat drawableCompat = AnimatedVectorDrawableCompat.create(mContext, R.drawable.avd_controller_animator);
        imageView.setImageDrawable(drawableCompat);

        mLoginButton = view.findViewById(R.id.mbtn_login);

        mLogoAnimatable2Compat = drawableCompat;
        mLoginButton.setOnClickListener(view1 -> {
            if (mLogoAnimatable2Compat != null) {
                mLogoAnimatable2Compat.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        mLogoAnimatable2Compat.start();
                    }
                });

                validateUserInfo();
            }
        });

//        (Navigation.createNavigateOnClickListener(
//                R.id.action_login_to_home, null));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        isIPEntered();
    }

    private void isIPEntered() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.ip_shared_preference_name), Context.MODE_PRIVATE);
        String ipAddress = sharedPreferences.getString(getString(R.string.ip_key), null);
        if (ipAddress != null) {
        } else {
            Navigation.findNavController(getActivity(), R.id.navHostFragment)
                    .navigate(R.id.action_login_to_Ip);
        }
    }

    private boolean validateUserInfo() {
        mLogoAnimatable2Compat.start();

        String userName = mUserNameTextInputLayout.getEditText().getText().toString();
        if (userName.isEmpty()) {
            mUserNameTextInputLayout.setError(getString(R.string.username_required));
            mLogoAnimatable2Compat.stop();
            return false;
        } else {
            mUserNameTextInputLayout.setError(null);

            String password = mPasswordTextInputLayout.getEditText().getText().toString();
            if (password.isEmpty()) {
                mPasswordTextInputLayout.setError(getString(R.string.password_required));
                mLogoAnimatable2Compat.stop();

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

                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                                            getString(R.string.user_shared_preferences_name), Context.MODE_PRIVATE
                                    );

                                    sharedPreferences.edit()
                                            .putString(getString(R.string.username_key), userName)
                                            .putString(getString(R.string.user_title_key), user.getUserTitle())
                                            .apply();

                                    ((MainActivity) getActivity()).navigateToLogin();
                                } else {
                                    mPasswordTextInputLayout.setError(getString(R.string.wrong_password));
                                }
                            }
                        }
                        if (!isUserExists) {
                            mUserNameTextInputLayout.setError(getString(R.string.username_not_found));
                        }
                    }

                    mLogoAnimatable2Compat.stop();
                });
    }
}