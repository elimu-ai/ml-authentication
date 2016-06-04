package org.literacyapp.deviceadmin;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import org.literacyapp.util.Log;

import java.io.IOException;

public class DeviceAdmin extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.d(getClass(), "onEnabled");
        super.onEnabled(context, intent);
    }

    @Override
    public void onLockTaskModeEntering(Context context, Intent intent, String pkg) {
        Log.d(getClass(), "onLockTaskModeEntering");
        super.onLockTaskModeEntering(context, intent, pkg);
    }

    @Override
    public void onLockTaskModeExiting(Context context, Intent intent) {
        Log.d(getClass(), "onLockTaskModeExiting");
        super.onLockTaskModeExiting(context, intent);
    }
}
