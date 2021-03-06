package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 党建学习
 * Created by dbz on 2017/5/14.
 */
public class Party implements Parcelable {
    private String id;
    private String createFk;
    private String updateFk;
    private String createTime;
    private String updateTime;
    private long valid_flag;
    private String title;
    private String content;
    private String fileName;
    private String filePath;
    private String realName;
    private int type;


    public Party() {
    }

    public Party(String id, String createFk, String updateFk, String createTime, String
            updateTime, long valid_flag, String title, String content, String fileName, String
                         filePath, String realName) {
        this.id = id;
        this.createFk = createFk;
        this.updateFk = updateFk;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.valid_flag = valid_flag;
        this.title = title;
        this.content = content;
        this.fileName = fileName;
        this.filePath = filePath;
        this.realName = realName;
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

    public void setValid_flag(long valid_flag) {
        this.valid_flag = valid_flag;
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

    public long getValid_flag() {
        return valid_flag;
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

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
        parcel.writeLong(valid_flag);
        parcel.writeString(realName);
        parcel.writeInt(type);
    }

    public static final Creator<Party> CREATOR = new Creator<Party>() {
        public Party createFromParcel(Parcel in) {
            return new Party(in);
        }

        public Party[] newArray(int size) {
            return new Party[size];
        }
    };

    private Party(Parcel in) {
        id = in.readString();
        createFk = in.readString();
        updateFk = in.readString();
        createTime = in.readString();
        title = in.readString();
        updateTime = in.readString();
        content = in.readString();
        fileName = in.readString();
        filePath = in.readString();
        valid_flag = in.readLong();
        realName = in.readString();
        type = in.readInt();
    }

}
