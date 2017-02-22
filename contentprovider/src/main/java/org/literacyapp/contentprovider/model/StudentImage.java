package org.literacyapp.contentprovider.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.literacyapp.contentprovider.dao.DaoSession;
import org.literacyapp.contentprovider.dao.StudentImageCollectionEventDao;
import org.literacyapp.contentprovider.dao.StudentImageDao;
import org.literacyapp.contentprovider.dao.StudentImageFeatureDao;
import org.literacyapp.contentprovider.dao.converter.CalendarConverter;
import org.literacyapp.contentprovider.model.analytics.StudentImageCollectionEvent;

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

    private long studentImageFeatureId;

    @ToOne(joinProperty = "studentImageFeatureId")
    private StudentImageFeature studentImageFeature;

    private long studentImageCollectionEventId;

    @NotNull
    @ToOne(joinProperty = "studentImageCollectionEventId")
    private StudentImageCollectionEvent studentImageCollectionEvent;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1350049245)
    private transient StudentImageDao myDao;

    @Generated(hash = 320295069)
    public StudentImage(Long id, @NotNull Calendar timeCollected,
            @NotNull String imageFileUrl, long studentImageFeatureId,
            long studentImageCollectionEventId) {
        this.id = id;
        this.timeCollected = timeCollected;
        this.imageFileUrl = imageFileUrl;
        this.studentImageFeatureId = studentImageFeatureId;
        this.studentImageCollectionEventId = studentImageCollectionEventId;
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

    public long getStudentImageFeatureId() {
        return this.studentImageFeatureId;
    }

    public void setStudentImageFeatureId(long studentImageFeatureId) {
        this.studentImageFeatureId = studentImageFeatureId;
    }

    public long getStudentImageCollectionEventId() {
        return this.studentImageCollectionEventId;
    }

    public void setStudentImageCollectionEventId(
            long studentImageCollectionEventId) {
        this.studentImageCollectionEventId = studentImageCollectionEventId;
    }

    @Generated(hash = 1724262783)
    private transient Long studentImageFeature__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1620286522)
    public StudentImageFeature getStudentImageFeature() {
        long __key = this.studentImageFeatureId;
        if (studentImageFeature__resolvedKey == null
                || !studentImageFeature__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StudentImageFeatureDao targetDao = daoSession
                    .getStudentImageFeatureDao();
            StudentImageFeature studentImageFeatureNew = targetDao.load(__key);
            synchronized (this) {
                studentImageFeature = studentImageFeatureNew;
                studentImageFeature__resolvedKey = __key;
            }
        }
        return studentImageFeature;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 549461688)
    public void setStudentImageFeature(
            @NotNull StudentImageFeature studentImageFeature) {
        if (studentImageFeature == null) {
            throw new DaoException(
                    "To-one property 'studentImageFeatureId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.studentImageFeature = studentImageFeature;
            studentImageFeatureId = studentImageFeature.getId();
            studentImageFeature__resolvedKey = studentImageFeatureId;
        }
    }

    @Generated(hash = 563660621)
    private transient Long studentImageCollectionEvent__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1744330159)
    public StudentImageCollectionEvent getStudentImageCollectionEvent() {
        long __key = this.studentImageCollectionEventId;
        if (studentImageCollectionEvent__resolvedKey == null
                || !studentImageCollectionEvent__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StudentImageCollectionEventDao targetDao = daoSession
                    .getStudentImageCollectionEventDao();
            StudentImageCollectionEvent studentImageCollectionEventNew = targetDao
                    .load(__key);
            synchronized (this) {
                studentImageCollectionEvent = studentImageCollectionEventNew;
                studentImageCollectionEvent__resolvedKey = __key;
            }
        }
        return studentImageCollectionEvent;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2140332106)
    public void setStudentImageCollectionEvent(
            @NotNull StudentImageCollectionEvent studentImageCollectionEvent) {
        if (studentImageCollectionEvent == null) {
            throw new DaoException(
                    "To-one property 'studentImageCollectionEventId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.studentImageCollectionEvent = studentImageCollectionEvent;
            studentImageCollectionEventId = studentImageCollectionEvent.getId();
            studentImageCollectionEvent__resolvedKey = studentImageCollectionEventId;
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
