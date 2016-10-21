package org.literacyapp.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import java.util.Calendar;
import org.literacyapp.dao.converter.CalendarConverter;

import org.literacyapp.dao.StudentImageCollectionEvent;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "STUDENT_IMAGE_COLLECTION_EVENT".
*/
public class StudentImageCollectionEventDao extends AbstractDao<StudentImageCollectionEvent, Long> {

    public static final String TABLENAME = "STUDENT_IMAGE_COLLECTION_EVENT";

    /**
     * Properties of entity StudentImageCollectionEvent.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property DeviceId = new Property(1, Long.class, "deviceId", false, "DEVICE_ID");
        public final static Property Time = new Property(2, Long.class, "time", false, "TIME");
        public final static Property ApplicationId = new Property(3, Long.class, "applicationId", false, "APPLICATION_ID");
        public final static Property StudentId = new Property(4, Long.class, "studentId", false, "STUDENT_ID");
    };

    private DaoSession daoSession;

    private final CalendarConverter timeConverter = new CalendarConverter();

    public StudentImageCollectionEventDao(DaoConfig config) {
        super(config);
    }
    
    public StudentImageCollectionEventDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"STUDENT_IMAGE_COLLECTION_EVENT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"DEVICE_ID\" INTEGER," + // 1: deviceId
                "\"TIME\" INTEGER," + // 2: time
                "\"APPLICATION_ID\" INTEGER," + // 3: applicationId
                "\"STUDENT_ID\" INTEGER);"); // 4: studentId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STUDENT_IMAGE_COLLECTION_EVENT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, StudentImageCollectionEvent entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long deviceId = entity.getDeviceId();
        if (deviceId != null) {
            stmt.bindLong(2, deviceId);
        }
 
        Calendar time = entity.getTime();
        if (time != null) {
            stmt.bindLong(3, timeConverter.convertToDatabaseValue(time));
        }
 
        Long applicationId = entity.getApplicationId();
        if (applicationId != null) {
            stmt.bindLong(4, applicationId);
        }
 
        Long studentId = entity.getStudentId();
        if (studentId != null) {
            stmt.bindLong(5, studentId);
        }
    }

    @Override
    protected void attachEntity(StudentImageCollectionEvent entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public StudentImageCollectionEvent readEntity(Cursor cursor, int offset) {
        StudentImageCollectionEvent entity = new StudentImageCollectionEvent( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // deviceId
            cursor.isNull(offset + 2) ? null : timeConverter.convertToEntityProperty(cursor.getLong(offset + 2)), // time
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // applicationId
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4) // studentId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, StudentImageCollectionEvent entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDeviceId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setTime(cursor.isNull(offset + 2) ? null : timeConverter.convertToEntityProperty(cursor.getLong(offset + 2)));
        entity.setApplicationId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setStudentId(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(StudentImageCollectionEvent entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(StudentImageCollectionEvent entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getDeviceDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getStudentDao().getAllColumns());
            builder.append(" FROM STUDENT_IMAGE_COLLECTION_EVENT T");
            builder.append(" LEFT JOIN DEVICE T0 ON T.\"DEVICE_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN STUDENT T1 ON T.\"STUDENT_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected StudentImageCollectionEvent loadCurrentDeep(Cursor cursor, boolean lock) {
        StudentImageCollectionEvent entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Device device = loadCurrentOther(daoSession.getDeviceDao(), cursor, offset);
        entity.setDevice(device);
        offset += daoSession.getDeviceDao().getAllColumns().length;

        Student student = loadCurrentOther(daoSession.getStudentDao(), cursor, offset);
        entity.setStudent(student);

        return entity;    
    }

    public StudentImageCollectionEvent loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<StudentImageCollectionEvent> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<StudentImageCollectionEvent> list = new ArrayList<StudentImageCollectionEvent>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<StudentImageCollectionEvent> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<StudentImageCollectionEvent> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}