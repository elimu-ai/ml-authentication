package org.literacyapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

public class CustomDaoMaster extends DaoMaster {

    public CustomDaoMaster(SQLiteDatabase db) {
        super(db);
        Log.i(getClass().getName(), "CustomDaoMaster");
    }

    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i(getClass().getName(), "Upgrading schema from version " + oldVersion + " to " + newVersion);

            if (oldVersion < 1001018) {
                // Add new tables and/or columns automatically (include only the DAO classes that have been modified)
                DbMigrationHelper.migrate(db,
                        DeviceDao.class,
                        StudentImageDao.class
                );
            }
            if (oldVersion < 1001022) {
                DbMigrationHelper.migrate(db,
                        AudioDao.class
                );
            }
            if (oldVersion < 1001023) {
                DbMigrationHelper.migrate(db,
                        AudioDao.class
                );
            }

            if (oldVersion < 1001025) {
                DbMigrationHelper.migrate(db,
                        StudentImageDao.class,
                        StudentImageCollectionEventDao.class
                );
            }

            if (oldVersion < 1001026) {
                DbMigrationHelper.migrate(db,
                        StudentImageDao.class,
                        StudentImageCollectionEventDao.class
                );
            }

            if (oldVersion < 1001027) {
                DbMigrationHelper.migrate(db,
                        WordDao.class
                );
            }

            if (oldVersion < 1001028) {
                DbMigrationHelper.migrate(db,
                        LetterDao.class,
                        VideoDao.class,
                        JoinVideosWithLettersDao.class
                );
            }

            // If tables and/or columns have been renamed, add custom script.
//            db.execSQL("...");
        }
    }
}
