package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 民情直通车
 *
 * @author dbz
 */
public class Mqztc implements Parcelable {

    private String id;
    private String title;// 标题
    private String description;// 描述
    private String answer;// 回答
    private String answerMan;// 回答人
    private String answerTime;// 回答时间
    private String questionMan;// 问题人
    private String questionTime;// 问题时间
    private int isDel;// 删除标志

    private String answerManXm;
    private String questionManXm;

    private String fileName;
    private String filePath;
    private String address;

    public String getAnswerManXm() {
        return answerManXm;
    }

    public void setAnswerManXm(String answerManXm) {
        this.answerManXm = answerManXm;
    }

    public String getQuestionManXm() {
        return questionManXm;
    }

    public void setQuestionManXm(String questionManXm) {
        this.questionManXm = questionManXm;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public String getQuestionMan() {
        return questionMan;
    }

    public void setQuestionMan(String questionMan) {
        this.questionMan = questionMan;
    }

    public String getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(String questionTime) {
        this.questionTime = questionTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerMan() {
        return answerMan;
    }

    public void setAnswerMan(String answerMan) {
        this.answerMan = answerMan;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(answer);
        parcel.writeString(answerMan);
        parcel.writeString(answerTime);
        parcel.writeString(questionMan);
        parcel.writeString(questionTime);
        parcel.writeInt(isDel);
        parcel.writeString(fileName);
        parcel.writeString(filePath);
        parcel.writeString(address);
        parcel.writeString(answerManXm);
        parcel.writeString(questionManXm);
    }

    public static final Creator<Mqztc> CREATOR = new Creator<Mqztc>() {
        public Mqztc createFromParcel(Parcel in) {
            return new Mqztc(in);
        }

        public Mqztc[] newArray(int size) {
            return new Mqztc[size];
        }
    };

    private Mqztc(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        answer = in.readString();
        answerMan = in.readString();
        answerTime = in.readString();
        questionMan = in.readString();
        questionTime = in.readString();
        isDel = in.readInt();
        fileName = in.readString();
        filePath = in.readString();
        address = in.readString();
        answerManXm = in.readString();
        questionManXm = in.readString();
    }
}
