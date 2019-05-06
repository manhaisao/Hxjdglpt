package com.xzz.hxjdglpt.model;

public class Zongzhi {
    private Integer id;

    private String zongname;

    private String zongphone;

    private String funame;

    private String fuphone;

    private Long pid;

    private String chengyuannum;
    private String dangyuannum;
    private String dangzhibunum;

    private String gridid;

    private String vId;

    private String vname;

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }

    public String getGridid() {
        return gridid;
    }

    public void setGridid(String gridid) {
        this.gridid = gridid;
    }

    public String getDangyuannum() {
        return dangyuannum;
    }

    public void setDangyuannum(String dangyuannum) {
        this.dangyuannum = dangyuannum;
    }

    public String getChengyuannum() {
        return chengyuannum;
    }

    public void setChengyuannum(String chengyuannum) {
        this.chengyuannum = chengyuannum;
    }

    public String getDangzhibunum() {
        return dangzhibunum;
    }

    public void setDangzhibunum(String dangzhibunum) {
        this.dangzhibunum = dangzhibunum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZongname() {
        return zongname;
    }

    public void setZongname(String zongname) {
        this.zongname = zongname == null ? null : zongname.trim();
    }

    public String getZongphone() {
        return zongphone;
    }

    public void setZongphone(String zongphone) {
        this.zongphone = zongphone == null ? null : zongphone.trim();
    }

    public String getFuname() {
        return funame;
    }

    public void setFuname(String funame) {
        this.funame = funame == null ? null : funame.trim();
    }

    public String getFuphone() {
        return fuphone;
    }

    public void setFuphone(String fuphone) {
        this.fuphone = fuphone == null ? null : fuphone.trim();
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}