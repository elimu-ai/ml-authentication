package org.literacyapp;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.literacyapp.dao.DaoMaster;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.service.ScreenOnService;

public class LiteracyApplication extends Application {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate();

        // Initialize greenDAO database
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "literacyapp-db", null);
        db = openHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        // Start service for detecting when the screen is turned on
        Intent screenOnServiceIntent = new Intent(getApplicationContext(), ScreenOnService.class);
        startService(screenOnServiceIntent);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
