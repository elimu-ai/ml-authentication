package org.literacyapp.model;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.literacyapp.dao.converter.CalendarConverter;

import java.util.Calendar;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Based on {@link org.literacyapp.model.gson.StudentImageFeatureGson}
 */
@Entity
public class StudentImage {

    @Id(autoincrement = true)
    private Long id;

    // private Student student;

    @NotNull
    @Convert(converter = CalendarConverter.class, columnType = Long.class)
    private Calendar timeCreated;

    @NotNull
    private String svmVector;

    @Generated(hash = 1220454956)
    public StudentImage(Long id, @NotNull Calendar timeCreated,
                        @NotNull String svmVector) {
        this.id = id;
        this.timeCreated = timeCreated;
        this.svmVector = svmVector;
    }

    @Generated(hash = 472695614)
    public StudentImage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Calendar timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getSvmVector() {
        return svmVector;
    }

    public void setSvmVector(String svmVector) {
        this.svmVector = svmVector;
    }
}
