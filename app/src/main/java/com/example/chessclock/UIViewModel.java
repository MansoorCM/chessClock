package com.example.chessclock;

import android.app.Application;
import android.os.CountDownTimer;
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
    public static CountDownTimer timerOne, timerTwo, delayTimer;

    MutableLiveData<Boolean> oneisplaying=new MutableLiveData<>();
    MutableLiveData<Boolean> twoisplaying=new MutableLiveData<>();
    boolean finished;
    MutableLiveData<Boolean> paused=new MutableLiveData<>();
    MutableLiveData<Boolean> pauseVisible=new MutableLiveData<>();

    MutableLiveData<Long> timeOne=new MutableLiveData<>();
    MutableLiveData<Long> timeTwo=new MutableLiveData<>();
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

        oneisplaying.setValue(false);
        twoisplaying.setValue(false);
        finished = false;
        paused.setValue(false);
        firstmove = true;

        timeOne.setValue(getTimeFromText(Objects.requireNonNull(timeControl).getTime()));
        timeTwo.setValue(getTimeFromText(timeControl.getTime()));
        increment = getTimeFromText(timeControl.getIncrement());
        mode = timeControl.getMode();

        timeOneStart = timeOne.getValue();
        timeTwoStart = timeTwo.getValue();

    }

    private static long getTimeFromText(String text) {
        int hour = (text.charAt(0) - '0') * 10 + (text.charAt(1) - '0');
        int minute = (text.charAt(3) - '0') * 10 + (text.charAt(4) - '0');
        int second = (text.charAt(6) - '0') * 10 + (text.charAt(7) - '0');

        long time = hour * 60 * 60 + minute * 60 +
                second;
        return time * 1000;
    }

    //getting the text of the time inorder to display in the textview
    static String getTextFromTime(long time) {
        int val = (int) (time / 1000);
        String hour = String.valueOf(val / (60 * 60));
        val %= 60 * 60;
        String min = String.valueOf(val / 60);
        String second = String.valueOf(val % 60);

        if (second.length() == 1) {
            second = "0" + second;
        }
        if (hour.equals("0")) {
            return min + ":" + second;
        } else {
            return hour + ":" + min + ":" + second;
        }

    }

    public void setTimeControl(TimeControl timeControl) {
        Log.i("init viewmodel", "settimecontrol");
        this.timeControl = timeControl;

        initialize();
    }


    private void startFirstTimer() {
        timerOne = new CountDownTimer(timeOneStart, 1000) {

            public void onTick(long millisUntilFinished) {
                timeOne.setValue( millisUntilFinished);
                //playerOne.setText(getTextFromTime(viewModel.timeOne));
            }

            public void onFinish() {
                timeOne.setValue(0L);

                finished = true;
                pauseVisible.setValue(false);
            }
        };
    }

    private void startSecondTimer() {
        timerTwo = new CountDownTimer(timeTwoStart, 1000) {


            public void onTick(long millisUntilFinished) {
                timeTwo.setValue( millisUntilFinished);
                //playerTwo.setText(getTextFromTime(viewModel.timeTwo));
            }

            public void onFinish() {
                timeTwo.setValue(0L);
                //finished = true;
                finished = true;
                pauseVisible.setValue(false);
            }
        };
    }



    void startdelayTimer() {
        delayTimer = new CountDownTimer(increment, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (oneisplaying.getValue()) {
                    startFirstTimer();
                    timerOne.start();
                } else if (twoisplaying.getValue()) {
                    startSecondTimer();
                    timerTwo.start();
                }
            }
        };
    }

    void firstPlayerClick() {
        if (paused.getValue()) {
            startGameFromPauseState();
        } else {
            if (mode != null) {
                Log.d("TAG", mode);
            }
            if (!oneisplaying.getValue() && !twoisplaying.getValue()) {
                pauseVisible.setValue(true);
            }
            if (!finished && (oneisplaying.getValue() || !twoisplaying.getValue())) {

                oneisplaying.setValue( false);

                twoisplaying.setValue( true);
                assert mode != null;
                if (mode.equals("Delay")) {
                    if (delayTimer != null) {
                        delayTimer.cancel();
                    }
                }
                if (timerOne != null) {
                    timerOne.cancel();

                }

                assert mode != null;
                if (mode.equals("Fischer")) {
                    if (firstmove) {
                        firstmove = false;
                        timeOneStart = timeOne.getValue();
                    } else {
                        timeOneStart = timeOne.getValue() + increment;
                        timeOne.setValue(timeOneStart);
                    }

                } else if (mode.equals("Bronstein")) {
                    timeOneStart = Math.max(timeOne.getValue() + increment
                            , timeOneStart);
                    timeOne.setValue(timeOneStart);
                }


                //timeTwo = two;
                if (mode.equals("Delay")) {
                    startdelayTimer();
                    delayTimer.start();
                    timeOneStart = timeOne.getValue();
                } else {
                    startSecondTimer();
                    timerTwo.start();
                }


            }
        }
    }

    void startGameFromPauseState() {
        paused.setValue(false);
        pauseVisible.setValue(true);

        if (oneisplaying.getValue()) {
            startFirstTimer();
            timerOne.start();
        } else if (twoisplaying.getValue()) {
            startSecondTimer();
            timerTwo.start();
        }
    }

    void secondPlayerClicked() {
        if (paused.getValue()) {
            startGameFromPauseState();
        } else {
            if (!oneisplaying.getValue() && !twoisplaying.getValue()) {
                pauseVisible.setValue(true);
            }
            if (!finished && (twoisplaying.getValue() || !oneisplaying.getValue())) {
                twoisplaying.setValue(false);
                oneisplaying.setValue(true);
                if (mode.equals("Delay")) {
                    if (delayTimer != null) {
                        delayTimer.cancel();
                    }
                }
                if (timerTwo != null) {
                    timerTwo.cancel();
                }

                assert mode != null;
                if (mode.equals("Fischer")) {
                    if (firstmove) {
                        firstmove = false;
                        timeTwoStart = timeTwo.getValue();
                    } else {
                        timeTwoStart = timeTwo.getValue() + increment;
                        timeTwo.setValue(timeTwoStart);
                    }

                } else if (mode.equals("Bronstein")) {
                    timeTwoStart = Math.max(timeTwo.getValue() + increment,
                            timeTwoStart);
                    timeTwo.setValue(timeTwoStart);
                }

                if (mode.equals("Delay")) {
                    startdelayTimer();
                    delayTimer.start();
                    timeTwoStart = timeTwo.getValue();
                } else {
                    startFirstTimer();
                    timerOne.start();
                }

            }

        }
    }

    void pauseClickHelper() {
        paused.setValue(true);
        timeOneStart = timeOne.getValue();
        timeTwoStart = timeTwo.getValue();
        if (timerOne != null) {
            timerOne.cancel();
        }
        if (timerTwo != null) {
            timerTwo.cancel();
        }

        pauseVisible.setValue(false);
    }

    void onclickPositiveButton() {
        oneisplaying.setValue( false);
        twoisplaying.setValue( false);

        if (timerOne != null) {
            timerOne.cancel();
        }
        if (timerTwo != null) {
            timerTwo.cancel();
        }

        initialize();
    }
}
