package org.literacyapp.contentprovider.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinLettersWithAllophones {

    @Id
    private Long id;

    private long letterId;

    private long allophoneId;

    @Generated(hash = 249120739)
    public JoinLettersWithAllophones(Long id, long letterId, long allophoneId) {
        this.id = id;
        this.letterId = letterId;
        this.allophoneId = allophoneId;
    }

    @Generated(hash = 246556632)
    public JoinLettersWithAllophones() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getLetterId() {
        return this.letterId;
    }

    public void setLetterId(long letterId) {
        this.letterId = letterId;
    }

    public long getAllophoneId() {
        return this.allophoneId;
    }

    public void setAllophoneId(long allophoneId) {
        this.allophoneId = allophoneId;
    }
}
