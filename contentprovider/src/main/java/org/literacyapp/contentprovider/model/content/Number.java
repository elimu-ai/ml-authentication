package org.literacyapp.contentprovider.model.content;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.literacyapp.contentprovider.dao.DaoSession;
import org.literacyapp.contentprovider.dao.NumberDao;
import org.literacyapp.contentprovider.dao.WordDao;
import org.literacyapp.contentprovider.dao.converter.CalendarConverter;
import org.literacyapp.contentprovider.dao.converter.ContentStatusConverter;
import org.literacyapp.contentprovider.dao.converter.LocaleConverter;
import org.literacyapp.contentprovider.model.JoinNumbersWithWords;
import org.literacyapp.model.enums.Locale;
import org.literacyapp.model.enums.content.ContentStatus;

import java.util.Calendar;
import java.util.List;

/**
 * Based on {@link org.literacyapp.model.gson.content.NumberGson}
 */
@Entity
public class Number {

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
    private Integer value;

    private String symbol;

    @ToMany
    @JoinEntity(entity = JoinNumbersWithWords.class, sourceProperty = "numberId", targetProperty = "wordId")
    private List<Word> words;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1854423557)
    private transient NumberDao myDao;

    @Generated(hash = 345753333)
    public Number(Long id, @NotNull Locale locale, Calendar timeLastUpdate,
            @NotNull Integer revisionNumber, @NotNull ContentStatus contentStatus,
            @NotNull Integer value, String symbol) {
        this.id = id;
        this.locale = locale;
        this.timeLastUpdate = timeLastUpdate;
        this.revisionNumber = revisionNumber;
        this.contentStatus = contentStatus;
        this.value = value;
        this.symbol = symbol;
    }

    @Generated(hash = 1191593292)
    public Number() {
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

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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
    @Generated(hash = 489333703)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNumberDao() : null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 295048042)
    public List<Word> getWords() {
        if (words == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WordDao targetDao = daoSession.getWordDao();
            List<Word> wordsNew = targetDao._queryNumber_Words(id);
            synchronized (this) {
                if (words == null) {
                    words = wordsNew;
                }
            }
        }
        return words;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1954400333)
    public synchronized void resetWords() {
        words = null;
    }
}
