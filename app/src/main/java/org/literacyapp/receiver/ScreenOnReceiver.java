package org.literacyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.display.DisplayManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;

import java.util.Calendar;

/**
 * If more than 30 minutes have passed since the last StudentImageCollection event, open iamge
 * collection activity.
 */
public class ScreenOnReceiver extends BroadcastReceiver {

    private final static int MINUTES_OF_INACTIVITY_BETWEEN_SESSIONS = 30;

    private static final String PREF_LAST_STUDENT_IMAGE_COLLECTION = "pref_last_student_image_collection";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(getClass().getName(), "onReceive");

        // For some reason, this receiver triggers when the screen is turned _off_ for the first time after booting the device.
        // Detect if screen is active
        DisplayManager displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        for (Display display : displayManager.getDisplays()) {
            if (display.getState() != Display.STATE_ON) {
                return;
            }
        }

        Calendar calendarLastCollectionEvent = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long timeOfLastCollectionInMillis = sharedPreferences.getLong(PREF_LAST_STUDENT_IMAGE_COLLECTION, 0);
        if (timeOfLastCollectionInMillis > 0) {
            calendarLastCollectionEvent = Calendar.getInstance();
            calendarLastCollectionEvent.setTimeInMillis(timeOfLastCollectionInMillis);
            Log.i(getClass().getName(), "calendarLastCollectionEvent.getTime(): " + calendarLastCollectionEvent.getTime());
        }
        Calendar calendarLastTimeOfScreenOff = Calendar.getInstance();
        calendarLastTimeOfScreenOff.add(Calendar.MINUTE, -MINUTES_OF_INACTIVITY_BETWEEN_SESSIONS);
        Log.i(getClass().getName(), "calendarLastTimeOfScreenOff.getTime(): " + calendarLastTimeOfScreenOff.getTime());
//        if ((calendarLastCollectionEvent == null) || (calendarLastCollectionEvent.before(calendarLastTimeOfScreenOff))) {
//            Intent studentImageCollectionIntent = new Intent(context, StudentImageCollectionActivity.class);
//            studentImageCollectionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(studentImageCollectionIntent);
//
//            sharedPreferences.edit().putLong(PREF_LAST_STUDENT_IMAGE_COLLECTION, Calendar.getInstance().getTimeInMillis()).commit();
//        }
    }
}
