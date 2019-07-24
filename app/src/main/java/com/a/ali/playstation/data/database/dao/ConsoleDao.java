package com.a.ali.playstation.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.a.ali.playstation.data.model.Console;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ConsoleDao {
    @Query("SELECT * FROM Console")
    List<Console> selectAll();

    @Query("SELECT * FROM Console")
    LiveData<List<Console>> selectAllLiveData();

    @Insert(onConflict = REPLACE)
    void insertAll(List<Console> consoles);

    @Query("DELETE FROM Console")
    void deleteAll();

    @Query("SELECT id FROM Console WHERE dev_code = :dev_code")
    LiveData<Integer> getId(String dev_code);
}