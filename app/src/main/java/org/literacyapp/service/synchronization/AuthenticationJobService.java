package org.literacyapp.service.synchronization;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import org.literacyapp.authentication.AuthenticationActivity;
import org.literacyapp.authentication.TrainingHelper;

/**
 * Created by sladomic on 26.11.16.
 */

public class AuthenticationJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStartJob");
        Intent authenticationIntent = new Intent(getApplicationContext(), AuthenticationActivity.class);
        authenticationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(authenticationIntent);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStopJob");
        return false;
    }
}
