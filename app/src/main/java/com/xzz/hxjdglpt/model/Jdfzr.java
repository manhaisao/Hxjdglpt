package com.xzz.hxjdglpt.model;

public class Jdfzr {
    private Integer id;

    private String name;

    private String phone;

    private String type;

    private String pid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    

    public Jdfzr(String type, String pid) {
		super();
		this.type = type;
		this.pid = pid;
	}

	public Jdfzr(String pid) {
		super();
		this.pid = pid;
	}

	public Jdfzr() {
		super();
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }
}