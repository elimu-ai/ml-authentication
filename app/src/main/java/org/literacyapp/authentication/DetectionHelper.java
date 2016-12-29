package org.literacyapp.authentication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.authentication.animaloverlay.AnimalOverlay;
import org.literacyapp.authentication.fallback.StudentRegistrationActivity;
import org.literacyapp.authentication.fallback.StudentSelectionActivity;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.StudentDao;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;

/**
 * Created by sladomic on 27.12.16.
 */

public class DetectionHelper {
    private static final Scalar RED_COLOR = new Scalar(255, 0, 0, 255);
    private static final int MAX_TIME_BEFORE_FALLBACK = 15000;

    public static boolean isFaceInsideFrame(AnimalOverlay animalOverlay, Mat img, Rect face){
        if (animalOverlay != null){
            Point frameTopLeft = new Point(animalOverlay.getFrameStartX(), animalOverlay.getFrameStartY());
            Point frameBottomRight = new Point(animalOverlay.getFrameEndX(), animalOverlay.getFrameEndY());
            Rect frame = new Rect(frameTopLeft, frameBottomRight);
            if ((face.tl().x >= frame.tl().x) && (face.tl().y >= frame.tl().y) && (face.br().x <= frame.br().x) && (face.br().y <= frame.br().y)){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static void drawArrowFromFaceToFrame(AnimalOverlay animalOverlay, Mat img, Rect face){
        Rect mirroredFace = MatOperation.getMirroredFaceForFrontCamera(img, face);
        Point pointFace = new Point(mirroredFace.tl().x + mirroredFace.width / 2, mirroredFace.tl().y + mirroredFace.height / 2);
        Point pointFrame = new Point(animalOverlay.getFrameStartX() + (animalOverlay.getFrameEndX() - animalOverlay.getFrameStartX()) / 2, animalOverlay.getFrameStartY() + (animalOverlay.getFrameEndY() - animalOverlay.getFrameStartY()) / 2);
        Imgproc.arrowedLine(img, pointFace, pointFrame, RED_COLOR, 20, Imgproc.LINE_8, 0, 0.2);
    }

    public static synchronized boolean shouldFallbackActivityBeStarted(long startTimeFallback, long currentTime){
        if (startTimeFallback + MAX_TIME_BEFORE_FALLBACK < currentTime){
            return true;
        } else {
            return false;
        }
    }

    public static synchronized void startFallbackActivity(Context context, String classNameForLogging){
        // Initialize DB Session
        LiteracyApplication literacyApplication = (LiteracyApplication) context;
        DaoSession daoSession = literacyApplication.getDaoSession();

        // Create required DB Objects
        StudentDao studentDao = daoSession.getStudentDao();

        if (studentDao.count() > 0){
            Intent studentSelectionIntent = new Intent(context, StudentSelectionActivity.class);
            studentSelectionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.i(classNameForLogging, "StudentSelectionActivity will be started, because no faces were found in the last " + MAX_TIME_BEFORE_FALLBACK / 1000 + " seconds and some Students are already existing.");
            context.startActivity(studentSelectionIntent);
        } else {
            Intent studentRegistrationIntent = new Intent(context, StudentRegistrationActivity.class);
            studentRegistrationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.i(classNameForLogging, "StudentRegistrationActivity will be started, because no faces were found in the last " + MAX_TIME_BEFORE_FALLBACK / 1000 + " seconds and no Students are existing yet.");
            context.startActivity(studentRegistrationIntent);
        }
    }
}
