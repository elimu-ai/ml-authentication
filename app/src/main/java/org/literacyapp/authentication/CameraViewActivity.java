package org.literacyapp.authentication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;

import org.literacyapp.R;
import org.literacyapp.util.MultimediaHelper;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;

public class CameraViewActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private JavaCameraView preview;
    private PreProcessorFactory ppF;
    private Boolean diagnoseMode;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        diagnoseMode = Boolean.FALSE;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        preview = (JavaCameraView) findViewById(R.id.CameraView);

        preview.setCameraIndex(1);

        preview.setVisibility(SurfaceView.VISIBLE);
        preview.setCvCameraViewListener(this);

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
        Mat img = ppF.getCroppedImage(imgCopy);
        if(img != null) {
            Rect[] faces = ppF.getFacesForRecognition();
            if ((faces != null) && (faces.length == 1)) {
                faces = MatOperation.rotateFaces(imgRgba, faces, ppF.getAngleForRecognition());

                if(diagnoseMode){
                    MatOperation.drawRectangleAndLabelOnPreview(imgRgba, faces[0], "Face detected", true);
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
        preview.enableView();
    }

    private void addOverlay(Mat imgRgba){
        Mat imgForeGround = new Mat();
        Mat imgBackGround = new Mat();
        Mat imgMask = new Mat();

        // Load overlay mask (to be completed...)
        Mat imgOverlay = Imgcodecs.imread(MultimediaHelper.getImageDirectory() + "/kangaroo.jpg",Imgcodecs.IMREAD_UNCHANGED);
        Imgproc.cvtColor(imgOverlay, imgOverlay, Imgproc.COLOR_BGR2RGBA);

        // Create a mask of overlay and create its inverse mask also
        Imgproc.cvtColor(imgOverlay, imgMask, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.threshold(imgMask, imgMask, 127, 255, Imgproc.THRESH_BINARY_INV);
        Core.bitwise_not(imgMask,imgMask);

        //Black-out the area of overlay in img
        Imgproc.cvtColor(imgMask, imgMask, Imgproc.COLOR_GRAY2RGBA);
        Core.bitwise_and(imgMask,imgRgba,imgBackGround);

        // Take only region of overlay from overlay image.
        Core.bitwise_not(imgMask,imgMask);
        Core.bitwise_and(imgOverlay,imgMask,imgForeGround);

        // Add overlay to frame
        Core.add(imgForeGround,imgBackGround,imgRgba);

    }
}
