package org.literacyapp.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import org.literacyapp.authentication.helper.StartPrefsHelper;
import org.literacyapp.authentication.thread.TrainingThread;

public class FaceRecognitionTrainingJobService extends JobService {

    private TrainingThread trainingThread;
    private JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStartJob");
        if (StartPrefsHelper.activateAuthentication()) {
            this.jobParameters = jobParameters;
            trainingThread = new TrainingThread(this);
            trainingThread.start();
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStopJob");
        if ((trainingThread != null) && (trainingThread.isAlive())) {
            trainingThread.interrupt();
        }
        return false;
    }

    public JobParameters getJobParameters() {
        return jobParameters;
    }
}
