package com.a.ali.playstation.data.repository;

import android.app.Application;

import com.a.ali.playstation.util.AppExecutors;

public abstract class AppRepository {
    protected AppExecutors mAppExecutors;
    protected Application mApplication;

    protected AppRepository(Application mApplication) {
        this.mApplication = mApplication;

        this.mAppExecutors = AppExecutors.getInstance();
    }
}