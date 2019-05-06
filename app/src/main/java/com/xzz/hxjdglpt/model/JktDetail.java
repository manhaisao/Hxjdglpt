package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Administrator on 2019\5\4 0004.
 * { “content” ：“string” ，“createTime” ：“string” ，“createUser” ：0 ，“id” ：0 ，“lawTypeId” ：0 ，“title” ：“string“ ，”updateTime“ ：”string“，“updateUser” ：0 }
 */

public class JktDetail implements Parcelable {

    private String content;

    private String createTime;

    private int createUser;

    private int id;

    private int lawTypeId;

    private String title;

    private String updateTime;

    private int updateUser;


    protected JktDetail(Parcel in) {
        content = in.readString();
        createTime = in.readString();
        createUser = in.readInt();
        id = in.readInt();
        lawTypeId = in.readInt();
        title = in.readString();
        updateTime = in.readString();
        updateUser = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(createTime);
        dest.writeInt(createUser);
        dest.writeInt(id);
        dest.writeInt(lawTypeId);
        dest.writeString(title);
        dest.writeString(updateTime);
        dest.writeInt(updateUser);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JktDetail> CREATOR = new Creator<JktDetail>() {
        @Override
        public JktDetail createFromParcel(Parcel in) {
            return new JktDetail(in);
        }

        @Override
        public JktDetail[] newArray(int size) {
            return new JktDetail[size];
        }
    };

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

    public int getCreateUser() {
        return createUser;
    }

    public void setCreateUser(int createUser) {
        this.createUser = createUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLawTypeId() {
        return lawTypeId;
    }

    public void setLawTypeId(int lawTypeId) {
        this.lawTypeId = lawTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(int updateUser) {
        this.updateUser = updateUser;
    }
}
