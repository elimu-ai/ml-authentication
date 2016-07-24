package org.literacyapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.literacyapp.util.DeviceInfoHelper;
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

        String deviceId = DeviceInfoHelper.getDeviceId(this);
        Log.d(getClass(), "deviceId: " + deviceId);
    }
}
