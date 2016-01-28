package org.literacyapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent bootintent = new Intent(context, MainActivity.class);
        bootintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(bootintent);
    }
}
