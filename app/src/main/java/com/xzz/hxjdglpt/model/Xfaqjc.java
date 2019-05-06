package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 消防安全、安全生产
 *
 * @author Administrator
 */
public class Xfaqjc implements Parcelable {

    private long id;

    private long pId;// 外键，企业ID,安全生产时用到

    private int type;// 类型，1：消防安全，2：安全生产

    private int jclb;// 检查类别，1：日常检查，2：专项检查

    private String jcstime;// 检查开始时间

    private String jcetime;// 检查结束时间

    private String ddld;// 带队领导

    private String ldphone;// 带队领导联系号码

    private String jccy;// 检查组成员

    private String ptjcr;// 陪同检查人

    private String ptjcrPhone;// 陪同检查人联系号码

    private int isZj;// 企业是否开展自查

    private int isSj;// 是否自查资料上交

    private String content;// 检查内容

    private String filename;

    private String filepath;

    private long fbr;

    private String fbsj;
    private String gridId;//消防安全检查时用到


    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    private String question;// 存在问题

    private int cljl;// 处理措施，1：现场责令改正 ，2：下达整改通知书 ，3：其他

    private String bz;// 备注

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getJclb() {
        return jclb;
    }

    public void setJclb(int jclb) {
        this.jclb = jclb;
    }

    public String getJcstime() {
        return jcstime;
    }

    public void setJcstime(String jcstime) {
        this.jcstime = jcstime;
    }

    public String getJcetime() {
        return jcetime;
    }

    public void setJcetime(String jcetime) {
        this.jcetime = jcetime;
    }

    public String getDdld() {
        return ddld;
    }

    public void setDdld(String ddld) {
        this.ddld = ddld;
    }

    public String getLdphone() {
        return ldphone;
    }

    public void setLdphone(String ldphone) {
        this.ldphone = ldphone;
    }

    public String getJccy() {
        return jccy;
    }

    public void setJccy(String jccy) {
        this.jccy = jccy;
    }

    public String getPtjcr() {
        return ptjcr;
    }

    public void setPtjcr(String ptjcr) {
        this.ptjcr = ptjcr;
    }

    public String getPtjcrPhone() {
        return ptjcrPhone;
    }

    public void setPtjcrPhone(String ptjcrPhone) {
        this.ptjcrPhone = ptjcrPhone;
    }

    public int getIsZj() {
        return isZj;
    }

    public void setIsZj(int isZj) {
        this.isZj = isZj;
    }

    public int getIsSj() {
        return isSj;
    }

    public void setIsSj(int isSj) {
        this.isSj = isSj;
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

    public int getCljl() {
        return cljl;
    }

    public void setCljl(int cljl) {
        this.cljl = cljl;
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
        parcel.writeInt(type);
        parcel.writeInt(jclb);
        parcel.writeString(jcstime);
        parcel.writeString(jcetime);
        parcel.writeString(ddld);
        parcel.writeString(ldphone);
        parcel.writeString(jccy);
        parcel.writeString(ptjcr);
        parcel.writeString(ptjcrPhone);
        parcel.writeInt(isZj);
        parcel.writeInt(isSj);
        parcel.writeString(content);
        parcel.writeString(filename);
        parcel.writeString(filepath);
        parcel.writeLong(fbr);
        parcel.writeString(fbsj);
        parcel.writeString(bz);
        parcel.writeString(gridId);
        parcel.writeString(question);
        parcel.writeInt(cljl);

    }

    public static final Parcelable.Creator<Xfaqjc> CREATOR = new Parcelable.Creator<Xfaqjc>() {
        public Xfaqjc createFromParcel(Parcel in) {
            return new Xfaqjc(in);
        }

        public Xfaqjc[] newArray(int size) {
            return new Xfaqjc[size];
        }
    };

    private Xfaqjc(Parcel in) {
        id = in.readLong();
        pId = in.readLong();
        type = in.readInt();
        jclb = in.readInt();
        jcstime = in.readString();
        jcetime = in.readString();
        ddld = in.readString();
        ldphone = in.readString();
        jccy = in.readString();
        ptjcr = in.readString();
        ptjcrPhone = in.readString();
        isZj = in.readInt();
        isSj = in.readInt();
        content = in.readString();
        filename = in.readString();
        filepath = in.readString();
        fbr = in.readLong();
        fbsj = in.readString();
        bz = in.readString();
        gridId = in.readString();
        question = in.readString();
        cljl = in.readInt();
    }

}
