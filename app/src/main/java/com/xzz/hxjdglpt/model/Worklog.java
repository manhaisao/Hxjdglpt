package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Worklog implements Parcelable {
    private String id;

    private String name;

    private String mark;

    private String rec;

    private String send;

    private String workContent;

    private String status;

    private String type;

    private String fileName;

    private String filePath;

    private String time;

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
        this.name = name == null ? null : name.trim();
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark == null ? null : mark.trim();
    }

    public String getRec() {
        return rec;
    }

    public void setRec(String rec) {
        this.rec = rec == null ? null : rec.trim();
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send == null ? null : send.trim();
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(mark);
        parcel.writeString(rec);
        parcel.writeString(send);
        parcel.writeString(workContent);
        parcel.writeString(status);
        parcel.writeString(type);
        parcel.writeString(fileName);
        parcel.writeString(filePath);
        parcel.writeString(time);
        parcel.writeString(address);
    }

    public static final Creator<Worklog> CREATOR = new Creator<Worklog>() {
        public Worklog createFromParcel(Parcel in) {
            return new Worklog(in);
        }

        public Worklog[] newArray(int size) {
            return new Worklog[size];
        }
    };

    private Worklog(Parcel in) {
        id = in.readString();
        name = in.readString();
        mark = in.readString();
        rec = in.readString();
        send = in.readString();
        workContent = in.readString();
        status = in.readString();
        type = in.readString();
        fileName = in.readString();
        filePath = in.readString();
        time = in.readString();
        address = in.readString();
    }
}