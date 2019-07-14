package com.a.ali.playstation.data.network.networkUtil;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static final int THREAD_COUNT = 3;
    private static AppExecutors ourInstance = null;
    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static AppExecutors getInstance() {
        if (ourInstance == null) {
            ourInstance = new AppExecutors(
                    Executors.newSingleThreadExecutor(),
                    Executors.newFixedThreadPool(THREAD_COUNT),
                    new MainThreadExecutor()
            );
        }
        return ourInstance;
    }

    public void executeOnMainThread(Runnable runnable) {
        ourInstance.mainThread.execute(runnable);
    }

    public void executeOnNetworkThread(Runnable runnable) {
        ourInstance.networkIO.execute(runnable);
    }

    public void executeOnDiskIOThread(Runnable runnable) {
        ourInstance.diskIO.execute(runnable);
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}