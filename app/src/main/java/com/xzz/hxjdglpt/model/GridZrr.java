package com.xzz.hxjdglpt.model;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Comparator;

public class GridZrr {

    private long userId;
    private String userName;
    private String gridId;
    private String vName;
    private String realName;
    private long vId;
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

    public long getvId() {
        return vId;
    }

    public void setvId(long vId) {
        this.vId = vId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public static class ContactsComparactor implements Comparator<GridZrr> {

        @Override
        public int compare(GridZrr contacts1, GridZrr contacts2) {
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
