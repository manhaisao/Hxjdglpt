package com.xzz.hxjdglpt.model;

public class BusinessJob {
    private Long id;

    private String gridid;

    private String item;

    private String headName;

    private String headPhone;

    private String listBusiness;

    private String myBusiness;

    private String gtsh;

    private String yhbzsh;

    private String remarks;

    private String picpath;

    private int lt;

    private int my;
    private int jt;
    private int zbs;//总部
    private int gt;//个体
    private int jz;//建筑

    private int dkd;//代开点

    private String hszrr;
    private String hszrrphone;

    private String wgzrr;// 网格责任人

    private String wgzrrdh;// 网格责任人电话

    public int getDkd() {
        return dkd;
    }

    public void setDkd(int dkd) {
        this.dkd = dkd;
    }

    public int getZbs() {
        return zbs;
    }

    public void setZbs(int zbs) {
        this.zbs = zbs;
    }

    public int getGt() {
        return gt;
    }

    public void setGt(int gt) {
        this.gt = gt;
    }

    public int getJz() {
        return jz;
    }

    public void setJz(int jz) {
        this.jz = jz;
    }

    public String getWgzrr() {
        return wgzrr;
    }

    public void setWgzrr(String wgzrr) {
        this.wgzrr = wgzrr;
    }

    public String getWgzrrdh() {
        return wgzrrdh;
    }

    public void setWgzrrdh(String wgzrrdh) {
        this.wgzrrdh = wgzrrdh;
    }

    public String getHszrr() {
        return hszrr;
    }

    public void setHszrr(String hszrr) {
        this.hszrr = hszrr;
    }

    public String getHszrrphone() {
        return hszrrphone;
    }

    public void setHszrrphone(String hszrrphone) {
        this.hszrrphone = hszrrphone;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getGridid() {
        return gridid;
    }

    public void setGridid(String gridid) {
        this.gridid = gridid;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item == null ? null : item.trim();
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public String getHeadPhone() {
        return headPhone;
    }

    public void setHeadPhone(String headPhone) {
        this.headPhone = headPhone;
    }

    public String getListBusiness() {
        return listBusiness;
    }

    public void setListBusiness(String listBusiness) {
        this.listBusiness = listBusiness;
    }

    public String getMyBusiness() {
        return myBusiness;
    }

    public void setMyBusiness(String myBusiness) {
        this.myBusiness = myBusiness == null ? null : myBusiness.trim();
    }

    public String getGtsh() {
        return gtsh;
    }

    public void setGtsh(String gtsh) {
        this.gtsh = gtsh == null ? null : gtsh.trim();
    }

    public String getYhbzsh() {
        return yhbzsh;
    }

    public void setYhbzsh(String yhbzsh) {
        this.yhbzsh = yhbzsh == null ? null : yhbzsh.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

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
}