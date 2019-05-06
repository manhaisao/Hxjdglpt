package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 最新通知
 * Created by dbz on 2017/5/16.
 */
public class Notice implements Parcelable {
    private String id;
    private String createFk;
    private String updateFk;
    private String createTime;
    private String updateTime;
    private long validFlag;
    private String title;
    private String content;
    private String fileName;
    private String filePath;
    private String realName;

    public String getId() {
        return id;
    }

    public String getCreateFk() {
        return createFk;
    }

    public String getUpdateFk() {
        return updateFk;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public long getValidFlag() {
        return validFlag;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreateFk(String createFk) {
        this.createFk = createFk;
    }

    public void setUpdateFk(String updateFk) {
        this.updateFk = updateFk;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setValidFlag(long validFlag) {
        this.validFlag = validFlag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(createFk);
        parcel.writeString(updateFk);
        parcel.writeString(createTime);
        parcel.writeString(title);
        parcel.writeString(updateTime);
        parcel.writeString(content);
        parcel.writeString(fileName);
        parcel.writeString(filePath);
        parcel.writeLong(validFlag);
        parcel.writeString(realName);
    }

    public static final Parcelable.Creator<Notice> CREATOR = new Parcelable.Creator<Notice>() {
        public Notice createFromParcel(Parcel in) {
            return new Notice(in);
        }

        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };

    private Notice(Parcel in) {
        id = in.readString();
        createFk = in.readString();
        updateFk = in.readString();
        createTime = in.readString();
        title = in.readString();
        updateTime = in.readString();
        content = in.readString();
        fileName = in.readString();
        filePath = in.readString();
        validFlag = in.readLong();
        realName = in.readString();
    }

}
