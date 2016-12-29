package org.literacyapp.authentication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.authentication.animaloverlay.AnimalOverlay;
import org.literacyapp.authentication.animaloverlay.AnimalOverlayHelper;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.model.Student;
import org.literacyapp.model.StudentImageCollectionEvent;
import org.literacyapp.util.EnvironmentSettings;
import org.literacyapp.util.MultimediaHelper;
import org.literacyapp.util.StudentUpdateHelper;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import ch.zhaw.facerecognitionlibrary.Recognition.SupportVectorMachine;
import ch.zhaw.facerecognitionlibrary.Recognition.TensorFlow;
import pl.droidsonroids.gif.GifImageView;

public class AuthenticationActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    public static final String AUTHENTICATION_ANIMATION_ALREADY_PLAYED_IDENTIFIER = "AuthenticationAnimationAlreadyPlayed";
    public static final String ANIMAL_OVERLAY_IDENTIFIER = "AnimalOverlayName";
    private static final int NUMBER_OF_MAXIMUM_TRIES = 3;
    private SupportVectorMachine svm;
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
    private Thread tensorFlowLoadingThread;
    private RecognitionThread recognitionThread;
    private GifImageView authenticationAnimation;
    private boolean recognitionThreadStarted;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        authenticationAnimation = (GifImageView) findViewById(R.id.authentication_animation);
        MultimediaHelper.setAuthenticationInstructionAnimation(getApplicationContext(), authenticationAnimation);

        // Initialize DB Session
        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        DaoSession daoSession = literacyApplication.getDaoSession();
        studentImageCollectionEventDao = daoSession.getStudentImageCollectionEventDao();

        if (!readyForAuthentication()){
            startStudentImageCollectionActivity(false);
        }

        preview = (JavaCameraView) findViewById(R.id.CameraView);

        // Use front-camera
        preview.setCameraIndex(1);

        preview.setVisibility(SurfaceView.VISIBLE);
        preview.setCvCameraViewListener(this);

        mediaPlayerInstruction = MediaPlayer.create(this, R.raw.face_instruction);

        startTimeFallback = new Date().getTime();

        final TrainingHelper trainingHelper = new TrainingHelper(getApplicationContext());
        svm = trainingHelper.getSvm();

        tensorFlowLoadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                tensorFlow = trainingHelper.getInitializedTensorFlow();
            }
        });

        recognitionThreadStarted = false;

        animalOverlayHelper = new AnimalOverlayHelper(getApplicationContext());
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
        if (!tensorFlowLoadingThread.isAlive()){
            prepareForAuthentication();

            if (!recognitionThread.isAlive() && recognitionThreadStarted) {
                String svmProbability = svm.recognizeProbability(recognitionThread.getSvmString());
                Student student = getStudentFromProbability(svmProbability);
                numberOfTries++;
                recognitionThreadStarted = false;
                Log.i(getClass().getName(), "Number of authentication/recognition tries: " + numberOfTries);
                if (student != null) {
                    new StudentUpdateHelper(getApplicationContext(), student).updateStudent();
                    finish();
                } else if (numberOfTries >= NUMBER_OF_MAXIMUM_TRIES) {
                    startStudentImageCollectionActivity(true);
                }
            }

            Mat imgCopy = new Mat();

            // Store original image for face recognition
            imgRgba.copyTo(imgCopy);

            // Mirror front camera image
            Core.flip(imgRgba,imgRgba,1);

            // Face detection
            long currentTime = new Date().getTime();

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
                                if (mediaPlayerAnimalSound != null){
                                    mediaPlayerAnimalSound.start();
                                }
                                recognitionThread = new RecognitionThread(svm, tensorFlow);
                                recognitionThread.setImg(img);
                                recognitionThread.start();
                                recognitionThreadStarted = true;
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
            mediaPlayerAnimalSound = MediaPlayer.create(this, getResources().getIdentifier(animalOverlay.getSoundFile(), "raw", getPackageName()));
        }
        preview.enableView();
        mediaPlayerInstruction.start();
        tensorFlowLoadingThread.start();
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

            recognitionThread = new RecognitionThread(svm, tensorFlow);
            startTimeFallback = new Date().getTime();
        }
    }

    /**
     * Determines if and which Student has been recognized. The Student is only returned if the probability is above 90%
     * @param svmProbability
     * @return
     */
    private synchronized Student getStudentFromProbability(String svmProbability){
        // Example string for svmProbability: labels 1 2\n2 0.458817 0.541183
        StringTokenizer stringTokenizerSvmProbability = new StringTokenizer(svmProbability, "\n");
        // Example string for header: labels 1 2
        String header = stringTokenizerSvmProbability.nextToken();
        // Example string for content: 2 0.458817 0.541183
        String content = stringTokenizerSvmProbability.nextToken();

        StringTokenizer stringTokenizerHeader = new StringTokenizer(header, " ");
        // Skip first token
        stringTokenizerHeader.nextToken();
        StringTokenizer stringTokenizerContent = new StringTokenizer(content, " ");
        // First token shows the label with the highest probability
        int eventIdWithHighestProbability = Integer.valueOf(stringTokenizerContent.nextToken());

        HashMap<Integer, Double> probabilityMap = new HashMap<Integer, Double>();
        while(stringTokenizerHeader.hasMoreTokens()){
            probabilityMap.put(Integer.valueOf(stringTokenizerHeader.nextToken()), Double.valueOf(stringTokenizerContent.nextToken()));
        }

        if (probabilityMap.get(eventIdWithHighestProbability) > 0.9){
            StudentImageCollectionEvent studentImageCollectionEvent = studentImageCollectionEventDao.load((long) eventIdWithHighestProbability);
            Student student = studentImageCollectionEvent.getStudent();
            return student;
        } else {
            return null;
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
        long svmTrainingsExecutedCount = studentImageCollectionEventDao.queryBuilder().where(StudentImageCollectionEventDao.Properties.SvmTrainingExecuted.eq(true)).count();
        Log.i(getClass().getName(), "readyForAuthentication: svmTrainingsExecutedCount: " + svmTrainingsExecutedCount);
        boolean classifierFilesExist = TrainingHelper.classifierFilesExist();
        Log.i(getClass().getName(), "readyForAuthentication: classifierFilesExist: " + classifierFilesExist);
        if ((svmTrainingsExecutedCount > 0) && classifierFilesExist){
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
    }
}
