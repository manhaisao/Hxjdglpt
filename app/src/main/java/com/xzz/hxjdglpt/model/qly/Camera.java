package com.xzz.hxjdglpt.model.qly;

import android.os.Parcel;
import android.os.Parcelable;

public class Camera implements Parcelable {
    private String id;// 视频通道ID，即镜头ID

    private String name;// 视频通道名称，即镜头名称

    private String runStatus;// 镜头运行状态:0:不在线;1:在线;2:休眠;3:（镜头）能力未上报;4:不在线停机;5:在线停机;6:休眠停机

    private String subjectId;// 所属设备组ID

    private String ptzType;// 0：固定枪机 1：有云台枪 2：球机

    private String devRights;// 设备权限

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

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getPtzType() {
        return ptzType;
    }

    public void setPtzType(String ptzType) {
        this.ptzType = ptzType;
    }

    public String getDevRights() {
        return devRights;
    }

    public void setDevRights(String devRights) {
        this.devRights = devRights;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(runStatus);
        parcel.writeString(subjectId);
        parcel.writeString(ptzType);
        parcel.writeString(devRights);
    }

    public static final Parcelable.Creator<Camera> CREATOR = new Parcelable.Creator<Camera>() {
        public Camera createFromParcel(Parcel in) {
            return new Camera(in);
        }

        public Camera[] newArray(int size) {
            return new Camera[size];
        }
    };

    private Camera(Parcel in) {
        id = in.readString();
        name = in.readString();
        runStatus = in.readString();
        subjectId = in.readString();
        ptzType = in.readString();
        devRights = in.readString();
    }
}
