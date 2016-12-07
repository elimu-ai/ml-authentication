package org.literacyapp.model.content.multimedia;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinVideosWithWords {

    @Id
    private Long id;

    private long videoId;

    private long wordId;

    @Generated(hash = 189346429)
    public JoinVideosWithWords(Long id, long videoId, long wordId) {
        this.id = id;
        this.videoId = videoId;
        this.wordId = wordId;
    }

    @Generated(hash = 1193380007)
    public JoinVideosWithWords() {
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

    public long getWordId() {
        return this.wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }
}
