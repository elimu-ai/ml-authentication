package org.literacyapp.authentication.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Date;


public class StartPrefsHelper {

    private static final String PREF_FIRST_STARTUP_DATE = "first_startup_date";

    private static final long SEVEN_DAYS_IN_MILLISECONDS = 604_800_000; // 7d*24h*60m*60s*1000;

    public static boolean scheduleAfterBoot(Context context) {
        long firstStartupDate = getFirstStartupDate(context);
        long currentTime = System.currentTimeMillis();
        Log.i(StartPrefsHelper.class.getName(), "First start-up: " + new Date(firstStartupDate) + " currentTime: " + new Date(currentTime));
        if (firstStartupDate == 0) {
            storeFirstStartupDate(context);
            Log.i(StartPrefsHelper.class.getName(), "First start-up more than seven days ago: " + false);
            return false;
        } else {
            Log.i(StartPrefsHelper.class.getName(), "First start-up more than seven days ago: " + ((firstStartupDate + SEVEN_DAYS_IN_MILLISECONDS) < currentTime));
            return ((firstStartupDate + SEVEN_DAYS_IN_MILLISECONDS) < currentTime);
        }
    }

    private static long getFirstStartupDate(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getLong(PREF_FIRST_STARTUP_DATE, 0);
    }

    private static boolean storeFirstStartupDate(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.edit().putLong(PREF_FIRST_STARTUP_DATE, System.currentTimeMillis()).commit();
    }
}