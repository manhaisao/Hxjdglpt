package com.xzz.hxjdglpt.model;

public class Mqztcgl {

	private long glid;

	private String wt;// 問題

	private long wtr;// 問問題人

	private String wtsj;// 問問題時間

	private String wtaddreess;// 問問題地點

	private long pid;// 外鍵

	private String wtrname;// 问问题人姓名
	
	private int type;//类型区分1：回答人2：回复人


	public Mqztcgl(long pid) {
		super();
		this.pid = pid;
	}


	public Mqztcgl() {
		super();
	}

	public String getWtrname() {
		return wtrname;
	}

	public void setWtrname(String wtrname) {
		this.wtrname = wtrname;
	}

	public long getGlid() {
		return glid;
	}

	public void setGlid(long glid) {
		this.glid = glid;
	}

	public String getWt() {
		return wt;
	}

	public void setWt(String wt) {
		this.wt = wt;
	}


	public long getWtr() {
		return wtr;
	}

	public void setWtr(long wtr) {
		this.wtr = wtr;
	}

	public String getWtsj() {
		return wtsj;
	}

	public void setWtsj(String wtsj) {
		this.wtsj = wtsj;
	}

	public String getWtaddreess() {
		return wtaddreess;
	}

	public void setWtaddreess(String wtaddreess) {
		this.wtaddreess = wtaddreess;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
