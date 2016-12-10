package org.literacyapp.model.content.multimedia;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinImagesWithLetters {

    @Id
    private Long id;

    private long imageId;

    private long letterId;

    @Generated(hash = 1860449205)
    public JoinImagesWithLetters(Long id, long imageId, long letterId) {
        this.id = id;
        this.imageId = imageId;
        this.letterId = letterId;
    }

    @Generated(hash = 1535476177)
    public JoinImagesWithLetters() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getImageId() {
        return this.imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public long getLetterId() {
        return this.letterId;
    }

    public void setLetterId(long letterId) {
        this.letterId = letterId;
    }
}
