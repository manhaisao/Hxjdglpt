package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Zdry implements Parcelable {

    private long id;

    private String name;

    private String sfzh;

    private String phone;

    private String address;

    private String question;

    private String bald;

    private String zrr;

    private String bald_zw;

    private String bald_phone;

    private String zrr_zw;

    private String zrr_phone;

    private String bar;

    private String bar_zw;

    private String bar_phone;

    private String mj;

    private String mj_zw;

    private String mj_phone;

    private String bz;

    private String gridId;

    private String jdhf_filename;// 街道回复文件名称

    private String jdhf_path;// 街道回复路径

    private String qjhf_filename;// 区级回复文件名称

    private String qjhf_path;// 区级回复路径

    private String sjhf_filename;// 市级回复文件名称

    private String sjhf_path;// 市级回复路径

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getBald() {
        return bald;
    }

    public void setBald(String bald) {
        this.bald = bald;
    }

    public String getZrr() {
        return zrr;
    }

    public void setZrr(String zrr) {
        this.zrr = zrr;
    }

    public String getBald_zw() {
        return bald_zw;
    }

    public void setBald_zw(String bald_zw) {
        this.bald_zw = bald_zw;
    }

    public String getBald_phone() {
        return bald_phone;
    }

    public void setBald_phone(String bald_phone) {
        this.bald_phone = bald_phone;
    }

    public String getZrr_zw() {
        return zrr_zw;
    }

    public void setZrr_zw(String zrr_zw) {
        this.zrr_zw = zrr_zw;
    }

    public String getZrr_phone() {
        return zrr_phone;
    }

    public void setZrr_phone(String zrr_phone) {
        this.zrr_phone = zrr_phone;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    public String getBar_zw() {
        return bar_zw;
    }

    public void setBar_zw(String bar_zw) {
        this.bar_zw = bar_zw;
    }

    public String getBar_phone() {
        return bar_phone;
    }

    public void setBar_phone(String bar_phone) {
        this.bar_phone = bar_phone;
    }

    public String getMj() {
        return mj;
    }

    public void setMj(String mj) {
        this.mj = mj;
    }

    public String getMj_zw() {
        return mj_zw;
    }

    public void setMj_zw(String mj_zw) {
        this.mj_zw = mj_zw;
    }

    public String getMj_phone() {
        return mj_phone;
    }

    public void setMj_phone(String mj_phone) {
        this.mj_phone = mj_phone;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getJdhf_filename() {
        return jdhf_filename;
    }

    public void setJdhf_filename(String jdhf_filename) {
        this.jdhf_filename = jdhf_filename;
    }

    public String getJdhf_path() {
        return jdhf_path;
    }

    public void setJdhf_path(String jdhf_path) {
        this.jdhf_path = jdhf_path;
    }

    public String getQjhf_filename() {
        return qjhf_filename;
    }

    public void setQjhf_filename(String qjhf_filename) {
        this.qjhf_filename = qjhf_filename;
    }

    public String getQjhf_path() {
        return qjhf_path;
    }

    public void setQjhf_path(String qjhf_path) {
        this.qjhf_path = qjhf_path;
    }

    public String getSjhf_filename() {
        return sjhf_filename;
    }

    public void setSjhf_filename(String sjhf_filename) {
        this.sjhf_filename = sjhf_filename;
    }

    public String getSjhf_path() {
        return sjhf_path;
    }

    public void setSjhf_path(String sjhf_path) {
        this.sjhf_path = sjhf_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(sfzh);
        parcel.writeString(address);
        parcel.writeString(question);
        parcel.writeString(bald);
        parcel.writeString(zrr);
        parcel.writeString(bald_zw);
        parcel.writeString(bald_phone);
        parcel.writeString(zrr_zw);
        parcel.writeString(zrr_phone);
        parcel.writeString(bar);
        parcel.writeString(bar_zw);
        parcel.writeString(bar_phone);
        parcel.writeString(mj);
        parcel.writeString(mj_zw);
        parcel.writeString(mj_phone);
        parcel.writeString(gridId);
        parcel.writeString(bz);
        parcel.writeString(jdhf_filename);
        parcel.writeString(jdhf_path);
        parcel.writeString(qjhf_filename);
        parcel.writeString(qjhf_path);
        parcel.writeString(sjhf_filename);
        parcel.writeString(sjhf_path);
    }

    public static final Creator<Zdry> CREATOR = new Creator<Zdry>() {
        public Zdry createFromParcel(Parcel in) {
            return new Zdry(in);
        }

        public Zdry[] newArray(int size) {
            return new Zdry[size];
        }
    };

    private Zdry(Parcel in) {
        id = in.readLong();
        name = in.readString();
        phone = in.readString();
        sfzh = in.readString();
        address = in.readString();
        question = in.readString();
        bald = in.readString();
        zrr = in.readString();
        bald_zw = in.readString();
        bald_phone = in.readString();
        zrr_zw = in.readString();
        zrr_phone = in.readString();
        bar = in.readString();
        bar_zw = in.readString();
        bar_phone = in.readString();
        mj = in.readString();
        mj_zw = in.readString();
        mj_phone = in.readString();
        gridId = in.readString();
        bz = in.readString();
        jdhf_filename = in.readString();
        jdhf_path = in.readString();
        qjhf_filename = in.readString();
        qjhf_path = in.readString();
        sjhf_filename = in.readString();
        sjhf_path = in.readString();
    }

}
