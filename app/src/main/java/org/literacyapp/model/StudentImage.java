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
    @Convert(converter = CalendarConverter.class, columnType = Long.class)
    private Calendar timeCollected;

    @NotNull
    private String imageFileUrl;

    @ToOne(joinProperty = "id")
    private StudentImageFeature studentImageFeature;

    @NotNull
    @ToOne(joinProperty = "id")
    private StudentImageCollectionEvent studentImageCollectionEvent;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1350049245)
    private transient StudentImageDao myDao;

    @Generated(hash = 1699503080)
    public StudentImage(Long id, @NotNull Calendar timeCollected, @NotNull String imageFileUrl) {
        this.id = id;
        this.timeCollected = timeCollected;
        this.imageFileUrl = imageFileUrl;
    }

    @Generated(hash = 1888893194)
    public StudentImage() {
    }

    @Generated(hash = 1724262783)
    private transient Long studentImageFeature__resolvedKey;

    @Generated(hash = 563660621)
    private transient Long studentImageCollectionEvent__resolvedKey;

    @Keep
    public void setStudentImageFeature(StudentImageFeature studentImageFeature) {
        this.studentImageFeature = studentImageFeature;
    }

    @Keep
    public void setStudentImageCollectionEvent(StudentImageCollectionEvent studentImageCollectionEvent) {
        this.studentImageCollectionEvent = studentImageCollectionEvent;
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
    @Generated(hash = 1014901179)
    public StudentImageFeature getStudentImageFeature() {
        Long __key = this.id;
        if (studentImageFeature__resolvedKey == null || !studentImageFeature__resolvedKey.equals(__key)) {
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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1759679793)
    public StudentImageCollectionEvent getStudentImageCollectionEvent() {
        Long __key = this.id;
        if (studentImageCollectionEvent__resolvedKey == null
                || !studentImageCollectionEvent__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StudentImageCollectionEventDao targetDao = daoSession.getStudentImageCollectionEventDao();
            StudentImageCollectionEvent studentImageCollectionEventNew = targetDao.load(__key);
            synchronized (this) {
                studentImageCollectionEvent = studentImageCollectionEventNew;
                studentImageCollectionEvent__resolvedKey = __key;
            }
        }
        return studentImageCollectionEvent;
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
