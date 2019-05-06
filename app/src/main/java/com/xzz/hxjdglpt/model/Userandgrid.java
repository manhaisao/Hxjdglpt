package com.xzz.hxjdglpt.model;

public class Userandgrid {
    private Integer id;

    private Integer userid;

    private String gridid;


    public Userandgrid(Integer userid, String gridid) {
        super();
        this.userid = userid;
        this.gridid = gridid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getGridid() {
        return gridid;
    }

    public void setGridid(String gridid) {
        this.gridid = gridid;
    }
}