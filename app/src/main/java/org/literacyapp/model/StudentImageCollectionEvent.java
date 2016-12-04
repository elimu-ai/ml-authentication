package org.literacyapp.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.DeviceDao;
import org.literacyapp.dao.StudentDao;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.dao.converter.CalendarConverter;

import java.util.Calendar;

/**
 * Based on {@link org.literacyapp.model.gson.analytics.StudentImageCollectionEventGson}
 */
@Entity
public class StudentImageCollectionEvent {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @ToOne(joinProperty = "id")
    private Device device;

    @NotNull
    @Convert(converter = CalendarConverter.class, columnType = Long.class)
    private Calendar time;

//    @ToOne
//    private Application application;

    @ToOne(joinProperty = "id")
    private Student student;

    private boolean svmTrainingExecuted;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 136770604)
    private transient StudentImageCollectionEventDao myDao;

    @Generated(hash = 505686841)
    public StudentImageCollectionEvent(Long id, @NotNull Calendar time,
            boolean svmTrainingExecuted) {
        this.id = id;
        this.time = time;
        this.svmTrainingExecuted = svmTrainingExecuted;
    }

    @Generated(hash = 802935259)
    public StudentImageCollectionEvent() {
    }

    @Generated(hash = 708752895)
    private transient Long device__resolvedKey;

    @Generated(hash = 79695740)
    private transient Long student__resolvedKey;

    @Keep
    public void setDevice(Device device) {
        this.device = device;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getTime() {
        return this.time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public boolean getSvmTrainingExecuted() {
        return this.svmTrainingExecuted;
    }

    public void setSvmTrainingExecuted(boolean svmTrainingExecuted) {
        this.svmTrainingExecuted = svmTrainingExecuted;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 944779146)
    public Device getDevice() {
        Long __key = this.id;
        if (device__resolvedKey == null || !device__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DeviceDao targetDao = daoSession.getDeviceDao();
            Device deviceNew = targetDao.load(__key);
            synchronized (this) {
                device = deviceNew;
                device__resolvedKey = __key;
            }
        }
        return device;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1631318883)
    public Student getStudent() {
        Long __key = this.id;
        if (student__resolvedKey == null || !student__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StudentDao targetDao = daoSession.getStudentDao();
            Student studentNew = targetDao.load(__key);
            synchronized (this) {
                student = studentNew;
                student__resolvedKey = __key;
            }
        }
        return student;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1371817362)
    public void setStudent(Student student) {
        synchronized (this) {
            this.student = student;
            id = student == null ? null : student.getId();
            student__resolvedKey = id;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 76995743)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStudentImageCollectionEventDao() : null;
    }
}
