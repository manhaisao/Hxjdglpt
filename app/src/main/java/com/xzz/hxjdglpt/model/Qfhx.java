package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2019\3\9 0009.
 */

public class Qfhx implements Parcelable {
    private int id;
    private String title;
    private String createTime;
    private String createFk;
    private String updateTime;
    private String updateFk;
    private String fileName;
    private String realName;
    private String filePath;
    private String content;


    private int validFlag;
    private String videoPath;
    private String videoName;

    private String type;


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(int validFlag) {
        this.validFlag = validFlag;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getUpdateFk() {
        return updateFk;
    }

    public void setUpdateFk(String updateFk) {
        this.updateFk = updateFk;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getCreateFk() {
        return createFk;
    }

    public void setCreateFk(String createFk) {
        this.createFk = createFk;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(createTime);
        parcel.writeString(createFk);
        parcel.writeString(updateTime);
        parcel.writeString(updateFk);
        parcel.writeString(fileName);
        parcel.writeString(filePath);
        parcel.writeString(realName);
        parcel.writeString(content);
        parcel.writeInt(validFlag);
        parcel.writeString(videoPath);
        parcel.writeString(videoName);
        parcel.writeString(type);
    }

    public static final Parcelable.Creator<Qfhx> CREATOR = new Parcelable.Creator<Qfhx>() {
        public Qfhx createFromParcel(Parcel in) {
            return new Qfhx(in);
        }
        public Qfhx[] newArray(int size) {
            return new Qfhx[size];
        }
    };

    private Qfhx(Parcel in) {
        id = in.readInt();
        title = in.readString();
        createTime = in.readString();
        createFk = in.readString();
        updateTime = in.readString();
        updateFk = in.readString();
        fileName = in.readString();
        filePath = in.readString();
        realName = in.readString();
        content = in.readString();
        validFlag = in.readInt();
        videoPath = in.readString();
        videoName = in.readString();
        type = in.readString();
    }
}
