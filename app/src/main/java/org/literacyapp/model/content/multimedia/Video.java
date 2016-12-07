package org.literacyapp.model.content.multimedia;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.LetterDao;
import org.literacyapp.dao.NumberDao;
import org.literacyapp.dao.VideoDao;
import org.literacyapp.dao.WordDao;
import org.literacyapp.dao.converter.CalendarConverter;
import org.literacyapp.dao.converter.ContentStatusConverter;
import org.literacyapp.dao.converter.LocaleConverter;
import org.literacyapp.dao.converter.StringSetConverter;
import org.literacyapp.dao.converter.VideoFormatConverter;
import org.literacyapp.model.content.Letter;
import org.literacyapp.model.content.Number;
import org.literacyapp.model.content.Word;
import org.literacyapp.model.enums.Locale;
import org.literacyapp.model.enums.content.ContentStatus;
import org.literacyapp.model.enums.content.LiteracySkill;
import org.literacyapp.model.enums.content.NumeracySkill;
import org.literacyapp.model.enums.content.VideoFormat;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * Based on {@link org.literacyapp.model.gson.content.multimedia.VideoGson}
 */
@Entity
public class Video {

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
    private String contentType; // MIME type

    @Convert(converter = StringSetConverter.class, columnType = String.class)
    private Set<LiteracySkill> literacySkills;

    @Convert(converter = StringSetConverter.class, columnType = String.class)
    private Set<NumeracySkill> numeracySkills;

    @ToMany
    @JoinEntity(entity = JoinVideosWithLetters.class, sourceProperty = "videoId", targetProperty = "letterId")
    private List<Letter> letters;

    @ToMany
    @JoinEntity(entity = JoinVideosWithNumbers.class, sourceProperty = "videoId", targetProperty = "numberId")
    private List<Number> numbers;

    @ToMany
    @JoinEntity(entity = JoinVideosWithWords.class, sourceProperty = "videoId", targetProperty = "wordId")
    private List<Word> words;

    @NotNull
    private String title;

    @NotNull
    @Convert(converter = VideoFormatConverter.class, columnType = String.class)
    private VideoFormat videoFormat;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2004496110)
    private transient VideoDao myDao;

    @Generated(hash = 1068961400)
    public Video(Long id, @NotNull Locale locale, Calendar timeLastUpdate, @NotNull Integer revisionNumber,
            @NotNull ContentStatus contentStatus, @NotNull String contentType, Set<LiteracySkill> literacySkills,
            Set<NumeracySkill> numeracySkills, @NotNull String title, @NotNull VideoFormat videoFormat) {
        this.id = id;
        this.locale = locale;
        this.timeLastUpdate = timeLastUpdate;
        this.revisionNumber = revisionNumber;
        this.contentStatus = contentStatus;
        this.contentType = contentType;
        this.literacySkills = literacySkills;
        this.numeracySkills = numeracySkills;
        this.title = title;
        this.videoFormat = videoFormat;
    }

    @Generated(hash = 237528154)
    public Video() {
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

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Set<LiteracySkill> getLiteracySkills() {
        return this.literacySkills;
    }

    public void setLiteracySkills(Set<LiteracySkill> literacySkills) {
        this.literacySkills = literacySkills;
    }

    public Set<NumeracySkill> getNumeracySkills() {
        return this.numeracySkills;
    }

    public void setNumeracySkills(Set<NumeracySkill> numeracySkills) {
        this.numeracySkills = numeracySkills;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public VideoFormat getVideoFormat() {
        return this.videoFormat;
    }

    public void setVideoFormat(VideoFormat videoFormat) {
        this.videoFormat = videoFormat;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 435833813)
    public List<Letter> getLetters() {
        if (letters == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LetterDao targetDao = daoSession.getLetterDao();
            List<Letter> lettersNew = targetDao._queryVideo_Letters(id);
            synchronized (this) {
                if (letters == null) {
                    letters = lettersNew;
                }
            }
        }
        return letters;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 520859111)
    public synchronized void resetLetters() {
        letters = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 991779275)
    public List<Number> getNumbers() {
        if (numbers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NumberDao targetDao = daoSession.getNumberDao();
            List<Number> numbersNew = targetDao._queryVideo_Numbers(id);
            synchronized (this) {
                if (numbers == null) {
                    numbers = numbersNew;
                }
            }
        }
        return numbers;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1968814974)
    public synchronized void resetNumbers() {
        numbers = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1001665935)
    public List<Word> getWords() {
        if (words == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WordDao targetDao = daoSession.getWordDao();
            List<Word> wordsNew = targetDao._queryVideo_Words(id);
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
    @Generated(hash = 658121286)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getVideoDao() : null;
    }
}