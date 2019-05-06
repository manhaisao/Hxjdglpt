package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Sqfxry implements Parcelable {

    private long id;

    private String name;

    private String sfzh;

    private String phone;

    private String address;

    private String zm;// 罪名

    private String xqxz;// 原判刑期及刑种

    private String jzSTime;// 矫正起时间

    private String jzETime;// 矫正止时间

    private String bz;

    private String gridId;

    private String jzType;// 矫正类别

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZm() {
        return zm;
    }

    public void setZm(String zm) {
        this.zm = zm;
    }

    public String getXqxz() {
        return xqxz;
    }

    public void setXqxz(String xqxz) {
        this.xqxz = xqxz;
    }

    public String getJzSTime() {
        return jzSTime;
    }

    public void setJzSTime(String jzSTime) {
        this.jzSTime = jzSTime;
    }

    public String getJzETime() {
        return jzETime;
    }

    public void setJzETime(String jzETime) {
        this.jzETime = jzETime;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getJzType() {
        return jzType;
    }

    public void setJzType(String jzType) {
        this.jzType = jzType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(sfzh);
        parcel.writeString(phone);
        parcel.writeString(address);
        parcel.writeString(zm);
        parcel.writeString(xqxz);
        parcel.writeString(jzSTime);
        parcel.writeString(jzETime);
        parcel.writeString(bz);
        parcel.writeString(gridId);
        parcel.writeString(jzType);
    }

    public static final Parcelable.Creator<Sqfxry> CREATOR = new Parcelable.Creator<Sqfxry>() {
        public Sqfxry createFromParcel(Parcel in) {
            return new Sqfxry(in);
        }

        public Sqfxry[] newArray(int size) {
            return new Sqfxry[size];
        }
    };

    private Sqfxry(Parcel in) {
        id = in.readLong();
        name = in.readString();
        sfzh = in.readString();
        phone = in.readString();
        address = in.readString();
        zm = in.readString();
        xqxz = in.readString();
        jzSTime = in.readString();
        jzETime = in.readString();
        bz = in.readString();
        gridId = in.readString();
        jzType = in.readString();
    }
}
