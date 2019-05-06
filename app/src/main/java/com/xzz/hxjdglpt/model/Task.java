package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private String id;

    private String name;

    private String createtime;

    private String updatetime;

    private String description;

    private String createuser;

    private String filename;

    private String filepath;

    private String type;

    private String receiveuser;

    private String receiveuserName;

    private int status;//0/1 已/未

    private String endTime;

    private String thReason;

    private String creatoruserName;// 创建人姓名

    public String getThReason() {
        return thReason;
    }

    public void setThReason(String thReason) {
        this.thReason = thReason;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser == null ? null : createuser.trim();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath == null ? null : filepath.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getReceiveuser() {
        return receiveuser;
    }

    public void setReceiveuser(String receiveuser) {
        this.receiveuser = receiveuser == null ? null : receiveuser.trim();
    }

    public String getReceiveuserName() {
        return receiveuserName;
    }

    public void setReceiveuserName(String receiveuserName) {
        this.receiveuserName = receiveuserName;
    }

    public String getCreatoruserName() {
        return creatoruserName;
    }

    public void setCreatoruserName(String creatoruserName) {
        this.creatoruserName = creatoruserName;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(createtime);
        parcel.writeString(updatetime);
        parcel.writeString(description);
        parcel.writeString(createuser);
        parcel.writeString(filename);
        parcel.writeString(filepath);
        parcel.writeString(type);
        parcel.writeString(receiveuser);
        parcel.writeInt(status);
        parcel.writeString(endTime);
        parcel.writeString(thReason);
        parcel.writeString(receiveuserName);
        parcel.writeString(creatoruserName);
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    private Task(Parcel in) {
        id = in.readString();
        name = in.readString();
        createtime = in.readString();
        updatetime = in.readString();
        description = in.readString();
        createuser = in.readString();
        filename = in.readString();
        filepath = in.readString();
        type = in.readString();
        receiveuser = in.readString();
        status = in.readInt();
        endTime = in.readString();
        thReason = in.readString();
        receiveuserName = in.readString();
        creatoruserName = in.readString();
    }
}