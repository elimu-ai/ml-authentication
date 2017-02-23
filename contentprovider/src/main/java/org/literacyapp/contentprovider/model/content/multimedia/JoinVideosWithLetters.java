package org.literacyapp.contentprovider.model.content.multimedia;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class JoinVideosWithLetters {

    @Id
    private Long id;

    private long videoId;

    private long letterId;

    @Generated(hash = 755328155)
    public JoinVideosWithLetters(Long id, long videoId, long letterId) {
        this.id = id;
        this.videoId = videoId;
        this.letterId = letterId;
    }

    @Generated(hash = 105517389)
    public JoinVideosWithLetters() {
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

    public long getLetterId() {
        return this.letterId;
    }

    public void setLetterId(long letterId) {
        this.letterId = letterId;
    }
}
