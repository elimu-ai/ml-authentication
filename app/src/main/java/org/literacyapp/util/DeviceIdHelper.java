package org.literacyapp.util;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

public class DeviceIdHelper extends Application{

    private static String deviceId;

    public static String getDeviceid(Context context){

        deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return deviceId;
    }


}
