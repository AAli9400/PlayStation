package com.a.ali.playstation.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.a.ali.playstation.data.model.CafeOrder;

import java.util.List;

@Dao
public interface CafeDao {
    @Query("SELECT * FROM CafeOrder WHERE deviceCode = :consoleCode")
    LiveData<List<CafeOrder>> selectByConsoleCode(String consoleCode);

    @Insert
    void insertAll(List<CafeOrder> cafeOrders);

    @Query("DELETE FROM CafeOrder")
    void deleteAll();

    @Query("DELETE FROM CafeOrder WHERE  deviceCode = :consoleCode")
    void deleteAll(String consoleCode);
}