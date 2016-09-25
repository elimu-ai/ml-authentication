package org.literacyapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import org.literacyapp.authentication.CameraViewActivity;
import org.literacyapp.receiver.DownloadContentAlarmReceiver;
import org.literacyapp.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

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

        // Ask for permissions
        int permissionCheckWriteExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheckWriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            return;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long timeOfLastSynchronizationInMillis = sharedPreferences.getLong(DownloadContentAlarmReceiver.PREF_LAST_CONTENT_SYNC, 0);
        if (timeOfLastSynchronizationInMillis == 0) {
            // Download content
            Intent intent = new Intent("org.literacyapp.receiver.DownloadContentAlarmReceiver");
            intent.setPackage("org.literacyapp");
            sendBroadcast(intent);
        } else {
            // Assume content has already been downloaded
 //           Intent categoryIntent = new Intent(this, CategoryActivity.class);
//            startActivity(categoryIntent);
            startActivity(new Intent(getApplicationContext(), CameraViewActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted

                // Restart application
                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                // Permission denied

                // Close application
                finish();
            }
        }
    }
}
