package com.xzz.hxjdglpt.model.qly;

public class CameraRights {
	private String recordView;// 录像浏览（查询录像和播放） 0：无权限 1：有权限

	private String ptz;// 云台控制权限（0-9个等级） -1表示无权限 其他是云镜控制优先级（0-9） 9标识控制优先级最高

	private String alarmManage;// 告警管理权限 0：无权限 1：有权限

	public String getRecordView() {
		return recordView;
	}

	public void setRecordView(String recordView) {
		this.recordView = recordView;
	}

	public String getPtz() {
		return ptz;
	}

	public void setPtz(String ptz) {
		this.ptz = ptz;
	}

	public String getAlarmManage() {
		return alarmManage;
	}

	public void setAlarmManage(String alarmManage) {
		this.alarmManage = alarmManage;
	}
}
