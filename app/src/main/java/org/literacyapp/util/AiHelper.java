package org.literacyapp.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by sladomic on 26.11.16.
 */

public class AiHelper {
    private static final String ASSETS_ANIMAL_TEMPLATE_PATH = "animal_template";

    public static File getAiDirectory() {
        File aiDirectory = new File(Environment.getExternalStorageDirectory() + "/.literacyapp/ai");
        if (!aiDirectory.exists()) {
            aiDirectory.mkdirs();
        }
        return aiDirectory;
    }

    public static File getModelDirectory() {
        File modelDirectory = new File(getAiDirectory(), "model");
        if (!modelDirectory.exists()) {
            modelDirectory.mkdir();
        }
        return modelDirectory;
    }

    public static File getSvmDirectory() {
        File svmDirectory = new File(getAiDirectory(), "svm");
        if (!svmDirectory.exists()) {
            svmDirectory.mkdir();
        }
        return svmDirectory;
    }

    public static File getAnimalTemplateDirectory(Context context) {
        File animalTemplateDirectory = new File(getAiDirectory(), "animalTemplate");
        if (!animalTemplateDirectory.exists()) {
            animalTemplateDirectory.mkdir();
        }

        copyAnimalTemplatesToSdCard(context, animalTemplateDirectory);

        return animalTemplateDirectory;
    }

    private static void copyAnimalTemplatesToSdCard(Context context, File animalTemplateDirectory){
        AssetManager assetManager = context.getAssets();
        String[] animalTemplateAssets = null;
        try {
            animalTemplateAssets = assetManager.list(ASSETS_ANIMAL_TEMPLATE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (animalTemplateAssets != null){
            for(String animalTemplateAsset : animalTemplateAssets){
                File animalTemplateFile = new File(animalTemplateDirectory, animalTemplateAsset);
                if (!animalTemplateFile.exists()){
                    try {
                        InputStream inputStream = assetManager.open(ASSETS_ANIMAL_TEMPLATE_PATH + "/" + animalTemplateAsset);
                        OutputStream outputStream = new FileOutputStream(animalTemplateFile);
                        Log.i(AiHelper.class.getName(), "Copying overlay template to " + animalTemplateDirectory.getAbsolutePath() + animalTemplateAsset);
                        MultimediaHelper.copyFile(inputStream, outputStream);
                        Log.i(AiHelper.class.getName(), "Overlay template has been copied successfully to " + animalTemplateDirectory.getAbsolutePath() + animalTemplateAsset);
                    } catch (IOException e) {
                        Log.i(AiHelper.class.getName(), "Overlay template has failed to be copied to  " + animalTemplateDirectory.getAbsolutePath() + animalTemplateAsset);
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
