package com.xzz.hxjdglpt.model;

import java.io.Serializable;
import java.util.Date;

public class Menu implements Serializable {
    private static final long serialVersionUID = 2L;
    private String menuId;
    private String menuName;
    private long parentId;
    private int subMenuSort;
    private String menuUrl;
    private int isDel;
    private String createTime;
    private String updateTime;

    public Menu() {
    }

    public Menu(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getSubMenuSort() {
        return subMenuSort;
    }

    public void setSubMenuSort(int subMenuSort) {
        this.subMenuSort = subMenuSort;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
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
}
