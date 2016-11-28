package org.literacyapp.model.content.multimedia;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.literacyapp.dao.converter.CalendarConverter;
import org.literacyapp.dao.converter.ContentStatusConverter;
import org.literacyapp.dao.converter.LocaleConverter;
import org.literacyapp.dao.converter.StringSetConverter;
import org.literacyapp.dao.converter.VideoFormatConverter;
import org.literacyapp.model.enums.Locale;
import org.literacyapp.model.enums.content.ContentStatus;
import org.literacyapp.model.enums.content.LiteracySkill;
import org.literacyapp.model.enums.content.NumeracySkill;
import org.literacyapp.model.enums.content.VideoFormat;

import java.util.Calendar;
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

    @NotNull
    private String title;

    @NotNull
    @Convert(converter = VideoFormatConverter.class, columnType = String.class)
    private VideoFormat videoFormat;

    @Generated(hash = 1068961400)
    public Video(Long id, @NotNull Locale locale, Calendar timeLastUpdate,
            @NotNull Integer revisionNumber, @NotNull ContentStatus contentStatus,
            @NotNull String contentType, Set<LiteracySkill> literacySkills,
            Set<NumeracySkill> numeracySkills, @NotNull String title,
            @NotNull VideoFormat videoFormat) {
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
}
