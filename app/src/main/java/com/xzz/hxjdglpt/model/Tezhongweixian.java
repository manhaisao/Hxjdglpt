package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Tezhongweixian implements Parcelable {
    private Integer id;
    private String name;
    private String type;
    private String address;
    private String danwei;
    private String fr;
    private String frphone;
    private String aq;
    private String aqphone;
    private String gridid;
    private String gridzrr;
    private String gridzrrphone;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
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

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr == null ? null : fr.trim();
    }

    public String getFrphone() {
        return frphone;
    }

    public void setFrphone(String frphone) {
        this.frphone = frphone == null ? null : frphone.trim();
    }

    public String getAq() {
        return aq;
    }

    public void setAq(String aq) {
        this.aq = aq == null ? null : aq.trim();
    }

    public String getAqphone() {
        return aqphone;
    }

    public void setAqphone(String aqphone) {
        this.aqphone = aqphone == null ? null : aqphone.trim();
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

    public String getGridzrrphone() {
        return gridzrrphone;
    }

    public void setGridzrrphone(String gridzrrphone) {
        this.gridzrrphone = gridzrrphone == null ? null : gridzrrphone.trim();
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
        parcel.writeString(fr);
        parcel.writeString(address);
        parcel.writeString(danwei);
        parcel.writeString(frphone);
        parcel.writeString(type);
        parcel.writeString(gridid);
        parcel.writeString(gridzrr);
        parcel.writeString(gridzrrphone);
        parcel.writeString(filepath);
        parcel.writeString(filename);
        parcel.writeString(bz);
        parcel.writeString(aq);
        parcel.writeString(aqphone);
    }

    public static final Parcelable.Creator<Tezhongweixian> CREATOR = new Parcelable.Creator<Tezhongweixian>() {
        public Tezhongweixian createFromParcel(Parcel in) {
            return new Tezhongweixian(in);
        }

        public Tezhongweixian[] newArray(int size) {
            return new Tezhongweixian[size];
        }
    };

    private Tezhongweixian(Parcel in) {
        id = in.readInt();
        name = in.readString();
        fr = in.readString();
        address = in.readString();
        danwei = in.readString();
        frphone = in.readString();
        type = in.readString();
        gridid = in.readString();
        gridzrr = in.readString();
        gridzrrphone = in.readString();
        filepath = in.readString();
        filename = in.readString();
        bz = in.readString();
        aq = in.readString();
        aqphone = in.readString();
    }
}