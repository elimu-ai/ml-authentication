package org.literacyapp.contentprovider.model.content;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.literacyapp.contentprovider.dao.converter.CalendarConverter;
import org.literacyapp.contentprovider.dao.converter.ContentStatusConverter;
import org.literacyapp.contentprovider.dao.converter.GradeLevelConverter;
import org.literacyapp.contentprovider.dao.converter.LocaleConverter;
import org.literacyapp.contentprovider.model.content.multimedia.Image;
import org.literacyapp.model.enums.GradeLevel;
import org.literacyapp.model.enums.Locale;
import org.literacyapp.model.enums.content.ContentStatus;

import java.util.Calendar;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.literacyapp.contentprovider.dao.DaoSession;
import org.literacyapp.contentprovider.dao.ImageDao;
import org.literacyapp.contentprovider.dao.StoryBookDao;

/**
 * Based on {@link org.literacyapp.model.gson.content.StoryBookGson}
 */
@Entity
public class StoryBook {

    @Id
    private Long id;

    @NotNull
    @Convert(converter = LocaleConverter.class, columnType = String.class)
    private Locale locale;

    @Convert(converter = CalendarConverter.class, columnType = Long.class)
    private Calendar timeLastUpdate;

    @NotNull
    private Integer revisionNumber; // [1, 2, 3, ...]

    @NotNull
    @Convert(converter = ContentStatusConverter.class, columnType = String.class)
    private ContentStatus contentStatus;

    @NotNull
    private String title;

    private long coverImageId;

    @ToOne(joinProperty = "coverImageId")
    private Image coverImage;

    @NotNull
    @Convert(converter = GradeLevelConverter.class, columnType = String.class)
    private GradeLevel gradeLevel;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 256360698)
    private transient StoryBookDao myDao;

    @Generated(hash = 1731918440)
    private transient Long coverImage__resolvedKey;

    // TODO: pages

    @Generated(hash = 185999927)
    public StoryBook(Long id, @NotNull Locale locale, Calendar timeLastUpdate,
            @NotNull Integer revisionNumber, @NotNull ContentStatus contentStatus,
            @NotNull String title, long coverImageId,
            @NotNull GradeLevel gradeLevel) {
        this.id = id;
        this.locale = locale;
        this.timeLastUpdate = timeLastUpdate;
        this.revisionNumber = revisionNumber;
        this.contentStatus = contentStatus;
        this.title = title;
        this.coverImageId = coverImageId;
        this.gradeLevel = gradeLevel;
    }

    @Generated(hash = 232147559)
    public StoryBook() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Calendar getTimeLastUpdate() {
        return this.timeLastUpdate;
    }

    public void setTimeLastUpdate(Calendar timeLastUpdate) {
        this.timeLastUpdate = timeLastUpdate;
    }

    public Integer getRevisionNumber() {
        return this.revisionNumber;
    }

    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public ContentStatus getContentStatus() {
        return this.contentStatus;
    }

    public void setContentStatus(ContentStatus contentStatus) {
        this.contentStatus = contentStatus;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GradeLevel getGradeLevel() {
        return this.gradeLevel;
    }

    public void setGradeLevel(GradeLevel gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public long getCoverImageId() {
        return this.coverImageId;
    }

    public void setCoverImageId(long coverImageId) {
        this.coverImageId = coverImageId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1989913581)
    public Image getCoverImage() {
        long __key = this.coverImageId;
        if (coverImage__resolvedKey == null
                || !coverImage__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ImageDao targetDao = daoSession.getImageDao();
            Image coverImageNew = targetDao.load(__key);
            synchronized (this) {
                coverImage = coverImageNew;
                coverImage__resolvedKey = __key;
            }
        }
        return coverImage;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 508023874)
    public void setCoverImage(@NotNull Image coverImage) {
        if (coverImage == null) {
            throw new DaoException(
                    "To-one property 'coverImageId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.coverImage = coverImage;
            coverImageId = coverImage.getId();
            coverImage__resolvedKey = coverImageId;
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
    @Generated(hash = 1536503294)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStoryBookDao() : null;
    }

    // TODO: pages
}
