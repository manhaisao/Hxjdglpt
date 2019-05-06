package com.xzz.hxjdglpt.model;

public class Sfxz {
    private Long id;

    private String gridid;

    private String headname;

    private String headphone;

    private String gridname;

    private String gridphone;

    private int sqfsman;// 社区服刑

    private int sqsjman;

    private int zsjjman;

    private int qsnwffz;

    private String remark;

    private String gname;

    private String picpath;

    private int zsjsb;// 肇事肇祸精神病患者,
    private int zdqsn;// 重点青少年,
    private int sqjd;// 社区戒毒人员,
    private int xjry;// 邪教人员,
    private int azbjry;// 安置帮教人员
    // 从其他表中统计
    private int mdjfdjy;// 矛盾纠纷调解员
    private int mdjfxxy;// 矛盾纠纷信息员
    private int fzxcy;// 法制宣传员
    private int flgw;// 法律顾问
    private int pfzyz;// 普法志愿者
    // 保存网格里面的
    private int mdjfdjy_g;// 矛盾纠纷调解员
    private int mdjfxxy_g;// 矛盾纠纷信息员
    private int fzxcy_g;// 法制宣传员
    private int flgw_g;// 法律顾问
    private int pfzyz_g;// 普法志愿者

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

    public String getHeadname() {
        return headname;
    }

    public void setHeadname(String headname) {
        this.headname = headname == null ? null : headname.trim();
    }

    public String getHeadphone() {
        return headphone;
    }

    public void setHeadphone(String headphone) {
        this.headphone = headphone == null ? null : headphone.trim();
    }

    public String getGridname() {
        return gridname;
    }

    public void setGridname(String gridname) {
        this.gridname = gridname == null ? null : gridname.trim();
    }

    public String getGridphone() {
        return gridphone;
    }

    public void setGridphone(String gridphone) {
        this.gridphone = gridphone == null ? null : gridphone.trim();
    }

    public int getSqfsman() {
        return sqfsman;
    }

    public void setSqfsman(int sqfsman) {
        this.sqfsman = sqfsman;
    }

    public int getSqsjman() {
        return sqsjman;
    }

    public void setSqsjman(int sqsjman) {
        this.sqsjman = sqsjman;
    }

    public int getZsjjman() {
        return zsjjman;
    }

    public void setZsjjman(int zsjjman) {
        this.zsjjman = zsjjman;
    }

    public int getQsnwffz() {
        return qsnwffz;
    }

    public void setQsnwffz(int qsnwffz) {
        this.qsnwffz = qsnwffz;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public int getZsjsb() {
        return zsjsb;
    }

    public void setZsjsb(int zsjsb) {
        this.zsjsb = zsjsb;
    }

    public int getZdqsn() {
        return zdqsn;
    }

    public void setZdqsn(int zdqsn) {
        this.zdqsn = zdqsn;
    }

    public int getSqjd() {
        return sqjd;
    }

    public void setSqjd(int sqjd) {
        this.sqjd = sqjd;
    }

    public int getXjry() {
        return xjry;
    }

    public void setXjry(int xjry) {
        this.xjry = xjry;
    }

    public int getAzbjry() {
        return azbjry;
    }

    public void setAzbjry(int azbjry) {
        this.azbjry = azbjry;
    }

    public int getMdjfdjy() {
        return mdjfdjy;
    }

    public void setMdjfdjy(int mdjfdjy) {
        this.mdjfdjy = mdjfdjy;
    }

    public int getMdjfxxy() {
        return mdjfxxy;
    }

    public void setMdjfxxy(int mdjfxxy) {
        this.mdjfxxy = mdjfxxy;
    }

    public int getFzxcy() {
        return fzxcy;
    }

    public void setFzxcy(int fzxcy) {
        this.fzxcy = fzxcy;
    }

    public int getFlgw() {
        return flgw;
    }

    public void setFlgw(int flgw) {
        this.flgw = flgw;
    }

    public int getPfzyz() {
        return pfzyz;
    }

    public void setPfzyz(int pfzyz) {
        this.pfzyz = pfzyz;
    }

    public int getMdjfdjy_g() {
        return mdjfdjy_g;
    }

    public void setMdjfdjy_g(int mdjfdjy_g) {
        this.mdjfdjy_g = mdjfdjy_g;
    }

    public int getMdjfxxy_g() {
        return mdjfxxy_g;
    }

    public void setMdjfxxy_g(int mdjfxxy_g) {
        this.mdjfxxy_g = mdjfxxy_g;
    }

    public int getFzxcy_g() {
        return fzxcy_g;
    }

    public void setFzxcy_g(int fzxcy_g) {
        this.fzxcy_g = fzxcy_g;
    }

    public int getFlgw_g() {
        return flgw_g;
    }

    public void setFlgw_g(int flgw_g) {
        this.flgw_g = flgw_g;
    }

    public int getPfzyz_g() {
        return pfzyz_g;
    }

    public void setPfzyz_g(int pfzyz_g) {
        this.pfzyz_g = pfzyz_g;
    }
}