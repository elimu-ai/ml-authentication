package org.literacyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.literacyapp.authentication.CameraViewActivity;

public class ScreenOnReceiver extends BroadcastReceiver {
    public ScreenOnReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LiteracyApplication literacyApplication = (LiteracyApplication) context.getApplicationContext();
        if (literacyApplication.TEST_MODE){
            Intent cameraViewIntent = new Intent(context, CameraViewActivity.class);
            cameraViewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(cameraViewIntent);
        }
    }
}
