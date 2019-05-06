package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Hospital implements Parcelable {
    private long id;

    private String name;// 单位名称

    private String phone;// 热线电话

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


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(phone);
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        public Hospital createFromParcel(Parcel in) {
            return new Hospital(in);
        }

        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };

    private Hospital(Parcel in) {
        id = in.readLong();
        name = in.readString();
        phone = in.readString();
    }
}
