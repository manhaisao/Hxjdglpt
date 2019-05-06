package com.xzz.hxjdglpt.model.ls;

import java.io.Serializable;

public class Work implements Serializable{
    private Integer id;

    private String name;

    private String fuwu;

    private String address;

    private String fzr;

    private String fzrphone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFuwu() {
        return fuwu;
    }

    public void setFuwu(String fuwu) {
        this.fuwu = fuwu == null ? null : fuwu.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getFzr() {
        return fzr;
    }

    public void setFzr(String fzr) {
        this.fzr = fzr == null ? null : fzr.trim();
    }

    public String getFzrphone() {
        return fzrphone;
    }

    public void setFzrphone(String fzrphone) {
        this.fzrphone = fzrphone == null ? null : fzrphone.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}