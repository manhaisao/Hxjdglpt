package com.xzz.hxjdglpt.model;

/**
 * 申请进入论坛的人
 *
 * @author dbz
 */
public class PlotUserList {

    private long id;
    private long userid;// 提问人
    private long plotid;// 小区ID
    private int status;// 申请状态;申请状态：0申请中，1申请成功
    private String plotName;// 小区名称

    private String sqsj;// 申请时间

    private String fbrXm;
    private String picture;

    private int lx;
    private long yqrid;// 邀请人ID
    private String yqrName;

    public long getYqrid() {
        return yqrid;
    }

    public void setYqrid(long yqrid) {
        this.yqrid = yqrid;
    }

    public String getYqrName() {
        return yqrName;
    }

    public void setYqrName(String yqrName) {
        this.yqrName = yqrName;
    }

    public int getLx() {
        return lx;
    }

    public void setLx(int lx) {
        this.lx = lx;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFbrXm() {
        return fbrXm;
    }

    public void setFbrXm(String fbrXm) {
        this.fbrXm = fbrXm;
    }

    public String getPlotName() {
        return plotName;
    }

    public void setPlotName(String plotName) {
        this.plotName = plotName;
    }

    public String getSqsj() {
        return sqsj;
    }

    public void setSqsj(String sqsj) {
        this.sqsj = sqsj;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getPlotid() {
        return plotid;
    }

    public void setPlotid(long plotid) {
        this.plotid = plotid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
