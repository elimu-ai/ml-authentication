package org.literacyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

/**
 * If more than 30 minutes have passed since the last StudentImageCollection event, open iamge
 * collection activity.
 */
public class ScreenOnReceiver extends BroadcastReceiver {

    private static final String PREF_LAST_STUDENT_IMAGE_COLLECTION = "pref_last_student_image_collection";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(getClass().getName(), "onReceive");

        Calendar calendarLastCollectionEvent = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long timeOfLastCollectionInMillis = sharedPreferences.getLong(PREF_LAST_STUDENT_IMAGE_COLLECTION, 0);
        if (timeOfLastCollectionInMillis > 0) {
            calendarLastCollectionEvent = Calendar.getInstance();
            calendarLastCollectionEvent.setTimeInMillis(timeOfLastCollectionInMillis);
            Log.i(getClass().getName(), "calendarLastCollectionEvent.getTime(): " + calendarLastCollectionEvent.getTime());
        }
        Calendar calendar30MinutesAgo = Calendar.getInstance();
        calendar30MinutesAgo.add(Calendar.MINUTE, -30);
        Log.i(getClass().getName(), "calendar30MinutesAgo.getTime(): " + calendar30MinutesAgo.getTime());
        if ((calendarLastCollectionEvent == null) || (calendarLastCollectionEvent.before(calendar30MinutesAgo))) {
//            Intent studentImageCollectionIntent = new Intent(context, StudentImageCollectionActivity.class);
//            studentImageCollectionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(studentImageCollectionIntent);
//
//            sharedPreferences.edit().putLong(PREF_LAST_STUDENT_IMAGE_COLLECTION, Calendar.getInstance().getTimeInMillis()).commit();
        }
    }
}
