package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Dbry implements Parcelable {
    private String id;

    private String name;

    private String birth;

    private String address;

    private String type;

    private String bz;

    private String gridId;

    private String picpath;

    private String dbCode;// 低保证号

    private String pNum;// 低保人数

    private String sfzh;//身份证
    private String wgy;//网格员

    private String wgyphone;//网格员电话

    private String reason;// 低保原因

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

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth == null ? null : birth.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz == null ? null : bz.trim();
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId == null ? null : gridId.trim();
    }

    public String getDbCode() {
        return dbCode;
    }

    public void setDbCode(String dbCode) {
        this.dbCode = dbCode;
    }

    public String getpNum() {
        return pNum;
    }

    public void setpNum(String pNum) {
        this.pNum = pNum;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getWgy() {
        return wgy;
    }

    public void setWgy(String wgy) {
        this.wgy = wgy;
    }

    public String getWgyphone() {
        return wgyphone;
    }

    public void setWgyphone(String wgyphone) {
        this.wgyphone = wgyphone;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(birth);
        parcel.writeString(address);
        parcel.writeString(type);
        parcel.writeString(bz);
        parcel.writeString(gridId);
        parcel.writeString(picpath);
        parcel.writeString(dbCode);
        parcel.writeString(pNum);
        parcel.writeString(sfzh);
        parcel.writeString(wgy);
        parcel.writeString(wgyphone);
        parcel.writeString(reason);
    }

    public static final Parcelable.Creator<Dbry> CREATOR = new Parcelable.Creator<Dbry>() {
        public Dbry createFromParcel(Parcel in) {
            return new Dbry(in);
        }

        public Dbry[] newArray(int size) {
            return new Dbry[size];
        }
    };

    private Dbry(Parcel in) {
        id = in.readString();
        name = in.readString();
        birth = in.readString();
        address = in.readString();
        type = in.readString();
        bz = in.readString();
        gridId = in.readString();
        picpath = in.readString();
        dbCode = in.readString();
        pNum = in.readString();
        sfzh = in.readString();
        wgy = in.readString();
        wgyphone = in.readString();
        reason = in.readString();
    }
}