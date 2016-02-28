package org.literacyapp.util;

import org.literacyapp.model.enums.Environment;

public class Log {

    public static void d(String tag, String msg) {
        if (EnvironmentSettings.ENVIRONMENT != Environment.PROD) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (EnvironmentSettings.ENVIRONMENT != Environment.PROD) {
            android.util.Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (EnvironmentSettings.ENVIRONMENT != Environment.PROD) {
            android.util.Log.e(tag, msg, tr);
        }
    }
}
