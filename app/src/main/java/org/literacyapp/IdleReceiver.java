package org.literacyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.literacyapp.util.Log;

public class IdleReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(getClass(), "onReceive");

        Intent idleIntent = new Intent(context, MainActivity.class);
        idleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(idleIntent);
    }
}
