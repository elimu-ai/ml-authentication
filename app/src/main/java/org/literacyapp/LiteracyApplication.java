package org.literacyapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import org.literacyapp.contentprovider.dao.CustomDaoMaster;
import org.literacyapp.contentprovider.dao.DaoSession;
import org.literacyapp.util.VersionHelper;

public class LiteracyApplication extends Application {

    public static final int FACE_RECOGNITION_TRAINING_JOB_ID = 0;
    public static final int CONTENT_SYNCRHONIZATION_JOB_ID = 1;
    public static final int AUTHENTICATION_JOB_ID = 2;
    public static final int MERGE_SIMILAR_STUDENTS_JOB_ID = 3;

    public static final String PREF_APP_VERSION_CODE = "pref_app_version_code";

    private DaoSession daoSession;

    private TextToSpeech tts;

    @Override
    public void onCreate() {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate();

        // Check if the application's versionCode was upgraded
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int oldVersionCode = sharedPreferences.getInt(PREF_APP_VERSION_CODE, 0);
        int newVersionCode = VersionHelper.getAppVersionCode(getApplicationContext());
        if (oldVersionCode == 0) {
            sharedPreferences.edit().putInt(PREF_APP_VERSION_CODE, newVersionCode).commit();
            oldVersionCode = newVersionCode;
        }
        if (oldVersionCode < newVersionCode) {
            Log.i(getClass().getName(), "Upgrading application from version " + oldVersionCode + " to " + newVersionCode);
//            if (oldVersionCode < ???) {
//                // Put relevant tasks required for upgrading here
//            }
            sharedPreferences.edit().putInt(PREF_APP_VERSION_CODE, newVersionCode).commit();
        }

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
        if (daoSession == null) {
            // Initialize greenDAO database
            String dbName = Environment.getExternalStorageDirectory() + "/.literacyapp/database/literacyapp-db";
            CustomDaoMaster.DevOpenHelper openHelper = new CustomDaoMaster.DevOpenHelper(getApplicationContext(), dbName, null);
            SQLiteDatabase db = openHelper.getWritableDatabase();
            CustomDaoMaster daoMaster = new CustomDaoMaster(db);
            daoSession = daoMaster.newSession();
        }

        return daoSession;
    }

    public TextToSpeech getTts() {
        return tts;
    }
}
