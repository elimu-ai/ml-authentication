package org.literacyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.literacyapp.contentprovider.ContentProvider;
import org.literacyapp.contentprovider.dao.LetterDao;
import org.literacyapp.contentprovider.model.content.Letter;
import org.literacyapp.service.synchronization.ReadDeviceAsyncTask;
import org.literacyapp.util.ConnectivityHelper;
import org.literacyapp.util.RootHelper;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    public static final int PERMISSION_REQUEST_CAMERA = 1;

    private LetterDao letterDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onCreate");
        super.onStart();

        // Ask for permissions
        int permissionCheckWriteExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheckWriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            return;
        }
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            return;
        }

        // Obtain permission to change system settings
        try {
            RootHelper.runAsRoot(new String[] {"pm grant org.literacyapp android.permission.WRITE_SECURE_SETTINGS"});
        } catch (IOException | InterruptedException e) {
            Log.e(getClass().getName(), null, e);
        }

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplication();
        letterDao = literacyApplication.getDaoSession().getLetterDao();

        ContentProvider.initializeDb(this);
        List<Letter> letters = ContentProvider.getUnlockedLetters();
        Log.i(getClass().getName(), "letters: " + letters);

        if (letterDao.loadAll().isEmpty()) {
            // Download content
            boolean isWifiEnabled = ConnectivityHelper.isWifiEnabled(getApplicationContext());
            Log.i(getClass().getName(), "isWifiEnabled: " + isWifiEnabled);
            boolean isWifiConnected = ConnectivityHelper.isWifiConnected(getApplicationContext());
            Log.i(getClass().getName(), "isWifiConnected: " + isWifiConnected);
            if (!isWifiEnabled) {
                Toast.makeText(getApplicationContext(), getString(R.string.wifi_needs_to_be_enabled), Toast.LENGTH_SHORT).show();
            } else if (!isWifiConnected) {
                Toast.makeText(getApplicationContext(), getString(R.string.wifi_needs_to_be_connected), Toast.LENGTH_SHORT).show();
            } else {
                new ReadDeviceAsyncTask(getApplicationContext()).execute();
            }
        } else {
            // Assume content has already been downloaded
            Intent categoryIntent = new Intent(this, CategoryActivity.class);
            startActivity(categoryIntent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ((requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE)
                || (requestCode == PERMISSION_REQUEST_CAMERA)) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted

                // Restart application
                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
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
