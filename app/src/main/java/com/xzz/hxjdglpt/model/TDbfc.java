package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TDbfc implements Parcelable {
    private Integer id;

    private String name;

    private String title;

    private String fileName;

    private String filePath;

    private Integer age;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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
        parcel.writeString(name);
        parcel.writeInt(age == null ? 0 : age);
        parcel.writeString(title);
        parcel.writeString(fileName);
        parcel.writeString(filePath);
        parcel.writeString(content);
    }

    public static final Parcelable.Creator<TDbfc> CREATOR = new Parcelable.Creator<TDbfc>() {
        public TDbfc createFromParcel(Parcel in) {
            return new TDbfc(in);
        }

        public TDbfc[] newArray(int size) {
            return new TDbfc[size];
        }
    };

    private TDbfc(Parcel in) {
        id = in.readInt();
        name = in.readString();
        age = in.readInt();
        title = in.readString();
        fileName = in.readString();
        filePath = in.readString();
        content = in.readString();
    }
}