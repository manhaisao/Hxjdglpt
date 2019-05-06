package com.xzz.hxjdglpt.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2019\4\14 0014.
 *
 *  "id": 2,
 "plotId": 2,
 "name": "a",
 "ownerName": "a",
 "phone": "a",
 "area": 1.1,
 "fileName": "a",
 "filePath": "a",
 "comment": "a"
 *
 */

public class Jyh implements Serializable {

    private int id;

    private int plotId;

    private String name;

    private String ownerName;

    private String phone;

    private String area;

    private String fileName;

    private String filePath;

    private String comment;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlotId() {
        return plotId;
    }

    public void setPlotId(int plotId) {
        this.plotId = plotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
