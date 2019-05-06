package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ylfn implements Parcelable {
    private String id;

    private String name;

    private String no;

    private String birthday;

    private String hjtype;

    private String gridId;

    private String bz;

    private String address;

    private String selected;

    private String picpath;

    private String hyzk;

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no == null ? null : no.trim();
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    public String getHjtype() {
        return hjtype;
    }

    public void setHjtype(String hjtype) {
        this.hjtype = hjtype == null ? null : hjtype.trim();
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getHyzk() {
        return hyzk;
    }

    public void setHyzk(String hyzk) {
        this.hyzk = hyzk;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(no);
        parcel.writeString(birthday);
        parcel.writeString(hjtype);
        parcel.writeString(gridId);
        parcel.writeString(bz);
        parcel.writeString(address);
        parcel.writeString(selected);
        parcel.writeString(picpath);
        parcel.writeString(hyzk);
    }

    public static final Parcelable.Creator<Ylfn> CREATOR = new Parcelable.Creator<Ylfn>() {
        public Ylfn createFromParcel(Parcel in) {
            return new Ylfn(in);
        }

        public Ylfn[] newArray(int size) {
            return new Ylfn[size];
        }
    };

    private Ylfn(Parcel in) {
        id = in.readString();
        name = in.readString();
        no = in.readString();
        birthday = in.readString();
        hjtype = in.readString();
        gridId = in.readString();
        bz = in.readString();
        address = in.readString();
        selected = in.readString();
        picpath = in.readString();
        hyzk = in.readString();
    }

}