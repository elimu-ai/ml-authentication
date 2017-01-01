package org.literacyapp.service.synchronization;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import org.literacyapp.authentication.MergeThread;
import org.literacyapp.authentication.TrainingThread;

public class MergeSimilarStudentsJobService extends JobService {
    private MergeThread mergeThread;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStartJob");
        mergeThread = new MergeThread(getApplicationContext());
        mergeThread.start();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStopJob");
        mergeThread.interrupt();
        return false;
    }
}
