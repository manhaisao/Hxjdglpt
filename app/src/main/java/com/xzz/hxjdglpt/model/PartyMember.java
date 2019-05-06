package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 党员信息
 * Created by dbz on 2017/6/29.
 */

public class PartyMember implements Parcelable {
    private String id;

    private String name;

    private String stime;

    private String depart;

    private String phone;

    private String xl;

    private String bz;

    private String gridId;
    private String no;// 身份证

    private int bcNum;//本村数

    private int ghNum;//挂户数

    private int jlyNum;//社会养老数

    private String userId;//用户

    private String picpath;

    private String type; //区分 类别区分 党员 困难党员/01/02'

    private String xb;// 性别

    private String mz;// 民族

    private String gzdwjzw;// 工作单位及职务

    private String birth;// 出生日期

    private String isliuru;// 流入流出
    private String reason;//困难原因

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIsliuru() {
        return isliuru;
    }

    public void setIsliuru(String isliuru) {
        this.isliuru = isliuru;
    }


    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart == null ? null : depart.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getXl() {
        return xl;
    }

    public void setXl(String xl) {
        this.xl = xl == null ? null : xl.trim();
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz == null ? null : bz.trim();
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId == null ? null : gridId.trim();
    }

    public int getBcNum() {
        return bcNum;
    }

    public void setBcNum(int bcNum) {
        this.bcNum = bcNum;
    }

    public int getGhNum() {
        return ghNum;
    }

    public void setGhNum(int ghNum) {
        this.ghNum = ghNum;
    }

    public int getJlyNum() {
        return jlyNum;
    }

    public void setJlyNum(int jlyNum) {
        this.jlyNum = jlyNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getGzdwjzw() {
        return gzdwjzw;
    }

    public void setGzdwjzw(String gzdwjzw) {
        this.gzdwjzw = gzdwjzw;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(stime);
        parcel.writeString(depart);
        parcel.writeString(phone);
        parcel.writeString(xl);
        parcel.writeString(bz);
        parcel.writeString(gridId);
        parcel.writeInt(bcNum);
        parcel.writeInt(ghNum);
        parcel.writeString(userId);
        parcel.writeString(picpath);
        parcel.writeString(type);
        parcel.writeString(xb);
        parcel.writeString(mz);
        parcel.writeString(gzdwjzw);
        parcel.writeString(birth);
        parcel.writeString(no);
        parcel.writeString(isliuru);
        parcel.writeString(reason);
    }

    public static final Parcelable.Creator<PartyMember> CREATOR = new Parcelable
            .Creator<PartyMember>() {
        public PartyMember createFromParcel(Parcel in) {
            return new PartyMember(in);
        }

        public PartyMember[] newArray(int size) {
            return new PartyMember[size];
        }
    };

    private PartyMember(Parcel in) {
        id = in.readString();
        name = in.readString();
        stime = in.readString();
        depart = in.readString();
        phone = in.readString();
        xl = in.readString();
        bz = in.readString();
        gridId = in.readString();
        bcNum = in.readInt();
        ghNum = in.readInt();
        userId = in.readString();
        picpath = in.readString();
        type = in.readString();
        xb = in.readString();
        mz = in.readString();
        gzdwjzw = in.readString();
        birth = in.readString();
        no = in.readString();
        isliuru = in.readString();
        reason = in.readString();
    }
}
