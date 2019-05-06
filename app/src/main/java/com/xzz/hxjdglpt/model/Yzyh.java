package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 *  {
 "id": 1,
 "title": "知道吗",
 "content": "知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道知道",
 "createTime": null,
 "createUser": null,
 "updateTime": null,
 "updateUser": null
 }
 */



public class Yzyh implements Serializable {

    private int id;

    private String title;

    private String content;

    private String createTime;

    private String createUser;

    private String updateTime;

    private String updateUser;

    private String filePath;

    private String createUserName;

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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



}
