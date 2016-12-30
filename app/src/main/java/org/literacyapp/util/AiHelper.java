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
}
