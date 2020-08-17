package com.example.chessclock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chessclock.data.TimeControl;

import static com.example.chessclock.UIViewModel.getTextFromTime;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TIME_SELECT_REQUEST = 1;
    TextView playerOne;
    TextView playerTwo;
    ImageView settings, pause, refresh;

    TimeControl storedtimeControl;
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

            /*  if user has selected a time control,it will be passed to the viewmodel, else the default
            time control in the viewmodel will be used.*/
            if (storedtimeControl != null) {
                viewModel.timeControl = storedtimeControl;
            }
            viewModel.initialize();
        }

        viewModel.paused.observe(this, ispaused -> {
            if (!ispaused) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });
        viewModel.timeOne.observe(this, aLong ->
                {
                    if (aLong == 0) {
                        playerOne.setText("");
                        playerOne.setBackgroundResource(R.drawable.ic_flag);
                    } else {
                        playerOne.setText(getTextFromTime(aLong));
                    }

                }
        );
        viewModel.timeTwo.observe(this, aLong -> playerTwo.setText(getTextFromTime(aLong)));

        viewModel.pauseVisible.observe(this, visible ->
        {
            if (visible)
                pause.setVisibility(View.VISIBLE);
            else
                pause.setVisibility(View.INVISIBLE);
        });

        viewModel.oneisplaying.observe(this, playing -> {
            if (playing) {
                playerOne.setBackgroundColor(getResources().getColor(R.color.lightGreenSelect));
            } else {
                playerOne.setBackgroundColor(getResources().getColor(R.color.colorgrey));
            }
        });

        viewModel.twoisplaying.observe(this, playing -> {
            if (playing) {
                playerTwo.setBackgroundColor(getResources().getColor(R.color.lightGreenSelect));
            } else {
                playerTwo.setBackgroundColor(getResources().getColor(R.color.colorgrey));
            }
        });
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
                viewModel.initialize();

            }

        }
    }

    //player one clicked his clock.this function stops his clock and starts his opponents clock.
    public void clickone(View view) { viewModel.firstPlayerClick(); }

    //player two clicked his clock.this function stops his clock and starts his opponents clock.
    public void clickTwo(View view) { viewModel.secondPlayerClicked(); }

    public void pauseclicked(View view) { viewModel.pauseClickHelper(); }


    //time reset to the starting value.
    public void refreshclicked(View view) {
        pauseclicked(pause);
        viewModel.firstmove = true;

        new AlertDialog.Builder(view.getContext())
                .setTitle("Reset the clock?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> viewModel.onclickPositiveButton())

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


    public void settingsclicked(View view) {
        pauseclicked(pause);
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, TIME_SELECT_REQUEST);
    }
}
