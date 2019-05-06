package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Jyfw implements Parcelable {

    private long id;

    private String title;

    private String content;

    private String path;

    private long fbr;

    private String fbsj;

    private String fbrName;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getFbr() {
        return fbr;
    }

    public void setFbr(long fbr) {
        this.fbr = fbr;
    }

    public String getFbsj() {
        return fbsj;
    }

    public void setFbsj(String fbsj) {
        this.fbsj = fbsj;
    }

    public String getFbrName() {
        return fbrName;
    }

    public void setFbrName(String fbrName) {
        this.fbrName = fbrName;
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
        parcel.writeString(path);
        parcel.writeLong(fbr);
        parcel.writeString(fbsj);
        parcel.writeString(fbrName);
    }

    public static final Parcelable.Creator<Jyfw> CREATOR = new Parcelable.Creator<Jyfw>() {
        public Jyfw createFromParcel(Parcel in) {
            return new Jyfw(in);
        }

        public Jyfw[] newArray(int size) {
            return new Jyfw[size];
        }
    };

    private Jyfw(Parcel in) {
        id = in.readLong();
        title = in.readString();
        content = in.readString();
        path = in.readString();
        fbr = in.readLong();
        fbsj = in.readString();
        fbrName = in.readString();

    }
}
