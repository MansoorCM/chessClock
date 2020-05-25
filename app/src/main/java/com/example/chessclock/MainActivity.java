package com.example.chessclock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chessclock.data.TimeControl;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TIME_SELECT_REQUEST = 1;
    TextView playerOne;
    TextView playerTwo;
    ImageView settings, pause, refresh;
    //private SharedPreferences mSharedPreferences;
    private static final String NAMEKEY="nameKey";
    private static final String TIMEKEY="timekey";
    private static final String INCKEY="incKey";
    private static final String MODEKEY="modeKey";
    TimeControl storedtimeControl;
    //    public static long one, two;
//    long timeOne, timeTwo;
//    long increment;
    public static CountDownTimer timerOne, timerTwo,delayTimer;
    //    boolean finished, oneisplaying, twoisplaying;
//    String mode;
    UIViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerOne = findViewById(R.id.playerOne);
        playerTwo = findViewById(R.id.playerTwo);
        settings = findViewById(R.id.settings);
        pause = findViewById(R.id.pause);
        refresh = findViewById(R.id.refresh);

//        timeControl=new TimeControl(mSharedPreferences.getString(NAMEKEY,"bullet_sample"),
//                mSharedPreferences.getString(TIMEKEY,"00:01:00"),
//                mSharedPreferences.getString(MODEKEY,"Fischer"),
//                mSharedPreferences.getString(INCKEY,"00:00:01"));
        viewModel = new ViewModelProvider(this).get(UIViewModel.class);
        if(viewModel.notNull!=1)
        {
            Log.i("init","reached viewmodel first time");
            //viewModel.timeControl=timeControl;
            if(storedtimeControl!=null)
            {
                viewModel.timeControl=storedtimeControl;
            }
            initialize();
        }else
            {
                Log.i("init","reached viewmodel already exists");
            }

//        viewModel.getTimeControl().observe(this, new Observer<TimeControl>() {
//            @Override
//            public void onChanged(TimeControl timeControl) {
////                if(viewModel.notNull!=-1)
////                {
////                    viewModel.notNull=-1;
////                }else
////                {
//                    Log.i("init","refresh clicked");
//                    refreshclicked(refresh);
//               // }
//
//
//
//            }
//        });

