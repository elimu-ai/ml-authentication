package org.literacyapp.authentication.animaloverlay;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.literacyapp.util.AiHelper;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sladomic on 18.12.16.
 */

public class AnimalOverlayHelper {
    private static final String ANIMAL_OVERLAYS_CONFIG_JSON = "AnimalOverlaysConfig.json";
    private Context context;
    private Mat imgOverlay;
    private Mat imgMask;
    private Mat imgInvMask;

    public AnimalOverlayHelper(Context context){
        this.context = context;
        imgMask = new Mat();
        imgInvMask = new Mat();
    }

    public AnimalOverlay createOverlay(String animalOverlayName) {
        Log.i(getClass().getName(), "createOverlay");
        List<AnimalOverlay> animalOverlays = getAnimalOverlays(animalOverlayName);

        if (animalOverlays.size() > 0){
            AnimalOverlay animalOverlay;

            if (!TextUtils.isEmpty(animalOverlayName) && (animalOverlays.size() == 1)){
                animalOverlay = animalOverlays.get(0);
            } else {
                int randomIndex = (int) (Math.random() * animalOverlays.size());
                animalOverlay = animalOverlays.get(randomIndex);
            }

            imgOverlay = Imgcodecs.imread(animalOverlay.getAnimalTemplateFile().getAbsolutePath(), Imgcodecs.IMREAD_UNCHANGED);
            Imgproc.cvtColor(imgOverlay, imgOverlay, Imgproc.COLOR_BGR2RGBA);

            // Create a mask of overlay and create its inverse mask also
            Imgproc.cvtColor(imgOverlay, imgMask, Imgproc.COLOR_BGRA2GRAY);
            Imgproc.threshold(imgMask, imgMask, 250, 255, Imgproc.THRESH_BINARY_INV);
            Core.bitwise_not(imgMask,imgMask);
            Imgproc.cvtColor(imgMask, imgMask, Imgproc.COLOR_GRAY2RGBA);

            Core.bitwise_not(imgMask,imgInvMask);

            return animalOverlay;
        } else {
            return null;
        }
    }

    public void addOverlay(Mat imgRgba){
        if (imgOverlay != null && !imgOverlay.empty()){
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

    private List<AnimalOverlay> getAnimalOverlays(String animalOverlayName){
        File[] animalTemplateFiles = AiHelper.getAnimalTemplateDirectory(context).listFiles();
        AnimalOverlaysMap animalOverlaysMap = getAnimalOverlaysMap();
        if (animalOverlaysMap == null){
            Log.e(getClass().getName(), "The asset: " + ANIMAL_OVERLAYS_CONFIG_JSON + " is missing or cannot be parsed.");
            return null;
        }
        List<AnimalOverlay> animalOverlays = new ArrayList<>();
        for (File animalTemplateFile : animalTemplateFiles){
            String assetName = animalTemplateFile.getName();
            if (animalOverlaysMap.getAnimalOverlays().get(assetName) != null){
                int imageWidth = animalOverlaysMap.getAnimalOverlays().get(assetName).getImageWidth();
                if (imageWidth <= 0){
                    Log.w(getClass().getName(), "The AnimalOverlay " + assetName + " is discarded because of the imageWidth: " + imageWidth + " being <= 0");
                    continue;
                }
                int imageHeight = animalOverlaysMap.getAnimalOverlays().get(assetName).getImageHeight();
                if (imageHeight <= 0){
                    Log.w(getClass().getName(), "The AnimalOverlay " + assetName + " is discarded because of the imageHeight: " + imageHeight + " being <= 0");
                    continue;
                }
                int frameStartX = animalOverlaysMap.getAnimalOverlays().get(assetName).getFrameStartX();
                if (frameStartX < 0){
                    Log.w(getClass().getName(), "The AnimalOverlay " + assetName + " is discarded because of the frameStartX: " + frameStartX + " being < 0");
                    continue;
                }
                int frameStartY = animalOverlaysMap.getAnimalOverlays().get(assetName).getFrameStartY();
                if (frameStartY < 0){
                    Log.w(getClass().getName(), "The AnimalOverlay " + assetName + " is discarded because of the frameStartY: " + frameStartY + " being < 0");
                    continue;
                }
                int frameEndX = animalOverlaysMap.getAnimalOverlays().get(assetName).getFrameEndX();
                if (frameEndX > imageWidth){
                    Log.w(getClass().getName(), "The AnimalOverlay " + assetName + " is discarded because of the frameEndX: " + frameEndX + " being > imageWidth: " + imageWidth);
                    continue;
                }
                int frameEndY = animalOverlaysMap.getAnimalOverlays().get(assetName).getFrameEndY();
                if (frameEndY > imageHeight){
                    Log.w(getClass().getName(), "The AnimalOverlay " + assetName + " is discarded because of the frameEndY: " + frameEndY + " being > imageHeight: " + imageHeight);
                    continue;
                }
                String soundFile = animalOverlaysMap.getAnimalOverlays().get(assetName).getSoundFile();
                if (TextUtils.isEmpty(soundFile)){
                    Log.w(getClass().getName(), "The AnimalOverlay " + assetName + " is discarded because of the soundFile: " + soundFile + " being empty");
                    continue;
                }
                if (context.getResources().getIdentifier(soundFile, "raw", context.getPackageName()) == 0){
                    Log.w(getClass().getName(), "The AnimalOverlay " + assetName + " is discarded because the soundFile: " + soundFile + " is not existing");
                    continue;
                }

                AnimalOverlay animalOverlay = new AnimalOverlay(assetName, animalTemplateFile, imageWidth, imageHeight, frameStartX, frameStartY, frameEndX, frameEndY, soundFile);

                // If only a specific animalOverlay shall be returned
                if (assetName.equals(animalOverlayName)){
                    animalOverlays = new ArrayList<>();
                    animalOverlays.add(animalOverlay);
                    return animalOverlays;
                }

                animalOverlays.add(animalOverlay);
            } else {
                Log.w(getClass().getName(), "The config for the animalOverlay " + assetName + " is missing. Please check the config file " + ANIMAL_OVERLAYS_CONFIG_JSON + " in the assets folder.");
            }
        }
        return animalOverlays;
    }

    private AnimalOverlaysMap getAnimalOverlaysMap(){
        Gson gson = new Gson();
        AnimalOverlaysMap animalOverlayMap = new AnimalOverlaysMap();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open(ANIMAL_OVERLAYS_CONFIG_JSON));
            animalOverlayMap = gson.fromJson(inputStreamReader, AnimalOverlaysMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return animalOverlayMap;
    }
}
