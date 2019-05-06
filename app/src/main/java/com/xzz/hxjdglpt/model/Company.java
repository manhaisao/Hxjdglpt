package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * 
 *企业信息
 */
public class Company implements Parcelable {
    private long id;
    private String name;// 企业名称
    private String address;// 经营地址 //企业地址
    private String yyzzh;// 营业执照号
    private int pNum;// 职工数
    String num;
    private String area;// 经营范围 ①工业 ②商业 ③交通运输业④餐饮娱乐业 ⑤建筑业 ⑥其它
    private String linkman;// 法人//法定代表人
    private String linkmanPhone;// 联系电话
    private String bz;// 备注
    private String gridId;// 网格号
    private String picpath;// 执照
    private String type;// 企业类别 //企业种类
    private String zczb;// 注册资本
    private String zcsj;// 注册时间or成立日期
    private String comtype;// 列统企业 01 民营企业02集体企业03总部企业04
    private String kpqk;// 开票情况
    private String gongzi;// 人均工资
    private String isnas;// 是否纳税
    private String nsqk;// 纳税情况
    private String aqzrr;// 安全责任人
    private String aqzrrPhone;// 安全责任人电话
    private String aqy;// 安全员
    private String aqyPhone;// 安全员电话
    //增加字段
    private String xinyong;
    //属地单位
    private String danwie;
    //网格责任人
    private String gridzrr;
    private String gridzrrdh;// 网格责任人电话
    private String xfqnum;

    private String mhqnum;

    private String qingk;

    public String getXfqnum() {
        return xfqnum;
    }

    public void setXfqnum(String xfqnum) {
        this.xfqnum = xfqnum;
    }

    public String getMhqnum() {
        return mhqnum;
    }

    public void setMhqnum(String mhqnum) {
        this.mhqnum = mhqnum;
    }

    public String getQingk() {
        return qingk;
    }

    public void setQingk(String qingk) {
        this.qingk = qingk;
    }

    public String getGridzrrdh() {
        return gridzrrdh;
    }

    public void setGridzrrdh(String gridzrrdh) {
        this.gridzrrdh = gridzrrdh;
    }

    public String getDanwie() {
        return danwie;
    }

    public void setDanwei(String danwei) {
        this.danwie = danwei;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getXinyong() {
        return xinyong;
    }

    public void setXinyong(String xinyong) {
        this.xinyong = xinyong;
    }


    public String getGridzrr() {
        return gridzrr;
    }

    public void setGridzrr(String gridzrr) {
        this.gridzrr = gridzrr;
    }

    public String getKpqk() {
        return kpqk;
    }

    public void setKpqk(String kpqk) {
        this.kpqk = kpqk;
    }

    public String getGongzi() {
        return gongzi;
    }


    public void setGongzi(String gongzi) {
        this.gongzi = gongzi;
    }

    public String getIsnas() {
        return isnas;
    }

    public void setIsnas(String isnas) {
        this.isnas = isnas;
    }

    public String getNsqk() {
        return nsqk;
    }

    public void setNsqk(String nsqk) {
        this.nsqk = nsqk;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZczb() {
        return zczb;
    }

    public void setZczb(String zczb) {
        this.zczb = zczb;
    }

    public String getZcsj() {
        return zcsj;
    }

    public void setZcsj(String zcsj) {
        this.zcsj = zcsj;
    }

    public String getComtype() {
        return comtype;
    }

    public void setComtype(String comtype) {
        this.comtype = comtype;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public Company(long id) {
        super();
        this.id = id;
    }

    public Company(long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Company() {
        super();
    }

    public Company(String name) {
        super();
        this.name = name;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getYyzzh() {
        if (yyzzh == null) {
            return "";
        }
        return yyzzh;
    }

    public void setYyzzh(String yyzzh) {
        this.yyzzh = yyzzh;
    }

    public int getpNum() {
        return pNum;
    }

    public void setpNum(int pNum) {
        this.pNum = pNum;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkmanPhone() {
        return linkmanPhone;
    }

    public void setLinkmanPhone(String linkmanPhone) {
        this.linkmanPhone = linkmanPhone;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getAqzrr() {
        return aqzrr;
    }

    public void setAqzrr(String aqzrr) {
        this.aqzrr = aqzrr;
    }

    public String getAqzrrPhone() {
        return aqzrrPhone;
    }

    public void setAqzrrPhone(String aqzrrPhone) {
        this.aqzrrPhone = aqzrrPhone;
    }

    public String getAqy() {
        return aqy;
    }

    public void setAqy(String aqy) {
        this.aqy = aqy;
    }

    public String getAqyPhone() {
        return aqyPhone;
    }

    public void setAqyPhone(String aqyPhone) {
        this.aqyPhone = aqyPhone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(yyzzh);
        parcel.writeInt(pNum);
        parcel.writeString(area);
        parcel.writeString(linkman);
        parcel.writeString(linkmanPhone);
        parcel.writeString(bz);
        parcel.writeString(gridId);
        parcel.writeString(picpath);
        parcel.writeString(type);
        parcel.writeString(zczb);
        parcel.writeString(zcsj);
        parcel.writeString(comtype);
        parcel.writeString(kpqk);
        parcel.writeString(gongzi);
        parcel.writeString(isnas);
        parcel.writeString(nsqk);
        parcel.writeString(aqzrr);
        parcel.writeString(aqzrrPhone);
        parcel.writeString(aqy);
        parcel.writeString(aqyPhone);
        parcel.writeString(xinyong);
        parcel.writeString(danwie);
        parcel.writeString(gridzrr);
        parcel.writeString(gridzrrdh);
        parcel.writeString(xfqnum);
        parcel.writeString(mhqnum);
        parcel.writeString(qingk);
    }

    public static final Parcelable.Creator<Company> CREATOR = new Parcelable.Creator<Company>() {
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    private Company(Parcel in) {
        id = in.readLong();
        name = in.readString();
        address = in.readString();
        yyzzh = in.readString();
        pNum = in.readInt();
        area = in.readString();
        linkman = in.readString();
        linkmanPhone = in.readString();
        bz = in.readString();
        gridId = in.readString();
        picpath = in.readString();
        type = in.readString();
        zczb = in.readString();
        zcsj = in.readString();
        comtype = in.readString();
        kpqk = in.readString();
        gongzi = in.readString();
        isnas = in.readString();
        nsqk = in.readString();
        aqzrr = in.readString();
        aqzrrPhone = in.readString();
        aqy = in.readString();
        aqyPhone = in.readString();
        xinyong = in.readString();
        danwie = in.readString();
        gridzrr = in.readString();
        gridzrrdh = in.readString();
        xfqnum = in.readString();
        mhqnum = in.readString();
        qingk = in.readString();
    }
}
