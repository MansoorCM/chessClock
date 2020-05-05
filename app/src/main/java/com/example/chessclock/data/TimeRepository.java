package com.example.chessclock.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TimeRepository {
    private TimeControlDao mTimeDao;
    private LiveData<List<TimeControl>> mAllTimes;

    TimeRepository(Application application)
    {
        TimeRoomDatabase database=TimeRoomDatabase.getINSTANCE(application);
        mTimeDao=database.timeDao();
        mAllTimes=mTimeDao.getAll();
    }

    LiveData<List<TimeControl>> getmAllTimes() {
        return mAllTimes;
    }
    void insert(TimeControl control)
    {
        TimeRoomDatabase.executorService.execute(()->{mTimeDao.insertTime(control);});
    }
}
