package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 小区工作动态
 *
 * @author Administrator
 */
public class PlotGzdt implements Parcelable {

    private long id;// 主键

    private String title;// 标题

    private String content;// 内容

    private long fbr;// 发布人

    private String fbrName;// 发布人姓名

    private String fbsj;// 发布时间

    private long plotId;// 小区ID

    public long getPlotId() {
        return plotId;
    }

    public void setPlotId(long plotId) {
        this.plotId = plotId;
    }

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
        parcel.writeLong(plotId);
        parcel.writeString(filename);
        parcel.writeString(filepath);
    }

    public static final Creator<PlotGzdt> CREATOR = new Creator<PlotGzdt>() {
        public PlotGzdt createFromParcel(Parcel in) {
            return new PlotGzdt(in);
        }

        public PlotGzdt[] newArray(int size) {
            return new PlotGzdt[size];
        }
    };

    private PlotGzdt(Parcel in) {
        id = in.readLong();
        title = in.readString();
        content = in.readString();
        fbr = in.readLong();
        fbrName = in.readString();
        fbsj = in.readString();
        plotId = in.readLong();
        filename = in.readString();
        filepath = in.readString();
    }
}
