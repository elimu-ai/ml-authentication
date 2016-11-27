package org.literacyapp.dao.converter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.literacyapp.dao.DaoMaster;

public class CustomDaoMaster extends DaoMaster {

    public CustomDaoMaster(SQLiteDatabase db) {
        super(db);
        Log.i(getClass().getName(), "CustomDaoMaster");
    }

    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(getClass().getName(), "Upgrading schema from version " + oldVersion + " to " + newVersion);
            // TODO: add new tables and/or columns automatically
            // TODO: execute custom migration script for a specific version
        }
    }
}
