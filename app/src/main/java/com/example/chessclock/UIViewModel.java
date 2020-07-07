package com.example.chessclock;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.example.chessclock.data.TimeControl;

import java.util.Objects;

public class UIViewModel extends AndroidViewModel {
    boolean firsttime = false;
    boolean firstmove = true;
    TimeControl timeControl = new TimeControl("bullet_sample", "00:01:00",
            "Fischer", "00:00:01");

    boolean oneisplaying;
    boolean twoisplaying;
    boolean finished;
    MutableLiveData<Boolean> paused=new MutableLiveData<>();

    long timeOne;
    long timeTwo;
    long timeOneStart;
    long timeTwoStart;
    long increment;

    String mode;
    int notNull;

    public UIViewModel(@NonNull Application application) {
        super(application);

    }


    void initialize() {

        notNull = 1;

        oneisplaying = false;
        twoisplaying = false;
        finished = false;
        paused.setValue(false);
        firstmove = true;

        timeOne = getTimeFromText(Objects.requireNonNull(timeControl).getTime());
        timeTwo = getTimeFromText(timeControl.getTime());
        increment = getTimeFromText(timeControl.getIncrement());
        mode = timeControl.getMode();

        timeOneStart = timeOne;
        timeTwoStart = timeTwo;

    }

    private static long getTimeFromText(String text) {
        int hour = (text.charAt(0) - '0') * 10 + (text.charAt(1) - '0');
        int minute = (text.charAt(3) - '0') * 10 + (text.charAt(4) - '0');
        int second = (text.charAt(6) - '0') * 10 + (text.charAt(7) - '0');

        long time = hour * 60 * 60 + minute * 60 +
                second;
        return time * 1000;
    }

    public void setTimeControl(TimeControl timeControl) {
        Log.i("init viewmodel", "settimecontrol");
        this.timeControl = timeControl;

        initialize();
    }

}
