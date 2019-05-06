package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Administrator on 2019\5\4 0004.
 {
 "id": 7,
 "name": "7",
 "description": null,
 "videoUrl": null,
 "createTime": null,
 "createUser": null,
 "updateTime": null,
 "updateUser": null
 }
 */

public class Pfwsp implements Parcelable {

    private String createTime;

    private String createUser;

    private int id;

    private String name;

    private String updateTime;

    private String updateUser;

    private String videoUrl;

    private String  description;


    protected Pfwsp(Parcel in) {
        createTime = in.readString();
        createUser = in.readString();
        id = in.readInt();
        name = in.readString();
        updateTime = in.readString();
        updateUser = in.readString();
        videoUrl = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createTime);
        dest.writeString(createUser);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(updateTime);
        dest.writeString(updateUser);
        dest.writeString(videoUrl);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Pfwsp> CREATOR = new Creator<Pfwsp>() {
        @Override
        public Pfwsp createFromParcel(Parcel in) {
            return new Pfwsp(in);
        }

        @Override
        public Pfwsp[] newArray(int size) {
            return new Pfwsp[size];
        }
    };

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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
