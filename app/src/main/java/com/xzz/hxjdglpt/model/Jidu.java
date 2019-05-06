package com.xzz.hxjdglpt.model;

public class Jidu {
	private Integer id;

	private String type;

	private String nasuinum;

	private String shijiao;

	private String gridid;

	private String jidu;

	private String yujiao;

	private String pid;

	private String bz;

	public Integer getId() {
		return id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public String getNasuinum() {
		return nasuinum;
	}

	public void setNasuinum(String nasuinum) {
		this.nasuinum = nasuinum == null ? null : nasuinum.trim();
	}

	public String getShijiao() {
		return shijiao;
	}

	public void setShijiao(String shijiao) {
		this.shijiao = shijiao == null ? null : shijiao.trim();
	}

	public String getGridid() {
		return gridid;
	}

	public void setGridid(String gridid) {
		this.gridid = gridid == null ? null : gridid.trim();
	}

	public String getJidu() {
		return jidu;
	}

	public void setJidu(String jidu) {
		this.jidu = jidu == null ? null : jidu.trim();
	}

	public String getYujiao() {
		return yujiao;
	}

	public void setYujiao(String yujiao) {
		this.yujiao = yujiao == null ? null : yujiao.trim();
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz == null ? null : bz.trim();
	}
}