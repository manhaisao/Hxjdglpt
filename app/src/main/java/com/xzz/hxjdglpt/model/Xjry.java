package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Xjry implements Parcelable {
    private long id;

    private String name;

    private String sfzh;

    private String phone;

    private String address;

    private String zzmm;// 政治面貌

    private String whcd;// 文化程度

    private String dw;// 工作单位（职业）

    private String xjName;// 邪教名称

    private String jTime;// 参加时间

    private String bz;

    private String gridId;

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

    public String getZzmm() {
        return zzmm;
    }

    public void setZzmm(String zzmm) {
        this.zzmm = zzmm;
    }

    public String getWhcd() {
        return whcd;
    }

    public void setWhcd(String whcd) {
        this.whcd = whcd;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public String getXjName() {
        return xjName;
    }

    public void setXjName(String xjName) {
        this.xjName = xjName;
    }

    public String getjTime() {
        return jTime;
    }

    public void setjTime(String jTime) {
        this.jTime = jTime;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
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
        parcel.writeString(zzmm);
        parcel.writeString(whcd);
        parcel.writeString(dw);
        parcel.writeString(xjName);
        parcel.writeString(jTime);
        parcel.writeString(bz);
        parcel.writeString(gridId);
    }

    public static final Parcelable.Creator<Xjry> CREATOR = new Parcelable.Creator<Xjry>() {
        public Xjry createFromParcel(Parcel in) {
            return new Xjry(in);
        }

        public Xjry[] newArray(int size) {
            return new Xjry[size];
        }
    };

    private Xjry(Parcel in) {
        id = in.readLong();
        name = in.readString();
        sfzh = in.readString();
        phone = in.readString();
        address = in.readString();
        zzmm = in.readString();
        whcd = in.readString();
        dw = in.readString();
        xjName = in.readString();
        jTime = in.readString();
        bz = in.readString();
        gridId = in.readString();
    }
}
