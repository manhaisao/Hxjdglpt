package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Haoren implements Parcelable {
    private Integer id;

    private String name;

    private String cunju;//	所属党支部

    private String shiji;

    private String filepath;

    private String filename;

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

    public String getCunju() {
        return cunju;
    }

    public void setCunju(String cunju) {
        this.cunju = cunju == null ? null : cunju.trim();
    }

    public String getShiji() {
        return shiji;
    }

    public void setShiji(String shiji) {
        this.shiji = shiji == null ? null : shiji.trim();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(cunju);
        parcel.writeString(shiji);
        parcel.writeString(filepath);
        parcel.writeString(filename);
    }

    public static final Parcelable.Creator<Haoren> CREATOR = new Parcelable.Creator<Haoren>() {
        public Haoren createFromParcel(Parcel in) {
            return new Haoren(in);
        }

        public Haoren[] newArray(int size) {
            return new Haoren[size];
        }
    };

    private Haoren(Parcel in) {
        id = in.readInt();
        name = in.readString();
        cunju = in.readString();
        shiji = in.readString();
        filepath = in.readString();
        filename = in.readString();
    }
}