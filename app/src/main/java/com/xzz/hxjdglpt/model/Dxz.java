package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Dxz implements Parcelable {
    private Long xid;

    private Long xpid;

    private String xname;

    public Long getXid() {
        return xid;
    }

    public void setXid(Long xid) {
        this.xid = xid;
    }

    public Long getXpid() {
        return xpid;
    }

    public void setXpid(Long xpid) {
        this.xpid = xpid;
    }

    public String getXname() {
        return xname;
    }

    public void setXname(String xname) {
        this.xname = xname == null ? null : xname.trim();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(xid);
        parcel.writeLong(xpid);
        parcel.writeString(xname);
    }

    public static final Parcelable.Creator<Dxz> CREATOR = new Parcelable.Creator<Dxz>() {
        public Dxz createFromParcel(Parcel in) {
            return new Dxz(in);
        }

        public Dxz[] newArray(int size) {
            return new Dxz[size];
        }
    };

    private Dxz(Parcel in) {
        xid = in.readLong();
        xpid = in.readLong();
        xname = in.readString();
    }
}