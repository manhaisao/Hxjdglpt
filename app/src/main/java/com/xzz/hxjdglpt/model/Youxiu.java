package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Youxiu  implements Parcelable{
    private Integer id;

    private String name;

    private String sex;

    private String danwie;

    private String dzb;

    private String shiji;

    private String filename;

    private String filepath;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getDanwie() {
        return danwie;
    }

    public void setDanwie(String danwie) {
        this.danwie = danwie == null ? null : danwie.trim();
    }

    public String getDzb() {
        return dzb;
    }

    public void setDzb(String dzb) {
        this.dzb = dzb == null ? null : dzb.trim();
    }

    public String getShiji() {
        return shiji;
    }

    public void setShiji(String shiji) {
        this.shiji = shiji == null ? null : shiji.trim();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(sex);
        parcel.writeString(danwie);
        parcel.writeString(dzb);
        parcel.writeString(shiji);
        parcel.writeString(filepath);
        parcel.writeString(filename);
    }

    public static final Parcelable.Creator<Youxiu> CREATOR = new Parcelable.Creator<Youxiu>() {
        public Youxiu createFromParcel(Parcel in) {
            return new Youxiu(in);
        }

        public Youxiu[] newArray(int size) {
            return new Youxiu[size];
        }
    };

    private Youxiu(Parcel in) {
        id = in.readInt();
        name = in.readString();
        sex = in.readString();
        danwie = in.readString();
        dzb = in.readString();
        shiji = in.readString();
        filepath = in.readString();
        filename = in.readString();
    }
}