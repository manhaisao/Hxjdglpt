package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Dszn implements Parcelable {
	private Long id;

	private String znName;// 子女姓名

	private String sex;// 性别

	private String fName;// 父亲姓名

	private String fSfzh;// 父亲身份证

	private String mName;// 母亲姓名

	private String mSfzh;// 母亲身份证

	private String address;// 地址

	private String phone;// 联系电话

	private String bz;// 备注

	private String gridId;// 网格号

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getZnName() {
		return znName;
	}

	public void setZnName(String znName) {
		this.znName = znName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getfSfzh() {
		return fSfzh;
	}

	public void setfSfzh(String fSfzh) {
		this.fSfzh = fSfzh;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmSfzh() {
		return mSfzh;
	}

	public void setmSfzh(String mSfzh) {
		this.mSfzh = mSfzh;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flag) {
		parcel.writeLong(id);
		parcel.writeString(znName);
		parcel.writeString(sex);
		parcel.writeString(fName);
		parcel.writeString(fSfzh);
		parcel.writeString(mName);
		parcel.writeString(mSfzh);
		parcel.writeString(address);
		parcel.writeString(phone);
		parcel.writeString(bz);
		parcel.writeString(gridId);
	}

	public static final Parcelable.Creator<Dszn> CREATOR = new Parcelable.Creator<Dszn>() {
		public Dszn createFromParcel(Parcel in) {
			return new Dszn(in);
		}

		public Dszn[] newArray(int size) {
			return new Dszn[size];
		}
	};

	private Dszn(Parcel in) {
		id = in.readLong();
		znName = in.readString();
		sex = in.readString();
		fName = in.readString();
		fSfzh = in.readString();
		mName = in.readString();
		mSfzh = in.readString();
		address = in.readString();
		phone = in.readString();
		bz = in.readString();
		gridId = in.readString();
	}
}
