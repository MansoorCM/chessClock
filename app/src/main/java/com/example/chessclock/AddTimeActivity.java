package com.example.chessclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ikovac.timepickerwithseconds.MyTimePickerDialog;


public class AddTimeActivity extends AppCompatActivity {
    TextView addTime, addIncrement;
    EditText enterName;
    String mode;
    int id;
    public static final String EXTRA_REPLY="timeControl";
    int timeHour=0;
    int timeMin=15;
    int timeSec=0;
    int incHour=0;
    int incMin=0;
    int incSec=10;
    RadioGroup radioGroup;
    boolean editMode=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time);

        addTime = findViewById(R.id.tv_entertime);
        addIncrement = findViewById(R.id.tv_enter_increment);
        enterName=findViewById(R.id.editTextName);
        radioGroup=findViewById(R.id.radiogroup);

        Intent intent=getIntent();
        if(intent.hasExtra(TimeListActivity.EDIT_KEY))
        {
            String[] arrayExtra=intent.getStringArrayExtra(TimeListActivity.EDIT_KEY);
            assert arrayExtra != null;
            id= Integer.parseInt(arrayExtra[0]);
            enterName.setText(arrayExtra[1]);
            addTime.setText(arrayExtra[2]);
            int[] time=getTimeFromText(arrayExtra[2]);
            timeHour=time[0];
            timeMin=time[1];
            timeSec=time[2];
            addIncrement.setText(arrayExtra[3]);
            int[] inctime=getTimeFromText(arrayExtra[3]);
            incHour=inctime[0];
            incMin=inctime[1];
            incSec=inctime[2];
            mode=arrayExtra[4];
            Log.d("TAG","edit reached");
            Toast.makeText(this, "edit reached", Toast.LENGTH_SHORT).show();



            if (mode.equals(getString(R.string.bronstein)))
            {
                radioGroup.check(R.id.bronstein);
            }else if (mode.equals(getString(R.string.delay)))
            {
                radioGroup.check(R.id.delay);
            }else if (mode.equals(getString(R.string.fischer)))
            {
                radioGroup.check(R.id.fischer);
            }
            editMode=true;
        }else
        {
            editMode=false;
        }


        addTime.setOnClickListener(view -> {
            MyTimePickerDialog mTimePicker = new MyTimePickerDialog(AddTimeActivity.this,
                    (view12, hourOfDay, minute, seconds) -> addTime.setText(String.valueOf(hourOfDay + ":" +
                            minute + ":" + seconds)), timeHour, timeMin, timeSec, true);
            mTimePicker.show();
        });
        addIncrement.setOnClickListener(view -> {
            MyTimePickerDialog mTimePicker = new MyTimePickerDialog(AddTimeActivity.this,
                    (view1, hourOfDay, minute, seconds) -> addIncrement.setText(String.valueOf(hourOfDay + ":" +
                            minute + ":" + seconds)), incHour, incMin, incSec, true);
            mTimePicker.show();
        });
    }


    private int[] getTimeFromText(String text)
    {
        int hour= (text.charAt(0)-'0')*10+(text.charAt(1)-'0');
        int minute= (text.charAt(3)-'0')*10+(text.charAt(4)-'0');
        int second= (text.charAt(6)-'0')*10+(text.charAt(7)-'0');
        return new int[]{hour,minute,second};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newitem,menu);
        if(editMode)
        {
            menu.findItem(R.id.action_save).setTitle(R.string.update);
        }else
        {
            menu.findItem(R.id.action_save).setTitle(R.string.save);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_save)
        {
            String name=enterName.getText().toString();
            if(name.equals(""))
            {
                Toast.makeText(this, "Enter a name for the new time control"
                        , Toast.LENGTH_SHORT).show();
            }else if (mode==null)
            {
                Toast.makeText(this, "Select the game mode"
                        , Toast.LENGTH_SHORT).show();
            }else
            {
                if(addTime.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Set the game time"
                            , Toast.LENGTH_SHORT).show();
                }else if(addIncrement.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Set the increment time"
                            , Toast.LENGTH_SHORT).show();
                }else
                {
                    Log.d("increment",addIncrement.getText().toString());
                    String time=correctTimeFormat(addTime.getText().toString());
                    String increment=correctTimeFormat(addIncrement.getText().toString());
                    //TimeControl timeControl=new TimeControl(name,time,mode,increment);
                    Intent replyIntent=new Intent();
                    replyIntent.putExtra(EXTRA_REPLY,new String[]{name,time,mode,increment});
                    setResult(RESULT_OK,replyIntent);
                    //viewModel.insert(timeControl);
                    finish();
                }

            }



        }
        return super.onOptionsItemSelected(item);
    }

    public String correctTimeFormat(String time)
    {
        Log.d("TAG",time);
        String[] array=time.split(":");
        if(array[0].length()==1)
        {
            array[0]="0".concat(array[0]);
        }
        if(array[1].length()==1)
        {
            array[1]="0".concat(array[1]);
        }
        if(array[2].length()==1)
        {
            array[2]="0".concat(array[2]);
        }
        Log.d("TAG",array[0]+":"+array[1]+":"+array[2]);
        return array[0]+":"+array[1]+":"+array[2];
        }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked.
        switch (view.getId()) {
            case R.id.bronstein:
                if (checked)
                {
                    mode= String.valueOf(((RadioButton) view).getText());
                    Log.d("TAG",mode);
                    Toast.makeText(this, mode, Toast.LENGTH_SHORT).show();
                }

                    // Same day service

                break;
            case R.id.delay:
                if (checked)
                {
                    mode= String.valueOf(((RadioButton) view).getText());
                    Log.d("TAG",mode);
                    Toast.makeText(this, mode, Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.fischer:
                if (checked)
                {
                    mode= String.valueOf(((RadioButton) view).getText());
                    Log.d("TAG",mode);
                    Toast.makeText(this, mode, Toast.LENGTH_SHORT).show();
                }


                break;
            default:
                // Do nothing.
                break;
        }
    }
}
