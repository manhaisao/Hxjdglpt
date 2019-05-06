package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CityInfo implements Parcelable {
    private Integer id;

    private String gridid;

    private String daolu;

    private String type;

    private String name;

    private String daiyu;

    private String phone;
    private String bz;
    
    public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGridid() {
        return gridid;
    }

    public void setGridid(String gridid) {
        this.gridid = gridid == null ? null : gridid.trim();
    }

    public String getDaolu() {
        return daolu;
    }

    public void setDaolu(String daolu) {
        this.daolu = daolu == null ? null : daolu.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDaiyu() {
        return daiyu;
    }

    public void setDaiyu(String daiyu) {
        this.daiyu = daiyu == null ? null : daiyu.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(id);
        parcel.writeString(gridid);
        parcel.writeString(daolu);
        parcel.writeString(type);
        parcel.writeString(name);
        parcel.writeString(daiyu);
        parcel.writeString(phone);
        parcel.writeString(bz);
    }

    public static final Parcelable.Creator<CityInfo> CREATOR = new Parcelable.Creator<CityInfo>() {
        public CityInfo createFromParcel(Parcel in) {
            return new CityInfo(in);
        }

        public CityInfo[] newArray(int size) {
            return new CityInfo[size];
        }
    };

    private CityInfo(Parcel in) {
        id = in.readInt();
        gridid = in.readString();
        daolu = in.readString();
        type = in.readString();
        name = in.readString();
        daiyu = in.readString();
        phone = in.readString();
        bz = in.readString();
    }
}