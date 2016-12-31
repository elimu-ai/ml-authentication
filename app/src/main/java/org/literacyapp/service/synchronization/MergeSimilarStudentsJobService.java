package org.literacyapp.service.synchronization;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.literacyapp.authentication.TrainingHelper;

public class MergeSimilarStudentsJobService extends JobService {
    public static boolean isRunning = false;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStartJob");
        isRunning = true;
        TrainingHelper trainingHelper = new TrainingHelper(getApplicationContext());
        trainingHelper.findAndMergeSimilarStudents();
        isRunning = false;
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStopJob");
        return false;
    }
}
