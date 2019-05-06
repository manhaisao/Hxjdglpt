package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Zddw implements Parcelable {
    private Integer id;

    private String zongname;// 党委书记、支部书记、组长

    private String zongphone;// 党委书记、支部书记、组长电话

    private String funame;// 党委副书记、支部副书记

    private String fuphone;// 党委副书记、支部副书记电话

    private String dxznum;// 党支部或党小组数

    private String type;// 1：党委:2：党支部3：党小组

    private String gridId;
    private String dynum;
    private String vname;

    private String px;
    private String chengyuannum;
    private String name;

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChengyuannum() {
        return chengyuannum;
    }

    public void setChengyuannum(String chengyuannum) {
        this.chengyuannum = chengyuannum;
    }

    public String getDxznum() {
        return dxznum;
    }

    public void setDxznum(String dxznum) {
        this.dxznum = dxznum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getPx() {
        return px;
    }

    public void setPx(String px) {
        this.px = px;
    }

    public String getDynum() {
        return dynum;
    }

    public void setDynum(String dynum) {
        this.dynum = dynum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZongname() {
        return zongname;
    }

    public void setZongname(String zongname) {
        this.zongname = zongname == null ? null : zongname.trim();
    }

    public String getZongphone() {
        return zongphone;
    }

    public void setZongphone(String zongphone) {
        this.zongphone = zongphone == null ? null : zongphone.trim();
    }

    public String getFuname() {
        return funame;
    }

    public void setFuname(String funame) {
        this.funame = funame == null ? null : funame.trim();
    }

    public String getFuphone() {
        return fuphone;
    }

    public void setFuphone(String fuphone) {
        this.fuphone = fuphone == null ? null : fuphone.trim();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(id);
        parcel.writeString(zongname);
        parcel.writeString(zongphone);
        parcel.writeString(funame);
        parcel.writeString(fuphone);
        parcel.writeString(dxznum);
        parcel.writeString(type);
        parcel.writeString(gridId);
        parcel.writeString(dynum);
        parcel.writeString(vname);
        parcel.writeString(px);
        parcel.writeString(chengyuannum);
        parcel.writeString(name);
    }

    public static final Parcelable.Creator<Zddw> CREATOR = new Parcelable.Creator<Zddw>() {
        public Zddw createFromParcel(Parcel in) {
            return new Zddw(in);
        }

        public Zddw[] newArray(int size) {
            return new Zddw[size];
        }
    };

    private Zddw(Parcel in) {
        id = in.readInt();
        zongname = in.readString();
        zongphone = in.readString();
        funame = in.readString();
        fuphone = in.readString();
        dxznum = in.readString();
        type = in.readString();
        gridId = in.readString();
        dynum = in.readString();
        vname = in.readString();
        px = in.readString();
        chengyuannum = in.readString();
        name = in.readString();
    }
}