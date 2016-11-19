package org.literacyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.literacyapp.service.ScreenOnService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(getClass().getName(), "onReceive");

//        // Automatically open application
//        Intent bootIntent = new Intent(context, MainActivity.class);
//        bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(bootIntent);

        // Start service for detecting when the screen is turned on
        Intent screenOnServiceIntent = new Intent(context, ScreenOnService.class);
        context.startService(screenOnServiceIntent);
    }
}
