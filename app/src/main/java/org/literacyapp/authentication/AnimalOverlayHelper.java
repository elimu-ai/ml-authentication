package org.literacyapp.authentication;

import android.content.Context;
import android.util.Log;

import org.literacyapp.util.AiHelper;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

/**
 * Created by sladomic on 18.12.16.
 */

public class AnimalOverlayHelper {
    private Context context;
    private Mat imgOverlay;
    private Mat imgMask;
    private Mat imgInvMask;

    public AnimalOverlayHelper(Context context){
        this.context = context;
        imgMask = new Mat();
        imgInvMask = new Mat();
    }

    public void createOverlay() {
        Log.i(getClass().getName(), "createOverlay");
        File[] animalTemplateFiles = AiHelper.getAnimalTemplateDirectory(context).listFiles();

        int randomNumber = (int) (Math.random() * animalTemplateFiles.length);

        imgOverlay = Imgcodecs.imread(animalTemplateFiles[randomNumber].getAbsolutePath(), Imgcodecs.IMREAD_UNCHANGED);
        Imgproc.cvtColor(imgOverlay, imgOverlay, Imgproc.COLOR_BGR2RGBA);

        // Create a mask of overlay and create its inverse mask also
        Imgproc.cvtColor(imgOverlay, imgMask, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.threshold(imgMask, imgMask, 250, 255, Imgproc.THRESH_BINARY_INV);
        Core.bitwise_not(imgMask,imgMask);
        Imgproc.cvtColor(imgMask, imgMask, Imgproc.COLOR_GRAY2RGBA);

        Core.bitwise_not(imgMask,imgInvMask);
    }

    public void addOverlay(Mat imgRgba){
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
