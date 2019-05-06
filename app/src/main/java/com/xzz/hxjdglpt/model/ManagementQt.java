package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ManagementQt implements Parcelable {
    private String id;

    private String name;

    private String address;

    private Integer employeenum;

    private String yyzzh;

    private String area;

    private String linkman;

    private String linkmanphone;

    private String bz;

    private String gridid;

    private String picpath;// 经营执照

    private String type;// 类型

    // 建筑工地增加的属性
    private String zrr;
    private String zrrphone;
    private String aqy;
    private String aqyphone;
    private String gridzrr;
    private String gridphone;
    // 增加的字段
    private String shigf;// 施工方
    private String danwie;// 属地单位
    // 法定代表人
    private String faren;
    // 注册资本
    private String zczb;
    // 护税责任人
    private String husui;

    private String yzmc;// 业主名称

    private String farenphone;// 法定代表人电话

    private String husuiphone;// 护税责任人电话

    public String getZrr() {
        return zrr;
    }

    public void setZrr(String zrr) {
        this.zrr = zrr;
    }

    public String getZrrphone() {
        return zrrphone;
    }

    public void setZrrphone(String zrrphone) {
        this.zrrphone = zrrphone;
    }

    public String getAqy() {
        return aqy;
    }

    public void setAqy(String aqy) {
        this.aqy = aqy;
    }

    public String getAqyphone() {
        return aqyphone;
    }

    public void setAqyphone(String aqyphone) {
        this.aqyphone = aqyphone;
    }

    public String getGridzrr() {
        return gridzrr;
    }

    public void setGridzrr(String gridzrr) {
        this.gridzrr = gridzrr;
    }

    public String getGridphone() {
        return gridphone;
    }

    public void setGridphone(String gridphone) {
        this.gridphone = gridphone;
    }

    public String getShigf() {
        return shigf;
    }

    public void setShigf(String shigf) {
        this.shigf = shigf;
    }

    public String getDanwie() {
        return danwie;
    }

    public void setDanwie(String danwie) {
        this.danwie = danwie;
    }

    public String getFaren() {
        return faren;
    }

    public void setFaren(String faren) {
        this.faren = faren;
    }

    public String getZczb() {
        return zczb;
    }

    public void setZczb(String zczb) {
        this.zczb = zczb;
    }

    public String getHusui() {
        return husui;
    }

    public void setHusui(String husui) {
        this.husui = husui;
    }

    public String getYzmc() {
        return yzmc;
    }

    public void setYzmc(String yzmc) {
        this.yzmc = yzmc;
    }

    public String getFarenphone() {
        return farenphone;
    }

    public void setFarenphone(String farenphone) {
        this.farenphone = farenphone;
    }

    public String getHusuiphone() {
        return husuiphone;
    }

    public void setHusuiphone(String husuiphone) {
        this.husuiphone = husuiphone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getEmployeenum() {
        return employeenum;
    }

    public void setEmployeenum(Integer employeenum) {
        this.employeenum = employeenum;
    }

    public String getYyzzh() {
        return yyzzh;
    }

    public void setYyzzh(String yyzzh) {
        this.yyzzh = yyzzh == null ? null : yyzzh.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman == null ? null : linkman.trim();
    }

    public String getLinkmanphone() {
        return linkmanphone;
    }

    public void setLinkmanphone(String linkmanphone) {
        this.linkmanphone = linkmanphone == null ? null : linkmanphone.trim();
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz == null ? null : bz.trim();
    }

    public String getGridid() {
        return gridid;
    }

    public void setGridid(String gridid) {
        this.gridid = gridid == null ? null : gridid.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeInt(employeenum);
        parcel.writeString(yyzzh);
        parcel.writeString(area);
        parcel.writeString(linkman);
        parcel.writeString(linkmanphone);
        parcel.writeString(bz);
        parcel.writeString(gridid);
        parcel.writeString(picpath);
        parcel.writeString(type);

        parcel.writeString(zrr);
        parcel.writeString(zrrphone);
        parcel.writeString(aqy);
        parcel.writeString(aqyphone);
        parcel.writeString(gridzrr);
        parcel.writeString(gridphone);
        parcel.writeString(shigf);
        parcel.writeString(danwie);
        parcel.writeString(faren);
        parcel.writeString(zczb);
        parcel.writeString(husui);
        parcel.writeString(yzmc);
        parcel.writeString(farenphone);
        parcel.writeString(husuiphone);
    }

    public static final Creator<ManagementQt> CREATOR = new Creator<ManagementQt>()

    {
        public ManagementQt createFromParcel(Parcel in) {
            return new ManagementQt(in);
        }

        public ManagementQt[] newArray(int size) {
            return new ManagementQt[size];
        }
    };

    private ManagementQt(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        employeenum = in.readInt();
        yyzzh = in.readString();
        area = in.readString();
        linkman = in.readString();
        linkmanphone = in.readString();
        bz = in.readString();
        gridid = in.readString();
        picpath = in.readString();
        type = in.readString();
        zrr = in.readString();
        zrrphone = in.readString();
        aqy = in.readString();
        aqyphone = in.readString();
        gridzrr = in.readString();
        gridphone = in.readString();
        shigf = in.readString();
        danwie = in.readString();
        faren = in.readString();
        zczb = in.readString();
        husui = in.readString();
        yzmc = in.readString();
        farenphone = in.readString();
        husuiphone = in.readString();
    }
}