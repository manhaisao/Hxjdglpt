/*
 * 文 件 名:  UserInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  manhan
 * 修改时间:  2009-9-30
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.xzz.hxjdglpt.model.qly;

import java.io.Serializable;

public class UserInfo implements Serializable {
    /**
     * 序列
     */
    private static final long serialVersionUID = -3318621668339622294L;


    private String myPassW;//保存未解密的密码

    /**
     * 用户真实姓名
     */
    private String userName;

    /**
     * 用户登录密码
     */
    private String password;


    /**
     * 主键
     */
    private String id;


    /**
     * 用户编号
     */
    private String userID;


    /**
     * 用户登录域
     */
    private String loginDomain;

    /**
     * 用户登录账号
     */
    private String loginName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userId) {
        this.userID = userId;
    }


    public String getLoginDomain() {
        return loginDomain;
    }

    public void setLoginDomain(String loginDomain) {
        this.loginDomain = loginDomain;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMyPassW() {
        return myPassW;
    }

    public void setMyPassW(String myPassW) {
        this.myPassW = myPassW;
    }
}
