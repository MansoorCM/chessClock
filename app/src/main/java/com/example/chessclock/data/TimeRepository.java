package com.example.chessclock.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    void delete(TimeControl control)
    {
        TimeRoomDatabase.executorService.execute(()->{mTimeDao.delete(control);});
    }

    void update(TimeControl control)
    {
        TimeRoomDatabase.executorService.execute(()->{mTimeDao.update(control);});
    }
    TimeControl getByName(String name)
    {
        Future future=TimeRoomDatabase.executorService.submit(()->{
            TimeControl data=mTimeDao.selectByName(name);
        });
        TimeControl timeControl=null;
        if(future.isDone())
        {
            try {
                timeControl= (TimeControl) future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        return timeControl;
    }
}
