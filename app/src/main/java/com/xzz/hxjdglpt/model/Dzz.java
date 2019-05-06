package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Dzz implements Parcelable {
    private Long id;

    private String pid;

    private Long type;

    private String name;

    private int hasDzz;

    private String vname;

    private int dzzDynum;

    public int getDzzDynum() {
        return dzzDynum;
    }

    public void setDzzDynum(int dzzDynum) {
        this.dzzDynum = dzzDynum;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    private List<Dzb> dzb = new ArrayList<>();// 党支部

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public int getHasDzz() {
        return hasDzz;
    }

    public void setHasDzz(int hasDzz) {
        this.hasDzz = hasDzz;
    }

    public List<Dzb> getDzb() {
        return dzb;
    }

    public void setDzb(List<Dzb> dzb) {
        this.dzb = dzb;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(id);
        parcel.writeString(pid);
        parcel.writeLong(type);
        parcel.writeString(name);
        parcel.writeInt(hasDzz);
        parcel.writeString(vname);
        parcel.writeList(dzb);
        parcel.writeInt(dzzDynum);
    }

    public static final Parcelable.Creator<Dzz> CREATOR = new Parcelable.Creator<Dzz>() {
        public Dzz createFromParcel(Parcel in) {
            return new Dzz(in);
        }

        public Dzz[] newArray(int size) {
            return new Dzz[size];
        }
    };

    private Dzz(Parcel in) {
        id = in.readLong();
        pid = in.readString();
        type = in.readLong();
        name = in.readString();
        hasDzz = in.readInt();
        vname = in.readString();
        if (dzb == null) {
            dzb = new ArrayList<Dzb>();
        }
        in.readList(dzb, Dzb.class.getClassLoader());
        dzzDynum = in.readInt();
    }
}