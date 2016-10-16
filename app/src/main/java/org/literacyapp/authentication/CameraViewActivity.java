package org.literacyapp.authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import org.literacyapp.R;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;

public class CameraViewActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private JavaCameraView preview;
    private PreProcessorFactory ppF;

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
        imgRgba.copyTo(imgCopy);

        Mat img = ppF.getCroppedImage(imgCopy);
        if(img != null) {
            Rect[] faces = ppF.getFacesForRecognition();
            if ((faces != null) && (faces.length == 1)) {
                faces = MatOperation.rotateFaces(imgRgba, faces, ppF.getAngleForRecognition());

                MatOperation.drawRectangleAndLabelOnPreview(imgRgba, faces[0], "Face detected", true);
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


}
