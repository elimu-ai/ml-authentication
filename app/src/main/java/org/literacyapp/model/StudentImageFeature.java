package org.literacyapp.model;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.literacyapp.dao.converter.CalendarConverter;

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
    private String svmVector;

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
}
