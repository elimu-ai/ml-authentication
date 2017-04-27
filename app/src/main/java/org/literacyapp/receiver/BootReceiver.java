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
import org.literacyapp.service.synchronization.AuthenticationJobService;
import org.literacyapp.service.synchronization.MergeSimilarStudentsJobService;
import org.literacyapp.authentication.helper.StartPrefsHelper;

public class BootReceiver extends BroadcastReceiver {

    public static final int MINUTES_BETWEEN_AUTHENTICATIONS = 30;
    private static final int MINUTES_BETWEEN_FACE_RECOGNITION_TRAININGS = 15;
    private static final int HOURS_BETWEEN_MERGING_SIMILAR_STUDENTS = 24;

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

        if (StartPrefsHelper.scheduleAfterBoot(context)){
            scheduleAuthenticationJobs(context);
        } else {
            Log.i(getClass().getName(), "Authentication jobs won't be scheduled because the 7 days after first start-up haven't passed yet.");
        }
    }

    private static void scheduleAuthenticationJobs(Context context){
        // Initiate background job for face recognition training
        scheduleFaceRecognitionTranining(context);

        // Initiate background job for face recognition authentication
        scheduleAuthentication(context, MINUTES_BETWEEN_AUTHENTICATIONS);

        // Initiate background job for merging similar students
        scheduleMergeSimilarStudents(context);
    }

    public static void scheduleFaceRecognitionTranining(Context context){
        ComponentName componentNameFaceRecognitionTranining = new ComponentName(context, FaceRecognitionTrainingJobService.class);
        JobInfo.Builder builderFaceRecognitionTranining = new JobInfo.Builder(LiteracyApplication.FACE_RECOGNITION_TRAINING_JOB_ID, componentNameFaceRecognitionTranining);
        int faceRecognitionTrainingPeriodic = MINUTES_BETWEEN_FACE_RECOGNITION_TRAININGS * 60 * 1000;
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

    public static void scheduleMergeSimilarStudents(Context context){
        ComponentName componentNameMergeSimilarStudents = new ComponentName(context, MergeSimilarStudentsJobService.class);
        JobInfo.Builder builderMergeSimilarStudents = new JobInfo.Builder(LiteracyApplication.MERGE_SIMILAR_STUDENTS_JOB_ID, componentNameMergeSimilarStudents);
        boolean requiresCharging = true;
        builderMergeSimilarStudents.setRequiresCharging(requiresCharging);
        boolean requiresDeviceIdle = true;
        builderMergeSimilarStudents.setRequiresDeviceIdle(requiresDeviceIdle);
        int mergeSimilarStudentsPeriodic = HOURS_BETWEEN_MERGING_SIMILAR_STUDENTS * 60 * 60 * 1000;
        builderMergeSimilarStudents.setPeriodic(mergeSimilarStudentsPeriodic);
        JobInfo mergeSimilarStudentsJobInfo = builderMergeSimilarStudents.build();
        JobScheduler jobSchedulerMergeSimilarStudents = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobSchedulerMergeSimilarStudents.schedule(mergeSimilarStudentsJobInfo);
        Log.i(context.getClass().getName(), "MERGE_SIMILAR_STUDENTS_JOB with ID " + LiteracyApplication.MERGE_SIMILAR_STUDENTS_JOB_ID + " has been scheduled with periodic time = " + mergeSimilarStudentsPeriodic + " requiresCharging = " + requiresCharging + " requiresDeviceIdle = " + requiresDeviceIdle);
    }
}
