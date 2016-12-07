package org.literacyapp.model.content.multimedia;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinVideosWithNumbers {

    @Id
    private Long id;

    private long videoId;

    private long numberId;

    @Generated(hash = 672934104)
    public JoinVideosWithNumbers(Long id, long videoId, long numberId) {
        this.id = id;
        this.videoId = videoId;
        this.numberId = numberId;
    }

    @Generated(hash = 356118008)
    public JoinVideosWithNumbers() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVideoId() {
        return this.videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }

    public long getNumberId() {
        return this.numberId;
    }

    public void setNumberId(long numberId) {
        this.numberId = numberId;
    }
}
