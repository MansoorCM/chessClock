package com.example.chessclock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chessclock.data.TimeControl;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    TextView playerOne;
    TextView playerTwo;
    ImageView settings, pause, refresh;
//    public static long one, two;
//    long timeOne, timeTwo;
//    long increment;
    public static CountDownTimer timerOne, timerTwo;
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

        viewModel=new ViewModelProvider(this).get(UIViewModel.class);
        viewModel.getTimeControl().observe(this, new Observer<TimeControl>() {
            @Override
            public void onChanged(TimeControl timeControl) {
                refreshclicked(refresh);
            }
        });

        initialize();


    }

    void initialize() {
        viewModel.initialize();
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
                viewModel.timeOne=millisUntilFinished;
                playerOne.setText(getTextFromTime(viewModel.timeOne));
                //Log.d(TAG, "one is: " + (one / 1000));
            }

            public void onFinish() {
                playerOne.setText("");
                playerOne.setBackgroundResource(R.drawable.ic_flag);
                //finished = true;
                viewModel.finished=true;
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
                viewModel.timeTwo=millisUntilFinished;
                playerTwo.setText(getTextFromTime(viewModel.timeTwo));
                //Log.d(TAG, "two is: " + (two / 1000));
            }

            public void onFinish() {
                playerTwo.setText("");
                playerTwo.setBackgroundResource(R.drawable.ic_flag);
                //finished = true;
                viewModel.finished=true;
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

    //player one clicked his clock.this function stops his clock and starts his opponents clock.
    public void clickone(View view) {
        if (viewModel.mode != null) {
            Log.d("TAG", viewModel.mode);
        }
        if (!viewModel.oneisplaying && !viewModel.twoisplaying) {
            pause.setVisibility(View.VISIBLE);
        }
        if (!viewModel.finished && (viewModel.oneisplaying || !viewModel.twoisplaying)) {
            //oneisplaying = false;
            viewModel.oneisplaying=false;
            //twoisplaying = true;
            viewModel.twoisplaying=true;
            if (timerOne != null) {
                timerOne.cancel();
                playerOne.setBackgroundColor(getResources().getColor(R.color.colorgrey));
            }

            assert viewModel.mode != null;
            if(viewModel.mode.equals("Fischer"))
            {
                viewModel.timeOneStart=viewModel.timeOne+viewModel.increment;
            }else if(viewModel.mode.equals("Delay"))
            {
                viewModel.timeOneStart=Math.max(viewModel.timeOne+viewModel.increment
                        ,viewModel.timeOneStart);
            }

            playerOne.setText(getTextFromTime(viewModel.timeOneStart));
            //timeTwo = two;
            startSecondTimer();
            playerTwo.setBackgroundColor(getResources().getColor(R.color.lightGreenSelect));
            timerTwo.start();
        }

    }

    //player two clicked his clock.this function stops his clock and starts his opponents clock.
    public void clickTwo(View view) {
        if (!viewModel.oneisplaying && !viewModel.twoisplaying) {
            pause.setVisibility(View.VISIBLE);
        }
        if (!viewModel.finished && (viewModel.twoisplaying || !viewModel.oneisplaying)) {
            viewModel.twoisplaying = false;
            viewModel.oneisplaying = true;
            if (timerTwo != null) {
                timerTwo.cancel();
                playerTwo.setBackgroundColor(getResources().getColor(R.color.colorgrey));
            }

            assert viewModel.mode != null;
            if(viewModel.mode.equals("Fischer"))
            {
                viewModel.timeTwoStart=viewModel.timeTwo+viewModel.increment;
            }else if(viewModel.mode.equals("Delay"))
            {
                viewModel.timeTwoStart=Math.max(viewModel.timeTwo+viewModel.increment,
                        viewModel.timeTwoStart);
            }
            playerTwo.setText(getTextFromTime(viewModel.timeTwoStart));
            //timeOne = one;
            startFirstTimer();
            playerOne.setBackgroundColor(getResources().getColor(R.color.lightGreenSelect));
            timerOne.start();
        }

    }

    public void pauseclicked(View view) {
        if (timerOne != null) {
            timerOne.cancel();
        }
        if (timerTwo != null) {
            timerTwo.cancel();
        }
        viewModel.oneisplaying = false;
        viewModel.twoisplaying = false;
        pause.setVisibility(View.INVISIBLE);
        if(!viewModel.finished)
        {
            playerOne.setBackgroundColor(getResources().getColor(R.color.colorgrey));
            playerTwo.setBackgroundColor(getResources().getColor(R.color.colorgrey));
        }

    }

    //time reset to the starting value.
    public void refreshclicked(View view) {
        pauseclicked(pause);
        if(viewModel.firsttime)
        {
            viewModel.firsttime=false;
            onclickPositiveButton();
        }else
        {
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
        }

    }

    void onclickPositiveButton()
    {
        viewModel.oneisplaying = false;
        viewModel.twoisplaying = false;
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
        Intent intent = new Intent(this, TimeControlActivity.class);
        startActivity(intent);
    }
}
