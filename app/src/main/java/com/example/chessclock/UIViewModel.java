package com.example.chessclock;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.chessclock.data.TimeControl;

import java.util.Objects;

public class UIViewModel extends AndroidViewModel {
    boolean firsttime=true;
    boolean firstmove=true;
    MutableLiveData<TimeControl> timeControl=new MutableLiveData<TimeControl>();
    //MutableLiveData<TimeControl> sample=new MutableLiveData<TimeControl>();
    //TimeControl control=new TimeControl("bullet_sample","00:01:00",, "00:00:01");
    boolean oneisplaying;
    boolean twoisplaying;
    boolean finished;
    boolean paused;

    long timeOne;
    long timeTwo;
    long timeOneStart;
    long timeTwoStart;
    long increment;

    String mode;



//    UIViewModel()
//    {
//        if(timeControl.getValue()==null)
//        {
//            timeControl.setValue(new TimeControl("bullet_sample","00:01:00",
//                    "Fischer", "00:00:01"));
//        }
//        initialize();
//    }

    public UIViewModel(@NonNull Application application) {
        super(application);
        if(timeControl.getValue()==null)
        {
            timeControl.setValue(new TimeControl("bullet_sample","00:01:00",
                    "Fischer", "00:00:01"));
        }
        initialize();
    }


    void initialize()
    {
        oneisplaying=false;
        twoisplaying=false;
        finished=false;
        paused=false;
        firstmove=true;
        firsttime=true;
//        if(timeControl==null)
//        {
//            timeOne=(long) 60000;
//            timeTwo=(long) 60000;
//            increment=(long) 1000;
//            mode="Fischer";
//        }else
//        {
            timeOne=getTimeFromText(Objects.requireNonNull(timeControl.getValue()).getTime());
            timeTwo=getTimeFromText(timeControl.getValue().getTime());
            increment=getTimeFromText(timeControl.getValue().getIncrement());
            mode=timeControl.getValue().getMode();
       // }
        timeOneStart=timeOne;
        timeTwoStart=timeTwo;

    }

    private static long getTimeFromText(String text) {
        int hour = (text.charAt(0) - '0') * 10 + (text.charAt(1) - '0');
        int minute = (text.charAt(3) - '0') * 10 + (text.charAt(4) - '0');
        int second = (text.charAt(6) - '0') * 10 + (text.charAt(7) - '0');

      /*  Log.d("TAG", String.valueOf(hour));
        Log.d("TAG", String.valueOf(minute));
        Log.d("TAG", String.valueOf(second));


        Log.d("TAG", hour + ":" + minute + ":" + second);*/
        long time = hour * 60 * 60 + minute * 60 +
                second;
        return time * 1000;
    }
    public String getMode()
    {
      return mode;
    }

    public boolean getFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished= finished;
    }

    public boolean getPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.finished= paused;
    }

    public boolean getOneisplaying() {
        return oneisplaying;
    }

    public void setOneisplaying(boolean oneisplaying) {
        this.oneisplaying= oneisplaying;
    }

    public boolean getTwoisplaying() {
        return twoisplaying;
    }

    public void setTwoisplaying(boolean twoisplaying) {
        this.twoisplaying= twoisplaying;
    }

    public long getIncrement() {
        return increment;
    }

    public long getTimeOne() {
        return timeOne;
    }

    public void setTimeOne(long timeOne) {
        this.timeOne= timeOne;
        timeOneStart=timeOne;
    }

    public long getTimeTwo() {
        return timeTwo;
    }

    public void setTimeTwo(long timeTwo) {
        this.timeTwo= timeTwo;
        timeTwoStart=timeTwo;
    }

    public long getTimeOneStart() {
        return timeOneStart;
    }

    public void setTimeOneStart(long timeOne) {
        this.timeOneStart= timeOne;
    }

    public long getTimeTwoStart() {
        return timeTwoStart;
    }

    public void setTimeTwoStart(long timeTwo) {
        this.timeTwoStart= timeTwo;
    }
    public void setTimeControl(TimeControl timeControl) {
        this.timeControl.setValue(timeControl);
        initialize();
    }

    public LiveData<TimeControl> getTimeControl() {

            return timeControl;

    }
}
