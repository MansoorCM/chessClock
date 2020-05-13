package com.example.chessclock.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TimeViewModel extends AndroidViewModel {
    private TimeRepository mTimeRepository;
    private LiveData<List<TimeControl>> mAllTimes;

    public TimeViewModel(@NonNull Application application) {
        super(application);
        mTimeRepository = new TimeRepository(application);
        mAllTimes = mTimeRepository.getmAllTimes();
    }

    public LiveData<List<TimeControl>> getmAlltimes() {
        return mAllTimes;
    }
    public void insert(TimeControl timeControl)
    {
        mTimeRepository.insert(timeControl);
    }
    public void update(TimeControl timeControl)
    {
        mTimeRepository.update(timeControl);
    }
    public void delete(TimeControl timeControl)
    {
        mTimeRepository.delete(timeControl);
    }
    public TimeControl getByName(String name){ return mTimeRepository.getByName(name);}
}
