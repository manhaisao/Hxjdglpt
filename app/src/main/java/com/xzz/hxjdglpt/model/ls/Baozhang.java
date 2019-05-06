package com.xzz.hxjdglpt.model.ls;

import java.io.Serializable;
import java.util.List;

public class Baozhang implements Serializable{
    private Integer id;

    private String fzr;

    private String fzrphone;

    private String jbr;

    private String jbrphone;

    private String content;

    private List<Baozhang> ps;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public List<Baozhang> getPs() {
        return ps;
    }

    public void setPs(List<Baozhang> ps) {
        this.ps = ps;
    }
}