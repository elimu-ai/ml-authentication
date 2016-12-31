package org.literacyapp.authentication;

import android.content.Context;
import android.util.Log;

import org.apache.commons.math3.analysis.function.StepFunction;
import org.literacyapp.LiteracyApplication;
import org.literacyapp.dao.AuthenticationEventDao;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.model.Device;
import org.literacyapp.model.Student;
import org.literacyapp.model.analytics.AuthenticationEvent;
import org.literacyapp.receiver.BootReceiver;
import org.literacyapp.util.DeviceInfoHelper;
import org.literacyapp.util.StudentUpdateHelper;

import java.util.Calendar;
import java.util.List;

/**
 * Created by sladomic on 30.12.16.
 */

public class AuthenticationHelper {
    private static final double[] countSteps = { 0, 5, 10, 15, 20, 25, 30, 35, 40, 45 };
    private static final double[] exponentialDelayStepsInMinutes = { 30, 60, 120, 240, 480, 960, 1920, 3840, 7680, 15360 };

    public static synchronized void updateCurrentStudent(Student student, Context context, boolean isFallback){
        LiteracyApplication literacyApplication = (LiteracyApplication) context;
        DaoSession daoSession = literacyApplication.getDaoSession();
        AuthenticationEventDao authenticationEventDao = daoSession.getAuthenticationEventDao();
        Device device = DeviceInfoHelper.getDevice(context);

        new StudentUpdateHelper(context, student).updateStudent();

        // Call addExponentialDelayIfSameStudent before the new authenticationEvent is stored
        addExponentialDelayIfSameStudent(context, authenticationEventDao, student);

        AuthenticationEvent authenticationEvent = new AuthenticationEvent();
        authenticationEvent.setStudent(student);
        authenticationEvent.setTime(Calendar.getInstance());
        authenticationEvent.setIsFallback(isFallback);
        authenticationEvent.setDevice(device);
        authenticationEventDao.insert(authenticationEvent);
        Log.i(context.getClass().getName(), "AuthenticationEvent: " + authenticationEvent.getId() + " has been stored with the properties Student: " + student.getUniqueId() + " IsFallback: " + authenticationEvent.getIsFallback());
    }

    private static void addExponentialDelayIfSameStudent(Context context, AuthenticationEventDao authenticationEventDao, Student newStudent){
        List<AuthenticationEvent> authenticationEvents = authenticationEventDao.queryBuilder().orderDesc(AuthenticationEventDao.Properties.Time).list();
        if (authenticationEvents.size() > 0){
            int countOfRecurrentLogins = 0;
            for (AuthenticationEvent authenticationEvent : authenticationEvents){
                Student oldStudent = authenticationEvent.getStudent();
                if (oldStudent.getUniqueId().equals(newStudent.getUniqueId())){
                    countOfRecurrentLogins++;
                } else {
                    break;
                }
            }
            Log.i(context.getClass().getName(), "addExponentialDelayIfSameStudent: countOfRecurrentLogins: " + countOfRecurrentLogins);

            // Creates a stepFunction which will return the minutes according to the countOfRecurrentLogins as follows:
            // 0 <= countOfRecurrentLogins < 5: 30 minutes
            // 5 <= countOfRecurrentLogins < 10: 60 minutes
            // 10 <= countOfRecurrentLogins < 15: 120 minutes
            // and so on...
            StepFunction stepFunction = new StepFunction(countSteps, exponentialDelayStepsInMinutes);
            int exponentialDelayBetweenAuthentications = (int) stepFunction.value(countOfRecurrentLogins);

            Log.i(context.getClass().getName(), "addExponentialDelayIfSameStudent: exponentialDelayBetweenAuthentications: " + exponentialDelayBetweenAuthentications);
            BootReceiver.scheduleAuthentication(context, exponentialDelayBetweenAuthentications);
        }
    }
}
