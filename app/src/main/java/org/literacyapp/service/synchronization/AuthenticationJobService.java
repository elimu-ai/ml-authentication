package org.literacyapp.service.synchronization;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.util.Log;
import android.view.Display;

import org.literacyapp.authentication.AuthenticationActivity;
import org.literacyapp.authentication.TrainingHelper;
import org.literacyapp.receiver.BootReceiver;
import org.literacyapp.service.FaceRecognitionTrainingJobService;

/**
 * Created by sladomic on 26.11.16.
 */

public class AuthenticationJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStartJob");
        if (!isScreenTurnedOff()){
            if (!rescheduleIfTrainingJobServiceIsRunning()){
                Intent authenticationIntent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                authenticationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(authenticationIntent);
                Log.i(getClass().getName(), "The Authentication has been started.");
            }
        } else {
            Log.i(getClass().getName(), "The Authentication was skipped because the screen was turned off.");
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(getClass().getName(), "onStopJob");
        return false;
    }

    private boolean rescheduleIfTrainingJobServiceIsRunning(){
        boolean isFaceRecognitionTrainingJobServiceRunning = FaceRecognitionTrainingJobService.isRunning;
        Log.i(getClass().getName(), "isFaceRecognitionTrainingJobServiceRunning: " + isFaceRecognitionTrainingJobServiceRunning);
        if (isFaceRecognitionTrainingJobServiceRunning){
            BootReceiver.scheduleAuthentication(getApplicationContext(), BootReceiver.MINUTES_BETWEEN_AUTHENTICATIONS);
            Log.i(getClass().getName(), "The AuthenticationJobService has been rescheduled, because the CPU hungry FaceRecognitionTrainingJobService is running at the moment.");
        }
        return isFaceRecognitionTrainingJobServiceRunning;
    }

    private boolean isScreenTurnedOff(){
        boolean isScreenTurnedOff = false;
        DisplayManager displayManager = (DisplayManager) getApplicationContext().getSystemService(Context.DISPLAY_SERVICE);
        for (Display display : displayManager.getDisplays()) {
            if (display.getState() != Display.STATE_ON) {
                // Skip authentication
                isScreenTurnedOff = true;
            }
        }
        return isScreenTurnedOff;
    }
}
