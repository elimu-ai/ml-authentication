package org.literacyapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.literacyapp.deviceadmin.DeviceAdmin;
import org.literacyapp.util.DeviceIdHelper;
import org.literacyapp.util.Log;

import edu.cmu.pocketsphinx.demo.PocketSphinxActivity;

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

        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        Log.d(getClass(), "configurationInfo.getGlEsVersion(): " + configurationInfo.getGlEsVersion());
        Log.d(getClass(), "configurationInfo.reqGlEsVersion: " + configurationInfo.reqGlEsVersion);

        String deviceId = DeviceIdHelper.getDeviceId(this);
        Log.d(getClass(), "deviceId: " + deviceId);

        new LoadContentAsyncTask().execute();
    }

    private class LoadContentAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(getClass(), "doInBackground");

            try {
                // TODO: download updated content from server
                // TODO: store in database
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(getClass(), null, e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(getClass(), "onPostExecute");
            super.onPostExecute(aVoid);

            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            startActivity(intent);

            finish();
        }
    }
}
