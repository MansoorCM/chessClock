package com.example.chessclock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    static TextView playerOne;
    static TextView playerTwo;
    ImageView settings, pause, refresh;
    public static long one = 30000, two = 30000;
    long timeOne = one, timeTwo = two;
    public static CountDownTimer timerOne, timerTwo;
    public static boolean finished = false, oneisplaying = false, twoisplaying = false;
    public static String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerOne = findViewById(R.id.playerOne);
        playerTwo = findViewById(R.id.playerTwo);
        settings = findViewById(R.id.settings);
        pause = findViewById(R.id.pause);
        refresh = findViewById(R.id.refresh);

        if(TimeControlActivity.newTime)
        {
            oneisplaying = false;
            twoisplaying = false;
            if (timerOne != null) {
                timerOne.cancel();
            }
            if (timerTwo != null) {
                timerTwo.cancel();
            }
            TimeControlActivity.newTime=false;
        }
        initialize();


    }

    public static void initialize() {
        playerOne.setText(getTextFromTime(one));
        playerTwo.setText(getTextFromTime(two));
    }

    private void startFirstTimer() {
        timerOne = new CountDownTimer(timeOne, 1000) {

            public void onTick(long millisUntilFinished) {
                one = millisUntilFinished;
                playerOne.setText(getTextFromTime(one));
                Log.d(TAG, "one is: " + (one / 1000));
            }

            public void onFinish() {
                playerOne.setText(R.string.done);
                finished = true;
            }
        };
    }


    public static long getTimeFromText(String text)
    {
        int hour= (text.charAt(0)-'0')*10+(text.charAt(1)-'0');
        int minute= (text.charAt(3)-'0')*10+(text.charAt(4)-'0');
        int second= (text.charAt(6)-'0')*10+(text.charAt(7)-'0');

            Log.d("TAG", String.valueOf(hour));
        Log.d("TAG",String.valueOf(minute));
        Log.d("TAG", String.valueOf(second));


        Log.d("TAG",hour+":"+minute+":"+second);
        long time=hour*60*60+minute*60+
                second;
        return time*1000;
    }

    private void startSecondTimer() {
        timerTwo = new CountDownTimer(timeTwo, 1000) {

            public void onTick(long millisUntilFinished) {
                two = millisUntilFinished;
                playerTwo.setText(getTextFromTime(two));
                Log.d(TAG, "two is: " + (two / 1000));
            }

            public void onFinish() {
                playerTwo.setText(R.string.done);
                finished = true;
                pause.setVisibility(View.INVISIBLE);
            }
        };
    }

    private static String getTextFromTime(long time) {
        int val = (int) (time / 1000);
        String hour=String.valueOf(val/(60*60));
        val%=60*60;
        String min = String.valueOf(val / 60);
        String second = String.valueOf(val % 60);
//        if(min.length()==1)
//        {
//            min="0"+min;
//        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        //Log.d(TAG,min + ":" + second);
        if (hour.equals("0"))
        {
            return min + ":" + second;
        }else
        {
            return hour+":"+min + ":" + second;
        }




    }

    //player one clicked his clock.this stops his and starts his opponents clock.
    public void clickone(View view) {
        if(mode!=null)
        {
            Log.d("TAG",mode);
        }
        if (!oneisplaying && !twoisplaying) {
            pause.setVisibility(View.VISIBLE);
        }
        if (!finished && (oneisplaying || !twoisplaying)) {
            oneisplaying = false;
            twoisplaying = true;
            if (timerOne != null) {
                timerOne.cancel();
                playerOne.setBackgroundColor(getResources().getColor(R.color.colorgrey));
            }
            timeTwo = two;
            startSecondTimer();
            playerTwo.setBackgroundColor(getResources().getColor(R.color.lightGreenSelect));
            timerTwo.start();
        }

    }

    //player two clicked his clock.
    public void clickTwo(View view) {
        if (!oneisplaying && !twoisplaying) {
            pause.setVisibility(View.VISIBLE);
        }
        if (!finished && (twoisplaying || !oneisplaying)) {
            twoisplaying = false;
            oneisplaying = true;
            if (timerTwo != null) {
                timerTwo.cancel();
                playerTwo.setBackgroundColor(getResources().getColor(R.color.colorgrey));
            }
            timeOne = one;
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
        oneisplaying = false;
        twoisplaying = false;
        pause.setVisibility(View.INVISIBLE);
        playerOne.setBackgroundColor(getResources().getColor(R.color.colorgrey));
        playerTwo.setBackgroundColor(getResources().getColor(R.color.colorgrey));
    }

    //time reset to the starting value.
    public void refreshclicked(View view) {
        pauseclicked(pause);
        new AlertDialog.Builder(view.getContext())
                .setTitle("Reset the clock?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        oneisplaying = false;
                        twoisplaying = false;
                        if (timerOne != null) {
                            timerOne.cancel();
                        }
                        if (timerTwo != null) {
                            timerTwo.cancel();
                        }
                        one = 30000;
                        two = 30000;
                        timeOne = one;
                        timeTwo = two;
                        initialize();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void settingsclicked(View view) {
        Intent intent=new Intent(this,TimeControlActivity.class);
        startActivity(intent);
    }
}
