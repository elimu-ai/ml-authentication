package org.literacyapp.contentprovider.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class JoinNumbersWithWords {

    @Id
    private Long id;

    private long numberId;

    private long wordId;

    @Generated(hash = 1257011017)
    public JoinNumbersWithWords(Long id, long numberId, long wordId) {
        this.id = id;
        this.numberId = numberId;
        this.wordId = wordId;
    }

    @Generated(hash = 1483633553)
    public JoinNumbersWithWords() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getNumberId() {
        return this.numberId;
    }

    public void setNumberId(long numberId) {
        this.numberId = numberId;
    }

    public long getWordId() {
        return this.wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }
}
