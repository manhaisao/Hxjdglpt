package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Lsrt implements Parcelable {

    private Long id;

    private String name;// 姓名

    private String address;// 地址

    private String jhrName;// 监护人姓名

    private String bz;

    private String gridId;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJhrName() {
        return jhrName;
    }

    public void setJhrName(String jhrName) {
        this.jhrName = jhrName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(jhrName);
        parcel.writeString(bz);
        parcel.writeString(gridId);
    }

    public static final Parcelable.Creator<Lsrt> CREATOR = new Parcelable.Creator<Lsrt>() {
        public Lsrt createFromParcel(Parcel in) {
            return new Lsrt(in);
        }

        public Lsrt[] newArray(int size) {
            return new Lsrt[size];
        }
    };

    private Lsrt(Parcel in) {
        id = in.readLong();
        name = in.readString();
        address = in.readString();
        jhrName = in.readString();
        bz = in.readString();
        gridId = in.readString();
    }
}
