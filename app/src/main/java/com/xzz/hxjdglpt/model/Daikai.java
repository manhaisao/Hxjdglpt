package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Daikai implements Parcelable {
    private Integer id;

    private String name;

    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
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
        parcel.writeString(address);
        parcel.writeString(bz);
        parcel.writeString(time);
    }

    public static final Parcelable.Creator<Daikai> CREATOR = new Parcelable.Creator<Daikai>() {
        public Daikai createFromParcel(Parcel in) {
            return new Daikai(in);
        }

        public Daikai[] newArray(int size) {
            return new Daikai[size];
        }
    };

    private Daikai(Parcel in) {
        id = in.readInt();
        name = in.readString();
        address = in.readString();
        bz = in.readString();
        time = in.readString();
    }

}