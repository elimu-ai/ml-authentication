package org.literacyapp.util;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

public class DeviceIdHelper extends Application{

    private static String device_id;

    public static String getDeviceid(Context context){

        device_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return device_id;
    }


}
