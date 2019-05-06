package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ZfzzInfoVillage implements Parcelable {

    private Long id;

    private String name;

    private String phone;

    private String vId;

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

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
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
        parcel.writeString(vId);
        parcel.writeString(type);
        parcel.writeString(bz);
    }

    public static final Creator<ZfzzInfoVillage> CREATOR = new Creator<ZfzzInfoVillage>() {
        public ZfzzInfoVillage createFromParcel(Parcel in) {
            return new ZfzzInfoVillage(in);
        }

        public ZfzzInfoVillage[] newArray(int size) {
            return new ZfzzInfoVillage[size];
        }
    };

    private ZfzzInfoVillage(Parcel in) {
        id = in.readLong();
        name = in.readString();
        phone = in.readString();
        vId = in.readString();
        type = in.readString();
        bz = in.readString();
    }
}
