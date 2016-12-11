package org.literacyapp.authentication;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.DeviceDao;
import org.literacyapp.dao.StudentDao;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.dao.StudentImageDao;
import org.literacyapp.model.Device;
import org.literacyapp.model.StudentImage;
import org.literacyapp.model.StudentImageCollectionEvent;
import org.literacyapp.util.AiHelper;
import org.literacyapp.util.DeviceInfoHelper;
import org.literacyapp.util.MultimediaHelper;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
import ch.zhaw.facerecognitionlibrary.Helpers.MatName;
import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;

/**
 * Activity to collect images via the front camera view, adding an overlay and storing images of detected faces
 */

public class CameraViewActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private JavaCameraView preview;
    private PreProcessorFactory ppF;
    private long lastTime;
    private StudentImageDao studentImageDao;
    private StudentImageCollectionEventDao studentImageCollectionEventDao;
    private Device device;
    private DeviceDao deviceDao;
    private Mat imgOverlay;
    private LiteracyApplication literacyApplication;
    private List<Mat> studentImages;
    private List<Mat> testImages;

    // Image collection parameters
    private static final boolean DIAGNOSE_MODE = true;
    private static final long TIMER_DIFF = 200;
    private static final int NUMBER_OF_IMAGES = 20;
    private int imagesProcessed;

    // Mat objects for overlay
    private Mat imgMask  = new Mat();
    private Mat imgInvMask  = new Mat();

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.face_instruction);
        mediaPlayer.start();

        preview = (JavaCameraView) findViewById(R.id.CameraView);

        preview.setCameraIndex(1);

        preview.setVisibility(SurfaceView.VISIBLE);
        preview.setCvCameraViewListener(this);

        lastTime = new Date().getTime();

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

        // ToDo studenImageCollectionEvent creation local or external
        // studentImageCollectionEvent = new StudentImageCollectionEvent(collectionEventId);
        studentImages = new ArrayList<>();
//        if (literacyApplication.TEST_MODE){
//            testImages = new ArrayList<>();
//        }

      //studentImageCollectionEvent = new StudentImageCollectionEvent(collectionEventId);
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
        Mat imgCopy = new Mat();

        // Store original image for face recognition
        imgRgba.copyTo(imgCopy);

        // Mirror front camera image
        Core.flip(imgRgba,imgRgba,1);

        // Face detection
        long time = new Date().getTime();

        if(lastTime + TIMER_DIFF < time){
//            if (literacyApplication.TEST_MODE){
//                testImages.add(imgCopy);
//            }
            List<Mat> images = ppF.getCroppedImage(imgCopy);
            if(images != null && images.size() == 1){
                Mat img = images.get(0);
                if(img != null) {
                    Rect[] faces = ppF.getFacesForRecognition();
                    if ((faces != null) && (faces.length == 1)) {
                        faces = MatOperation.rotateFaces(imgRgba, faces, ppF.getAngleForRecognition());

                        // Name = DeviceId_CollectionEventId_ImageNumber
                        studentImages.add(img);

                        if(DIAGNOSE_MODE) {
                            MatOperation.drawRectangleAndLabelOnPreview(imgRgba, faces[0], "Face detected", true);
                        }

                        // Stop after NUMBER_OF_IMAGES (settings option)
                        if(imagesProcessed > NUMBER_OF_IMAGES){
                            storeStudentImages();
//                        if (literacyApplication.TEST_MODE){
//                            storeTestImages();
//                        }
                            finish();
                        }

                        imagesProcessed++;
                    }
                }
            }
        }

        // Add overlay
        addOverlay(imgRgba);

        return imgRgba;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ppF = new PreProcessorFactory(getApplicationContext());
        createOverlay();
        preview.enableView();
    }

    private synchronized void storeStudentImages(){
        StudentImageCollectionEvent studentImageCollectionEvent = new StudentImageCollectionEvent();
        studentImageCollectionEvent.setTime(Calendar.getInstance());
        studentImageCollectionEvent.setDevice(device);
        Long studentImageCollectionEventId = studentImageCollectionEventDao.insert(studentImageCollectionEvent);
        for(int i=0; i<studentImages.size(); i++){
            MatName matName = new MatName(Integer.toString(i), studentImages.get(i));
            FileHelper fileHelper = new FileHelper();
            String wholeFolderPath = MultimediaHelper.getStudentImageDirectory() + "/" + device.getDeviceId() + "/" + Long.toString(studentImageCollectionEventId);
            new File(wholeFolderPath).mkdirs();
            fileHelper.saveMatToImage(matName, wholeFolderPath + "/");

            String imageUrl = wholeFolderPath + "/" + Integer.toString(i) + ".png";
            StudentImage studentImage = new StudentImage();
            studentImage.setTimeCollected(Calendar.getInstance());
            studentImage.setImageFileUrl(imageUrl);
            studentImage.setStudentImageCollectionEvent(studentImageCollectionEvent);
            studentImageDao.insert(studentImage);
        }
    }

    private void createOverlay() {
        Log.i(getClass().getName(), "createOverlay");
        File[] animalTemplateFiles = AiHelper.getAnimalTemplateDirectory(getApplicationContext()).listFiles();

        int randomNumber = (int) (Math.random() * animalTemplateFiles.length);

        imgOverlay = Imgcodecs.imread(animalTemplateFiles[randomNumber].getAbsolutePath(), Imgcodecs.IMREAD_UNCHANGED);
        Imgproc.cvtColor(imgOverlay, imgOverlay, Imgproc.COLOR_BGR2RGBA);

        // Create a mask of overlay and create its inverse mask also
        Imgproc.cvtColor(imgOverlay, imgMask, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.threshold(imgMask, imgMask, 224, 255, Imgproc.THRESH_BINARY_INV);
        Core.bitwise_not(imgMask,imgMask);
        Imgproc.cvtColor(imgMask, imgMask, Imgproc.COLOR_GRAY2RGBA);

        Core.bitwise_not(imgMask,imgInvMask);
    }

    private void addOverlay(Mat imgRgba){
        Mat imgForeGround = new Mat();
        Mat imgBackGround = new Mat();

        //Black-out the area of overlay in img
        Core.bitwise_and(imgMask,imgRgba,imgBackGround);

        // Take only region of overlay from overlay image.
        Core.bitwise_and(imgOverlay,imgInvMask,imgForeGround);

        // Add overlay to frame
        Core.add(imgForeGround,imgBackGround,imgRgba);

    }

}
