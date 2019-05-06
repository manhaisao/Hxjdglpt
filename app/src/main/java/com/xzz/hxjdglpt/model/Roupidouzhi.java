package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Roupidouzhi implements Parcelable {
    private Integer id;

    private String name;

    private String jyz;

    private String address;

    private String danwei;

    private String time;

    private String type;

    private String gridid;

    private String gridzrr;

    private String zrrphone;

    private String filepath;

    private String filename;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
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

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath == null ? null : filepath.trim();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
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
        parcel.writeString(type);
        parcel.writeString(gridid);
        parcel.writeString(gridzrr);
        parcel.writeString(zrrphone);
        parcel.writeString(filepath);
        parcel.writeString(filename);
        parcel.writeString(bz);
    }

    public static final Parcelable.Creator<Roupidouzhi> CREATOR = new Parcelable.Creator<Roupidouzhi>() {
        public Roupidouzhi createFromParcel(Parcel in) {
            return new Roupidouzhi(in);
        }

        public Roupidouzhi[] newArray(int size) {
            return new Roupidouzhi[size];
        }
    };

    private Roupidouzhi(Parcel in) {
        id = in.readInt();
        name = in.readString();
        jyz = in.readString();
        address = in.readString();
        danwei = in.readString();
        time = in.readString();
        type = in.readString();
        gridid = in.readString();
        gridzrr = in.readString();
        zrrphone = in.readString();
        filepath = in.readString();
        filename = in.readString();
        bz = in.readString();
    }
}