package com.xzz.hxjdglpt.model;

public class Xshs {
    private Integer id;

    private String gridid;

    private String zrr;

    private String zrrphone;

    private String hsy;

    private String hsyphone;

    private String ltqy;

    private String myqy;

    private String gtgsh;

    private String yanhsh;

    private String mark;

    private String picpath;
    private int qiye = 0;//企业个数
    private int geti = 0;//个体工商户个数

    private int spdkd = 0;//税票代开点
    private int my;
    private int jt;
    private int zbs;// 总部
    private int jz;// 建筑
    private int lt;

    public int getLt() {
        return lt;
    }

    public void setLt(int lt) {
        this.lt = lt;
    }

    public int getMy() {
        return my;
    }

    public void setMy(int my) {
        this.my = my;
    }

    public int getJt() {
        return jt;
    }

    public void setJt(int jt) {
        this.jt = jt;
    }

    public int getZbs() {
        return zbs;
    }

    public void setZbs(int zbs) {
        this.zbs = zbs;
    }

    public int getJz() {
        return jz;
    }

    public void setJz(int jz) {
        this.jz = jz;
    }

    public int getSpdkd() {
        return spdkd;
    }

    public void setSpdkd(int spdkd) {
        this.spdkd = spdkd;
    }

    public int getQiye() {
        return qiye;
    }

    public void setQiye(int qiye) {
        this.qiye = qiye;
    }

    public int getGeti() {
        return geti;
    }

    public void setGeti(int geti) {
        this.geti = geti;
    }

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

    public String getHsy() {
        return hsy;
    }

    public void setHsy(String hsy) {
        this.hsy = hsy == null ? null : hsy.trim();
    }

    public String getHsyphone() {
        return hsyphone;
    }

    public void setHsyphone(String hsyphone) {
        this.hsyphone = hsyphone == null ? null : hsyphone.trim();
    }

    public String getLtqy() {
        return ltqy;
    }

    public void setLtqy(String ltqy) {
        this.ltqy = ltqy == null ? null : ltqy.trim();
    }

    public String getMyqy() {
        return myqy;
    }

    public void setMyqy(String myqy) {
        this.myqy = myqy == null ? null : myqy.trim();
    }

    public String getGtgsh() {
        return gtgsh;
    }

    public void setGtgsh(String gtgsh) {
        this.gtgsh = gtgsh == null ? null : gtgsh.trim();
    }

    public String getYanhsh() {
        return yanhsh;
    }

    public void setYanhsh(String yanhsh) {
        this.yanhsh = yanhsh == null ? null : yanhsh.trim();
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark == null ? null : mark.trim();
    }
}