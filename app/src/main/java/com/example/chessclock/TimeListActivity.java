package com.example.chessclock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.chessclock.data.TimeControl;
import com.example.chessclock.data.TimeViewModel;
import com.example.chessclock.recyclerview.TimeListAdapter;

import java.util.List;

/*shows the list of time controls stored in the database in a recylerview and allows the
user to select a time control to use in the clock(MainActivity) or add a new timecontrol
by navigating to the AddTimeActivity class*/
public class TimeListActivity extends AppCompatActivity implements TimeListAdapter.onClickItemListener {

    private static final int NEW_TIME_CONTROL_REQUEST_CODE = 1;
    public static final String EDIT_KEY = "edit_item";
    private static final int EDIT_TIME_CONTROL_REQUEST_CODE = 2;
    RecyclerView recyclerView;
    TimeListAdapter adapter;
    TimeViewModel viewModel;
    UIViewModel uiViewModel;
    static TimeControl selectedTimeControl;
    static int editId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_list);

        viewModel = new ViewModelProvider(this).get(TimeViewModel.class);
        uiViewModel = new ViewModelProvider(this).get(UIViewModel.class);

        recyclerView = findViewById(R.id.recyclerview);
        adapter = new TimeListAdapter(TimeListActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel.getmAlltimes().observe(this, words -> {
            adapter.setmAlltimes(words);
            Log.d("TAG", "database data changed");
            for (TimeControl timeControl : words) {
                Log.d("TAG", timeControl.getName());
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            assert data != null;
            if (data.hasExtra(AddTimeActivity.EXTRA_REPLY)) {
                String[] arrayExtra = data.getStringArrayExtra(AddTimeActivity.EXTRA_REPLY);
                assert arrayExtra != null;
                TimeControl timeControl = new TimeControl(arrayExtra[0], arrayExtra[1], arrayExtra[2], arrayExtra[3]);
                if (requestCode == NEW_TIME_CONTROL_REQUEST_CODE) {
                    viewModel.insert(timeControl);
                } else if (requestCode == EDIT_TIME_CONTROL_REQUEST_CODE) {
                    timeControl.setId(editId);
                    viewModel.update(timeControl);
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(this, AddTimeActivity.class);
            startActivityForResult(intent, NEW_TIME_CONTROL_REQUEST_CODE);
        } else if (item.getItemId() == R.id.action_edit) {
            if (selectedTimeControl != null) {
                Log.d("TAG", "edit clicked");
                Intent intent = new Intent(this, AddTimeActivity.class);
                intent.putExtra(EDIT_KEY, new String[]{String.valueOf(selectedTimeControl.getId()),
                        selectedTimeControl.getName(), selectedTimeControl.getTime(),
                        selectedTimeControl.getIncrement(), selectedTimeControl.getMode()});
                editId = selectedTimeControl.getId();
                Log.d("TAG", "from edit to addActivity");
                selectedTimeControl = null;
                startActivityForResult(intent, EDIT_TIME_CONTROL_REQUEST_CODE);
            } else {
                Toast.makeText(this, "select a time control", Toast.LENGTH_SHORT).show();
            }

        } else if (item.getItemId() == R.id.action_delete) {
            //deletes a time control fron the database
            if (selectedTimeControl != null) {
                viewModel.delete(selectedTimeControl);
                selectedTimeControl = null;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickrowItem(TimeControl timeControl) {
//        Log.d("TAG",String.valueOf(timeControl.getId()+" "+timeControl.getName()
//                +" "+timeControl.getMode())+" "+timeControl.getTime());
        selectedTimeControl = timeControl;
        // Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
    }

    public void onSelect(View view) {
        if (selectedTimeControl != null) {
            Intent replyIntent = new Intent();
            replyIntent.putExtra("timecontrol", new String[]{selectedTimeControl.getName(),
                    selectedTimeControl.getTime(), selectedTimeControl.getMode(),
                    selectedTimeControl.getIncrement()});
            setResult(RESULT_OK, replyIntent);
            //uiViewModel.setTimeControl(selectedTimeControl);
            selectedTimeControl = null;
            finish();
        } else {
            Toast.makeText(this, "Select a time control", Toast.LENGTH_SHORT).show();
        }
    }
}
