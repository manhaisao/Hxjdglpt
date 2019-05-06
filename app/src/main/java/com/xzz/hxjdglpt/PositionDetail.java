package com.xzz.hxjdglpt;

import java.io.Serializable;

/**
 *  "id": 1,
 "positionType": "02主干道",
 "positionName": "翔宇大道",
 "village": "新路村、河北村、城郊村",
 "address": "新路村、河北村、城郊村",
 "responsibleUser": "张三",
 "tel": "18512596901"
 */

public class PositionDetail implements Serializable {

    private int id;

    private String positionType;

    private String positionName;

    private String village;

    private String address;


    private String responsibleUser;

    private String tel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getResponsibleUser() {
        return responsibleUser;
    }

    public void setResponsibleUser(String responsibleUser) {
        this.responsibleUser = responsibleUser;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
