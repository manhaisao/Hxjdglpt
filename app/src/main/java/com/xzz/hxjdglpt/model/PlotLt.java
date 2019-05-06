package com.xzz.hxjdglpt.model;

/**
 * 论坛
 *
 * @author dbz
 */
public class PlotLt {

    private long id;
    private long userid;// 提问人
    private String fbsj;// 发布时间
    private String content;// 内容
    private long pid;// 小区ID
    private String fbrXm;
    private String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getFbsj() {
        return fbsj;
    }

    public void setFbsj(String fbsj) {
        this.fbsj = fbsj;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getFbrXm() {
        return fbrXm;
    }

    public void setFbrXm(String fbrXm) {
        this.fbrXm = fbrXm;
    }

}
