package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Jly implements Parcelable {
    private Integer id;

    private String fzr;

    private String fzrphone;

    private String gly;

    private String glyphone;

    private String aqy;

    private String aqyphone;

    private String wsy;

    private String wsyphone;

    private String hly;
    
    private String jizhong;
    
    private String shej;

    
    
    public String getJizhong() {
		return jizhong;
	}

	public void setJizhong(String jizhong) {
		this.jizhong = jizhong;
	}

	public String getShej() {
		return shej;
	}

	public void setShej(String shej) {
		this.shej = shej;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFzr() {
        return fzr;
    }

    public void setFzr(String fzr) {
        this.fzr = fzr == null ? null : fzr.trim();
    }

    public String getFzrphone() {
        return fzrphone;
    }

    public void setFzrphone(String fzrphone) {
        this.fzrphone = fzrphone == null ? null : fzrphone.trim();
    }

    public String getGly() {
        return gly;
    }

    public void setGly(String gly) {
        this.gly = gly == null ? null : gly.trim();
    }

    public String getGlyphone() {
        return glyphone;
    }

    public void setGlyphone(String glyphone) {
        this.glyphone = glyphone == null ? null : glyphone.trim();
    }

    public String getAqy() {
        return aqy;
    }

    public void setAqy(String aqy) {
        this.aqy = aqy == null ? null : aqy.trim();
    }

    public String getAqyphone() {
        return aqyphone;
    }

    public void setAqyphone(String aqyphone) {
        this.aqyphone = aqyphone == null ? null : aqyphone.trim();
    }

    public String getWsy() {
        return wsy;
    }

    public void setWsy(String wsy) {
        this.wsy = wsy == null ? null : wsy.trim();
    }

    public String getWsyphone() {
        return wsyphone;
    }

    public void setWsyphone(String wsyphone) {
        this.wsyphone = wsyphone == null ? null : wsyphone.trim();
    }

    public String getHly() {
        return hly;
    }

    public void setHly(String hly) {
        this.hly = hly == null ? null : hly.trim();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(id);
        parcel.writeString(fzr);
        parcel.writeString(fzrphone);
        parcel.writeString(gly);
        parcel.writeString(glyphone);
        parcel.writeString(aqy);
        parcel.writeString(aqyphone);
        parcel.writeString(wsy);
        parcel.writeString(wsyphone);
        parcel.writeString(hly);
        parcel.writeString(jizhong);
        parcel.writeString(shej);
    }

    public static final Parcelable.Creator<Jly> CREATOR = new Parcelable.Creator<Jly>() {
        public Jly createFromParcel(Parcel in) {
            return new Jly(in);
        }

        public Jly[] newArray(int size) {
            return new Jly[size];
        }
    };

    private Jly(Parcel in) {
        id = in.readInt();
        fzr = in.readString();
        fzrphone = in.readString();
        gly = in.readString();
        glyphone = in.readString();
        aqy = in.readString();
        aqyphone = in.readString();
        wsy = in.readString();
        wsyphone = in.readString();
        hly = in.readString();
        jizhong = in.readString();
        shej = in.readString();
    }

}