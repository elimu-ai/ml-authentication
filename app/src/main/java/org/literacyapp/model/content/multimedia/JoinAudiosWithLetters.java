package org.literacyapp.model.content.multimedia;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinAudiosWithLetters {

    @Id
    private Long id;

    private long audioId;

    private long letterId;

    @Generated(hash = 1261842274)
    public JoinAudiosWithLetters(Long id, long audioId, long letterId) {
        this.id = id;
        this.audioId = audioId;
        this.letterId = letterId;
    }

    @Generated(hash = 700909632)
    public JoinAudiosWithLetters() {
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

    public long getLetterId() {
        return this.letterId;
    }

    public void setLetterId(long letterId) {
        this.letterId = letterId;
    }
}
