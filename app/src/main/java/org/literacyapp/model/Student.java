package org.literacyapp.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Based on {@link org.literacyapp.model.gson.DeviceGson}
 */
@Entity
public class Student {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Unique
    private String deviceId;

    @Generated(hash = 840504470)
    public Student(Long id, @NotNull String deviceId) {
        this.id = id;
        this.deviceId = deviceId;
    }

    @Generated(hash = 1469582394)
    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
