package org.literacyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import org.literacyapp.receiver.DownloadContentAlarmReceiver;
import org.literacyapp.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass(), "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        Log.d(getClass(), "onCreate");
        super.onStart();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long timeOfLastSynchronizationInMillis = sharedPreferences.getLong(DownloadContentAlarmReceiver.PREF_LAST_CONTENT_SYNC, 0);
        if (timeOfLastSynchronizationInMillis == 0) {
            // Download content
            Intent intent = new Intent("org.literacyapp.receiver.DownloadContentAlarmReceiver");
            sendBroadcast(intent);
        } else {
            // Assume content has already been downloaded
            Intent categoryIntent = new Intent(this, CategoryActivity.class);
            startActivity(categoryIntent);
            finish();
        }
    }
}
