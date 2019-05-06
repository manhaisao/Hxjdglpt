package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Dzb implements Parcelable {
    private long bid;

    private long zpid;

    private String dzbname;

    private Integer ztype;

    private String gid;
    private int dbzDynum;//党支部党员数
    private List<Dxz> dxz = new ArrayList<>();// 党小组

    public int getDbzDynum() {
        return dbzDynum;
    }

    public void setDbzDynum(int dbzDynum) {
        this.dbzDynum = dbzDynum;
    }

    public String getDzbname() {
        return dzbname;
    }

    public void setDzbname(String dzbname) {
        this.dzbname = dzbname == null ? null : dzbname.trim();
    }

    public long getBid() {
        return bid;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    public long getZpid() {
        return zpid;
    }

    public void setZpid(long zpid) {
        this.zpid = zpid;
    }

    public Integer getZtype() {
        return ztype;
    }

    public void setZtype(Integer ztype) {
        this.ztype = ztype;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public List<Dxz> getDxz() {
        return dxz;
    }

    public void setDxz(List<Dxz> dxz) {
        this.dxz = dxz;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(bid);
        parcel.writeLong(zpid);
        parcel.writeString(dzbname);
        parcel.writeInt(ztype);
        parcel.writeString(gid);
        parcel.writeList(dxz);
        parcel.writeInt(dbzDynum);
    }

    public static final Parcelable.Creator<Dzb> CREATOR = new Parcelable.Creator<Dzb>() {
        public Dzb createFromParcel(Parcel in) {
            return new Dzb(in);
        }

        public Dzb[] newArray(int size) {
            return new Dzb[size];
        }
    };

    private Dzb(Parcel in) {
        bid = in.readLong();
        zpid = in.readLong();
        dzbname = in.readString();
        ztype = in.readInt();
        gid = in.readString();
        if (dxz == null) {
            dxz = new ArrayList<Dxz>();
        }
        in.readList(dxz, Dxz.class.getClassLoader());
        dbzDynum = in.readInt();
    }

}