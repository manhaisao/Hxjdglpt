package com.xzz.hxjdglpt.model.ls;

import java.io.Serializable;
import java.util.List;

public class ZhongcaiJbr implements Serializable{
	private Integer id;

	private String content;

	private String bumen;

	private String address;

	private String xly;

	private String xlyphone;

	private List<ZhongcaiJbr> ps;

	public String getXlyphone() {
		return xlyphone;
	}

	public void setXlyphone(String xlyphone) {
		this.xlyphone = xlyphone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public String getBumen() {
		return bumen;
	}

	public void setBumen(String bumen) {
		this.bumen = bumen == null ? null : bumen.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
	}

	public String getXly() {
		return xly;
	}

	public void setXly(String xly) {
		this.xly = xly == null ? null : xly.trim();
	}

	public List<ZhongcaiJbr> getPs() {
		return ps;
	}

	public void setPs(List<ZhongcaiJbr> ps) {
		this.ps = ps;
	}
}