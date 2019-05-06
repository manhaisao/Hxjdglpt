package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GzdtRdzj implements Parcelable {

    private long id;// 主键

    private String title;// 标题

    private String content;// 内容

    private long fbr;// 发布人

    private String fbrName;// 发布人姓名

    private String fbsj;// 发布时间

    private Integer type;// 类型

    private String filename;// 文件名称

    private String filepath;// 文件路径

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getFbr() {
        return fbr;
    }

    public void setFbr(long fbr) {
        this.fbr = fbr;
    }

    public String getFbrName() {
        return fbrName;
    }

    public void setFbrName(String fbrName) {
        this.fbrName = fbrName;
    }

    public String getFbsj() {
        return fbsj;
    }

    public void setFbsj(String fbsj) {
        this.fbsj = fbsj;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeLong(fbr);
        parcel.writeString(fbrName);
        parcel.writeString(fbsj);
        parcel.writeInt(type == null ? 0 : type);
        parcel.writeString(filename);
        parcel.writeString(filepath);
    }

    public static final Creator<GzdtRdzj> CREATOR = new Creator<GzdtRdzj>() {
        public GzdtRdzj createFromParcel(Parcel in) {
            return new GzdtRdzj(in);
        }

        public GzdtRdzj[] newArray(int size) {
            return new GzdtRdzj[size];
        }
    };

    private GzdtRdzj(Parcel in) {
        id = in.readLong();
        title = in.readString();
        content = in.readString();
        fbr = in.readLong();
        fbrName = in.readString();
        fbsj = in.readString();
        type = in.readInt();
        filename = in.readString();
        filepath = in.readString();
    }
}
