package org.literacyapp.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import org.literacyapp.LiteracyApplication;

public class TtsHelper {

    public static void speak(Context context, String text) {
        Log.i(TtsHelper.class.getName(), "speak");

        Log.i(TtsHelper.class.getName(), "text: " + text);

        LiteracyApplication literacyApplication = (LiteracyApplication) context;
        TextToSpeech tts = literacyApplication.getTts();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
