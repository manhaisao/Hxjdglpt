package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Sxgz implements Parcelable {

    private String id;
    private String name;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(content);
    }

    public static final Creator<Sxgz> CREATOR = new Creator<Sxgz>() {
        public Sxgz createFromParcel(Parcel in) {
            return new Sxgz(in);
        }

        public Sxgz[] newArray(int size) {
            return new Sxgz[size];
        }
    };

    private Sxgz(Parcel in) {
        id = in.readString();
        name = in.readString();
        content = in.readString();
    }
}
