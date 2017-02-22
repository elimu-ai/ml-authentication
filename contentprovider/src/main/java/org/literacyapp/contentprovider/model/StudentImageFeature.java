package org.literacyapp.contentprovider.model;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.literacyapp.contentprovider.dao.converter.CalendarConverter;

import java.util.Calendar;

/**
 * Based on {@link org.literacyapp.model.gson.StudentImageFeatureGson}
 */
@Entity
public class StudentImageFeature {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Convert(converter = CalendarConverter.class, columnType = Long.class)
    private Calendar timeCreated;

    @NotNull
    private String featureVector;

    @Generated(hash = 828845722)
    public StudentImageFeature(Long id, @NotNull Calendar timeCreated,
            @NotNull String featureVector) {
        this.id = id;
        this.timeCreated = timeCreated;
        this.featureVector = featureVector;
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

    public String getFeatureVector() {
        return this.featureVector;
    }

    public void setFeatureVector(String featureVector) {
        this.featureVector = featureVector;
    }
}
