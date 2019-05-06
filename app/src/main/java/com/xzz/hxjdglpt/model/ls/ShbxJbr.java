package com.xzz.hxjdglpt.model.ls;

import java.io.Serializable;
import java.util.List;

public class ShbxJbr implements Serializable{
    private Integer id;

    private String content;

    private String fzr;

    private String fzrphone;

    private String jbr;

    private String jbrphone;

    private List<ShbxJbr> ps;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
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

    public String getJbr() {
        return jbr;
    }

    public void setJbr(String jbr) {
        this.jbr = jbr == null ? null : jbr.trim();
    }

    public String getJbrphone() {
        return jbrphone;
    }

    public void setJbrphone(String jbrphone) {
        this.jbrphone = jbrphone == null ? null : jbrphone.trim();
    }

    public List<ShbxJbr> getPs() {
        return ps;
    }

    public void setPs(List<ShbxJbr> ps) {
        this.ps = ps;
    }
}