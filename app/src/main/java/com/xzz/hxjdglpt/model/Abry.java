package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Abry implements Parcelable {

    private long id;
    private String name;
    private String phone;
    private long pid;
    private String  fzxq;

    public String getFzxq() {
        return fzxq;
    }

    public void setFzxq(String fzxq) {
        this.fzxq = fzxq;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeLong(pid);
        parcel.writeString(phone);
    }

    public static final Parcelable.Creator<Abry> CREATOR = new Parcelable.Creator<Abry>() {
        public Abry createFromParcel(Parcel in) {
            return new Abry(in);
        }

        public Abry[] newArray(int size) {
            return new Abry[size];
        }
    };

    private Abry(Parcel in) {
        id = in.readLong();
        name = in.readString();
        pid = in.readLong();
        phone = in.readString();
    }
}
