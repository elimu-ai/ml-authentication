package org.literacyapp.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.StudentDao;
import org.literacyapp.dao.StudentImageDao;

/**
 * Based on {@link org.literacyapp.model.gson.StudentGson}
 */
@Entity
public class Student {

    @Id(autoincrement = true)
    private Long id;

    @ToOne
    private StudentImage avatar;

//    private List<Device> devices;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1943931642)
    private transient StudentDao myDao;

    @Generated(hash = 926952963)
    private transient boolean avatar__refreshed;

    @Generated(hash = 327198608)
    public Student(Long id) {
        this.id = id;
    }

    @Generated(hash = 1556870573)
    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1061269171)
    public StudentImage getAvatar() {
        if (avatar != null || !avatar__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StudentImageDao targetDao = daoSession.getStudentImageDao();
            targetDao.refresh(avatar);
            avatar__refreshed = true;
        }
        return avatar;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 1380169256)
    public StudentImage peakAvatar() {
        return avatar;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1646104004)
    public void setAvatar(StudentImage avatar) {
        synchronized (this) {
            this.avatar = avatar;
            avatar__refreshed = true;
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
    @Generated(hash = 1701634981)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStudentDao() : null;
    }
}
