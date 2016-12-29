package org.literacyapp.authentication;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
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
import org.literacyapp.authentication.fallback.StudentRegistrationActivity;
import org.literacyapp.authentication.fallback.StudentSelectionActivity;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.DeviceDao;
import org.literacyapp.dao.StudentDao;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.dao.StudentImageDao;
import org.literacyapp.model.Device;
import org.literacyapp.model.StudentImage;
import org.literacyapp.model.StudentImageCollectionEvent;
import org.literacyapp.service.FaceRecognitionTrainingJobService;
import org.literacyapp.util.DeviceInfoHelper;
import org.literacyapp.util.EnvironmentSettings;
import org.literacyapp.util.MediaPlayerHelper;
import org.literacyapp.util.StudentHelper;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
import ch.zhaw.facerecognitionlibrary.Helpers.MatName;
import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;

/**
 * Activity to collect images via the front camera view, adding an overlay and storing images of detected faces
 */

public class StudentImageCollectionActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private JavaCameraView preview;
    private PreProcessorFactory ppF;
    private long lastTime;
    private long startTimeFallback;
    private long startTimeAuthenticationAnimation;
    private StudentImageDao studentImageDao;
    private StudentImageCollectionEventDao studentImageCollectionEventDao;
    private Device device;
    private DeviceDao deviceDao;
    private LiteracyApplication literacyApplication;
    private List<Mat> studentImages;
    private AnimalOverlayHelper animalOverlayHelper;
    private AnimalOverlay animalOverlay;
    private MediaPlayer mediaPlayerInstruction;
    private MediaPlayer mediaPlayerAnimalSound;
    private ImageView authenticationAnimation;
    private boolean authenticationAnimationAlreadyPlayed;

    // Image collection parameters
    private static final boolean DIAGNOSE_MODE = true;
    private static final long TIMER_DIFF = 200;
    private static long AUTHENTICATION_ANIMATION_TIME = 4000;
    private static final int NUMBER_OF_IMAGES = 20;
    private int imagesProcessed;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_student_image_collection);
        authenticationAnimation = (ImageView)findViewById(R.id.authentication_animation);

        authenticationAnimationAlreadyPlayed = getIntent().getBooleanExtra(AuthenticationActivity.AUTHENTICATION_ANIMATION_ALREADY_PLAYED_IDENTIFIER, false);
        if (authenticationAnimationAlreadyPlayed){
            authenticationAnimation.setVisibility(View.INVISIBLE);
        }

        mediaPlayerInstruction = MediaPlayer.create(this, R.raw.face_instruction);

        preview = (JavaCameraView) findViewById(R.id.CameraView);

        preview.setCameraIndex(1);

        preview.setVisibility(SurfaceView.VISIBLE);
        preview.setCvCameraViewListener(this);

        lastTime = new Date().getTime();
        startTimeFallback = lastTime;
        startTimeAuthenticationAnimation = lastTime;

        // Reset imageProcessed counter
        imagesProcessed = 0;

        // Initialize DB Session
        literacyApplication = (LiteracyApplication) getApplicationContext();
        DaoSession daoSession = literacyApplication.getDaoSession();
        studentImageCollectionEventDao = literacyApplication.getDaoSession().getStudentImageCollectionEventDao();

        // Create required DB Objects
        studentImageCollectionEventDao = daoSession.getStudentImageCollectionEventDao();
        studentImageDao = daoSession.getStudentImageDao();
        deviceDao = daoSession.getDeviceDao();
        String deviceId = DeviceInfoHelper.getDeviceId(getApplicationContext());
        device = deviceDao.queryBuilder().where(DeviceDao.Properties.DeviceId.eq(deviceId)).unique();
        if (device == null) {
            device = new Device();
            device.setDeviceId(deviceId);
            deviceDao.insert(device);
        }

        studentImages = new ArrayList<>();

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

        // Face detection
        long currentTime = new Date().getTime();

        if (authenticationAnimationAlreadyPlayed || ((startTimeAuthenticationAnimation + AUTHENTICATION_ANIMATION_TIME) < currentTime)){
            prepareForAuthentication();

            Mat imgCopy = new Mat();

            // Store original image for face recognition
            imgRgba.copyTo(imgCopy);

            // Mirror front camera image
            Core.flip(imgRgba,imgRgba,1);

            Rect face = new Rect();
            boolean isFaceInsideFrame = false;
            boolean faceDetected = false;

            if((lastTime + TIMER_DIFF) < currentTime){
                lastTime = currentTime;
                List<Mat> images = ppF.getCroppedImage(imgCopy);
                if(images != null && images.size() == 1){
                    Mat img = images.get(0);
                    if(img != null) {
                        Rect[] faces = ppF.getFacesForRecognition();
                        if ((faces != null) && (faces.length == 1)) {
                            faces = MatOperation.rotateFaces(imgRgba, faces, ppF.getAngleForRecognition());
                            face = faces[0];
                            faceDetected = true;
                            // Reset startTimeFallback for fallback timeout, because at least one face has been detected
                            startTimeFallback = currentTime;
                            isFaceInsideFrame = DetectionHelper.isFaceInsideFrame(animalOverlay, imgRgba, face);

                            if (isFaceInsideFrame){
                                mediaPlayerAnimalSound.start();
                                studentImages.add(img);

                                if(DIAGNOSE_MODE) {
                                    MatOperation.drawRectangleAndLabelOnPreview(imgRgba, face, "Face detected", true);
                                }

                                // Stop after NUMBER_OF_IMAGES (settings option)
                                if(imagesProcessed == NUMBER_OF_IMAGES){
                                    storeStudentImages();
                                    finish();
                                }

                                imagesProcessed++;
                            }
                        }
                    }
                }
            }

            if (DetectionHelper.shouldFallbackActivityBeStarted(startTimeFallback, currentTime)){
                // Prevent from second execution of fallback activity because of threading
                startTimeFallback = currentTime;
                DetectionHelper.startFallbackActivity(getApplicationContext(), getClass().getName());
                finish();
            }

            // Add overlay
            animalOverlayHelper.addOverlay(imgRgba);

            if (faceDetected && !isFaceInsideFrame){
                DetectionHelper.drawArrowFromFaceToFrame(animalOverlay, imgRgba, face);
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
        animalOverlay = animalOverlayHelper.createOverlay();
        if (animalOverlay != null){
            mediaPlayerAnimalSound = MediaPlayer.create(this, getResources().getIdentifier(animalOverlay.getSoundFile(), "raw", getPackageName()));
        }
        preview.enableView();
        mediaPlayerInstruction.start();
    }

    private void prepareForAuthentication(){
        if (authenticationAnimation.getVisibility() == View.VISIBLE){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    authenticationAnimation.setVisibility(View.INVISIBLE);
                    preview.disableView();
                    preview.enableView();
                }
            });
            startTimeFallback = new Date().getTime();
        }
    }

    /**
     * Stores all the buffered StudentImages to the file system and database
     */
    private synchronized void storeStudentImages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StudentImageCollectionEvent studentImageCollectionEvent = new StudentImageCollectionEvent();
                studentImageCollectionEvent.setTime(Calendar.getInstance());
                studentImageCollectionEvent.setDevice(device);
                Long studentImageCollectionEventId = studentImageCollectionEventDao.insert(studentImageCollectionEvent);
                for(int i=0; i<studentImages.size(); i++){
                    MatName matName = new MatName(Integer.toString(i), studentImages.get(i));
                    FileHelper fileHelper = new FileHelper();
                    String wholeFolderPath = StudentHelper.getStudentImageDirectory() + "/" + device.getDeviceId() + "/" + Long.toString(studentImageCollectionEventId);
                    new File(wholeFolderPath).mkdirs();
                    fileHelper.saveMatToImage(matName, wholeFolderPath + "/");

                    String imageUrl = wholeFolderPath + "/" + Integer.toString(i) + ".png";
                    StudentImage studentImage = new StudentImage();
                    studentImage.setTimeCollected(Calendar.getInstance());
                    studentImage.setImageFileUrl(imageUrl);
                    studentImage.setStudentImageCollectionEvent(studentImageCollectionEvent);
                    studentImageDao.insert(studentImage);
                }
                Log.i(getClass().getName(), "storeStudentImages has finished successfully.");

                // Initiate background job ad-hoc for face recognition training
                ComponentName componentNameFaceRecognitionTranining = new ComponentName(getApplicationContext(), FaceRecognitionTrainingJobService.class);
                JobInfo.Builder builderFaceRecognitionTranining = new JobInfo.Builder(0, componentNameFaceRecognitionTranining);
                JobInfo faceRecognitionTrainingJobInfo = builderFaceRecognitionTranining.build();
                JobScheduler jobSchedulerFaceRecognitionTranining = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                jobSchedulerFaceRecognitionTranining.schedule(faceRecognitionTrainingJobInfo);
                Log.i(getClass().getName(), "FACE_RECOGNITION_TRAINING_JOB with ID " + 0 + " has been scheduled.");
            }
        }).start();
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
