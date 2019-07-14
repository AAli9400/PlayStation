package com.a.ali.playstation.data.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.a.ali.playstation.data.network.networkUtil.AppExecutors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class AppRepository {
    protected AppExecutors mAppExecutors;
    protected Application mApplication;

    protected AppRepository(Application mApplication) {
        this.mApplication = mApplication;

        this.mAppExecutors = AppExecutors.getInstance();
    }

    protected class ApiCallback<T> implements Callback<T> {
        private MutableLiveData<T> mResponse;
        private final String TAG = ApiCallback.class.getSimpleName();

        public ApiCallback(MutableLiveData<T> response) {
            super();
            mResponse = response;
        }

        @Override
        public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
            if (response.body() == null) {
                Log.e(TAG, response.code() + " // " + response.message());
                mAppExecutors.executeOnMainThread(() -> Toast.makeText(
                        mApplication,
                        response.code() + " // " + response.message(),
                        Toast.LENGTH_SHORT
                ).show());
            }

            mResponse.postValue(response.body());
        }

        @Override
        public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
            Log.e(TAG, t.getMessage());
            mAppExecutors.executeOnMainThread(() -> Toast.makeText(
                    mApplication,
                    t.getMessage(),
                    Toast.LENGTH_SHORT
            ).show());

            mResponse.postValue(null);
        }
    }
}
