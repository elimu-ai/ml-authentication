package org.literacyapp.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.StudentDao;
import org.literacyapp.dao.StudentImageFeatureDao;
import org.literacyapp.dao.converter.CalendarConverter;

import java.util.Calendar;

/**
 * Based on {@link org.literacyapp.model.gson.StudentImageFeatureGson}
 */
@Entity
public class StudentImageFeature {

    @Id(autoincrement = true)
    private Long id;

    @ToOne
    private Student student;

    @NotNull
    @Convert(converter = CalendarConverter.class, columnType = Long.class)
    private Calendar timeCreated;

    @NotNull
    private String svmVector;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 467012351)
    private transient StudentImageFeatureDao myDao;

    @Generated(hash = 1220454956)
    public StudentImageFeature(Long id, @NotNull Calendar timeCreated,
            @NotNull String svmVector) {
        this.id = id;
        this.timeCreated = timeCreated;
        this.svmVector = svmVector;
    }

    @Generated(hash = 472695614)
    public StudentImageFeature() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getTimeCreated() {
        return this.timeCreated;
    }

    public void setTimeCreated(Calendar timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getSvmVector() {
        return this.svmVector;
    }

    public void setSvmVector(String svmVector) {
        this.svmVector = svmVector;
    }

    @Generated(hash = 1402753087)
    private transient boolean student__refreshed;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 428694139)
    public Student getStudent() {
        if (student != null || !student__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StudentDao targetDao = daoSession.getStudentDao();
            targetDao.refresh(student);
            student__refreshed = true;
        }
        return student;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 1055461273)
    public Student peakStudent() {
        return student;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1990573816)
    public void setStudent(Student student) {
        synchronized (this) {
            this.student = student;
            student__refreshed = true;
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
    @Generated(hash = 667489400)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStudentImageFeatureDao() : null;
    }
}
