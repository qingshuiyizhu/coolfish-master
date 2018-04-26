package org.com.coolfish.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

@Table(name = "kuyu_account")
@Entity
public class KuyuAccount extends IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tetle;

    private Date addtime;

    private String text;

    public String getTetle() {
        return tetle;
    }

    public void setTetle(String tetle) {
        this.tetle = tetle == null ? null : tetle.trim();
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "text", columnDefinition = "text", nullable = true)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    public KuyuAccount(Long id, String text) {
        this.setId(id);
        this.text = text;
    }

    public KuyuAccount() {
        super();

    }

    @Override
    public String toString() {
        return "KuyuAccount [tetle=" + tetle + ", addtime=" + addtime + ", text=" + text + ", id=" + getId()
                + "]";
    }

}