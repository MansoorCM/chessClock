package com.example.chessclock.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TimeControl.class},version = 2,exportSchema = false)
abstract class TimeRoomDatabase extends RoomDatabase{
    abstract TimeControlDao timeDao();
    private static final int num_of_threads=4;
    private static TimeRoomDatabase INSTANCE;
    static final ExecutorService executorService=
            Executors.newFixedThreadPool(num_of_threads);

    private static RoomDatabase.Callback callback=new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            executorService.execute(()->{
                TimeControlDao dao=INSTANCE.timeDao();
                Log.d("TAG","data deleted");
                dao.deleteAll();
                TimeControl control = new TimeControl("5\'blitz","00:05:00","Fischer", "00:00:00");
                dao.insertTime(control);
                Log.d("TAG","data inserted");
                control = new TimeControl("world blitz","00:03:00","Fischer", "00:00:02");
                dao.insertTime(control);
                control = new TimeControl("world rapid","00:15:00","Fischer", "00:00:10");
                dao.insertTime(control);

            });
        }
    };


    static TimeRoomDatabase getINSTANCE(Context context)
    {
        if(INSTANCE==null)
        {
            synchronized (TimeRoomDatabase.class)
            {
                if(INSTANCE==null)
                {
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                            TimeRoomDatabase.class, "word_database")
                            .addCallback(callback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }

        }
        return INSTANCE;
    }
}
