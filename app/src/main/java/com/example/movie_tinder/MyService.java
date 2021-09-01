package com.example.movie_tinder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyService extends Service {

    @Override
    public void onCreate() {
        Friends_frag.newInstance("","").lwClick();
        return;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_NOT_STICKY;



    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
