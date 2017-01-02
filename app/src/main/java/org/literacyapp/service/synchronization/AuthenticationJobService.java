package org.literacyapp.service.synchronization;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.util.Log;
import android.view.Display;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.authentication.AuthenticationActivity;
import org.literacyapp.authentication.thread.AuthenticationThread;
import org.literacyapp.dao.AuthenticationEventDao;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.model.analytics.AuthenticationEvent;
import org.literacyapp.model.analytics.StudentImageCollectionEvent;
import org.literacyapp.receiver.BootReceiver;
import org.literacyapp.service.FaceRecognitionTrainingJobService;

import java.util.Date;
import java.util.List;

/**
 * Created by sladomic on 26.11.16.
 */

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
