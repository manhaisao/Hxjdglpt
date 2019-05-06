package com.xzz.hxjdglpt.model;

public class FamilyPlanning {
    private Long id;

    private String gridid;

    private String lead;

    private String leadphone;

    private String head;

    private String headphone;

    private String jdphone;

    private Integer ylfnnum;

    private Integer onechildfamilynum;

    private Integer jlfznum;

    private Integer ldrknum;

    private Integer sdjtnum;

    private Integer jsknnum;

    private String bz;

    private Integer lc;

    private Integer lr;

    private String picpath;

    private Integer shengfNum;// 省扶

    private Integer shifNum;// 市扶

    private Integer sdNum;// 失独

    private Integer scNum;// 伤残

    private Integer yyNum;//医院数量

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
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

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead == null ? null : lead.trim();
    }

    public String getLeadphone() {
        return leadphone;
    }

    public void setLeadphone(String leadphone) {
        this.leadphone = leadphone == null ? null : leadphone.trim();
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head == null ? null : head.trim();
    }

    public String getHeadphone() {
        return headphone;
    }

    public void setHeadphone(String headphone) {
        this.headphone = headphone == null ? null : headphone.trim();
    }

    public String getJdphone() {
        return jdphone;
    }

    public void setJdphone(String jdphone) {
        this.jdphone = jdphone == null ? null : jdphone.trim();
    }

    public Integer getYlfnnum() {
        return ylfnnum;
    }

    public void setYlfnnum(Integer ylfnnum) {
        this.ylfnnum = ylfnnum;
    }

    public Integer getOnechildfamilynum() {
        return onechildfamilynum;
    }

    public void setOnechildfamilynum(Integer onechildfamilynum) {
        this.onechildfamilynum = onechildfamilynum;
    }

    public Integer getJlfznum() {
        return jlfznum;
    }

    public void setJlfznum(Integer jlfznum) {
        this.jlfznum = jlfznum;
    }

    public Integer getLdrknum() {
        return ldrknum;
    }

    public void setLdrknum(Integer ldrknum) {
        this.ldrknum = ldrknum;
    }

    public Integer getSdjtnum() {
        return sdjtnum;
    }

    public void setSdjtnum(Integer sdjtnum) {
        this.sdjtnum = sdjtnum;
    }

    public Integer getJsknnum() {
        return jsknnum;
    }

    public void setJsknnum(Integer jsknnum) {
        this.jsknnum = jsknnum;
    }

    public Integer getLc() {
        return lc;
    }

    public void setLc(Integer lc) {
        this.lc = lc;
    }

    public Integer getLr() {
        return lr;
    }

    public void setLr(Integer lr) {
        this.lr = lr;
    }

    public Integer getShengfNum() {
        return shengfNum;
    }

    public void setShengfNum(Integer shengfNum) {
        this.shengfNum = shengfNum;
    }

    public Integer getShifNum() {
        return shifNum;
    }

    public void setShifNum(Integer shifNum) {
        this.shifNum = shifNum;
    }

    public Integer getSdNum() {
        return sdNum;
    }

    public void setSdNum(Integer sdNum) {
        this.sdNum = sdNum;
    }

    public Integer getScNum() {
        return scNum;
    }

    public void setScNum(Integer scNum) {
        this.scNum = scNum;
    }

    public Integer getYyNum() {
        return yyNum;
    }

    public void setYyNum(Integer yyNum) {
        this.yyNum = yyNum;
    }
}