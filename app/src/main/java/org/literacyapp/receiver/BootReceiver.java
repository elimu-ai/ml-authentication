package org.literacyapp.receiver;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.service.ContentSynchronizationJobService;
import org.literacyapp.service.FaceRecognitionTrainingJobService;
import org.literacyapp.service.ScreenOnService;
import org.literacyapp.service.synchronization.AuthenticationJobService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(getClass().getName(), "onReceive");

//        // Automatically open application
//        Intent bootIntent = new Intent(context, MainActivity.class);
//        bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(bootIntent);

        // Initiate background job for synchronizing with server content
        ComponentName componentName = new ComponentName(context, ContentSynchronizationJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(LiteracyApplication.CONTENT_SYNCRHONIZATION_JOB_ID, componentName);
        builder.setPeriodic(1000 * 60 * 30); // Every 30 minutes
        JobInfo jobInfo = builder.build();
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);

        // Initiate background job for face recognition training
        scheduleFaceRecognitionTranining(context);

        // Initiate authentication job for face recognition authentication
        scheduleAuthentication(context, 30);

        // Start service for detecting when the screen is turned on
        Intent screenOnServiceIntent = new Intent(context, ScreenOnService.class);
        context.startService(screenOnServiceIntent);

        // TODO: trigger the same code as in ScreenOnReceiver
    }

    public static void scheduleFaceRecognitionTranining(Context context){
        ComponentName componentNameFaceRecognitionTranining = new ComponentName(context, FaceRecognitionTrainingJobService.class);
        JobInfo.Builder builderFaceRecognitionTranining = new JobInfo.Builder(LiteracyApplication.FACE_RECOGNITION_TRAINING_JOB_ID, componentNameFaceRecognitionTranining);
        int faceRecognitionTrainingPeriodic = 15 * 60 * 1000;
        builderFaceRecognitionTranining.setPeriodic(faceRecognitionTrainingPeriodic); // Every 15 minutes
        JobInfo faceRecognitionTrainingJobInfo = builderFaceRecognitionTranining.build();
        JobScheduler jobSchedulerFaceRecognitionTranining = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobSchedulerFaceRecognitionTranining.schedule(faceRecognitionTrainingJobInfo);
        Log.i(context.getClass().getName(), "FACE_RECOGNITION_TRAINING_JOB with ID " + LiteracyApplication.FACE_RECOGNITION_TRAINING_JOB_ID + " has been scheduled with periodic time = " + faceRecognitionTrainingPeriodic);
    }

    public static void scheduleAuthentication(Context context, int minutesBetweenAuthentications){
        ComponentName componentNameAuthentication = new ComponentName(context, AuthenticationJobService.class);
        JobInfo.Builder builderAuthentication = new JobInfo.Builder(LiteracyApplication.AUTHENTICATION_JOB_ID, componentNameAuthentication);
        int authenticationPeriodic = minutesBetweenAuthentications * 60 * 1000;
        builderAuthentication.setPeriodic(authenticationPeriodic);
        JobInfo authenticationJobInfo = builderAuthentication.build();
        JobScheduler jobSchedulerAuthentication = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobSchedulerAuthentication.schedule(authenticationJobInfo);
        Log.i(context.getClass().getName(), "AUTHENTICATION_JOB with ID " + LiteracyApplication.AUTHENTICATION_JOB_ID + " has been scheduled with periodic time = " + authenticationPeriodic);
    }
}
