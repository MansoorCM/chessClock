package com.example.chessclock.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TimeControlDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTime(TimeControl control);

    @Query("DELETE FROM time_control")
    void deleteAll();

    @Query("SELECT * FROM time_control ORDER BY name ASC")
    LiveData<List<TimeControl>> getAll();

}
