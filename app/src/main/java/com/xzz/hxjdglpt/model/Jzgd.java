package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Jzgd implements Parcelable {
    private Long id;

    private String yzname;

    private String sgf;

    private String sddw;

    private String fr;

    private String frdh;

    private String zczb;

    private String hszrr;

    private String hszrrdh;

    private String aqzrr;

    private String aqzrrdh;

    private String aqy;

    private String aqydh;

    private String pbqk;

    private String wgzrr;

    private String wgzrrdh;

    private String picpath;

    private String bz;

    private String gId;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYzname() {
        return yzname;
    }

    public void setYzname(String yzname) {
        this.yzname = yzname == null ? null : yzname.trim();
    }

    public String getSgf() {
        return sgf;
    }

    public void setSgf(String sgf) {
        this.sgf = sgf == null ? null : sgf.trim();
    }

    public String getSddw() {
        return sddw;
    }

    public void setSddw(String sddw) {
        this.sddw = sddw == null ? null : sddw.trim();
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr == null ? null : fr.trim();
    }

    public String getFrdh() {
        return frdh;
    }

    public void setFrdh(String frdh) {
        this.frdh = frdh == null ? null : frdh.trim();
    }

    public String getZczb() {
        return zczb;
    }

    public void setZczb(String zczb) {
        this.zczb = zczb == null ? null : zczb.trim();
    }

    public String getHszrr() {
        return hszrr;
    }

    public void setHszrr(String hszrr) {
        this.hszrr = hszrr == null ? null : hszrr.trim();
    }

    public String getHszrrdh() {
        return hszrrdh;
    }

    public void setHszrrdh(String hszrrdh) {
        this.hszrrdh = hszrrdh == null ? null : hszrrdh.trim();
    }

    public String getAqzrr() {
        return aqzrr;
    }

    public void setAqzrr(String aqzrr) {
        this.aqzrr = aqzrr == null ? null : aqzrr.trim();
    }

    public String getAqzrrdh() {
        return aqzrrdh;
    }

    public void setAqzrrdh(String aqzrrdh) {
        this.aqzrrdh = aqzrrdh == null ? null : aqzrrdh.trim();
    }

    public String getAqy() {
        return aqy;
    }

    public void setAqy(String aqy) {
        this.aqy = aqy == null ? null : aqy.trim();
    }

    public String getAqydh() {
        return aqydh;
    }

    public void setAqydh(String aqydh) {
        this.aqydh = aqydh == null ? null : aqydh.trim();
    }

    public String getPbqk() {
        return pbqk;
    }

    public void setPbqk(String pbqk) {
        this.pbqk = pbqk == null ? null : pbqk.trim();
    }

    public String getWgzrr() {
        return wgzrr;
    }

    public void setWgzrr(String wgzrr) {
        this.wgzrr = wgzrr == null ? null : wgzrr.trim();
    }

    public String getWgzrrdh() {
        return wgzrrdh;
    }

    public void setWgzrrdh(String wgzrrdh) {
        this.wgzrrdh = wgzrrdh == null ? null : wgzrrdh.trim();
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath == null ? null : picpath.trim();
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
        parcel.writeLong(id);
        parcel.writeString(yzname);
        parcel.writeString(sgf);
        parcel.writeString(sddw);
        parcel.writeString(fr);
        parcel.writeString(frdh);
        parcel.writeString(zczb);
        parcel.writeString(hszrr);
        parcel.writeString(hszrrdh);
        parcel.writeString(aqzrr);
        parcel.writeString(aqzrrdh);
        parcel.writeString(aqy);
        parcel.writeString(aqydh);
        parcel.writeString(pbqk);
        parcel.writeString(wgzrr);
        parcel.writeString(wgzrrdh);
        parcel.writeString(picpath);
        parcel.writeString(bz);
        parcel.writeString(gId);
        parcel.writeString(name);
    }

    public static final Parcelable.Creator<Jzgd> CREATOR = new Parcelable.Creator<Jzgd>() {
        public Jzgd createFromParcel(Parcel in) {
            return new Jzgd(in);
        }

        public Jzgd[] newArray(int size) {
            return new Jzgd[size];
        }
    };

    private Jzgd(Parcel in) {
        id = in.readLong();
        yzname = in.readString();
        sgf = in.readString();
        sddw = in.readString();
        fr = in.readString();
        frdh = in.readString();
        zczb = in.readString();
        hszrr = in.readString();
        hszrrdh = in.readString();
        aqzrr = in.readString();
        aqzrrdh = in.readString();
        aqy = in.readString();
        aqydh = in.readString();
        pbqk = in.readString();
        wgzrr = in.readString();
        wgzrrdh = in.readString();
        picpath = in.readString();
        bz = in.readString();
        gId = in.readString();
        name = in.readString();
    }
}