package com.xzz.hxjdglpt.model;

/**
 * Created by Administrator on 2019/4/9 0009.
 */

import java.io.Serializable;

/**
 * {
 * "code": 0,
 * "msg": "string",
 * "result": [
 * {
 * "grid": "string",
 * "id": 0,
 * "name": "string",
 * "remark": "string",
 * "village": "string"
 * }
 * ]
 * }
 */
public class Hdqg implements Serializable{

    private String grid;

    private int id;

    private String name;

    private String remark;

    private String village;

    public String getGrid() {
        return grid;
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }
}
