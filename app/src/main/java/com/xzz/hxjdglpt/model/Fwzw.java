package com.xzz.hxjdglpt.model;

public class Fwzw {
    private Integer id;

    private String gridid;

    private String zrr;

    private String zrrphone;

    private String wjh;

    private String jdphone;

    private String mark;

    private String picpath;

    private String fwddzrr;
    private String fwddzrrphone;

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getGridid() {
        return gridid;
    }

    public void setGridid(String gridid) {
        this.gridid = gridid;
    }

    public String getZrr() {
        return zrr;
    }

    public void setZrr(String zrr) {
        this.zrr = zrr == null ? null : zrr.trim();
    }

    public String getZrrphone() {
        return zrrphone;
    }

    public void setZrrphone(String zrrphone) {
        this.zrrphone = zrrphone == null ? null : zrrphone.trim();
    }

    public String getWjh() {
        return wjh;
    }

    public void setWjh(String wjh) {
        this.wjh = wjh == null ? null : wjh.trim();
    }

    public String getJdphone() {
        return jdphone;
    }

    public void setJdphone(String jdphone) {
        this.jdphone = jdphone == null ? null : jdphone.trim();
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark == null ? null : mark.trim();
    }

    public String getFwddzrr() {
        return fwddzrr;
    }

    public void setFwddzrr(String fwddzrr) {
        this.fwddzrr = fwddzrr;
    }

    public String getFwddzrrphone() {
        return fwddzrrphone;
    }

    public void setFwddzrrphone(String fwddzrrphone) {
        this.fwddzrrphone = fwddzrrphone;
    }
}