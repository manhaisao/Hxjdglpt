package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Gonggao implements Parcelable {
    private Integer id;

    private String renshi;

    private String tongbao;

    private String filename;

    private String filepath;

    private String createtime;

    private String createname;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreatename() {
        return createname;
    }

    public void setCreatename(String createname) {
        this.createname = createname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRenshi() {
        return renshi;
    }

    public void setRenshi(String renshi) {
        this.renshi = renshi == null ? null : renshi.trim();
    }

    public String getTongbao() {
        return tongbao;
    }

    public void setTongbao(String tongbao) {
        this.tongbao = tongbao == null ? null : tongbao.trim();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(id);
        parcel.writeString(renshi);
        parcel.writeString(tongbao);
        parcel.writeString(filepath);
        parcel.writeString(filename);
        parcel.writeString(createtime);
        parcel.writeString(createname);
        parcel.writeString(title);
    }

    public static final Parcelable.Creator<Gonggao> CREATOR = new Parcelable.Creator<Gonggao>() {
        public Gonggao createFromParcel(Parcel in) {
            return new Gonggao(in);
        }

        public Gonggao[] newArray(int size) {
            return new Gonggao[size];
        }
    };

    private Gonggao(Parcel in) {
        id = in.readInt();
        renshi = in.readString();
        tongbao = in.readString();
        filepath = in.readString();
        filename = in.readString();
        createtime = in.readString();
        createname = in.readString();
        title = in.readString();
    }
}