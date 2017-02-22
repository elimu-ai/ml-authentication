package org.literacyapp.contentprovider.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class JoinStudentsWithDevices {

    @Id
    private Long id;

    private long studentId;

    private long deviceId;

    @Generated(hash = 311762320)
    public JoinStudentsWithDevices(Long id, long studentId, long deviceId) {
        this.id = id;
        this.studentId = studentId;
        this.deviceId = deviceId;
    }

    @Generated(hash = 1777779190)
    public JoinStudentsWithDevices() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }
}
