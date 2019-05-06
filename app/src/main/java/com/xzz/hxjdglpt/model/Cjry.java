package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Cjry implements Parcelable {
    private String id;

    private String name;

    private String birth;

    private String address;

    private String bz;

    private String gridId;

    private int bcNum;//本村数

    private int ghNum;//挂户数

    private String hj;//户籍

    private String picpath;

    private String cjType;// 残疾类型

    private String cjCode;// 残疾证号

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

    public int getBcNum() {
        return bcNum;
    }

    public void setBcNum(int bcNum) {
        this.bcNum = bcNum;
    }

    public int getGhNum() {
        return ghNum;
    }

    public void setGhNum(int ghNum) {
        this.ghNum = ghNum;
    }

    public String getHj() {
        return hj;
    }

    public void setHj(String hj) {
        this.hj = hj;
    }

    public String getCjType() {
        return cjType;
    }

    public void setCjType(String cjType) {
        this.cjType = cjType;
    }

    public String getCjCode() {
        return cjCode;
    }

    public void setCjCode(String cjCode) {
        this.cjCode = cjCode;
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
        parcel.writeString(bz);
        parcel.writeString(gridId);
        parcel.writeInt(bcNum);
        parcel.writeInt(ghNum);
        parcel.writeString(hj);
        parcel.writeString(picpath);
        parcel.writeString(cjType);
        parcel.writeString(cjCode);
    }

    public static final Parcelable.Creator<Cjry> CREATOR = new Parcelable.Creator<Cjry>() {
        public Cjry createFromParcel(Parcel in) {
            return new Cjry(in);
        }

        public Cjry[] newArray(int size) {
            return new Cjry[size];
        }
    };

    private Cjry(Parcel in) {
        id = in.readString();
        name = in.readString();
        birth = in.readString();
        address = in.readString();
        bz = in.readString();
        gridId = in.readString();
        bcNum = in.readInt();
        ghNum = in.readInt();
        hj = in.readString();
        picpath = in.readString();
        cjType = in.readString();
        cjCode = in.readString();
    }
}