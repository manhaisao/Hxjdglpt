package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TDbml implements Parcelable {
    private Integer id;

    private String name;

    private Integer age;

    private String sex;

    private String danwei;

    private String zhiwu;

    private String phone;

    private String dbcj;

    private String xq;

    private String sort;

    private String dangpai;

    private String xueli;

    private String createTime;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei == null ? null : danwei.trim();
    }

    public String getZhiwu() {
        return zhiwu;
    }

    public void setZhiwu(String zhiwu) {
        this.zhiwu = zhiwu == null ? null : zhiwu.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getDbcj() {
        return dbcj;
    }

    public void setDbcj(String dbcj) {
        this.dbcj = dbcj == null ? null : dbcj.trim();
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq == null ? null : xq.trim();
    }

    public String getDangpai() {
        return dangpai;
    }

    public void setDangpai(String dangpai) {
        this.dangpai = dangpai;
    }

    public String getXueli() {
        return xueli;
    }

    public void setXueli(String xueli) {
        this.xueli = xueli;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(age == null ? 0 : age);
        parcel.writeString(sex);
        parcel.writeString(danwei);
        parcel.writeString(zhiwu);
        parcel.writeString(phone);
        parcel.writeString(dbcj);
        parcel.writeString(xq);
        parcel.writeString(sort);
        parcel.writeString(createTime);
        parcel.writeString(xueli);
        parcel.writeString(dangpai);
        parcel.writeString(filename);
        parcel.writeString(filepath);
    }

    public static final Parcelable.Creator<TDbml> CREATOR = new Parcelable.Creator<TDbml>() {
        public TDbml createFromParcel(Parcel in) {
            return new TDbml(in);
        }

        public TDbml[] newArray(int size) {
            return new TDbml[size];
        }
    };

    private TDbml(Parcel in) {
        id = in.readInt();
        name = in.readString();
        age = in.readInt();
        sex = in.readString();
        danwei = in.readString();
        zhiwu = in.readString();
        phone = in.readString();
        dbcj = in.readString();
        xq = in.readString();
        sort = in.readString();
        createTime = in.readString();
        xueli = in.readString();
        dangpai = in.readString();
        filename = in.readString();
        filepath = in.readString();
    }
}