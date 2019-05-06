package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Jzgy implements Parcelable {
	private Integer id;

	private String name;

	private String cunju;

	private String bz;

	private String dengji;

	private String no;

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

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz == null ? null : bz.trim();
	}

	public String getDengji() {
		return dengji;
	}

	public void setDengji(String dengji) {
		this.dengji = dengji == null ? null : dengji.trim();
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no == null ? null : no.trim();
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
		parcel.writeString(no);
	}

	public static final Parcelable.Creator<Jzgy> CREATOR = new Parcelable.Creator<Jzgy>() {
		public Jzgy createFromParcel(Parcel in) {
			return new Jzgy(in);
		}

		public Jzgy[] newArray(int size) {
			return new Jzgy[size];
		}
	};

	private Jzgy(Parcel in) {
		id = in.readInt();
		name = in.readString();
		cunju = in.readString();
		bz = in.readString();
		dengji = in.readString();
		no = in.readString();
	}
}