package org.literacyapp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import org.literacyapp.dao.DaoMaster;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.util.Log;

public class LiteracyApplication extends Application {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        Log.d(getClass(), "onCreate");
        super.onCreate();

        // Initialize greenDAO database
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "literacyapp-db", null);
        db = openHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
