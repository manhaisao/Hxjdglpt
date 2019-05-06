package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Grid implements Parcelable {
    public Grid(){}
    private String id;

    private String zm;

    private String vId;

    private int zNum;

    private int rkNum;

    private int hNum;

    private String area;

    private int wqNum;

    private String name;//村名字

    private String picpath;

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getZm() {
        return zm;
    }

    public void setZm(String zm) {
        this.zm = zm == null ? null : zm.trim();
    }

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }

    public int getzNum() {
        return zNum;
    }

    public void setzNum(int zNum) {
        this.zNum = zNum;
    }

    public int getRkNum() {
        return rkNum;
    }

    public void setRkNum(int rkNum) {
        this.rkNum = rkNum;
    }

    public int gethNum() {
        return hNum;
    }

    public void sethNum(int hNum) {
        this.hNum = hNum;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public int getWqNum() {
        return wqNum;
    }

    public void setWqNum(int wqNum) {
        this.wqNum = wqNum;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(zm);
        parcel.writeString(vId);
        parcel.writeInt(zNum);
        parcel.writeInt(rkNum);
        parcel.writeInt(hNum);
        parcel.writeString(area);
        parcel.writeInt(wqNum);
        parcel.writeString(name);
        parcel.writeString(picpath);

    }

    public static final Creator<Grid> CREATOR = new Creator<Grid>() {
        public Grid createFromParcel(Parcel in) {
            return new Grid(in);
        }

        public Grid[] newArray(int size) {
            return new Grid[size];
        }
    };

    private Grid(Parcel in) {
        id = in.readString();
        zm = in.readString();
        vId = in.readString();
        zNum = in.readInt();
        rkNum = in.readInt();
        hNum = in.readInt();
        area = in.readString();
        wqNum = in.readInt();
        name = in.readString();
        picpath = in.readString();
    }
}