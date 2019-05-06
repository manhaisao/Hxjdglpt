package com.xzz.hxjdglpt.model.qly;

public class SubjectInfo {

	private String subjectId;// 设备组ID

	private String parentId;// 父设备组ID，顶级设备组的父ID为空

	private String groupType;// 设备组类型， AREA_STATION：区域站点; CLIENT_STATION：客户站点
								// ;DEVICE_GROUP：设备分组

	private String subjectName;// 设备组名称

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

}
