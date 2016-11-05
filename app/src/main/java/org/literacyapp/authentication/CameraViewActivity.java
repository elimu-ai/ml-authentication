package org.literacyapp.authentication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.StudentImage;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.util.DeviceInfoHelper;
import org.literacyapp.util.MultimediaHelper;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.io.File;
import java.util.Date;

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
    private String deviceId;
    private String collectionEventId;
    private StudentImageCollectionEventDao studentImageCollectionEventDao;
    private int imagesProcessed;

    // Image collection parameters
    private static final boolean diagnoseMode = true;
    private static final long timerDiff = 100;
    private static final int numberOfImages = 20;


    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        preview = (JavaCameraView) findViewById(R.id.CameraView);

        preview.setCameraIndex(1);

        preview.setVisibility(SurfaceView.VISIBLE);
        preview.setCvCameraViewListener(this);

        lastTime = new Date().getTime();

        deviceId = DeviceInfoHelper.getDeviceId(getApplicationContext());
        // Calculate random CollectionEventId until the DB is not setup

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        studentImageCollectionEventDao = literacyApplication.getDaoSession().getStudentImageCollectionEventDao();

        collectionEventId = deviceId + String.format("%016d",studentImageCollectionEventDao.count() + 1);

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
        imagesProcessed = 1;

        // Store original image for face recognition
        imgRgba.copyTo(imgCopy);

        // Mirror front camera image
        Core.flip(imgRgba,imgRgba,1);

        // Face detection
        long time = new Date().getTime();

        if(lastTime + timerDiff < time){
            Mat img = ppF.getCroppedImage(imgCopy);
            if(img != null) {
                Rect[] faces = ppF.getFacesForRecognition();
                if ((faces != null) && (faces.length == 1)) {
                    faces = MatOperation.rotateFaces(imgRgba, faces, ppF.getAngleForRecognition());

                    // Name = DeviceId_CollectionEventId_ImageNumber
                    storeStudentImage(img);

                    if(diagnoseMode) {
                        MatOperation.drawRectangleAndLabelOnPreview(imgRgba, faces[0], "Face detected", true);
                    }

                    // Stop after numberOfImages (settings option)
                    if(imagesProcessed > numberOfImages){
                        finish();
                    }

                    imagesProcessed++;
                }
            }
        }

        return imgRgba;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ppF = new PreProcessorFactory(getApplicationContext());
        preview.enableView();
    }

    private void storeStudentImage(Mat img){

        String sId = collectionEventId + imagesProcessed;

        MatName matName = new MatName(sId, img);
        FileHelper fh = new FileHelper();
        String wholeFolderPath = MultimediaHelper.getStudentImageDirectory() + "/" + deviceId + "/" + collectionEventId;
        new File(wholeFolderPath).mkdirs();
        fh.saveMatToImage(matName, wholeFolderPath + "/");

        Long Id =  Long.parseLong(String.valueOf((int) (Math.random() * 1000000)));
        StudentImage studentImage = new StudentImage(Id, null, wholeFolderPath, null, null);

    }

}
