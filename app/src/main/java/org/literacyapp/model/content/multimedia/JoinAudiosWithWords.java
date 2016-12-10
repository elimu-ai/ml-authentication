package org.literacyapp.model.content.multimedia;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinAudiosWithWords {

    @Id
    private Long id;

    private long audioId;

    private long wordId;

    @Generated(hash = 131625956)
    public JoinAudiosWithWords(Long id, long audioId, long wordId) {
        this.id = id;
        this.audioId = audioId;
        this.wordId = wordId;
    }

    @Generated(hash = 1453641339)
    public JoinAudiosWithWords() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAudioId() {
        return this.audioId;
    }

    public void setAudioId(long audioId) {
        this.audioId = audioId;
    }

    public long getWordId() {
        return this.wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }
}
