//package com.example.chessclock;
//
//import android.app.Application;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.ViewModel;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.chessclock.data.TimeControl;
//
//public class UIViewModelFactory implements ViewModelProvider.Factory {
//    private Application application;
//    private TimeControl timeControl;
//    UIViewModelFactory(Application application,TimeControl timeControl)
//    {
//        this.application=application;
//        this.timeControl=timeControl;
//    }
//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        return (T) new UIViewModel(application,timeControl);
//    }
//}
