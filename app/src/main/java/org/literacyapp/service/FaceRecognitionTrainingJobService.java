package org.literacyapp.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import org.literacyapp.authentication.TrainingThread;

/**
 * Created by sladomic on 26.11.16.
 */

public class FaceRecognitionTrainingJobService extends JobService {
    private TrainingThread trainingThread;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStartJob");
        trainingThread = new TrainingThread(getApplicationContext());
        trainingThread.start();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStopJob");
        trainingThread.interrupt();
        return false;
    }
}
