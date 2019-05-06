package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Shjy implements Parcelable {
    private Integer id;

    private String sfz;

    private String cunju;

    private String dengji;

    private String bz;

    private String name;
    
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSfz() {
        return sfz;
    }

    public void setSfz(String sfz) {
        this.sfz = sfz == null ? null : sfz.trim();
    }

    public String getCunju() {
        return cunju;
    }

    public void setCunju(String cunju) {
        this.cunju = cunju == null ? null : cunju.trim();
    }

    public String getDengji() {
        return dengji;
    }

    public void setDengji(String dengji) {
        this.dengji = dengji == null ? null : dengji.trim();
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
        parcel.writeString(cunju);
        parcel.writeString(bz);
        parcel.writeString(dengji);
        parcel.writeString(sfz);
    }

    public static final Parcelable.Creator<Shjy> CREATOR = new Parcelable.Creator<Shjy>() {
        public Shjy createFromParcel(Parcel in) {
            return new Shjy(in);
        }

        public Shjy[] newArray(int size) {
            return new Shjy[size];
        }
    };

    private Shjy(Parcel in) {
        id = in.readInt();
        name = in.readString();
        cunju = in.readString();
        bz = in.readString();
        dengji = in.readString();
        sfz = in.readString();
    }
}