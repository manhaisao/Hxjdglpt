package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Zdqsn implements Parcelable {

    private long id;

    private String name;

    private String sfzh;

    private String phone;

    private String address;

    private String jhr;

    private String jhrPhone;

    private int relationType;// 监护人与当事人关系----1,夫妻 2,父子 3,父女 4,母子 5,母女 6,婆媳 7,兄弟
    // 8,兄妹
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

    public String getJhr() {
        return jhr;
    }

    public void setJhr(String jhr) {
        this.jhr = jhr;
    }

    public String getJhrPhone() {
        return jhrPhone;
    }

    public void setJhrPhone(String jhrPhone) {
        this.jhrPhone = jhrPhone;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
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
        parcel.writeString(jhr);
        parcel.writeString(jhrPhone);
        parcel.writeInt(relationType);
        parcel.writeString(bz);
        parcel.writeString(gridId);
    }

    public static final Parcelable.Creator<Zdqsn> CREATOR = new Parcelable.Creator<Zdqsn>() {
        public Zdqsn createFromParcel(Parcel in) {
            return new Zdqsn(in);
        }

        public Zdqsn[] newArray(int size) {
            return new Zdqsn[size];
        }
    };

    private Zdqsn(Parcel in) {
        id = in.readLong();
        name = in.readString();
        sfzh = in.readString();
        phone = in.readString();
        address = in.readString();
        jhr = in.readString();
        jhrPhone = in.readString();
        relationType = in.readInt();
        bz = in.readString();
        gridId = in.readString();
    }
}
