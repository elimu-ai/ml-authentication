package org.literacyapp.authentication.thread;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.util.Log;
import android.view.Display;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.authentication.AuthenticationActivity;
import org.literacyapp.dao.AuthenticationEventDao;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.model.analytics.AuthenticationEvent;
import org.literacyapp.model.analytics.StudentImageCollectionEvent;
import org.literacyapp.receiver.BootReceiver;
import org.literacyapp.service.synchronization.AuthenticationJobService;

import java.util.Date;
import java.util.List;

/**
 * Created by sladomic on 01.01.17.
 */

public class AuthenticationThread extends Thread {
    private Context context;
    private AuthenticationJobService authenticationJobService;

    public AuthenticationThread(AuthenticationJobService authenticationJobService){
        this.authenticationJobService = authenticationJobService;
        this.context = authenticationJobService.getApplicationContext();
    }

    @Override
    public void run() {
        if (!isScreenTurnedOff()){
            if (didTheMinimumTimePassSinceLastExecution()){
                Intent authenticationIntent = new Intent(context, AuthenticationActivity.class);
                authenticationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(authenticationIntent);
                Log.i(getClass().getName(), "The Authentication has been started.");
            } else {
                Log.i(getClass().getName(), "The Authentication was skipped because the minimum time since the last execution did not pass yet.");
            }
        } else {
            Log.i(getClass().getName(), "The Authentication was skipped because the screen was turned off.");
        }
        authenticationJobService.jobFinished(authenticationJobService.getJobParameters(), false);
    }

    private boolean isScreenTurnedOff(){
        boolean isScreenTurnedOff = false;
        DisplayManager displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        for (Display display : displayManager.getDisplays()) {
            if (display.getState() != Display.STATE_ON) {
                isScreenTurnedOff = true;
            }
        }
        Log.i(getClass().getName(), "isScreenTurnedOff: " + isScreenTurnedOff);
        return isScreenTurnedOff;
    }

    private boolean didTheMinimumTimePassSinceLastAuthentication(AuthenticationEventDao authenticationEventDao, long minimumTimeInMilliseconds){
        boolean didTheMinimumTimePassSinceLastAuthentication = true;
        // Get only the last AuthenticationEvent
        List<AuthenticationEvent> authenticationEvents = authenticationEventDao.queryBuilder().orderDesc(AuthenticationEventDao.Properties.Time).limit(1).list();
        if (authenticationEvents.size() > 0){
            AuthenticationEvent authenticationEvent = authenticationEvents.get(0);
            long lastAuthenticationTime = authenticationEvent.getTime().getTime().getTime();
            long currentTime = new Date().getTime();
            Log.i(getClass().getName(), "didTheMinimumTimePassSinceLastAuthentication: lastAuthenticationTime: " + new Date(lastAuthenticationTime) + " minimumTimeInMinutes: " + (minimumTimeInMilliseconds / 1000 / 60) + " currentTime: " + new Date(currentTime));
            if ((lastAuthenticationTime + minimumTimeInMilliseconds) > currentTime){
                didTheMinimumTimePassSinceLastAuthentication = false;
            }
        }
        return didTheMinimumTimePassSinceLastAuthentication;
    }

    private boolean didTheMinimumTimePassSinceLastCollection(StudentImageCollectionEventDao studentImageCollectionEventDao, long minimumTimeInMilliseconds){
        boolean didTheMinimumTimePassSinceLastCollection = true;
        // Get only the last StudentImageCollectionEvent
        List<StudentImageCollectionEvent> studentImageCollectionEvents = studentImageCollectionEventDao.queryBuilder().orderDesc(StudentImageCollectionEventDao.Properties.Time).limit(1).list();
        if (studentImageCollectionEvents.size() > 0){
            StudentImageCollectionEvent studentImageCollectionEvent = studentImageCollectionEvents.get(0);
            long lastCollectionTime = studentImageCollectionEvent.getTime().getTime().getTime();
            long currentTime = new Date().getTime();
            Log.i(getClass().getName(), "didTheMinimumTimePassSinceLastCollection: lastCollectionTime: " + new Date(lastCollectionTime) + " minimumTimeInMinutes: " + (minimumTimeInMilliseconds / 1000 / 60) + " currentTime: " + new Date(currentTime));
            if ((lastCollectionTime + minimumTimeInMilliseconds) > currentTime){
                didTheMinimumTimePassSinceLastCollection = false;
            }
        }
        return didTheMinimumTimePassSinceLastCollection;
    }

    private boolean didTheMinimumTimePassSinceLastExecution(){
        LiteracyApplication literacyApplication = (LiteracyApplication) context;
        DaoSession daoSession = literacyApplication.getDaoSession();
        AuthenticationEventDao authenticationEventDao = daoSession.getAuthenticationEventDao();
        StudentImageCollectionEventDao studentImageCollectionEventDao = daoSession.getStudentImageCollectionEventDao();
        long minimumTimeInMilliseconds = BootReceiver.MINUTES_BETWEEN_AUTHENTICATIONS * 60 * 1000;
        boolean didTheMinimumTimePassSinceLastAuthentication = didTheMinimumTimePassSinceLastAuthentication(authenticationEventDao, minimumTimeInMilliseconds);
        boolean didTheMinimumTimePassSinceLastCollection = didTheMinimumTimePassSinceLastCollection(studentImageCollectionEventDao, minimumTimeInMilliseconds);
        return (didTheMinimumTimePassSinceLastAuthentication && didTheMinimumTimePassSinceLastCollection);
    }
}
