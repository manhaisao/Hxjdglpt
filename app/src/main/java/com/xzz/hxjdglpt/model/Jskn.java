package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Jskn implements Parcelable {
    private Long id;

    private String name;// 姓名

    private String sfzh;// 身份证

    private String address;// 家庭住址

    private String reason;// 困难原因

    private String phone;// 联系电话

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        parcel.writeString(sfzh);
        parcel.writeString(address);
        parcel.writeString(reason);
        parcel.writeString(phone);
        parcel.writeString(bz);
        parcel.writeString(gridId);
    }

    public static final Parcelable.Creator<Jskn> CREATOR = new Parcelable.Creator<Jskn>() {
        public Jskn createFromParcel(Parcel in) {
            return new Jskn(in);
        }

        public Jskn[] newArray(int size) {
            return new Jskn[size];
        }
    };

    private Jskn(Parcel in) {
        id = in.readLong();
        name = in.readString();
        sfzh = in.readString();
        address = in.readString();
        reason = in.readString();
        phone = in.readString();
        bz = in.readString();
        gridId = in.readString();
    }
}
