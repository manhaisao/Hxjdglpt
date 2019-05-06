package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 1,信访诉求人员 2,肇事肇祸精神病患者 3,重点青少年 4,社区戒毒人员 5,社区服刑人员 6,安置帮教人员 7,邪教人员 的工作日志
 *
 * @author dbz
 */
public class Note implements Parcelable {

    private long id;

    private long pId;// 1,信访诉求人员 2,肇事肇祸精神病患者 3,重点青少年 4,社区戒毒人员 5,社区服刑人员
    // 6,安置帮教人员 7,邪教人员的ID

    private int isZdry;// 是否是信访诉求人员

    private int isXf;// 是否息访

    private int isZw;// 是否在位

    private String title;

    private String address;

    private String content;

    private String filename;

    private String filepath;

    private long fbr;

    private String fbsj;

    private String question;// 存在问题

    private String gzcs;// 工作措施

    private String cljl;// 处理结论

    private int type;// 日志类型----1,信访诉求人员 2,肇事肇祸精神病患者 3,重点青少年 4,社区戒毒人员 5,社区服刑人员
    // 6,安置帮教人员 7,邪教人员

    private int stzk;// 身体状况----1，稳定期 2，发病期

    private int isZy;// 是否住院

    private int isAsnj;// 是否按时尿检

    private int isJyaz;// 是否就业安置

    private int sxzk;// 思想状况---1,稳定期 2,活跃期

    private String bz;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getpId() {
        return pId;
    }

    public void setpId(long pId) {
        this.pId = pId;
    }

    public int getIsZdry() {
        return isZdry;
    }

    public void setIsZdry(int isZdry) {
        this.isZdry = isZdry;
    }

    public int getIsXf() {
        return isXf;
    }

    public void setIsXf(int isXf) {
        this.isXf = isXf;
    }

    public int getIsZw() {
        return isZw;
    }

    public void setIsZw(int isZw) {
        this.isZw = isZw;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public long getFbr() {
        return fbr;
    }

    public void setFbr(long fbr) {
        this.fbr = fbr;
    }

    public String getFbsj() {
        return fbsj;
    }

    public void setFbsj(String fbsj) {
        this.fbsj = fbsj;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getGzcs() {
        return gzcs;
    }

    public void setGzcs(String gzcs) {
        this.gzcs = gzcs;
    }

    public String getCljl() {
        return cljl;
    }

    public void setCljl(String cljl) {
        this.cljl = cljl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStzk() {
        return stzk;
    }

    public void setStzk(int stzk) {
        this.stzk = stzk;
    }

    public int getIsZy() {
        return isZy;
    }

    public void setIsZy(int isZy) {
        this.isZy = isZy;
    }

    public int getIsAsnj() {
        return isAsnj;
    }

    public void setIsAsnj(int isAsnj) {
        this.isAsnj = isAsnj;
    }

    public int getIsJyaz() {
        return isJyaz;
    }

    public void setIsJyaz(int isJyaz) {
        this.isJyaz = isJyaz;
    }

    public int getSxzk() {
        return sxzk;
    }

    public void setSxzk(int sxzk) {
        this.sxzk = sxzk;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(id);
        parcel.writeLong(pId);
        parcel.writeInt(isZdry);
        parcel.writeInt(isXf);
        parcel.writeInt(isZw);
        parcel.writeString(title);
        parcel.writeString(address);
        parcel.writeString(content);
        parcel.writeString(filename);
        parcel.writeString(filepath);
        parcel.writeLong(fbr);
        parcel.writeString(fbsj);
        parcel.writeString(question);
        parcel.writeString(gzcs);
        parcel.writeString(cljl);
        parcel.writeInt(type);
        parcel.writeInt(stzk);
        parcel.writeInt(isZy);
        parcel.writeInt(isAsnj);
        parcel.writeInt(isJyaz);
        parcel.writeInt(sxzk);
        parcel.writeString(bz);
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    private Note(Parcel in) {
        id = in.readLong();
        pId = in.readLong();
        isZdry = in.readInt();
        isXf = in.readInt();
        isZw = in.readInt();
        title = in.readString();
        address = in.readString();
        content = in.readString();
        filename = in.readString();
        filepath = in.readString();
        fbr = in.readLong();
        fbsj = in.readString();
        question = in.readString();
        gzcs = in.readString();
        cljl = in.readString();
        type = in.readInt();
        stzk = in.readInt();
        isZy = in.readInt();
        isAsnj = in.readInt();
        isJyaz = in.readInt();
        sxzk = in.readInt();
        bz = in.readString();
    }

}
