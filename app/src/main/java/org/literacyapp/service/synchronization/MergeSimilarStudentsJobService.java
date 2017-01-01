package org.literacyapp.service.synchronization;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import org.literacyapp.authentication.thread.MergeThread;

public class MergeSimilarStudentsJobService extends JobService {
    private MergeThread mergeThread;
    private JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStartJob");
        this.jobParameters = jobParameters;
        mergeThread = new MergeThread(this);
        mergeThread.start();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStopJob");
        mergeThread.interrupt();
        return false;
    }

    public JobParameters getJobParameters() {
        return jobParameters;
    }
}
