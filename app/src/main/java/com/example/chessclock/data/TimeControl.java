package com.example.chessclock.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "time_control")
public class TimeControl {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;
    @NonNull
    @ColumnInfo(name = "name")
    private String mName;
    @NonNull
    @ColumnInfo(name = "time")
    private String mTime;
    @NonNull
    @ColumnInfo(name = "mode")
    private String mMode;
    @NonNull
    @ColumnInfo(name = "increment")
    private String mIncrement;

    public TimeControl(@NonNull String mName, @NonNull String mTime, @NonNull String mode, @NonNull String mIncrement) {
        this.mName = mName;
        this.mTime = mTime;
        this.mMode = mode;
        this.mIncrement = mIncrement;
    }

    public String getName() {
        return mName;
    }

    public String getTime() {
        return mTime;
    }

    public String getMode() {
        return mMode;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    @NonNull
    public String getIncrement() {
        return mIncrement;
    }
}
