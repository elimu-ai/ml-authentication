package org.literacyapp.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.DeviceDao;
import org.literacyapp.dao.StudentImageDao;
import org.literacyapp.dao.StudentImageFeatureDao;
import org.literacyapp.dao.converter.CalendarConverter;

import java.util.Calendar;

/**
 * Based on {@link org.literacyapp.model.gson.StudentImageGson}
 */
@Entity
public class StudentImage {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @ToOne(joinProperty = "id")
    private Device device;

    @NotNull
    @Convert(converter = CalendarConverter.class, columnType = Long.class)
    private Calendar timeCollected;

    @NotNull
    private String imageFileUrl;

    @ToOne(joinProperty = "id")
    private StudentImageFeature studentImageFeature;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1350049245)
    private transient StudentImageDao myDao;

    @Generated(hash = 1699503080)
    public StudentImage(Long id, @NotNull Calendar timeCollected,
            @NotNull String imageFileUrl) {
        this.id = id;
        this.timeCollected = timeCollected;
        this.imageFileUrl = imageFileUrl;
    }

    @Generated(hash = 1888893194)
    public StudentImage() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getTimeCollected() {
        return this.timeCollected;
    }

    public void setTimeCollected(Calendar timeCollected) {
        this.timeCollected = timeCollected;
    }

    public String getImageFileUrl() {
        return this.imageFileUrl;
    }

    public void setImageFileUrl(String imageFileUrl) {
        this.imageFileUrl = imageFileUrl;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 733207827)
    public void setDevice(Device device) {
        synchronized (this) {
            this.device = device;
            id = device == null ? null : device.getId();
            device__resolvedKey = id;
        }
    }

    @Generated(hash = 708752895)
    private transient Long device__resolvedKey;

    @Generated(hash = 1724262783)
    private transient Long studentImageFeature__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1014901179)
    public StudentImageFeature getStudentImageFeature() {
        Long __key = this.id;
        if (studentImageFeature__resolvedKey == null
                || !studentImageFeature__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StudentImageFeatureDao targetDao = daoSession.getStudentImageFeatureDao();
            StudentImageFeature studentImageFeatureNew = targetDao.load(__key);
            synchronized (this) {
                studentImageFeature = studentImageFeatureNew;
                studentImageFeature__resolvedKey = __key;
            }
        }
        return studentImageFeature;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1559283393)
    public void setStudentImageFeature(StudentImageFeature studentImageFeature) {
        synchronized (this) {
            this.studentImageFeature = studentImageFeature;
            id = studentImageFeature == null ? null : studentImageFeature.getId();
            studentImageFeature__resolvedKey = id;
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
    @Generated(hash = 971423104)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStudentImageDao() : null;
    }
}
