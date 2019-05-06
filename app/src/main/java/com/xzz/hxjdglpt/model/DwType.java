package com.xzz.hxjdglpt.model;

/**
 * Created by Administrator on 2019\4\25 0025.
 */

import java.io.Serializable;

/**
 * {"id":1,"positionType":"03 次干道","createTime":"2019-04-19 10:53","createUser":7810,"updateTime":null,"updateUser":null,"createUserName":"郭涛","updateUserName":null}]
 */
public class DwType implements Serializable {

    private int id;

    private String positionType;

    private String createTime;

    private String createUserName;

    private String createUser;

    private String updateTime;

    private String updateUser;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    private String updateUserName;





}
