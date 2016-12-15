package org.literacyapp.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import org.literacyapp.receiver.ScreenOnReceiver;

public class ScreenOnService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(getClass().getName(), "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(getClass().getName(), "onStartCommand");

        ScreenOnReceiver screenOnReceiver = new ScreenOnReceiver();
        registerReceiver(screenOnReceiver, new IntentFilter("android.intent.action.SCREEN_ON"));

        // TODO: keep running after application is closed/killed

        return super.onStartCommand(intent, flags, startId);
    }
}
