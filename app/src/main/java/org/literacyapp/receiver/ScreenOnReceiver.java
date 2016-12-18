package org.literacyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.display.DisplayManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;

import org.literacyapp.authentication.fallback.StudentAuthenticationActivity;

import java.util.Calendar;

/**
 * If more than 30 minutes have passed since the last Student authentication, open
 * authentication activity.
 */
public class ScreenOnReceiver extends BroadcastReceiver {

    private final static int TIME_BETWEEN_AUTHENTICATIONS = 30;

    public static final String PREF_TIME_OF_LAST_AUTHENTICATION = "pref_time_of_last_authentication";

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        // Store time of last successful authentication
        Calendar calendarLastAuthentication = null;
        long timeOfLastAuthenticationInMillis = sharedPreferences.getLong(PREF_TIME_OF_LAST_AUTHENTICATION, 0);
        if (timeOfLastAuthenticationInMillis > 0) {
            calendarLastAuthentication = Calendar.getInstance();
            calendarLastAuthentication.setTimeInMillis(timeOfLastAuthenticationInMillis);
            Log.i(getClass().getName(), "calendarLastAuthenticationEvent.getTime(): " + calendarLastAuthentication.getTime());
        }

        // Calculate expiry time of last authentication
        Calendar calendarExpiry = null;
        if (calendarLastAuthentication != null) {
            calendarExpiry = Calendar.getInstance();
            calendarExpiry.setTime(calendarLastAuthentication.getTime());
            calendarExpiry.add(Calendar.MINUTE, TIME_BETWEEN_AUTHENTICATIONS); // TODO: back off exponentially
            Log.i(getClass().getName(), "calendarExpiry.getTime(): " + calendarExpiry.getTime());
        }

        // TODO: detect how long the device has been inactive

        // If new authentication required, open StudentAuthenticationActivity
        if ((calendarLastAuthentication == null) || (calendarExpiry.before(Calendar.getInstance()))) {
            Log.i(getClass().getName(), "Redirecting to authentication...");
            Intent authenticationIntent = new Intent(context, StudentAuthenticationActivity.class);
            authenticationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(authenticationIntent);
        }
    }
}