//        Intent replyIntent=getIntent();
//        if(replyIntent.get)
//        Log.d("TAG", String.valueOf(viewModel.oneisplaying));
//        Log.d("TAG", String.valueOf(viewModel.twoisplaying));
//        if(!viewModel.oneisplaying && !viewModel.twoisplaying)
//        {
//            Log.d("TAG","No one is playing");
//            initialize();
//        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseclicked(pause);
//        SharedPreferences.Editor editor=mSharedPreferences.edit();
//        editor.putString(NAMEKEY,viewModel.timeControl.getName());
//        editor.putString(TIMEKEY,viewModel.timeControl.getTime());
//        editor.putString(MODEKEY,viewModel.timeControl.getMode());
//        editor.putString(INCKEY,viewModel.timeControl.getIncrement());
//        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGameFromPauseState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==TIME_SELECT_REQUEST)
        {
            if(resultCode==RESULT_OK)
            {
                assert data != null;
                String[] time=data.getStringArrayExtra("timecontrol");
                assert time != null;
                TimeControl timeControl=new TimeControl(time[0],time[1],time[2],time[3]);
               // viewModel.firsttime=true;
                storedtimeControl=timeControl;
                viewModel.setTimeControl(timeControl);
                initialize();

            }

        }
    }

    void initialize() {
        viewModel.initialize();
        Log.d("TAG init", String.valueOf(viewModel.oneisplaying));
        Log.d("TAG init", String.valueOf(viewModel.twoisplaying));
//        finished=viewModel.finished;
//        oneisplaying=viewModel.oneisplaying;
//        twoisplaying=viewModel.twoisplaying;
//        timeOne=viewModel.timeOneStart;
//        timeTwo=viewModel.timeTwoStart;
//        one=viewModel.timeOne;
//        two=viewModel.timeTwo;
//        increment=viewModel.increment;
//        mode=viewModel.mode;
        playerOne.setText(getTextFromTime(viewModel.timeOneStart));
        playerTwo.setText(getTextFromTime(viewModel.timeTwoStart));
    }

    private void startFirstTimer() {
        timerOne = new CountDownTimer(viewModel.timeOneStart, 1000) {

            public void onTick(long millisUntilFinished) {
                //one = millisUntilFinished;
                viewModel.timeOne = millisUntilFinished;
                playerOne.setText(getTextFromTime(viewModel.timeOne));
                //Log.d(TAG, "one is: " + (one / 1000));
            }

            public void onFinish() {
                playerOne.setText("");
                playerOne.setBackgroundResource(R.drawable.ic_flag);
                //finished = true;
                viewModel.finished = true;
                pause.setVisibility(View.INVISIBLE);
            }
        };
    }


    public static long getTimeFromText(String text) {
        int hour = (text.charAt(0) - '0') * 10 + (text.charAt(1) - '0');
        int minute = (text.charAt(3) - '0') * 10 + (text.charAt(4) - '0');
        int second = (text.charAt(6) - '0') * 10 + (text.charAt(7) - '0');

        Log.d("TAG", String.valueOf(hour));
        Log.d("TAG", String.valueOf(minute));
        Log.d("TAG", String.valueOf(second));


        Log.d("TAG", hour + ":" + minute + ":" + second);
        long time = hour * 60 * 60 + minute * 60 +
                second;
        return time * 1000;
    }

    private void startSecondTimer() {
        timerTwo = new CountDownTimer(viewModel.timeTwoStart, 1000) {


            public void onTick(long millisUntilFinished) {
                //two = millisUntilFinished;
                viewModel.timeTwo = millisUntilFinished;
                playerTwo.setText(getTextFromTime(viewModel.timeTwo));
                //Log.d(TAG, "two is: " + (two / 1000));
            }

            public void onFinish() {
                playerTwo.setText("");
                playerTwo.setBackgroundResource(R.drawable.ic_flag);
                //finished = true;
                viewModel.finished = true;
                pause.setVisibility(View.INVISIBLE);
            }
        };
    }

    private static String getTextFromTime(long time) {
        int val = (int) (time / 1000);
        String hour = String.valueOf(val / (60 * 60));
        val %= 60 * 60;
        String min = String.valueOf(val / 60);
        String second = String.valueOf(val % 60);
//        if(min.length()==1)
//        {
//            min="0"+min;
//        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        if (hour.equals("0")) {
            return min + ":" + second;
        } else {
            return hour + ":" + min + ":" + second;
        }

    }

    void startdelayTimer()
    {
        delayTimer = new CountDownTimer(viewModel.increment, 1000) {

            public void onTick(long millisUntilFinished) {
                //two = millisUntilFinished;
//                viewModel.timeTwo = millisUntilFinished;
//                playerTwo.setText(getTextFromTime(viewModel.timeTwo));
                //Log.d(TAG, "two is: " + (two / 1000));
            }

            public void onFinish() {
//                playerTwo.setText("");
//                playerTwo.setBackgroundResource(R.drawable.ic_flag);
//                //finished = true;
//                viewModel.finished = true;
//                pause.setVisibility(View.INVISIBLE);
                if(viewModel.oneisplaying)
                {
                    startFirstTimer();
                    timerOne.start();
                }else if(viewModel.twoisplaying)
                {
                    startSecondTimer();
                    timerTwo.start();
                }
            }
        };
    }

    //player one clicked his clock.this function stops his clock and starts his opponents clock.
    public void clickone(View view) {
        if (viewModel.paused) {
            startGameFromPauseState();
        } else {
            if (viewModel.mode != null) {
                Log.d("TAG", viewModel.mode);
            }
            if (!viewModel.oneisplaying && !viewModel.twoisplaying) {
                pause.setVisibility(View.VISIBLE);
            }
            if (!viewModel.finished && (viewModel.oneisplaying || !viewModel.twoisplaying)) {
                //oneisplaying = false;
                viewModel.oneisplaying = false;
                //twoisplaying = true;
                viewModel.twoisplaying = true;
                assert viewModel.mode != null;
                if(viewModel.mode.equals("Delay"))
                {
                    if(delayTimer!=null)
                    {
                        delayTimer.cancel();
                    }
                }
                if (timerOne != null) {
                    timerOne.cancel();
                    playerOne.setBackgroundColor(getResources().getColor(R.color.colorgrey));
                }

                assert viewModel.mode != null;
                if (viewModel.mode.equals("Fischer")) {
                    if(viewModel.firstmove)
                    {
                     viewModel.firstmove=false;
                     viewModel.timeOneStart=viewModel.timeOne;
                    }else
                    {
                        viewModel.timeOneStart = viewModel.timeOne + viewModel.increment;
                    }

                } else if (viewModel.mode.equals("Bronstein")) {
                    viewModel.timeOneStart = Math.max(viewModel.timeOne + viewModel.increment
                            , viewModel.timeOneStart);
                }


                //timeTwo = two;
                if(viewModel.mode.equals("Delay"))
                {
                    startdelayTimer();
                    delayTimer.start();
                    viewModel.timeOneStart=viewModel.timeOne;
                }else
                {
                    playerOne.setText(getTextFromTime(viewModel.timeOneStart));
                    startSecondTimer();

                    timerTwo.start();
                }

                playerTwo.setBackgroundColor(getResources().getColor(R.color.lightGreenSelect));
            }
        }


    }

    void startGameFromPauseState() {
        viewModel.paused = false;
        pause.setVisibility(View.VISIBLE);
        playerOne.setText(getTextFromTime(viewModel.timeOneStart));
        playerTwo.setText(getTextFromTime(viewModel.timeTwoStart));
        if (viewModel.oneisplaying) {
            startFirstTimer();
            timerOne.start();
            playerOne.setBackgroundColor(getResources().getColor(R.color.lightGreenSelect));
        } else if (viewModel.twoisplaying) {
            startSecondTimer();
            timerTwo.start();
            playerTwo.setBackgroundColor(getResources().getColor(R.color.lightGreenSelect));
        }
    }

    //player two clicked his clock.this function stops his clock and starts his opponents clock.
    public void clickTwo(View view) {
        if (viewModel.paused) {
            startGameFromPauseState();
        } else {
            if (!viewModel.oneisplaying && !viewModel.twoisplaying) {
                pause.setVisibility(View.VISIBLE);
            }
            if (!viewModel.finished && (viewModel.twoisplaying || !viewModel.oneisplaying)) {
                viewModel.twoisplaying = false;
                viewModel.oneisplaying = true;
                if(viewModel.mode.equals("Delay"))
                {
                    if(delayTimer!=null)
                    {
                        delayTimer.cancel();
                    }
                }
                if (timerTwo != null) {
                    timerTwo.cancel();
                    playerTwo.setBackgroundColor(getResources().getColor(R.color.colorgrey));
                }

                assert viewModel.mode != null;
                if (viewModel.mode.equals("Fischer")) {
                    if(viewModel.firstmove)
                    {
                        viewModel.firstmove=false;
                        viewModel.timeTwoStart=viewModel.timeTwo;
                    }else
                    {
                        viewModel.timeTwoStart = viewModel.timeTwo + viewModel.increment;
                    }

                } else if (viewModel.mode.equals("Bronstein")) {
                    viewModel.timeTwoStart = Math.max(viewModel.timeTwo + viewModel.increment,
                            viewModel.timeTwoStart);
                }

                //timeOne = one;
                if(viewModel.mode.equals("Delay"))
                {
                    startdelayTimer();
                    delayTimer.start();
                    viewModel.timeTwoStart=viewModel.timeTwo;
                }else{
                    playerTwo.setText(getTextFromTime(viewModel.timeTwoStart));
                    startFirstTimer();
                    timerOne.start();
                }

                playerOne.setBackgroundColor(getResources().getColor(R.color.lightGreenSelect));

            }

        }

    }

    public void pauseclicked(View view) {
        viewModel.paused = true;
        viewModel.timeOneStart = viewModel.timeOne;
        viewModel.timeTwoStart = viewModel.timeTwo;
        if (timerOne != null) {
            timerOne.cancel();
        }
        if (timerTwo != null) {
            timerTwo.cancel();
        }
//        viewModel.oneisplaying = false;
//        viewModel.twoisplaying = false;
        pause.setVisibility(View.INVISIBLE);
//        if(!viewModel.finished)
//        {
//            playerOne.setBackgroundColor(getResources().getColor(R.color.colorgrey));
//            playerTwo.setBackgroundColor(getResources().getColor(R.color.colorgrey));
//        }

    }

    //time reset to the starting value.
    public void refreshclicked(View view) {
        pauseclicked(pause);
        viewModel.firstmove=true;
//        if (viewModel.firsttime) {
//            viewModel.firsttime = false;
//            onclickPositiveButton();
//        } else {
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Reset the clock?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            onclickPositiveButton();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
     //   }

    }

    void onclickPositiveButton() {
        viewModel.oneisplaying = false;
        viewModel.twoisplaying = false;
        playerOne.setBackgroundColor(getResources().getColor(R.color.colorgrey));
        playerTwo.setBackgroundColor(getResources().getColor(R.color.colorgrey));
        if (timerOne != null) {
            timerOne.cancel();
        }
        if (timerTwo != null) {
            timerTwo.cancel();
        }
//                        one = 10000;
//                        two = 10000;
//                        timeOne = one;
//                        timeTwo = two;
        initialize();
    }

    public void settingsclicked(View view) {
        pauseclicked(pause);
        Intent intent = new Intent(this, TimeListActivity.class);
        startActivityForResult(intent,TIME_SELECT_REQUEST);
        //startActivity(intent);
    }
}
