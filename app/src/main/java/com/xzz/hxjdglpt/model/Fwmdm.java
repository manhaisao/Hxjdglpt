package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * 服务面对面
 *
 * @author dbz
 */
public class Fwmdm implements Parcelable {

    private String id;
    private String title;// 标题
    private String description;// 描述
    private String answer;// 回答
    private String answerMan;// 回答人
    private String answerTime;// 回答时间
    private String questionMan;// 问题人
    private String questionTime;// 问题时间
    private int isDel;// 删除标志

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
    }

    public static final Parcelable.Creator<Fwmdm> CREATOR = new Parcelable.Creator<Fwmdm>() {
        public Fwmdm createFromParcel(Parcel in) {
            return new Fwmdm(in);
        }

        public Fwmdm[] newArray(int size) {
            return new Fwmdm[size];
        }
    };

    private Fwmdm(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        answer = in.readString();
        answerMan = in.readString();
        answerTime = in.readString();
        questionMan = in.readString();
        questionTime = in.readString();
        isDel = in.readInt();
    }
}
