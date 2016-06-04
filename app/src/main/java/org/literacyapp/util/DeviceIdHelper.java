package org.literacyapp.util;

import android.content.Context;
import android.provider.Settings;

public class DeviceIdHelper {

    public static String getDeviceId(Context context) {
        Log.d(DeviceIdHelper.class, "onCreate");

        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
