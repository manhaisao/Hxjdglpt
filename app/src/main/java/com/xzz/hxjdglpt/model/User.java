package com.xzz.hxjdglpt.model;



import net.sourceforge.pinyin4j.PinyinHelper;

import java.io.Serializable;
import java.util.Comparator;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String userName;
    private String password;
    private int isDel;
    private String roleId;
    private String createTime;
    private String updateTime;
    private String token;
    private String createFk;
    private String updateFk;
    private int sex;
    private int lev;
    private String levName;
    private long parentId;
    private String picture;
    private String realName;
    private String phone;
    private String qqcode;
    private String email;
    private String miToken;
    private int isMI;
    private char tag;

    public char getTag() {
        return tag;
    }

    public void setTag(char tag) {
        this.tag = tag;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public User(String userId) {
        this.userId = userId;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQqcode() {
        return qqcode;
    }

    public void setQqcode(String qqcode) {
        this.qqcode = qqcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreateFk() {
        return createFk;
    }

    public void setCreateFk(String createFk) {
        this.createFk = createFk;
    }

    public String getUpdateFk() {
        return updateFk;
    }

    public void setUpdateFk(String updateFk) {
        this.updateFk = updateFk;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getLev() {
        return lev;
    }

    public void setLev(int lev) {
        this.lev = lev;
    }

    public String getLevName() {
        return levName;
    }

    public void setLevName(String levName) {
        this.levName = levName;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getMiToken() {
        return miToken;
    }

    public void setMiToken(String miToken) {
        this.miToken = miToken;
    }

    public int getIsMI() {
        return isMI;
    }

    public void setIsMI(int isMI) {
        this.isMI = isMI;
    }

    public static class ContactsComparactor implements Comparator<User> {

        @Override
        public int compare(User contacts1, User contacts2) {
            String name1 = contacts1.getRealName();
            String name2 = contacts2.getRealName();
            char char1 = upChar(getChar(name1.charAt(0)));
            char char2 = upChar(getChar(name2.charAt(0)));
            contacts1.setTag(char1);
            contacts2.setTag(char2);
            return char1 - char2;
        }

        private char getChar(char chineseCharacter) {

            String[] str = PinyinHelper.toHanyuPinyinStringArray(chineseCharacter);
            if (str == null) {
                return chineseCharacter;
            }
            // Log.i("whw", "str[0]="+str[0]);
            return str[0].charAt(0);
        }

        private char upChar(char c) {
            String str = String.valueOf(c).toUpperCase();
            return str.charAt(0);
        }

    }


}
