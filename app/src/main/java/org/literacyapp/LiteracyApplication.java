package org.literacyapp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.converter.CustomDaoMaster;

public class LiteracyApplication extends Application {

    private DaoSession daoSession;

    private TextToSpeech tts;

    @Override
    public void onCreate() {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate();

        // Initialize greenDAO database
        CustomDaoMaster.DevOpenHelper openHelper = new CustomDaoMaster.DevOpenHelper(getApplicationContext(), "literacyapp-db", null);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        CustomDaoMaster daoMaster = new CustomDaoMaster(db);
        daoSession = daoMaster.newSession();

        // Initialize TTS
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.i(getClass().getName(), "TextToSpeech onInit");
                Log.i(getClass().getName(), "TextToSpeech status: " + status);
            }
        });
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }


    public TextToSpeech getTts() {
        return tts;
    }
}
