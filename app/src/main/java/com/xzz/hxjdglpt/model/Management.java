package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Management implements Parcelable {
    private String id;

    private String name;

    private String address;

    private Integer employeenum;

    private String yyzzh;

    private String area;

    private String linkman;

    private String linkmanphone;

    private String bz;

    private String gridid;

    private String picpath;// 经营执照

    private String gongzi;// 人均工资

    private String isnas;// 是否纳税

    private String nsqk;// 纳税情况

    private String type;// 类型

    private String gridzrr;
    private String danwei;
    private String time;
    private String gridzrrdh;// 网格责任人电话

    public String getGridzrrdh() {
        return gridzrrdh;
    }

    public void setGridzrrdh(String gridzrrdh) {
        this.gridzrrdh = gridzrrdh;
    }

    public String getGridzrr() {
        return gridzrr;
    }

    public void setGridzrr(String gridzrr) {
        this.gridzrr = gridzrr;
    }

    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getEmployeenum() {
        return employeenum;
    }

    public void setEmployeenum(Integer employeenum) {
        this.employeenum = employeenum;
    }

    public String getYyzzh() {
        return yyzzh;
    }

    public void setYyzzh(String yyzzh) {
        this.yyzzh = yyzzh == null ? null : yyzzh.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman == null ? null : linkman.trim();
    }

    public String getLinkmanphone() {
        return linkmanphone;
    }

    public void setLinkmanphone(String linkmanphone) {
        this.linkmanphone = linkmanphone == null ? null : linkmanphone.trim();
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz == null ? null : bz.trim();
    }

    public String getGridid() {
        return gridid;
    }

    public void setGridid(String gridid) {
        this.gridid = gridid == null ? null : gridid.trim();
    }

    public String getGongzi() {
        return gongzi;
    }

    public void setGongzi(String gongzi) {
        this.gongzi = gongzi;
    }

    public String getIsnas() {
        return isnas;
    }

    public void setIsnas(String isnas) {
        this.isnas = isnas;
    }

    public String getNsqk() {
        return nsqk;
    }

    public void setNsqk(String nsqk) {
        this.nsqk = nsqk;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeInt(employeenum == null ? 0 : employeenum);
        parcel.writeString(yyzzh);
        parcel.writeString(area);
        parcel.writeString(linkman);
        parcel.writeString(linkmanphone);
        parcel.writeString(bz);
        parcel.writeString(gridid);
        parcel.writeString(picpath);
        parcel.writeString(gongzi);
        parcel.writeString(isnas);
        parcel.writeString(nsqk);
        parcel.writeString(type);
        parcel.writeString(danwei);
        parcel.writeString(gridzrr);
        parcel.writeString(gridzrrdh);
        parcel.writeString(time);
    }

    public static final Parcelable.Creator<Management> CREATOR = new Parcelable
            .Creator<Management>() {
        public Management createFromParcel(Parcel in) {
            return new Management(in);
        }

        public Management[] newArray(int size) {
            return new Management[size];
        }
    };

    private Management(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        employeenum = in.readInt();
        yyzzh = in.readString();
        area = in.readString();
        linkman = in.readString();
        linkmanphone = in.readString();
        bz = in.readString();
        gridid = in.readString();
        picpath = in.readString();
        gongzi = in.readString();
        isnas = in.readString();
        nsqk = in.readString();
        type = in.readString();
        danwei = in.readString();
        gridzrr = in.readString();
        gridzrrdh = in.readString();
        time = in.readString();
    }
}