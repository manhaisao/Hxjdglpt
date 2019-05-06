package com.xzz.hxjdglpt.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2019\5\6 0006.
 * {"id":1,"lawTypeId":1,"title":"1","content":"2","fbr":123,"fbrName":null,"fbsj":"2019-05-06 00:02:52","filename":"","filepath":""}
 */

public class FljktDetail implements Parcelable {

    private int id;

    private int lawTypeId;

    private String title;

    private String content;

    private String fbr;

    private String fbrName;

    private String fbsj;

    private String filename;

    private String filepath;

    protected FljktDetail(Parcel in) {
        id = in.readInt();
        lawTypeId = in.readInt();
        title = in.readString();
        content = in.readString();
        fbr = in.readString();
        fbrName = in.readString();
        fbsj = in.readString();
        filename = in.readString();
        filepath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(lawTypeId);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(fbr);
        dest.writeString(fbrName);
        dest.writeString(fbsj);
        dest.writeString(filename);
        dest.writeString(filepath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FljktDetail> CREATOR = new Creator<FljktDetail>() {
        @Override
        public FljktDetail createFromParcel(Parcel in) {
            return new FljktDetail(in);
        }

        @Override
        public FljktDetail[] newArray(int size) {
            return new FljktDetail[size];
        }
    };

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFbr() {
        return fbr;
    }

    public void setFbr(String fbr) {
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


}
