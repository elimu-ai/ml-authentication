package org.literacyapp.model.content.multimedia;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinImagesWithNumbers {

    @Id
    private Long id;

    private long imageId;

    private long numberId;

    @Generated(hash = 1509677503)
    public JoinImagesWithNumbers(Long id, long imageId, long numberId) {
        this.id = id;
        this.imageId = imageId;
        this.numberId = numberId;
    }

    @Generated(hash = 569979416)
    public JoinImagesWithNumbers() {
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

    public long getNumberId() {
        return this.numberId;
    }

    public void setNumberId(long numberId) {
        this.numberId = numberId;
    }
}
