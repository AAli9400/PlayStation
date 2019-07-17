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
}
