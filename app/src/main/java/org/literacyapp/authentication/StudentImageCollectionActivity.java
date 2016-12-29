package org.literacyapp.authentication;

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
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.DeviceDao;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.dao.StudentImageDao;
import org.literacyapp.model.Device;
import org.literacyapp.model.StudentImage;
import org.literacyapp.model.StudentImageCollectionEvent;
import org.literacyapp.receiver.BootReceiver;
import org.literacyapp.util.DeviceInfoHelper;
import org.literacyapp.util.EnvironmentSettings;
import org.literacyapp.util.MultimediaHelper;
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

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
import ch.zhaw.facerecognitionlibrary.Helpers.MatName;
import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import pl.droidsonroids.gif.GifImageView;

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
    private GifImageView authenticationAnimation;
    private boolean authenticationAnimationAlreadyPlayed;
    private String animalOverlayName;

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
        authenticationAnimation = (GifImageView) findViewById(R.id.authentication_animation);
        MultimediaHelper.setAuthenticationInstructionAnimation(getApplicationContext(), authenticationAnimation);

        authenticationAnimationAlreadyPlayed = getIntent().getBooleanExtra(AuthenticationActivity.AUTHENTICATION_ANIMATION_ALREADY_PLAYED_IDENTIFIER, false);
        if (authenticationAnimationAlreadyPlayed){
            authenticationAnimation.setVisibility(View.INVISIBLE);
        }

        animalOverlayName = getIntent().getStringExtra(AuthenticationActivity.ANIMAL_OVERLAY_IDENTIFIER);

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
                if((images != null) && (images.size() == 1)){
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
        animalOverlay = animalOverlayHelper.getAnimalOverlay(animalOverlayName);
        if (animalOverlay != null){
            mediaPlayerAnimalSound = MediaPlayer.create(this, getResources().getIdentifier(animalOverlay.getSoundFile(), "raw", getPackageName()));
        }
        preview.enableView();
        if (!authenticationAnimationAlreadyPlayed){
            mediaPlayerInstruction.start();
        }
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

                // Initiate background job for face recognition training
                BootReceiver.scheduleFaceRecognitionTranining(getApplicationContext());

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
