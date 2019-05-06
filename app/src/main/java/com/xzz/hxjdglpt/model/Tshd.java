package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Tshd implements Parcelable {

    private Long id;

    private String title;

    private String content;

    private String picture;

    private Long vId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Long getvId() {
        return vId;
    }

    public void setvId(Long vId) {
        this.vId = vId;
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
        parcel.writeString(picture);
        parcel.writeLong(vId);
    }

    public static final Parcelable.Creator<Tshd> CREATOR = new Parcelable.Creator<Tshd>() {
        public Tshd createFromParcel(Parcel in) {
            return new Tshd(in);
        }

        public Tshd[] newArray(int size) {
            return new Tshd[size];
        }
    };

    private Tshd(Parcel in) {
        id = in.readLong();
        title = in.readString();
        content = in.readString();
        picture = in.readString();
        vId = in.readLong();
    }
}
