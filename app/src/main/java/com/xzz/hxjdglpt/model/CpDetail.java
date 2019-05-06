package com.xzz.hxjdglpt.model;

/**
 * Created by Administrator on 2019\3\30 0030.
 */


/**
 *  {
 "id": 2,
 "standardId": 1,
 "investigateContent": "内容2",
 "investigateStandard": "标准2",
 "investigateResult": "00",
 "createTime": "2019-04-09 23:03:10.0",
 "updateTime": null,
 "createUser": "1039",
 "updateUser": null,
 "createUserName": "zc",
 "updateUserName": null,
 "standardName": "标准名称",
 "remark": "标准备注"
 }
 */
public class CpDetail {
    private String createTime;

    private String createUser;

    private String createUserName;

    private String updateUserName;

    private String standardName;

    private String remark;

    private int id;

    private String investigateContent;

    private String investigateResult;
    private String investigateStandard;

    private int standardId;

    private String updateTime;

    private String updateUser;

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvestigateContent() {
        return investigateContent;
    }

    public void setInvestigateContent(String investigateContent) {
        this.investigateContent = investigateContent;
    }

    public String getInvestigateResult() {
        return investigateResult;
    }

    public void setInvestigateResult(String investigateResult) {
        this.investigateResult = investigateResult;
    }

    public String getInvestigateStandard() {
        return investigateStandard;
    }

    public void setInvestigateStandard(String investigateStandard) {
        this.investigateStandard = investigateStandard;
    }

    public int getStandardId() {
        return standardId;
    }

    public void setStandardId(int standardId) {
        this.standardId = standardId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
