package com.xzz.hxjdglpt.model;

public class CityManage {
	private Integer id;

	private String xmzl;

	private String gridid;

	private String zrr;

	private String zrrphone;

	private String ghr;

	private String ghrphone;

	private String bjy;

	private String bjyphone;

	private String bjygz;

	private String bjtime;

	private String ljc;

	private String ljt;

	private Integer ljclear;

	private String ljnum;

	private String othersb;

	private String mark;

	private String gname;

	private int hjbxs;

	private int xqwzswls;

	private String userId;

	private String picpath;

	private String daolu;// 道路

	private String hetang;// 河塘条数

	private String liudong;// 流动保洁员

	private String area;

	private String hs;

	private String rk;

	private String wgz;

	private String wgzphone;

	private String ljaddress;

	private String otherinfo;

	private String bz;
	private String jdphone;

	private String xqzs;//小区总数

	private String  ld;//道路

	private String bjxx;//背街小巷

	private String bjcl;//保洁车辆

	private String ba;//保安


	private String jhgb; //结合干部

	private String jhgbphone; // 联系电话


	private String jdwgy;  //街道网络员

	private String jdwgyphone;

	private String cjwgyphone;

	private String cjwgy; //村居网络员


	private int wgqyNum;

	public int getWgqyNum() {
		return wgqyNum;
	}

	public void setWgqyNum(int wgqyNum) {
		this.wgqyNum = wgqyNum;
	}

	public String getJdwgyphone() {
		return jdwgyphone;
	}

	public void setJdwgyphone(String jdwgyphone) {
		this.jdwgyphone = jdwgyphone;
	}

	public String getCjwgyphone() {
		return cjwgyphone;
	}

	public void setCjwgyphone(String cjwgyphone) {
		this.cjwgyphone = cjwgyphone;
	}

	public String getJhgb() {
		return jhgb;
	}

	public void setJhgb(String jhgb) {
		this.jhgb = jhgb;
	}

	public String getJhgbphone() {
		return jhgbphone;
	}

	public void setJhgbphone(String jhgbphone) {
		this.jhgbphone = jhgbphone;
	}

	public String getJdwgy() {
		return jdwgy;
	}

	public void setJdwgy(String jdwgy) {
		this.jdwgy = jdwgy;
	}

	public String getCjwgy() {
		return cjwgy;
	}

	public void setCjwgy(String cjwgy) {
		this.cjwgy = cjwgy;
	}

	public String getBjcl() {
		return bjcl;
	}

	public void setBjcl(String bjcl) {
		this.bjcl = bjcl;
	}

	public String getBa() {
		return ba;
	}

	public void setBa(String ba) {
		this.ba = ba;
	}

	public String getBjxx() {
		return bjxx;
	}

	public void setBjxx(String bjxx) {
		this.bjxx = bjxx;
	}

	public String getLd() {
		return ld;
	}

	public void setLd(String ld) {
		this.ld = ld;
	}

	public String getXqzs() {
		return xqzs;
	}

	public void setXqzs(String xqzs) {
		this.xqzs = xqzs;
	}

	public String getJdphone() {
		return jdphone;
	}

	public void setJdphone(String jdphone) {
		this.jdphone = jdphone;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getHs() {
		return hs;
	}

	public void setHs(String hs) {
		this.hs = hs;
	}

	public String getRk() {
		return rk;
	}

	public void setRk(String rk) {
		this.rk = rk;
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

	public String getLjaddress() {
		return ljaddress;
	}

	public void setLjaddress(String ljaddress) {
		this.ljaddress = ljaddress;
	}

	public String getOtherinfo() {
		return otherinfo;
	}

	public void setOtherinfo(String otherinfo) {
		this.otherinfo = otherinfo;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getLiudong() {
		return liudong;
	}

	public void setLiudong(String liudong) {
		this.liudong = liudong;
	}

	public String getDaolu() {
		return daolu;
	}

	public void setDaolu(String daolu) {
		this.daolu = daolu;
	}

	public String getHetang() {
		return hetang;
	}

	public void setHetang(String hetang) {
		this.hetang = hetang;
	}

	public String getPicpath() {
		return picpath;
	}

	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}

	public CityManage() {

		super();
	}

	public CityManage(String xmzl, String zrr) {
		super();
		this.xmzl = xmzl;
		this.zrr = zrr;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getXmzl() {
		return xmzl;
	}

	public void setXmzl(String xmzl) {
		this.xmzl = xmzl == null ? null : xmzl.trim();
	}

	public String getGridid() {
		return gridid;
	}

	public void setGridid(String gridid) {
		this.gridid = gridid;
	}

	public String getZrr() {
		return zrr;
	}

	public void setZrr(String zrr) {
		this.zrr = zrr == null ? null : zrr.trim();
	}

	public String getZrrphone() {
		return zrrphone;
	}

	public void setZrrphone(String zrrphone) {
		this.zrrphone = zrrphone == null ? null : zrrphone.trim();
	}

	public String getGhr() {
		return ghr;
	}

	public void setGhr(String ghr) {
		this.ghr = ghr == null ? null : ghr.trim();
	}

	public String getGhrphone() {
		return ghrphone;
	}

	public void setGhrphone(String ghrphone) {
		this.ghrphone = ghrphone == null ? null : ghrphone.trim();
	}

	public String getBjy() {
		return bjy;
	}

	public void setBjy(String bjy) {
		this.bjy = bjy == null ? null : bjy.trim();
	}

	public String getBjyphone() {
		return bjyphone;
	}

	public void setBjyphone(String bjyphone) {
		this.bjyphone = bjyphone == null ? null : bjyphone.trim();
	}

	public String getBjygz() {
		return bjygz;
	}

	public void setBjygz(String bjygz) {
		this.bjygz = bjygz == null ? null : bjygz.trim();
	}

	public String getBjtime() {
		return bjtime;
	}

	public void setBjtime(String bjtime) {
		this.bjtime = bjtime == null ? null : bjtime.trim();
	}

	public String getLjc() {
		return ljc;
	}

	public void setLjc(String ljc) {
		this.ljc = ljc == null ? null : ljc.trim();
	}

	public String getLjt() {
		return ljt;
	}

	public void setLjt(String ljt) {
		this.ljt = ljt == null ? null : ljt.trim();
	}

	public Integer getLjclear() {
		return ljclear;
	}

	public void setLjclear(Integer ljclear) {
		this.ljclear = ljclear;
	}

	public String getLjnum() {
		return ljnum;
	}

	public void setLjnum(String ljnum) {
		this.ljnum = ljnum;
	}

	public String getOthersb() {
		return othersb;
	}

	public void setOthersb(String othersb) {
		this.othersb = othersb == null ? null : othersb.trim();
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark == null ? null : mark.trim();
	}

	public int getHjbxs() {
		return hjbxs;
	}

	public void setHjbxs(int hjbxs) {
		this.hjbxs = hjbxs;
	}

	public int getXqwzswls() {
		return xqwzswls;
	}

	public void setXqwzswls(int xqwzswls) {
		this.xqwzswls = xqwzswls;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

}