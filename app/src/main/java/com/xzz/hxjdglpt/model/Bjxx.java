package com.xzz.hxjdglpt.model;

/**
 * Created by Administrator on 2019/4/9 0009.
 */

import java.io.Serializable;

/**
 *      {
 "result": [
 {
 "id": 1,
 "roadName": "学海路",
 "village": "北严村",
 "grid": "左一，右二",
 "remark": "呵呵"
 }
 ],
 "code": 0,
 "msg": "操作成功"
 }
 */
public class Bjxx implements Serializable{

    private int id;

    private String roadName;

    private String village;

    private String grid;

    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getGrid() {
        return grid;
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
