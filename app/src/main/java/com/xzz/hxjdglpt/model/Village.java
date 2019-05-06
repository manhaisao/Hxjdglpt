package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Village implements Parcelable {
    private String id;

    private String name;

    private int czgbNum;

    private int dorms;

    private String picpath;

    private List<Grid> gList;

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

    public int getCzgbNum() {
        return czgbNum;
    }

    public void setCzgbNum(int czgbNum) {
        this.czgbNum = czgbNum;
    }

    public int getDorms() {
        return dorms;
    }

    public void setDorms(int dorms) {
        this.dorms = dorms;
    }

    public List<Grid> getgList() {
        if (gList == null) {
            gList = new ArrayList<>();
        }
        return gList;
    }

    public void setgList(List<Grid> gList) {
        this.gList = gList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeInt(czgbNum);
        parcel.writeInt(dorms);
        parcel.writeString(picpath);
    }

    public static final Creator<Village> CREATOR = new Creator<Village>() {
        public Village createFromParcel(Parcel in) {
            return new Village(in);
        }

        public Village[] newArray(int size) {
            return new Village[size];
        }
    };

    private Village(Parcel in) {
        id = in.readString();
        name = in.readString();
        czgbNum = in.readInt();
        dorms = in.readInt();
        picpath = in.readString();
    }

    public Village() {
    }
}