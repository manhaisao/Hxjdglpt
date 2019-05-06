package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GridGzdt implements Parcelable {

    private long id;// 主键

    private String title;// 标题

    private String content;// 内容

    private long fbr;// 发布人

    private String fbrName;// 发布人姓名

    private String fbsj;// 发布时间

    private Integer type;// 类型

    private String filename;// 文件名称

    private String filepath;// 文件路径
    private String gridId;// 网格ID
    private String question;// 存在问题
    private String result;// 处理结果
    private String fbaddress;//发布地址

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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }
    public String getFbaddress() {
        return fbaddress;
    }

    public void setFbaddress(String fbaddress) {
        this.fbaddress = fbaddress;
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
        parcel.writeString(gridId);
        parcel.writeString(question);
        parcel.writeString(result);
        parcel.writeString(fbaddress);
    }

    public static final Creator<GridGzdt> CREATOR = new Creator<GridGzdt>() {
        public GridGzdt createFromParcel(Parcel in) {
            return new GridGzdt(in);
        }

        public GridGzdt[] newArray(int size) {
            return new GridGzdt[size];
        }
    };

    private GridGzdt(Parcel in) {
        id = in.readLong();
        title = in.readString();
        content = in.readString();
        fbr = in.readLong();
        fbrName = in.readString();
        fbsj = in.readString();
        type = in.readInt();
        filename = in.readString();
        filepath = in.readString();
        gridId = in.readString();
        question = in.readString();
        result = in.readString();
        fbaddress=in.readString();
    }
}
