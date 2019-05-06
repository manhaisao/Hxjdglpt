package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Zlj implements Parcelable {

    private Long id;

    private String name;

    private String gridId;

    private String bz;

    private String type;

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
    }

    public static final Parcelable.Creator<Zlj> CREATOR = new Parcelable.Creator<Zlj>() {
        public Zlj createFromParcel(Parcel in) {
            return new Zlj(in);
        }

        public Zlj[] newArray(int size) {
            return new Zlj[size];
        }
    };

    private Zlj(Parcel in) {
        id = in.readLong();
        name = in.readString();
        bz = in.readString();
        gridId = in.readString();
        type = in.readString();
    }
}
