package org.literacyapp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.literacyapp.dao.DaoMaster;
import org.literacyapp.dao.DaoSession;

public class LiteracyApplication extends Application {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    public static final boolean TEST_MODE = true;

    @Override
    public void onCreate() {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate();

        // Initialize greenDAO database
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "literacyapp-db", null);
        db = openHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

//        ScreenOnReceiver screenOnReceiver = new ScreenOnReceiver();
//        registerReceiver(screenOnReceiver, new IntentFilter("android.intent.action.SCREEN_ON"));
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
