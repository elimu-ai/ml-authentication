package org.literacyapp.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Based on {@link org.literacyapp.model.gson.StudentGson}
 */
@Entity
public class StudentImageFeature {

    @Id(autoincrement = true)
    private Long id;

    @Generated(hash = 327198608)
    public StudentImageFeature(Long id) {
        this.id = id;
    }

    @Generated(hash = 1556870573)
    public StudentImageFeature() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
