package org.literacyapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import org.literacyapp.service.TrainingJobService;
import org.literacyapp.util.Log;

public class IdleReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(getClass(), "onReceive");

        // Initiate background job for recording screenshots
        ComponentName componentName = new ComponentName(context, TrainingJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, componentName);
        JobInfo trainingJobInfo = builder.build();

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(trainingJobInfo);
    }
}
