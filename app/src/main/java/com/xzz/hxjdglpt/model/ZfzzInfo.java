package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ZfzzInfo implements Parcelable {

    private Long id;

    private String name;

    private String phone;

    private String gridId;

    private String type;

    private String bz;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        parcel.writeString(phone);
        parcel.writeString(gridId);
        parcel.writeString(type);
        parcel.writeString(bz);
    }

    public static final Parcelable.Creator<ZfzzInfo> CREATOR = new Parcelable.Creator<ZfzzInfo>() {
        public ZfzzInfo createFromParcel(Parcel in) {
            return new ZfzzInfo(in);
        }

        public ZfzzInfo[] newArray(int size) {
            return new ZfzzInfo[size];
        }
    };

    private ZfzzInfo(Parcel in) {
        id = in.readLong();
        name = in.readString();
        phone = in.readString();
        gridId = in.readString();
        type = in.readString();
        bz = in.readString();
    }
}
