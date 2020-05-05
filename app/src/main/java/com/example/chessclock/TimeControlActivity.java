package com.example.chessclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chessclock.data.TimeControl;
import com.example.chessclock.data.TimeViewModel;
import com.example.chessclock.recyclerview.TimeListAdapter;

import java.util.List;

public class TimeControlActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TimeListAdapter adapter;
    TimeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_control);

        viewModel= new ViewModelProvider(this).get(TimeViewModel.class);

        recyclerView=findViewById(R.id.recyclerview);
        adapter=new TimeListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel.getmAlltimes().observe(this, new Observer<List<TimeControl>>() {
            @Override
            public void onChanged(List<TimeControl> words) {
                adapter.setmAlltimes(words);
                Log.d("TAG","database data changed");
                for(TimeControl timeControl:words)
                {
                    Log.d("TAG",timeControl.getName());
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_add)
        {
            Toast.makeText(this, "add clicked!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
