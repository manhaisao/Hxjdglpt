package com.xzz.hxjdglpt.model.qly;

import java.util.List;

public class VUC {

	private String name;// VCU名称

	private String vcuId;// VCU编号

	private String status;// 设备状态值:0:不在线;1:在线;2:休眠;3:（镜头）能力未上报;4:不在线停机;5:在线停机;6:休眠停机

	private List<Camera> cameras;// 视频列表

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVcuId() {
		return vcuId;
	}

	public void setVcuId(String vcuId) {
		this.vcuId = vcuId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Camera> getCameras() {
		return cameras;
	}

	public void setCameras(List<Camera> cameras) {
		this.cameras = cameras;
	}

}
