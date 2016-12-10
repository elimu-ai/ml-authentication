package org.literacyapp.model.content.multimedia;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinAudiosWithNumbers {

    @Id
    private Long id;

    private long audioId;

    private long numberId;

    @Generated(hash = 1890839099)
    public JoinAudiosWithNumbers(Long id, long audioId, long numberId) {
        this.id = id;
        this.audioId = audioId;
        this.numberId = numberId;
    }

    @Generated(hash = 1739734461)
    public JoinAudiosWithNumbers() {
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

    public long getNumberId() {
        return this.numberId;
    }

    public void setNumberId(long numberId) {
        this.numberId = numberId;
    }
}
