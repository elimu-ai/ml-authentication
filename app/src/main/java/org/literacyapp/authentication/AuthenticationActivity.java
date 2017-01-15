package org.literacyapp.authentication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.authentication.animaloverlay.AnimalOverlay;
import org.literacyapp.authentication.animaloverlay.AnimalOverlayHelper;
import org.literacyapp.authentication.helper.AuthenticationHelper;
import org.literacyapp.authentication.helper.DetectionHelper;
import org.literacyapp.authentication.thread.RecognitionThread;
import org.literacyapp.authentication.thread.TrainingThread;
import org.literacyapp.dao.AuthenticationEventDao;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.model.Student;
import org.literacyapp.util.EnvironmentSettings;
import org.literacyapp.util.MultimediaHelper;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.Date;
import java.util.List;

import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import ch.zhaw.facerecognitionlibrary.Recognition.TensorFlow;
import pl.droidsonroids.gif.GifImageView;

public class AuthenticationActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    public static final long AUTHENTICATION_ANIMATION_TIME = 5000;
    public static final String AUTHENTICATION_ANIMATION_ALREADY_PLAYED_IDENTIFIER = "AuthenticationAnimationAlreadyPlayed";
    public static final String ANIMAL_OVERLAY_IDENTIFIER = "AnimalOverlayName";
    private static final int NUMBER_OF_MAXIMUM_TRIES = 3;
    private TensorFlow tensorFlow;
    private PreProcessorFactory ppF;
    private JavaCameraView preview;
    private AnimalOverlayHelper animalOverlayHelper;
    private StudentImageCollectionEventDao studentImageCollectionEventDao;
    private int numberOfTries;
    private AnimalOverlay animalOverlay;
    private MediaPlayer mediaPlayerInstruction;
    private MediaPlayer mediaPlayerAnimalSound;
    private long startTimeFallback;
    private long startTimeAuthenticationAnimation;
    private Thread tensorFlowLoadingThread;
    private RecognitionThread recognitionThread;
    private GifImageView authenticationAnimation;
    private boolean recognitionThreadStarted;
    private boolean activityStopped;
    private AuthenticationEventDao authenticationEventDao;
    private int screenBrightnessMode;
    private int screenBrightness;
    private int displayTemperatureNight;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        screenBrightnessMode = DetectionHelper.getScreenBrightnessMode(getApplicationContext());
        screenBrightness = DetectionHelper.getScreenBrightness(getApplicationContext());
        displayTemperatureNight = DetectionHelper.getDisplayTemperatureNight();

        authenticationAnimation = (GifImageView) findViewById(R.id.authentication_animation);
        MultimediaHelper.setAuthenticationInstructionAnimation(getApplicationContext(), authenticationAnimation);

        // Initialize DB Session
        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        DaoSession daoSession = literacyApplication.getDaoSession();
        studentImageCollectionEventDao = daoSession.getStudentImageCollectionEventDao();
        authenticationEventDao = daoSession.getAuthenticationEventDao();

        if (!readyForAuthentication()){
            startStudentImageCollectionActivity(false);
        }

        preview = (JavaCameraView) findViewById(R.id.CameraView);

        // Use front-camera
        preview.setCameraIndex(1);

        preview.setVisibility(SurfaceView.VISIBLE);
        preview.setCvCameraViewListener(this);

        final TrainingThread trainingThread = new TrainingThread(getApplicationContext());

        tensorFlowLoadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                tensorFlow = trainingThread.getInitializedTensorFlow();
            }
        });

        recognitionThreadStarted = false;

        animalOverlayHelper = new AnimalOverlayHelper(getApplicationContext());

        activityStopped = false;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat imgRgba = inputFrame.rgba();

        DetectionHelper.setIncreasedScreenBrightness(getApplicationContext(), imgRgba);

        long currentTime = new Date().getTime();

        if ((!tensorFlowLoadingThread.isAlive()) && ((startTimeAuthenticationAnimation + AUTHENTICATION_ANIMATION_TIME) < currentTime)){
            prepareForAuthentication();

            if (!recognitionThread.isAlive() && recognitionThreadStarted) {
                List<Student> students = recognitionThread.getRecognizedStudent();
                Student student = new Student();
                if (students.size() == 1){
                    student = students.get(0);
                }
                numberOfTries++;
                Log.i(getClass().getName(), "Number of authentication/recognition tries: " + numberOfTries);
                if ((student != null) && (students.size() == 1)) {
                    AuthenticationHelper.updateCurrentStudent(student, getApplicationContext(), false);
                    finish();
                } else if (numberOfTries >= NUMBER_OF_MAXIMUM_TRIES) {
                    startStudentImageCollectionActivity(true);
                }
                recognitionThreadStarted = false;
            }

            Mat imgCopy = new Mat();

            // Store original image for face recognition
            imgRgba.copyTo(imgCopy);

            // Mirror front camera image
            Core.flip(imgRgba,imgRgba,1);

            Rect face = new Rect();
            boolean isFaceInsideFrame = false;
            boolean faceDetected = false;

            List<Mat> images = ppF.getCroppedImage(imgCopy);
            if (images != null && images.size() == 1){
                Mat img = images.get(0);
                if (img != null){
                    Rect[] faces = ppF.getFacesForRecognition();
                    if (faces != null && faces.length == 1){
                        faces = MatOperation.rotateFaces(imgRgba, faces, ppF.getAngleForRecognition());
                        face = faces[0];
                        faceDetected = true;
                        // Reset startTimeFallback for fallback timeout, because at least one face has been detected
                        startTimeFallback = currentTime;
                        isFaceInsideFrame = DetectionHelper.isFaceInsideFrame(animalOverlay, imgRgba, face);

                        if (isFaceInsideFrame){
                            if (!recognitionThread.isAlive() && !recognitionThreadStarted){
                                if (!activityStopped){
                                    mediaPlayerAnimalSound.start();

                                    recognitionThread = new RecognitionThread(tensorFlow, studentImageCollectionEventDao);
                                    recognitionThread.setImg(img);
                                    recognitionThread.start();
                                    recognitionThreadStarted = true;
                                }
                            }
                        }
                    }
                }
            }

            if (faceDetected && !isFaceInsideFrame){
                DetectionHelper.drawArrowFromFaceToFrame(animalOverlay, imgRgba, face);
            }

            if (DetectionHelper.shouldFallbackActivityBeStarted(startTimeFallback, currentTime)){
                // Prevent from second execution of fallback activity because of threading
                startTimeFallback = currentTime;
                DetectionHelper.startFallbackActivity(getApplicationContext(), getClass().getName());
                finish();
            }

            EnvironmentSettings.freeMemory();
        }

        return imgRgba;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ppF = new PreProcessorFactory(getApplicationContext());
        numberOfTries = 0;
        animalOverlay = animalOverlayHelper.getAnimalOverlay("");
        if (animalOverlay != null) {
            mediaPlayerAnimalSound = MediaPlayer.create(this, getResources().getIdentifier(animalOverlay.getSoundFile(), MultimediaHelper.RESOURCES_RAW_FOLDER, getPackageName()));
        }
        preview.enableView();
        mediaPlayerInstruction = MediaPlayer.create(this, R.raw.auth_tablet_placement);
        mediaPlayerInstruction.start();
        tensorFlowLoadingThread.start();
        startTimeFallback = new Date().getTime();
        startTimeAuthenticationAnimation = new Date().getTime();
    }

    private void prepareForAuthentication(){
        if (authenticationAnimation.getVisibility() == View.VISIBLE){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    authenticationAnimation.setVisibility(View.INVISIBLE);

                    ImageView animalOverlayImageView = (ImageView)findViewById(R.id.animalOverlay);
                    animalOverlayImageView.setImageResource(getResources().getIdentifier(animalOverlay.getName(), MultimediaHelper.RESOURCES_DRAWABLE_FOLDER, getPackageName()));
                    animalOverlayImageView.setVisibility(View.VISIBLE);

                    preview.disableView();
                    preview.enableView();
                }
            });

            recognitionThread = new RecognitionThread(tensorFlow, studentImageCollectionEventDao);
            startTimeFallback = new Date().getTime();
        }
    }

    private synchronized void startStudentImageCollectionActivity(boolean authenticationAnimationAlreadyPlayed){
        Intent studentImageCollectionIntent = new Intent(getApplicationContext(), StudentImageCollectionActivity.class);
        studentImageCollectionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (authenticationAnimationAlreadyPlayed){
            studentImageCollectionIntent.putExtra(AUTHENTICATION_ANIMATION_ALREADY_PLAYED_IDENTIFIER, true);
            studentImageCollectionIntent.putExtra(ANIMAL_OVERLAY_IDENTIFIER, animalOverlay.getName());
        } else {
            studentImageCollectionIntent.putExtra(AUTHENTICATION_ANIMATION_ALREADY_PLAYED_IDENTIFIER, false);
        }
        startActivity(studentImageCollectionIntent);
        finish();
    }

    private boolean readyForAuthentication(){
        long meanFeatureVectorsCount = studentImageCollectionEventDao.queryBuilder().where(StudentImageCollectionEventDao.Properties.MeanFeatureVector.isNotNull()).count();
        Log.i(getClass().getName(), "readyForAuthentication: meanFeatureVectorsCount: " + meanFeatureVectorsCount);

        if (meanFeatureVectorsCount > 0){
            Log.i(getClass().getName(), "AuthenticationActivity is ready for authentication.");
            return true;
        } else {
            Log.w(getClass().getName(), "AuthenticationActivity is not ready for authentication.");
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayerInstruction.stop();
        mediaPlayerInstruction.release();
        mediaPlayerAnimalSound.stop();
        mediaPlayerAnimalSound.release();
        activityStopped = true;
        DetectionHelper.setDefaultScreenBrightnessAndMode(getApplicationContext(), screenBrightnessMode, screenBrightness, displayTemperatureNight);
    }
}
