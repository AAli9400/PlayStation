package com.a.ali.playstation.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.a.ali.playstation.data.model.Console;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface ConsoleDao {
    @Query("SELECT * FROM Console")
    List<Console> selectAll();

    @Query("SELECT * FROM Console")
    LiveData<List<Console>> selectAllLiveData();

    @Query("SELECT * FROM Console WHERE dev_code = :consoleCode")
    List<Console> selectByConsoleCode(String consoleCode);

    @Insert(onConflict = IGNORE)
    void insert(Console console);

    @Insert
    void insertAll(List<Console> consoles);

    @Update(onConflict = IGNORE)
    void delete(Console console);

    @Query("DELETE FROM Console")
    void deleteAll();
}