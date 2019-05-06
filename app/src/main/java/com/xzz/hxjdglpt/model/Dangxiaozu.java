package com.xzz.hxjdglpt.model;

import java.util.List;

public class Dangxiaozu {
    private Integer id;

    private String phone;

    private String dynum;

    private String pid;

    private String gridid;

    private String name;

    private String type;
    private String dangyuannum;
    private String vname;

    private String xname;

    private List<PartyMember> xzcy;

    public List<PartyMember> getXzcy() {
        return xzcy;
    }

    public void setXzcy(List<PartyMember> xzcy) {
        this.xzcy = xzcy;
    }

    private String wgy;
    private String wgyphone;

    public String getWgyphone() {
        return wgyphone;
    }

    public void setWgyphone(String wgyphone) {
        this.wgyphone = wgyphone;
    }

    public String getWgy() {
        return wgy;
    }

    public void setWgy(String wgy) {
        this.wgy = wgy;
    }

    public String getXname() {
        return xname;
    }

    public void setXname(String xname) {
        this.xname = xname;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getDangyuannum() {
        return dangyuannum;
    }

    public void setDangyuannum(String dangyuannum) {
        this.dangyuannum = dangyuannum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getDynum() {
        return dynum;
    }

    public void setDynum(String dynum) {
        this.dynum = dynum == null ? null : dynum.trim();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    public String getGridid() {
        return gridid;
    }

    public void setGridid(String gridid) {
        this.gridid = gridid == null ? null : gridid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
}