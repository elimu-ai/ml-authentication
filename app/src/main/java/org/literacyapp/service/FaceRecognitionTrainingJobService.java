package org.literacyapp.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import org.literacyapp.authentication.TrainingHelper;

/**
 * Created by sladomic on 26.11.16.
 */

public class FaceRecognitionTrainingJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStartJob");
        TrainingHelper trainingHelper = new TrainingHelper(getApplicationContext());
        trainingHelper.extractFeatures();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStopJob");
        return false;
    }
}
