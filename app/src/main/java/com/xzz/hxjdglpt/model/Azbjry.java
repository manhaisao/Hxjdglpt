package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Azbjry implements Parcelable {

    private long id;

    private String name;

    private String sfzh;

    private String phone;

    private String address;

    private String yzType;// 原罪类别

    private String fxjs;// 服刑监所

    private String xq;// 刑期

    private String xmrq;// 刑满日期

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

    public String getYzType() {
        return yzType;
    }

    public void setYzType(String yzType) {
        this.yzType = yzType;
    }

    public String getFxjs() {
        return fxjs;
    }

    public void setFxjs(String fxjs) {
        this.fxjs = fxjs;
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }

    public String getXmrq() {
        return xmrq;
    }

    public void setXmrq(String xmrq) {
        this.xmrq = xmrq;
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
        parcel.writeString(yzType);
        parcel.writeString(fxjs);
        parcel.writeString(xq);
        parcel.writeString(xmrq);
        parcel.writeString(bz);
        parcel.writeString(gridId);
    }

    public static final Parcelable.Creator<Azbjry> CREATOR = new Parcelable.Creator<Azbjry>() {
        public Azbjry createFromParcel(Parcel in) {
            return new Azbjry(in);
        }

        public Azbjry[] newArray(int size) {
            return new Azbjry[size];
        }
    };

    private Azbjry(Parcel in) {
        id = in.readLong();
        name = in.readString();
        sfzh = in.readString();
        phone = in.readString();
        address = in.readString();
        yzType = in.readString();
        fxjs = in.readString();
        xq = in.readString();
        xmrq = in.readString();
        bz = in.readString();
        gridId = in.readString();
    }
}
