package com.xzz.hxjdglpt.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 2、肇事肇祸精神病患者-管控小组 3、重点青少年-管控小组 4、社区戒毒人员-管控小组 5、社区服刑人员-矫正小组 6、安置帮教人员-帮教小组
 * 7、邪教人员-管控小组
 *
 * @author Administrator
 */
public class ZfzzGroup implements Parcelable {

    private long id;

    private String name;//

    private String ryxz;// 人员性质----1，社区医生 2，社区民警 3，村组干部 4，监管人

    private int type;// 类型---- 2,肇事肇祸精神病患者 3,重点青少年 4,社区戒毒人员 5,社区服刑人员 6,安置帮教人员
    // 7,邪教人员

    private long pId;// 外键----肇事肇祸精神病患者 ,重点青少年 ,社区戒毒人员 ,社区服刑人员 ,安置帮教人员 ,邪教人员的ID

    private String phone;// 电话

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRyxz() {
        return ryxz;
    }

    public void setRyxz(String ryxz) {
        this.ryxz = ryxz;
    }

    public long getpId() {
        return pId;
    }

    public void setpId(long pId) {
        this.pId = pId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(ryxz);
        parcel.writeInt(type);
        parcel.writeLong(pId);
        parcel.writeString(phone);
    }

    public static final Parcelable.Creator<ZfzzGroup> CREATOR = new Parcelable.Creator<ZfzzGroup>
            () {
        public ZfzzGroup createFromParcel(Parcel in) {
            return new ZfzzGroup(in);
        }

        public ZfzzGroup[] newArray(int size) {
            return new ZfzzGroup[size];
        }
    };

    private ZfzzGroup(Parcel in) {
        id = in.readLong();
        name = in.readString();
        ryxz = in.readString();
        type = in.readInt();
        pId = in.readLong();
        phone = in.readString();
    }
}
