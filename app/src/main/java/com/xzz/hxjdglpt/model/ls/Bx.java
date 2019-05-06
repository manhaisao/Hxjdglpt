package com.xzz.hxjdglpt.model.ls;

import java.io.Serializable;

public class Bx implements Serializable{

    private int id;

    private String type;

    private String content;

    private String bumen;

    private String phone;

    private String fzr;

    private String fzrphone;

    private String jbr;

    private String jbrphone;

    private String zhineng;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
    public void setBumen(String bumen){
        this.bumen = bumen;
    }
    public String getBumen(){
        return this.bumen;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhone(){
        return this.phone;
    }
    public void setFzr(String fzr){
        this.fzr = fzr;
    }
    public String getFzr(){
        return this.fzr;
    }
    public void setFzrphone(String fzrphone){
        this.fzrphone = fzrphone;
    }
    public String getFzrphone(){
        return this.fzrphone;
    }
    public void setJbr(String jbr){
        this.jbr = jbr;
    }
    public String getJbr(){
        return this.jbr;
    }
    public void setJbrphone(String jbrphone){
        this.jbrphone = jbrphone;
    }
    public String getJbrphone(){
        return this.jbrphone;
    }
    public void setZhineng(String zhineng){
        this.zhineng = zhineng;
    }
    public String getZhineng(){
        return this.zhineng;
    }

}
