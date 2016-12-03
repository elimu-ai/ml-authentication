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
import org.literacyapp.dao.StudentImageCollectionEventDao;
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
    @ToOne
    private Device device;

    @NotNull
    @Convert(converter = CalendarConverter.class, columnType = Long.class)
    private Calendar timeCollected;

    @NotNull
    private String imageFileUrl;

    @ToOne
    private StudentImageFeature studentImageFeature;

    @NotNull
    @ToOne
    private StudentImageCollectionEvent studentImageCollectionEvent;

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

    @Generated(hash = 1114998901)
    private transient boolean device__refreshed;

    @Generated(hash = 350087450)
    private transient boolean studentImageFeature__refreshed;

    @Generated(hash = 2145224794)
    private transient boolean studentImageCollectionEvent__refreshed;

    @Keep
    public void setStudentImageFeature(StudentImageFeature studentImageFeature) {
        this.studentImageFeature = studentImageFeature;
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
    @Generated(hash = 952566151)
    public Device getDevice() {
        if (device != null || !device__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DeviceDao targetDao = daoSession.getDeviceDao();
            targetDao.refresh(device);
            device__refreshed = true;
        }
        return device;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 400002126)
    public Device peakDevice() {
        return device;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1764284664)
    public void setDevice(@NotNull Device device) {
        if (device == null) {
            throw new DaoException(
                    "To-one property 'device' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.device = device;
            device__refreshed = true;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1683738455)
    public StudentImageFeature getStudentImageFeature() {
        if (studentImageFeature != null || !studentImageFeature__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StudentImageFeatureDao targetDao = daoSession
                    .getStudentImageFeatureDao();
            targetDao.refresh(studentImageFeature);
            studentImageFeature__refreshed = true;
        }
        return studentImageFeature;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 537228336)
    public StudentImageFeature peakStudentImageFeature() {
        return studentImageFeature;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1433496216)
    public StudentImageCollectionEvent getStudentImageCollectionEvent() {
        if (studentImageCollectionEvent != null
                || !studentImageCollectionEvent__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StudentImageCollectionEventDao targetDao = daoSession
                    .getStudentImageCollectionEventDao();
            targetDao.refresh(studentImageCollectionEvent);
            studentImageCollectionEvent__refreshed = true;
        }
        return studentImageCollectionEvent;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 476286207)
    public StudentImageCollectionEvent peakStudentImageCollectionEvent() {
        return studentImageCollectionEvent;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 503194988)
    public void setStudentImageCollectionEvent(
            @NotNull StudentImageCollectionEvent studentImageCollectionEvent) {
        if (studentImageCollectionEvent == null) {
            throw new DaoException(
                    "To-one property 'studentImageCollectionEvent' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.studentImageCollectionEvent = studentImageCollectionEvent;
            studentImageCollectionEvent__refreshed = true;
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
