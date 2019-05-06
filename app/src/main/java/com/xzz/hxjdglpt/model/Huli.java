package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Huli implements Parcelable {
    private Integer id;

    private String name;

    private String phone;

    private String no;

    private String qingkuang;

    private String time;

    private String bz;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no == null ? null : no.trim();
    }

    public String getQingkuang() {
        return qingkuang;
    }

    public void setQingkuang(String qingkuang) {
        this.qingkuang = qingkuang == null ? null : qingkuang.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz == null ? null : bz.trim();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(bz);
        parcel.writeString(qingkuang);
        parcel.writeString(no);
        parcel.writeString(time);
        parcel.writeString(bz);
    }

    public static final Parcelable.Creator<Huli> CREATOR = new Parcelable.Creator<Huli>() {
        public Huli createFromParcel(Parcel in) {
            return new Huli(in);
        }

        public Huli[] newArray(int size) {
            return new Huli[size];
        }
    };

    private Huli(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phone = in.readString();
        bz = in.readString();
        qingkuang = in.readString();
        no = in.readString();
        time= in.readString();
        bz = in.readString();
    }
}