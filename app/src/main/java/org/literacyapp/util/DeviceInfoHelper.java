package org.literacyapp.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DeviceInfoHelper {

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceModel(Context context) {
        String deviceModel = "";
        try {
            deviceModel = URLEncoder.encode(Build.MODEL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(DeviceInfoHelper.class, "Build.MODEL: " + Build.MODEL, e);
        }
        return deviceModel;
    }
}
