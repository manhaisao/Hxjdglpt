package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Tkgy implements Parcelable {

    private Long id;

    private String name;

    private String gridId;

    private String bz;

    private String type;

    private String csrq;

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

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(bz);
        parcel.writeString(gridId);
        parcel.writeString(type);
        parcel.writeString(csrq);
    }

    public static final Parcelable.Creator<Tkgy> CREATOR = new Parcelable.Creator<Tkgy>() {
        public Tkgy createFromParcel(Parcel in) {
            return new Tkgy(in);
        }

        public Tkgy[] newArray(int size) {
            return new Tkgy[size];
        }
    };

    private Tkgy(Parcel in) {
        id = in.readLong();
        name = in.readString();
        bz = in.readString();
        gridId = in.readString();
        type = in.readString();
        csrq = in.readString();
    }
}
