package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Yfdx implements Parcelable {

    private Long id;

    private String name;// 姓名

    private String code;// 身份证

    private String address;// 地址

    private String type;// 优抚类型

    private String bz;// 备注

    private String gridId;// 网格ID

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        parcel.writeString(code);
        parcel.writeString(address);
        parcel.writeString(type);
        parcel.writeString(bz);
        parcel.writeString(gridId);
    }

    public static final Parcelable.Creator<Yfdx> CREATOR = new Parcelable.Creator<Yfdx>() {
        public Yfdx createFromParcel(Parcel in) {
            return new Yfdx(in);
        }

        public Yfdx[] newArray(int size) {
            return new Yfdx[size];
        }
    };

    private Yfdx(Parcel in) {
        id = in.readLong();
        name = in.readString();
        code = in.readString();
        address = in.readString();
        type = in.readString();
        bz = in.readString();
        gridId = in.readString();
    }
}
