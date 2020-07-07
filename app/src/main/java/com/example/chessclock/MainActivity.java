package com.example.chessclock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chessclock.data.TimeControl;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TIME_SELECT_REQUEST = 1;
    TextView playerOne;
    TextView playerTwo;
    ImageView settings, pause, refresh;

    TimeControl storedtimeControl;
    public static CountDownTimer timerOne, timerTwo, delayTimer;
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

        viewModel = new ViewModelProvider(this).get(UIViewModel.class);
        if (viewModel.notNull != 1) {
            Log.i("init", "reached viewmodel first time");

            if (storedtimeControl != null) {
                viewModel.timeControl = storedtimeControl;
            }
            initialize();
        } else {
            Log.i("init", "reached viewmodel already exists");
        }

        viewModel.paused.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean ispaused) {
                if (!ispaused) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseclicked(pause);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startGameFromPauseState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TIME_SELECT_REQUEST) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String[] time = data.getStringArrayExtra("timecontrol");
                assert time != null;
                TimeControl timeControl = new TimeControl(time[0], time[1], time[2], time[3]);
                // viewModel.firsttime=true;
                storedtimeControl = timeControl;
                viewModel.setTimeControl(timeControl);
                initialize();

            }

        }
    }

    void initialize() {
        viewModel.initialize();

        //set the initial time for both players
        playerOne.setText(getTextFromTime(viewModel.timeOneStart));
        playerTwo.setText(getTextFromTime(viewModel.timeTwoStart));
    }

    private void startFirstTimer() {
        timerOne = new CountDownTimer(viewModel.timeOneStart, 1000) {

            public void onTick(long millisUntilFinished) {
                viewModel.timeOne = millisUntilFinished;
                playerOne.setText(getTextFromTime(viewModel.timeOne));
            }

            public void onFinish() {
                playerOne.setText("");
                playerOne.setBackgroundResource(R.drawable.ic_flag);

                viewModel.finished = true;
                pause.setVisibility(View.INVISIBLE);
            }
        };
    }


    private void startSecondTimer() {
        timerTwo = new CountDownTimer(viewModel.timeTwoStart, 1000) {


            public void onTick(long millisUntilFinished) {
                viewModel.timeTwo = millisUntilFinished;
                playerTwo.setText(getTextFromTime(viewModel.timeTwo));
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

    //getting the text of the time inorder to display in the textview
    private static String getTextFromTime(long time) {
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

    void startdelayTimer() {
        delayTimer = new CountDownTimer(viewModel.increment, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (viewModel.oneisplaying) {
                    startFirstTimer();
                    timerOne.start();
                } else if (viewModel.twoisplaying) {
                    startSecondTimer();
                    timerTwo.start();
                }
            }
        };
    }

    //player one clicked his clock.this function stops his clock and starts his opponents clock.
    public void clickone(View view) {
        if (viewModel.paused.getValue()) {
            startGameFromPauseState();
        } else {
            if (viewModel.mode != null) {
                Log.d("TAG", viewModel.mode);
            }
            if (!viewModel.oneisplaying && !viewModel.twoisplaying) {
                pause.setVisibility(View.VISIBLE);
            }
            if (!viewModel.finished && (viewModel.oneisplaying || !viewModel.twoisplaying)) {

                viewModel.oneisplaying = false;

                viewModel.twoisplaying = true;
                assert viewModel.mode != null;
                if (viewModel.mode.equals("Delay")) {
                    if (delayTimer != null) {
                        delayTimer.cancel();
                    }
                }
                if (timerOne != null) {
                    timerOne.cancel();
                    playerOne.setBackgroundColor(getResources().getColor(R.color.colorgrey));
                }

                assert viewModel.mode != null;
                if (viewModel.mode.equals("Fischer")) {
                    if (viewModel.firstmove) {
                        viewModel.firstmove = false;
                        viewModel.timeOneStart = viewModel.timeOne;
                    } else {
                        viewModel.timeOneStart = viewModel.timeOne + viewModel.increment;
                    }

                } else if (viewModel.mode.equals("Bronstein")) {
                    viewModel.timeOneStart = Math.max(viewModel.timeOne + viewModel.increment
                            , viewModel.timeOneStart);
                }


                //timeTwo = two;
                if (viewModel.mode.equals("Delay")) {
                    startdelayTimer();
                    delayTimer.start();
                    viewModel.timeOneStart = viewModel.timeOne;
                } else {
                    playerOne.setText(getTextFromTime(viewModel.timeOneStart));
                    startSecondTimer();

                    timerTwo.start();
                }

                playerTwo.setBackgroundColor(getResources().getColor(R.color.lightGreenSelect));
            }
        }


    }

    void startGameFromPauseState() {
        viewModel.paused.setValue(false);
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
        if (viewModel.paused.getValue()) {
            startGameFromPauseState();
        } else {
            if (!viewModel.oneisplaying && !viewModel.twoisplaying) {
                pause.setVisibility(View.VISIBLE);
            }
            if (!viewModel.finished && (viewModel.twoisplaying || !viewModel.oneisplaying)) {
                viewModel.twoisplaying = false;
                viewModel.oneisplaying = true;
                if (viewModel.mode.equals("Delay")) {
                    if (delayTimer != null) {
                        delayTimer.cancel();
                    }
                }
                if (timerTwo != null) {
                    timerTwo.cancel();
                    playerTwo.setBackgroundColor(getResources().getColor(R.color.colorgrey));
                }

                assert viewModel.mode != null;
                if (viewModel.mode.equals("Fischer")) {
                    if (viewModel.firstmove) {
                        viewModel.firstmove = false;
                        viewModel.timeTwoStart = viewModel.timeTwo;
                    } else {
                        viewModel.timeTwoStart = viewModel.timeTwo + viewModel.increment;
                    }

                } else if (viewModel.mode.equals("Bronstein")) {
                    viewModel.timeTwoStart = Math.max(viewModel.timeTwo + viewModel.increment,
                            viewModel.timeTwoStart);
                }

                if (viewModel.mode.equals("Delay")) {
                    startdelayTimer();
                    delayTimer.start();
                    viewModel.timeTwoStart = viewModel.timeTwo;
                } else {
                    playerTwo.setText(getTextFromTime(viewModel.timeTwoStart));
                    startFirstTimer();
                    timerOne.start();
                }

                playerOne.setBackgroundColor(getResources().getColor(R.color.lightGreenSelect));

            }

        }

    }

    public void pauseclicked(View view) {
        viewModel.paused.setValue(true);
        viewModel.timeOneStart = viewModel.timeOne;
        viewModel.timeTwoStart = viewModel.timeTwo;
        if (timerOne != null) {
            timerOne.cancel();
        }
        if (timerTwo != null) {
            timerTwo.cancel();
        }

        pause.setVisibility(View.INVISIBLE);

    }

    //time reset to the starting value.
    public void refreshclicked(View view) {
        pauseclicked(pause);
        viewModel.firstmove = true;

        new AlertDialog.Builder(view.getContext())
                .setTitle("Reset the clock?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> onclickPositiveButton())

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

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

        initialize();
    }

    public void settingsclicked(View view) {
        pauseclicked(pause);
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, TIME_SELECT_REQUEST);
    }
}
