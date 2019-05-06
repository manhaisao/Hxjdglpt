package com.xzz.hxjdglpt.model;

public class Zhibu {
    private Integer id;

    private String zhibuname;

    private String zhifuphone;

    private String funame;

    private String fuphone;

    private Integer type;

    private Long pid;
    private String chengyuannum;
    private String xiaozunum;
    private String dangyuannum;

    private String gridid;

    private String vId;
    private String vname;

    private String dzbname;

    public String getDzbname() {
        return dzbname;
    }

    public void setDzbname(String dzbname) {
        this.dzbname = dzbname;
    }

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

    public String getXiaozunum() {
        return xiaozunum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setXiaozunum(String xiaozunum) {
        this.xiaozunum = xiaozunum;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZhibuname() {
        return zhibuname;
    }

    public void setZhibuname(String zhibuname) {
        this.zhibuname = zhibuname == null ? null : zhibuname.trim();
    }

    public String getZhifuphone() {
        return zhifuphone;
    }

    public void setZhifuphone(String zhifuphone) {
        this.zhifuphone = zhifuphone == null ? null : zhifuphone.trim();
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
}