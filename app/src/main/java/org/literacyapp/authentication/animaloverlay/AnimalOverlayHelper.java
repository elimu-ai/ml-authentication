package org.literacyapp.authentication.animaloverlay;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by sladomic on 18.12.16.
 */

public class AnimalOverlayHelper {
    private static final String ANIMAL_OVERLAYS_CONFIG_JSON = "AnimalOverlaysConfig.json";
    private Context context;

    public AnimalOverlayHelper(Context context){
        this.context = context;
    }

    public AnimalOverlay getAnimalOverlay(String animalOverlayName) {
        Log.i(getClass().getName(), "getAnimalOverlay");
        List<AnimalOverlay> animalOverlays = getAnimalOverlays(animalOverlayName);

        if (animalOverlays.size() > 0){
            AnimalOverlay animalOverlay;

            if (!TextUtils.isEmpty(animalOverlayName) && (animalOverlays.size() == 1)){
                animalOverlay = animalOverlays.get(0);
            } else {
                int randomIndex = (int) (Math.random() * animalOverlays.size());
                animalOverlay = animalOverlays.get(randomIndex);
            }

            return animalOverlay;
        } else {
            return null;
        }
    }

    private List<AnimalOverlay> getAnimalOverlays(String animalOverlayName){
        AnimalOverlaysMap animalOverlaysMap = getAnimalOverlaysMapFromConfig();
        if (animalOverlaysMap == null){
            Log.e(getClass().getName(), "The asset: " + ANIMAL_OVERLAYS_CONFIG_JSON + " is missing or cannot be parsed.");
            return null;
        }
        Set<String> animalOverlaysKeySet = animalOverlaysMap.getAnimalOverlays().keySet();
        List<AnimalOverlay> animalOverlaysValidated = new ArrayList<>();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        for (String animalOverlayKey : animalOverlaysKeySet){
            if (animalOverlaysMap.getAnimalOverlays().get(animalOverlayKey) != null){
                if (context.getResources().getIdentifier(animalOverlayKey, "drawable", context.getPackageName()) == 0){
                    Log.w(getClass().getName(), "The AnimalOverlay " + animalOverlayKey + " is discarded because the image file: " + animalOverlayKey + " is not existing");
                    continue;
                }
                int frameStartX = animalOverlaysMap.getAnimalOverlays().get(animalOverlayKey).getFrameStartX();
                if (frameStartX < 0){
                    Log.w(getClass().getName(), "The AnimalOverlay " + animalOverlayKey + " is discarded because of the frameStartX: " + frameStartX + " being < 0");
                    continue;
                }
                int frameStartY = animalOverlaysMap.getAnimalOverlays().get(animalOverlayKey).getFrameStartY();
                if (frameStartY < 0){
                    Log.w(getClass().getName(), "The AnimalOverlay " + animalOverlayKey + " is discarded because of the frameStartY: " + frameStartY + " being < 0");
                    continue;
                }
                int frameEndX = animalOverlaysMap.getAnimalOverlays().get(animalOverlayKey).getFrameEndX();
                if (frameEndX > screenWidth){
                    Log.w(getClass().getName(), "The AnimalOverlay " + animalOverlayKey + " is discarded because of the frameEndX: " + frameEndX + " being > screenWidth: " + screenWidth);
                    continue;
                }
                int frameEndY = animalOverlaysMap.getAnimalOverlays().get(animalOverlayKey).getFrameEndY();
                if (frameEndY > screenHeight){
                    Log.w(getClass().getName(), "The AnimalOverlay " + animalOverlayKey + " is discarded because of the frameEndY: " + frameEndY + " being > screenHeight: " + screenHeight);
                    continue;
                }
                String soundFile = animalOverlaysMap.getAnimalOverlays().get(animalOverlayKey).getSoundFile();
                if (TextUtils.isEmpty(soundFile)){
                    Log.w(getClass().getName(), "The AnimalOverlay " + animalOverlayKey + " is discarded because of the soundFile: " + soundFile + " being empty");
                    continue;
                }
                if (context.getResources().getIdentifier(soundFile, "raw", context.getPackageName()) == 0){
                    Log.w(getClass().getName(), "The AnimalOverlay " + animalOverlayKey + " is discarded because the soundFile: " + soundFile + " is not existing");
                    continue;
                }

                AnimalOverlay animalOverlay = new AnimalOverlay(animalOverlayKey, frameStartX, frameStartY, frameEndX, frameEndY, soundFile);

                // If only a specific animalOverlay shall be returned
                if (animalOverlayKey.equals(animalOverlayName)){
                    animalOverlaysValidated = new ArrayList<>();
                    animalOverlaysValidated.add(animalOverlay);
                    return animalOverlaysValidated;
                }

                animalOverlaysValidated.add(animalOverlay);
            } else {
                Log.w(getClass().getName(), "The config for the animalOverlay " + animalOverlayKey + " is missing. Please check the config file " + ANIMAL_OVERLAYS_CONFIG_JSON + " in the assets folder.");
            }
        }
        return animalOverlaysValidated;
    }

    private AnimalOverlaysMap getAnimalOverlaysMapFromConfig(){
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
