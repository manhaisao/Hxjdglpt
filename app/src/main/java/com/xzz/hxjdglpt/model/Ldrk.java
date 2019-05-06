package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ldrk implements Parcelable {

    private Long id;

    private String name;// 姓名

    private String sfzh;// 身份证

    private String address;// 户籍地址

    private String nowAddress;// 现住址

    private String ldlx;//流动类型

    private String bz;// 备注

    private String gridId;// 网格号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNowAddress() {
        return nowAddress;
    }

    public void setNowAddress(String nowAddress) {
        this.nowAddress = nowAddress;
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

    public String getLdlx() {
        return ldlx;
    }

    public void setLdlx(String ldlx) {
        this.ldlx = ldlx;
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
        parcel.writeString(address);
        parcel.writeString(nowAddress);
        parcel.writeString(bz);
        parcel.writeString(ldlx);
        parcel.writeString(gridId);
    }

    public static final Parcelable.Creator<Ldrk> CREATOR = new Parcelable.Creator<Ldrk>() {
        public Ldrk createFromParcel(Parcel in) {
            return new Ldrk(in);
        }

        public Ldrk[] newArray(int size) {
            return new Ldrk[size];
        }
    };

    private Ldrk(Parcel in) {
        id = in.readLong();
        name = in.readString();
        sfzh = in.readString();
        address = in.readString();
        nowAddress = in.readString();
        bz = in.readString();
        ldlx= in.readString();
        gridId = in.readString();
    }
}
