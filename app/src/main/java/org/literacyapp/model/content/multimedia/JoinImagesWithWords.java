package org.literacyapp.model.content.multimedia;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinImagesWithWords {

    @Id
    private Long id;

    private long imageId;

    private long wordId;

    @Generated(hash = 22564991)
    public JoinImagesWithWords(Long id, long imageId, long wordId) {
        this.id = id;
        this.imageId = imageId;
        this.wordId = wordId;
    }

    @Generated(hash = 935098914)
    public JoinImagesWithWords() {
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

    public long getWordId() {
        return this.wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }
}
