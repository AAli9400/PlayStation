package com.a.ali.playstation.data.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.a.ali.playstation.data.database.AppDatabase;
import com.a.ali.playstation.data.database.dao.CafeDao;
import com.a.ali.playstation.data.database.dao.ConsoleDao;
import com.a.ali.playstation.data.model.CafeOrder;
import com.a.ali.playstation.data.model.Console;

import java.util.List;

public class AppDatabaseRepository extends AppRepository {
    private ConsoleDao mConsoleDao;
    private CafeDao mCafeDao;

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
        mCafeDao = appDatabase.cafeDao();
    }

    public LiveData<List<Console>> selectAllConsolesLiveData() {
        return mConsoleDao.selectAllLiveData();
    }

    public List<Console> selectAllConsoles() {
        return mConsoleDao.selectAll();
    }

    public void deleteAllConsolesData() {
        mAppExecutors.executeOnDiskIOThread(() -> mConsoleDao.deleteAll());
    }

    public void insertConsoles(@NonNull List<Console> consoles) {
        mAppExecutors.executeOnDiskIOThread(() -> mConsoleDao.insertAll(consoles));
    }

    public void deleteAllCafeOrders() {
        mAppExecutors.executeOnDiskIOThread(() -> mCafeDao.deleteAll());
    }

    public void insertAllCafeOrders(@NonNull List<CafeOrder> cafeOrders) {
        mAppExecutors.executeOnDiskIOThread(() -> mCafeDao.insertAll(cafeOrders));
    }

    public LiveData<List<CafeOrder>> selectOrders(@NonNull int id) {
        return mCafeDao.selectByConsoleId(id);
    }

    public LiveData<Integer> getConsoleId(@NonNull String dev_code) {
        return mConsoleDao.getId(dev_code);
    }
}