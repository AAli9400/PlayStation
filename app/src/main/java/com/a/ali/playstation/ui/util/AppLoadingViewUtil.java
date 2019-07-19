package com.a.ali.playstation.ui.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.a.ali.playstation.R;

public class AppLoadingViewUtil {
    private Animatable2Compat mLogoAnimatable2Compat;
    private AnimatedVectorDrawableCompat mDrawableCompat;
    private ImageView mLoadingImageView;

    public AppLoadingViewUtil(Context context, ImageView loadingImageView) {
        mDrawableCompat = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_controller_animator);
        mLogoAnimatable2Compat = mDrawableCompat;

        mLoadingImageView = loadingImageView;
    }

    public AnimatedVectorDrawableCompat getDrawable() {
        return mDrawableCompat;
    }

    public void show() {
        mLoadingImageView.setVisibility(View.VISIBLE);

        if (mLogoAnimatable2Compat != null) {
            mLogoAnimatable2Compat.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    mLogoAnimatable2Compat.start();
                }
            });
        }
    }

    public void hide() {
        mLogoAnimatable2Compat.stop();
        mLoadingImageView.setVisibility(View.GONE);
    }
}
