package org.literacyapp.authentication.helper;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.MainActivity;
import org.literacyapp.authentication.animaloverlay.AnimalOverlay;
import org.literacyapp.authentication.fallback.StudentRegistrationActivity;
import org.literacyapp.authentication.fallback.StudentSelectionActivity;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.StudentDao;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;

/**
 * Created by sladomic on 27.12.16.
 */

public class DetectionHelper {
    private static final Scalar RED_COLOR = new Scalar(255, 0, 0, 255);
    private static final int MAX_TIME_BEFORE_FALLBACK = 15000;
    private static final int SCREEN_BRIGHTNESS_INCREASE = 20;
    private static final int SCREEN_BRIGHTNESS_MAX = 255;
    private static final float IMAGE_BRIGHTNESS_THRESHOLD = 0.5f;
    private static final int SCREEN_BRIGHTNESS_DEFAULT = 85;
    private static final int SCREEN_BRIGHTNESS_MODE_DEFAULT = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;

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

    // http://answers.opencv.org/question/24260/how-to-determine-an-image-with-strong-or-weak-illumination-in-opencv/?answer=24342#post-id-24342
    private static double getImageBrightness(Mat img){
        Mat temp = new Mat();
        List<Mat> color = new ArrayList<Mat>(3);
        Mat lum = new Mat();
        temp = img;

        Core.split(temp, color);

        if(color.size() > 0){
            Core.multiply(color.get(0), new Scalar(0.299), color.get(0));
            Core.multiply(color.get(1), new Scalar(0.587), color.get(1));
            Core.multiply(color.get(2), new Scalar(0.114), color.get(2));

            Core.add(color.get(0),color.get(1),lum);
            Core.add(lum, color.get(2), lum);

            Scalar sum = Core.sumElems(lum);

            return sum.val[0]/((1<<8 - 1)*img.rows() * img.cols()) * 2;
        } else {
            return 1;
        }
    }

    public static synchronized void adjustScreenBrightness(Context context, Mat img){
        double currentImageBrightness = getImageBrightness(img);
        if (currentImageBrightness < IMAGE_BRIGHTNESS_THRESHOLD){
            setScreenBrightnessMode(context);
            setScreenBrightness(context, currentImageBrightness);
        }
    }

    private static synchronized void setScreenBrightnessMode(Context context){
        try {
            int screenBrightnessMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (screenBrightnessMode != Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL){
                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                Log.i(DetectionHelper.class.getName(), "adjustScreenBrightness: Screen brightness mode set to manual.");
            }
        } catch (Settings.SettingNotFoundException e) {
            Log.e(DetectionHelper.class.getName(), null, e);
        }
    }

    private static synchronized void setScreenBrightness(Context context, double currentImageBrightness){
        try {
            int currentScreenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            int increasedScreenBrightness = currentScreenBrightness + SCREEN_BRIGHTNESS_INCREASE;
            if (increasedScreenBrightness <= SCREEN_BRIGHTNESS_MAX){
                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, increasedScreenBrightness);
                Log.i(DetectionHelper.class.getName(), "adjustScreenBrightness: Screen brightness has been increased: currentImageBrightness: " + currentImageBrightness + " currentScreenBrightness: " + currentScreenBrightness + " increasedScreenBrightness: " + increasedScreenBrightness);
            }
        } catch (Settings.SettingNotFoundException e) {
            Log.e(DetectionHelper.class.getName(), null, e);
        }
    }

    public static synchronized int getScreenBrightness(Context context){
        try {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(DetectionHelper.class.getName(), null, e);
            return SCREEN_BRIGHTNESS_DEFAULT;
        }
    }

    public static synchronized int getScreenBrightnessMode(Context context){
        try {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(DetectionHelper.class.getName(), null, e);
            return SCREEN_BRIGHTNESS_MODE_DEFAULT;
        }
    }

    public static synchronized void setScreenBrightnessAndMode(Context context, int screenBrightnessMode, int screenBrightness){
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, screenBrightnessMode);
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, screenBrightness);
    }
}
