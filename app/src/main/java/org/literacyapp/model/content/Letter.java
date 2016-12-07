package org.literacyapp.model.content;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.literacyapp.dao.converter.CalendarConverter;
import org.literacyapp.dao.converter.ContentStatusConverter;
import org.literacyapp.dao.converter.LocaleConverter;
import org.literacyapp.model.enums.Locale;
import org.literacyapp.model.enums.content.ContentStatus;

import java.util.Calendar;

/**
 * Based on {@link org.literacyapp.model.gson.content.LetterGson}
 */
@Entity
public class Letter {

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
    private String text;

    private int usageCount;

    @Generated(hash = 1689266044)
    public Letter(Long id, @NotNull Locale locale, Calendar timeLastUpdate,
            @NotNull Integer revisionNumber, @NotNull ContentStatus contentStatus,
            @NotNull String text, int usageCount) {
        this.id = id;
        this.locale = locale;
        this.timeLastUpdate = timeLastUpdate;
        this.revisionNumber = revisionNumber;
        this.contentStatus = contentStatus;
        this.text = text;
        this.usageCount = usageCount;
    }

    @Generated(hash = 174375807)
    public Letter() {
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

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUsageCount() {
        return this.usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }
}
