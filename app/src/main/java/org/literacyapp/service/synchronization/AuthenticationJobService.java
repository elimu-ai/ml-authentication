package org.literacyapp.service.synchronization;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import org.literacyapp.authentication.thread.AuthenticationThread;

public class AuthenticationJobService extends JobService {

    private AuthenticationThread authenticationThread;
    private JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStartJob");
        this.jobParameters = jobParameters;
        authenticationThread = new AuthenticationThread(this);
        authenticationThread.start();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStopJob");
        authenticationThread.interrupt();
        return false;
    }

    public JobParameters getJobParameters() {
        return jobParameters;
    }
}
