package org.literacyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.literacyapp.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(getClass().getName(), "onReceive");

        Intent bootIntent = new Intent(context, MainActivity.class);
        bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(bootIntent);
    }
}
