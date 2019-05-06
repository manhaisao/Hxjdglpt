package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * 小区信息
 * 
 */
public class Plot implements Parcelable {
    private String id;
    private String gridId;
    private String name;
    private String area;
    private int pNum;// 楼房幢数
    private int hNum;// 总户数
    private int wNum;// 物管
    private int bNum;// 保洁人数
    private String pMan;// 监督人
    private String pManPhone;// 监督人
    private String vMan;// 小区负责人
    private String vManPhone;// 监督人
    private String picpath;
    private String wygs;// 物业公司

    private String wyphone;// 物业公司联系方式

    private String wyzjl;// 物业总经理

    private String wyzjlphone;// 物业总经理电话

    private String wyfzr;// 物业负责人

    private String wyfzrphone;// 物业负责人电话

    private String bz;
    private int baNum;
    private String zrrid;

    private String  jzmj; //建筑面积

    private String wgz; //网格长

    private String wgzphone;
    /**
     * 街道网格员
     */
    private String jdwgy;
    /**
     * 街道网格员电话
     */
    private String jdwagphone;
    /**
     * 村居网格员
     */
    private String cjwgy;
    /**
     * 村居网格员电话
     */
    private String cjwgyphone;

    /**
     * 经营户数
     */
    private int jyhNum;

    /**
     * 物业法人
     */
    private String wyfr;

    /**
     * 单元数
     */
    private int dyNum;


    public String getWyfr() {
        return wyfr;
    }

    public void setWyfr(String wyfr) {
        this.wyfr = wyfr;
    }

    public int getDyNum() {
        return dyNum;
    }

    public void setDyNum(int dyNum) {
        this.dyNum = dyNum;
    }

    public String getJzmj() {
        return jzmj;
    }

    public void setJzmj(String jzmj) {
        this.jzmj = jzmj;
    }

    public String getWgz() {
        return wgz;
    }

    public void setWgz(String wgz) {
        this.wgz = wgz;
    }

    public String getWgzphone() {
        return wgzphone;
    }

    public void setWgzphone(String wgzphone) {
        this.wgzphone = wgzphone;
    }

    public String getJdwgy() {
        return jdwgy;
    }

    public void setJdwgy(String jdwgy) {
        this.jdwgy = jdwgy;
    }

    public String getJdwagphone() {
        return jdwagphone;
    }

    public void setJdwagphone(String jdwagphone) {
        this.jdwagphone = jdwagphone;
    }

    public String getCjwgy() {
        return cjwgy;
    }

    public void setCjwgy(String cjwgy) {
        this.cjwgy = cjwgy;
    }

    public String getCjwgyphone() {
        return cjwgyphone;
    }

    public void setCjwgyphone(String cjwgyphone) {
        this.cjwgyphone = cjwgyphone;
    }

    public int getJyhNum() {
        return jyhNum;
    }

    public void setJyhNum(int jyhNum) {
        this.jyhNum = jyhNum;
    }

    public String getZrrid() {
        return zrrid;
    }

    public void setZrrid(String zrrid) {
        this.zrrid = zrrid;
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

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getpNum() {
        return pNum;
    }

    public void setpNum(int pNum) {
        this.pNum = pNum;
    }

    public int gethNum() {
        return hNum;
    }

    public void sethNum(int hNum) {
        this.hNum = hNum;
    }

    public int getwNum() {
        return wNum;
    }

    public void setwNum(int wNum) {
        this.wNum = wNum;
    }

    public int getbNum() {
        return bNum;
    }

    public void setbNum(int bNum) {
        this.bNum = bNum;
    }

    public String getpMan() {
        return pMan;
    }

    public void setpMan(String pMan) {
        this.pMan = pMan;
    }

    public String getpManPhone() {
        return pManPhone;
    }

    public void setpManPhone(String pManPhone) {
        this.pManPhone = pManPhone;
    }

    public String getvMan() {
        return vMan;
    }

    public void setvMan(String vMan) {
        this.vMan = vMan;
    }

    public String getvManPhone() {
        return vManPhone;
    }

    public void setvManPhone(String vManPhone) {
        this.vManPhone = vManPhone;
    }

    public String getWygs() {
        return wygs;
    }

    public void setWygs(String wygs) {
        this.wygs = wygs;
    }

    public String getWyphone() {
        return wyphone;
    }

    public void setWyphone(String wyphone) {
        this.wyphone = wyphone;
    }

    public String getWyzjl() {
        return wyzjl;
    }

    public void setWyzjl(String wyzjl) {
        this.wyzjl = wyzjl;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getWyzjlphone() {
        return wyzjlphone;
    }

    public void setWyzjlphone(String wyzjlphone) {
        this.wyzjlphone = wyzjlphone;
    }

    public String getWyfzr() {
        return wyfzr;
    }

    public void setWyfzr(String wyfzr) {
        this.wyfzr = wyfzr;
    }

    public String getWyfzrphone() {
        return wyfzrphone;
    }

    public void setWyfzrphone(String wyfzrphone) {
        this.wyfzrphone = wyfzrphone;
    }

    public int getBaNum() {
        return baNum;
    }

    public void setBaNum(int baNum) {
        this.baNum = baNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(gridId);
        parcel.writeString(name);
        parcel.writeString(area);
        parcel.writeInt(pNum);
        parcel.writeInt(hNum);
        parcel.writeInt(wNum);
        parcel.writeInt(bNum);
        parcel.writeString(pMan);
        parcel.writeString(pManPhone);
        parcel.writeString(vMan);
        parcel.writeString(vManPhone);
        parcel.writeString(picpath);
        parcel.writeString(wygs);
        parcel.writeString(wyphone);
        parcel.writeString(wyzjl);
        parcel.writeString(bz);
        parcel.writeString(wyzjlphone);
        parcel.writeString(wyfzr);
        parcel.writeString(wyfzrphone);
        parcel.writeInt(baNum);
        parcel.writeString(zrrid);
        parcel.writeString(jzmj);
        parcel.writeString(wgz);
        parcel.writeString(wgzphone);
        parcel.writeString(jdwgy);
        parcel.writeString(jdwagphone);
        parcel.writeString(cjwgy);
        parcel.writeString(cjwgyphone);

        parcel.writeInt(jyhNum);
        parcel.writeString(wyfr);
        parcel.writeInt(dyNum);


    }

    public static final Parcelable.Creator<Plot> CREATOR = new Parcelable.Creator<Plot>() {
        public Plot createFromParcel(Parcel in) {
            return new Plot(in);
        }

        public Plot[] newArray(int size) {
            return new Plot[size];
        }
    };

    private Plot(Parcel in) {
        id = in.readString();
        gridId = in.readString();
        name = in.readString();
        area = in.readString();
        pNum = in.readInt();
        hNum = in.readInt();
        wNum = in.readInt();
        bNum = in.readInt();
        pMan = in.readString();
        pManPhone = in.readString();
        vMan = in.readString();
        vManPhone = in.readString();
        picpath = in.readString();
        wygs = in.readString();
        wyphone = in.readString();
        wyzjl = in.readString();
        bz = in.readString();
        wyzjlphone = in.readString();
        wyfzr = in.readString();
        wyfzrphone = in.readString();
        baNum = in.readInt();
        zrrid = in.readString();
        jzmj=in.readString();
        wgz=in.readString();
        wgzphone=in.readString();
        jdwgy=in.readString();
        jdwagphone=in.readString();
        cjwgy=in.readString();
        cjwgyphone=in.readString();
        jyhNum = in.readInt();
        wyfr=in.readString();
        dyNum= in.readInt();

    }

}
