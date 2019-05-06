package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Jlfz implements Parcelable {

    private Long id;

    private String name;// 姓名

    private String sfzh;// 身份证

    private String type;// 奖扶类型

    private String address;// 家庭地址

    private String phone;// 电话

    private String picture;// 照片

    private int isRz;// 是否认证

    private String bz;// 备注

    private String gridId;// 网格号

    private String reason;// 退回原因

    private String ldlx;//流动类型

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getIsRz() {
        return isRz;
    }

    public void setIsRz(int isRz) {
        this.isRz = isRz;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
        parcel.writeString(type);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(picture);
        parcel.writeInt(isRz);
        parcel.writeString(bz);
        parcel.writeString(gridId);
        parcel.writeString(reason);
        parcel.writeString(ldlx);
    }

    public static final Parcelable.Creator<Jlfz> CREATOR = new Parcelable.Creator<Jlfz>() {
        public Jlfz createFromParcel(Parcel in) {
            return new Jlfz(in);
        }

        public Jlfz[] newArray(int size) {
            return new Jlfz[size];
        }
    };

    private Jlfz(Parcel in) {
        id = in.readLong();
        name = in.readString();
        sfzh = in.readString();
        type = in.readString();
        address = in.readString();
        phone = in.readString();
        picture = in.readString();
        isRz = in.readInt();
        bz = in.readString();
        gridId = in.readString();
        reason = in.readString();
        ldlx = in.readString();
    }
}
