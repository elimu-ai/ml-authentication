package org.literacyapp.util;

import org.literacyapp.model.enums.Environment;

public class Log {

    public static void d(Class clazz, String msg) {
        if (EnvironmentSettings.ENVIRONMENT != Environment.PROD) {
            android.util.Log.d(clazz.getName(), msg);
        }
    }

    public static void w(Class clazz, String msg) {
        if (EnvironmentSettings.ENVIRONMENT != Environment.PROD) {
            android.util.Log.w(clazz.getName(), msg);
        }
    }

    public static void e(Class clazz, String msg, Throwable tr) {
        if (EnvironmentSettings.ENVIRONMENT != Environment.PROD) {
            android.util.Log.e(clazz.getName(), msg, tr);
        }
    }
}
