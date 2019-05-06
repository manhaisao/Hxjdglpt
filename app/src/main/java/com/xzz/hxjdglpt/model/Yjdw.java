package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Yjdw implements Parcelable {
    private long id;

    private String name;// 单位名称

    private String type;// 类型

    private String phone;// 热线电话

    private int px;// 排序

    private String address;// 地址

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(phone);
        parcel.writeInt(px);
        parcel.writeString(address);
    }

    public static final Parcelable.Creator<Yjdw> CREATOR = new Parcelable.Creator<Yjdw>() {
        public Yjdw createFromParcel(Parcel in) {
            return new Yjdw(in);
        }

        public Yjdw[] newArray(int size) {
            return new Yjdw[size];
        }
    };

    private Yjdw(Parcel in) {
        id = in.readLong();
        name = in.readString();
        type = in.readString();
        phone = in.readString();
        px = in.readInt();
        address = in.readString();
    }
}
