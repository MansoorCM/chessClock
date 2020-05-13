package com.example.chessclock.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TimeControlDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTime(TimeControl control);

    @Query("DELETE FROM time_control")
    void deleteAll();

    @Delete
    void delete(TimeControl timeControl);

    @Update
    void update(TimeControl timeControl);

    @Query("SELECT * FROM time_control ORDER BY name ASC")
    LiveData<List<TimeControl>> getAll();

    @Query("SELECT * FROM time_control WHERE name LIKE :value LIMIT 1")
    TimeControl selectByName(String value);

}
