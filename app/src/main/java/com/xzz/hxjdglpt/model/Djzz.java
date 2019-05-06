package com.xzz.hxjdglpt.model;

import java.util.ArrayList;
import java.util.List;

public class Djzz {

    private int cjdzz = 0;// 村级党组织数量
//
//    private int zddwdzz = 0;// 驻地单位党组织数量
//
//    private int lxdzz = 0;// 两新党组织数量
//
//    private int shttzz = 0;// 社会团体组织数量
//
//    private int fgqyzz = 0;// 非公企业组织数量

    private long dzbnum = 0;// 黨支部

    private long dxznum = 0;// 黨小組

    private long dynum = 0;// 黨員數量

    private long lddynum = 0;// 流動黨員數量

    private List<Dzz> cDzz = new ArrayList<>();// 村党总支


    private List<Zddw> zDzz = new ArrayList<>();// 驻地单位

    private List<Liangxin> lDzb = new ArrayList<>();// 两新党组织党支部-社会团体

    private List<Liangxin> fDzb = new ArrayList<>();// 两新党组织党支部-非公企业

    private List<LsDzz> lsDzzList = new ArrayList<>();// 临时党组织

    public List<LsDzz> getLsDzzList() {
        return lsDzzList;
    }

    public void setLsDzzList(List<LsDzz> lsDzzList) {
        this.lsDzzList = lsDzzList;
    }

    public long getDzbnum() {
        return dzbnum;
    }

    public void setDzbnum(long dzbnum) {
        this.dzbnum = dzbnum;
    }

    public long getDxznum() {
        return dxznum;
    }

    public void setDxznum(long dxznum) {
        this.dxznum = dxznum;
    }

    public long getDynum() {
        return dynum;
    }

    public void setDynum(long dynum) {
        this.dynum = dynum;
    }

    public long getLddynum() {
        return lddynum;
    }

    public void setLddynum(long lddynum) {
        this.lddynum = lddynum;
    }

    public int getCjdzz() {
        return cjdzz;
    }

    public void setCjdzz(int cjdzz) {
        this.cjdzz = cjdzz;
    }

//    public int getZddwdzz() {
//        return zddwdzz;
//    }
//
//    public void setZddwdzz(int zddwdzz) {
//        this.zddwdzz = zddwdzz;
//    }
//
//    public int getLxdzz() {
//        return lxdzz;
//    }
//
//    public void setLxdzz(int lxdzz) {
//        this.lxdzz = lxdzz;
//    }
//
//    public int getShttzz() {
//        return shttzz;
//    }
//
//    public void setShttzz(int shttzz) {
//        this.shttzz = shttzz;
//    }
//
//    public int getFgqyzz() {
//        return fgqyzz;
//    }
//
//    public void setFgqyzz(int fgqyzz) {
//        this.fgqyzz = fgqyzz;
//    }

    public List<Dzz> getcDzz() {
        return cDzz;
    }

    public void setcDzz(List<Dzz> cDzz) {
        this.cDzz = cDzz;
    }

    // public List<Dzb> getcDzb() {
    // return cDzb;
    // }
    //
    // public void setcDzb(List<Dzb> cDzb) {
    // this.cDzb = cDzb;
    // }
    //
    // public List<Dxz> getcDxz() {
    // return cDxz;
    // }
    //
    // public void setcDxz(List<Dxz> cDxz) {
    // this.cDxz = cDxz;
    // }

    public List<Zddw> getzDzz() {
        return zDzz;
    }

    public void setzDzz(List<Zddw> zDzz) {
        this.zDzz = zDzz;
    }

    // public List<Dzb> getzDzb() {
    // return zDzb;
    // }
    //
    // public void setzDzb(List<Dzb> zDzb) {
    // this.zDzb = zDzb;
    // }
    //
    // public List<Dxz> getzDxz() {
    // return zDxz;
    // }
    //
    // public void setzDxz(List<Dxz> zDxz) {
    // this.zDxz = zDxz;
    // }

    public List<Liangxin> getlDzb() {
        return lDzb;
    }

    public void setlDzb(List<Liangxin> lDzb) {
        this.lDzb = lDzb;
    }

    public List<Liangxin> getfDzb() {
        return fDzb;
    }

    public void setfDzb(List<Liangxin> fDzb) {
        this.fDzb = fDzb;
    }

    // public List<Dxz> getlDxz() {
    // return lDxz;
    // }
    //
    // public void setlDxz(List<Dxz> lDxz) {
    // this.lDxz = lDxz;
    // }

}
