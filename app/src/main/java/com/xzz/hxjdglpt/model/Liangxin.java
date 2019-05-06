package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Liangxin implements Parcelable {
    private Integer id;

    private String zongname;

    private String zongphone;

    private String funame;

    private String fuphone;

    private String dynum;

    private String dxznum;

    private Integer type;

    private String gridid;

    private Integer px;

    private String px1;
    private String chengyuannum;

    private String vname;

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getChengyuannum() {
        return chengyuannum;
    }

    public void setChengyuannum(String chengyuannum) {
        this.chengyuannum = chengyuannum;
    }

    public String getPx1() {
        return px1;
    }

    public void setPx1(String px1) {
        this.px1 = px1;
    }

    private String leixing;

    public String getLeixing() {
        return leixing;
    }

    public void setLeixing(String leixing) {
        this.leixing = leixing;
    }

    private String name;

    private String type1;

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

    public String getDynum() {
        return dynum;
    }

    public void setDynum(String dynum) {
        this.dynum = dynum == null ? null : dynum.trim();
    }

    public String getDxznum() {
        return dxznum;
    }

    public void setDxznum(String dxznum) {
        this.dxznum = dxznum == null ? null : dxznum.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getGridid() {
        return gridid;
    }

    public void setGridid(String gridid) {
        this.gridid = gridid == null ? null : gridid.trim();
    }

    public Integer getPx() {
        return px;
    }

    public void setPx(Integer px) {
        this.px = px;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1 == null ? null : type1.trim();
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
        parcel.writeInt(type);
        parcel.writeString(gridid);
        parcel.writeString(dynum);
        parcel.writeString(vname);
        parcel.writeInt(px);
        parcel.writeString(chengyuannum);
        parcel.writeString(name);
        parcel.writeString(leixing);
        parcel.writeString(type1);
        parcel.writeString(px1);
    }

    public static final Parcelable.Creator<Liangxin> CREATOR = new Parcelable.Creator<Liangxin>() {
        public Liangxin createFromParcel(Parcel in) {
            return new Liangxin(in);
        }

        public Liangxin[] newArray(int size) {
            return new Liangxin[size];
        }
    };

    private Liangxin(Parcel in) {
        id = in.readInt();
        zongname = in.readString();
        zongphone = in.readString();
        funame = in.readString();
        fuphone = in.readString();
        dxznum = in.readString();
        type = in.readInt();
        gridid = in.readString();
        dynum = in.readString();
        vname = in.readString();
        px = in.readInt();
        chengyuannum = in.readString();
        name = in.readString();
        leixing = in.readString();
        type1 = in.readString();
        px1 = in.readString();
    }
}