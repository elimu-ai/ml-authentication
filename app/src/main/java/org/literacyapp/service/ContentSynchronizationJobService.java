package org.literacyapp.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;
import android.widget.Toast;

import org.literacyapp.R;
import org.literacyapp.service.synchronization.ReadDeviceAsyncTask;
import org.literacyapp.util.ConnectivityHelper;

/**
 * 1. Check if Device has already been stored on the server - ReadDeviceAsyncTask
 * 2. If not, register Device - RegisterDeviceAsyncTask
 * 3. Once registered, download content - DownloadContentAsyncTask
 */
public class ContentSynchronizationJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(getClass().getName(), "onStartJob");

        // Start processing work
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

        boolean isWorkProcessingPending = false;
        return isWorkProcessingPending;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(getClass().getName(), "onStopJob");

        boolean isJobToBeRescheduled = false;
        return isJobToBeRescheduled;
    }
}
