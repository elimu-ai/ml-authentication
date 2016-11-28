package org.literacyapp.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.DeviceDao;
import org.literacyapp.dao.StudentDao;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.dao.StudentImageDao;
import org.literacyapp.dao.converter.CalendarConverter;

import java.util.Calendar;
import java.util.List;

/**
 * Based on {@link org.literacyapp.model.gson.analytics.StudentImageCollectionEventGson}
 */
@Entity
public class StudentImageCollectionEvent {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @ToOne
    private Device device;

    @NotNull
    @Convert(converter = CalendarConverter.class, columnType = Long.class)
    private Calendar time;

//    @ToOne
//    private Application application;

    @ToOne
    private Student student;

//    @NotNull
    @ToMany(referencedJoinProperty = "id")
    private List<StudentImage> studentImages;

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

    @Generated(hash = 1114998901)
    private transient boolean device__refreshed;

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
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1492710689)
    public List<StudentImage> getStudentImages() {
        if (studentImages == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StudentImageDao targetDao = daoSession.getStudentImageDao();
            List<StudentImage> studentImagesNew = targetDao
                    ._queryStudentImageCollectionEvent_StudentImages(id);
            synchronized (this) {
                if (studentImages == null) {
                    studentImages = studentImagesNew;
                }
            }
        }
        return studentImages;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1313731009)
    public synchronized void resetStudentImages() {
        studentImages = null;
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
