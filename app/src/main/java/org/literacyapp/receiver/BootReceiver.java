package org.literacyapp.receiver;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.literacyapp.service.ContentSynchronizationJobService;
import org.literacyapp.service.FaceRecognitionTrainingJobService;
import org.literacyapp.service.ScreenOnService;

public class BootReceiver extends BroadcastReceiver {
    private static final int FACE_RECOGNITION_TRAINING_JOB_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(getClass().getName(), "onReceive");

//        // Automatically open application
//        Intent bootIntent = new Intent(context, MainActivity.class);
//        bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(bootIntent);

//        // Start service for detecting when the screen is turned on
//        Intent screenOnServiceIntent = new Intent(context, ScreenOnService.class);
//        context.startService(screenOnServiceIntent);

        // Initiate background job for synchronizing with server content
        ComponentName componentName = new ComponentName(context, ContentSynchronizationJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(1, componentName);
        builder.setPeriodic(1000 * 60 * 30); // Every 30 minutes
        JobInfo jobInfo = builder.build();
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
        // Start service for detecting when the screen is turned on
        Intent screenOnServiceIntent = new Intent(context, ScreenOnService.class);
        context.startService(screenOnServiceIntent);

        // Initiate background job for face recognition training
        ComponentName componentNameFaceRecognitionTranining = new ComponentName(context, FaceRecognitionTrainingJobService.class);
        JobInfo.Builder builderFaceRecognitionTranining = new JobInfo.Builder(FACE_RECOGNITION_TRAINING_JOB_ID, componentNameFaceRecognitionTranining);
        int faceRecognitionTrainingPeriodic = 15 * 60 * 1000;
        builderFaceRecognitionTranining.setPeriodic(faceRecognitionTrainingPeriodic); // Every 15 minutes
        JobInfo faceRecognitionTrainingJobInfo = builderFaceRecognitionTranining.build();
        JobScheduler jobSchedulerFaceRecognitionTranining = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobSchedulerFaceRecognitionTranining.schedule(faceRecognitionTrainingJobInfo);
        Log.i(getClass().getName(), "FACE_RECOGNITION_TRAINING_JOB with ID " + FACE_RECOGNITION_TRAINING_JOB_ID + " has been scheduled with periodic time = " + faceRecognitionTrainingPeriodic);
    }
}
