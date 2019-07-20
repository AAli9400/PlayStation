package com.a.ali.playstation.data.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.a.ali.playstation.data.database.AppDatabase;
import com.a.ali.playstation.data.database.dao.ConsoleDao;
import com.a.ali.playstation.data.model.Console;

import java.util.List;

public class AppDatabaseRepository extends AppRepository {
    private ConsoleDao mConsoleDao;

    private static AppDatabaseRepository mInstance = null;

    public static synchronized AppDatabaseRepository getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new AppDatabaseRepository(application);
        }
        return mInstance;
    }

    private AppDatabaseRepository(Application application) {
        super(application);

        AppDatabase appDatabase = AppDatabase.getInstance(application);
        mConsoleDao = appDatabase.consoleDao();
    }

    public LiveData<List<Console>> selectAllLiveData() {
        return mConsoleDao.selectAllLiveData();
    }

    public void deleteAllConsolesData() {
        mAppExecutors.executeOnDiskIOThread(() -> mConsoleDao.deleteAll());
    }

    public void insertConsoles(@NonNull List<Console> consoles) {
        mAppExecutors.executeOnDiskIOThread(() -> mConsoleDao.insertAll(consoles));
    }
}