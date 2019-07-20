package com.a.ali.playstation.data.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.a.ali.playstation.data.database.dao.CafeDao;
import com.a.ali.playstation.data.database.dao.ConsoleDao;
import com.a.ali.playstation.data.model.CafeOrder;
import com.a.ali.playstation.data.model.Console;

@Database(entities = {Console.class, CafeOrder.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ConsoleDao consoleDao();

    private static AppDatabase mInstance = null;
    public abstract CafeDao cafeDao();

    @NonNull
    public static synchronized AppDatabase getInstance(@NonNull Application application) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(
                    application, AppDatabase.class, "PlAyStAtIoN.db"
            ).build();
        }
        return mInstance;
    }
}