package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Yanhua implements Parcelable {
    private Integer id;
    private String name;
    private String jyz;
    private String address;
    private String danwei;
    private String time;
    private String yijian;
    private String pifu;
    private String dengji;
    private String gridid;
    private String gridzrr;
    private String zrrphone;
    private String filename;
    private String filepath;
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

    public String getJyz() {
        return jyz;
    }

    public void setJyz(String jyz) {
        this.jyz = jyz == null ? null : jyz.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei == null ? null : danwei.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public String getYijian() {
        return yijian;
    }

    public void setYijian(String yijian) {
        this.yijian = yijian == null ? null : yijian.trim();
    }

    public String getPifu() {
        return pifu;
    }

    public void setPifu(String pifu) {
        this.pifu = pifu == null ? null : pifu.trim();
    }

    public String getDengji() {
        return dengji;
    }

    public void setDengji(String dengji) {
        this.dengji = dengji == null ? null : dengji.trim();
    }

    public String getGridid() {
        return gridid;
    }

    public void setGridid(String gridid) {
        this.gridid = gridid == null ? null : gridid.trim();
    }

    public String getGridzrr() {
        return gridzrr;
    }

    public void setGridzrr(String gridzrr) {
        this.gridzrr = gridzrr == null ? null : gridzrr.trim();
    }

    public String getZrrphone() {
        return zrrphone;
    }

    public void setZrrphone(String zrrphone) {
        this.zrrphone = zrrphone == null ? null : zrrphone.trim();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath == null ? null : filepath.trim();
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
        parcel.writeString(jyz);
        parcel.writeString(address);
        parcel.writeString(danwei);
        parcel.writeString(time);
        parcel.writeString(yijian);
        parcel.writeString(pifu);
        parcel.writeString(gridid);
        parcel.writeString(gridzrr);
        parcel.writeString(zrrphone);
        parcel.writeString(filepath);
        parcel.writeString(filename);
        parcel.writeString(bz);
        parcel.writeString(dengji);
    }

    public static final Parcelable.Creator<Yanhua> CREATOR = new Parcelable.Creator<Yanhua>() {
        public Yanhua createFromParcel(Parcel in) {
            return new Yanhua(in);
        }

        public Yanhua[] newArray(int size) {
            return new Yanhua[size];
        }
    };

    private Yanhua(Parcel in) {
        id = in.readInt();
        name = in.readString();
        jyz = in.readString();
        address = in.readString();
        danwei = in.readString();
        time = in.readString();
        yijian = in.readString();
        pifu = in.readString();
        gridid = in.readString();
        gridzrr = in.readString();
        zrrphone = in.readString();
        filepath = in.readString();
        filename = in.readString();
        bz = in.readString();
        dengji = in.readString();
    }
}