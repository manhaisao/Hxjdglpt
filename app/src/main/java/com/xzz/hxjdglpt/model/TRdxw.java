package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TRdxw implements Parcelable {
    private Integer id;

    private String title;

    private String createTime;

    private String createFk;

    private String updateTime;

    private String updateFk;

    private String fileName;

    private String filePath;

    private String realname;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getCreateFk() {
        return createFk;
    }

    public void setCreateFk(String createFk) {
        this.createFk = createFk == null ? null : createFk.trim();
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }

    public String getUpdateFk() {
        return updateFk;
    }

    public void setUpdateFk(String updateFk) {
        this.updateFk = updateFk == null ? null : updateFk.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
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
        parcel.writeString(realname);
        parcel.writeString(content);
    }

    public static final Parcelable.Creator<TRdxw> CREATOR = new Parcelable.Creator<TRdxw>() {
        public TRdxw createFromParcel(Parcel in) {
            return new TRdxw(in);
        }

        public TRdxw[] newArray(int size) {
            return new TRdxw[size];
        }
    };

    private TRdxw(Parcel in) {
        id = in.readInt();
        title = in.readString();
        createTime = in.readString();
        createFk = in.readString();
        updateTime = in.readString();
        updateFk = in.readString();
        fileName = in.readString();
        filePath = in.readString();
        realname = in.readString();
        content = in.readString();
    }
}