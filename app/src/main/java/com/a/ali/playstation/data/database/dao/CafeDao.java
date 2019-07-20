package com.a.ali.playstation.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.a.ali.playstation.data.model.CafeOrder;
import com.a.ali.playstation.data.model.Console;

import java.util.List;

@Dao
public interface CafeDao {
    @Query("SELECT * FROM CafeOrder WHERE consoleId = :consoleId")
    LiveData<List<CafeOrder>> selectByConsoleId(int consoleId);

    @Insert
    void insertAll(List<CafeOrder> cafeOrders);

    @Query("DELETE FROM CafeOrder WHERE consoleId")
    void deleteAll();
}