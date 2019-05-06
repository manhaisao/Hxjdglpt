package com.xzz.hxjdglpt.model;

/**
 * 网格统计
 * 
 * @author dbz
 *
 */
public class GridTj {
	private String id;
	private Integer hNum;
	private String area;
	private Integer rkNum;
	private int dynum;// 党员数量
	private int ncdbnum;// 农村低保
	private int csdbnum;// 城市低保
	private int cjnum;// 残疾人数量
	private int tkwb;// --特困(五保)供养
	private int zlj;// --80岁以上尊老金
	private int zdrq;// --信访诉求人员
	private int lset;// -- 留守儿童
	private int yfdx;// --优抚对象
	private int jsb;// --肇事肇祸精神病患者
	private int ylfnNum;// 育龄妇女
	private int jlfzNum;// 奖励扶助
	private int ldrkNum;// 流动人口
	private int jsknNum;// 计生困难
	private int dszn;// 独生子女
	private int sdjtNum;// 失独家庭
	private int dzqsn;// --重点青少年
	private int sqjd;// --社区戒毒人员
	private int jftjy;// --纠纷调解员
	private int jfxxy;// --纠纷信息员
	private int xj;// --邪教人员
	private int fzxcy;// --法制宣传员
	private int pfzyz;// --普法志愿者
	private int azbj;// --安置帮教人员
	private int sqfsMan;// 社区服刑
	private int flgw;// --法律顾问
	private int qynum;// 企业数量
	private int gtnum;// 个体户数量
	private int plotNum;// 小区数量
	private int jzgd;// --建筑工地
	private int tzsb;// --特种设备
	private int wxhxp;// --危险化学品
	private int cpy;// --成品油
	private String wgz;// 网格长
	private String wgzPhone;// 网格长电话
	private String sqgj;// 社区干警
	private String sqgjPhone;// 社区干警电话

	private String zm;
	private String vId;
	private Integer zNum;
	private String name;// 村名字

	private Integer wqNum;
	private int cjbcnum;// 残疾人本村数量
	private int sjghnum;// 残疾人挂户数量
	private String sqsjMan;// 社区矫正
	private String zsjjMan;// 刑释解教
	private String qsnwffz;// 青少年违法犯罪
	private int ylfnlc;// 育龄妇女流出人口
	private int ylfnlr;// 育龄妇女流入人口
	private int ylfnbcNum;// 育龄妇女本村人口
	private int ylfnghNum;// 育龄妇女挂户人口
	private String ghr;// 管护人
	private String ghrPhone;// 管护人电话
	private String hjbxs;// 后街背巷数
	private String xqwzswls;// 小区无主商住楼数
	private String ywxcy;// 义务宣传员
	private String ywxcyPhone;// 义务宣传员号码
	private String zrr;// 责任人
	private String phone;// 责任人电话
	private String zrrName;// 责任人
	private int wjh;// 违建户数量

	public int getWjh() {
		return wjh;
	}

	public void setWjh(int wjh) {
		this.wjh = wjh;
	}

	public int getPlotNum() {
		return plotNum;
	}

	public void setPlotNum(int plotNum) {
		this.plotNum = plotNum;
	}

	public String getZrrName() {
		return zrrName;
	}

	public void setZrrName(String zrrName) {
		this.zrrName = zrrName;
	}

	public int getDszn() {
		return dszn;
	}

	public void setDszn(int dszn) {
		this.dszn = dszn;
	}

	public String getId() {
		return id;
	}

	public String getZrr() {
		return zrr;
	}

	public void setZrr(String zrr) {
		this.zrr = zrr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getZm() {
		return zm;
	}

	public void setZm(String zm) {
		this.zm = zm;
	}

	public String getvId() {
		return vId;
	}

	public void setvId(String vId) {
		this.vId = vId;
	}

	public Integer getzNum() {
		return zNum;
	}

	public void setzNum(Integer zNum) {
		this.zNum = zNum;
	}

	public Integer getRkNum() {
		return rkNum;
	}

	public void setRkNum(Integer rkNum) {
		this.rkNum = rkNum;
	}

	public Integer gethNum() {
		return hNum;
	}

	public void sethNum(Integer hNum) {
		this.hNum = hNum;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getWqNum() {
		return wqNum;
	}

	public void setWqNum(Integer wqNum) {
		this.wqNum = wqNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDynum() {
		return dynum;
	}

	public void setDynum(int dynum) {
		this.dynum = dynum;
	}

	public int getNcdbnum() {
		return ncdbnum;
	}

	public void setNcdbnum(int ncdbnum) {
		this.ncdbnum = ncdbnum;
	}

	public int getCsdbnum() {
		return csdbnum;
	}

	public void setCsdbnum(int csdbnum) {
		this.csdbnum = csdbnum;
	}

	public int getCjnum() {
		return cjnum;
	}

	public void setCjnum(int cjnum) {
		this.cjnum = cjnum;
	}

	public int getCjbcnum() {
		return cjbcnum;
	}

	public void setCjbcnum(int cjbcnum) {
		this.cjbcnum = cjbcnum;
	}

	public int getSjghnum() {
		return sjghnum;
	}

	public void setSjghnum(int sjghnum) {
		this.sjghnum = sjghnum;
	}

	public int getZdrq() {
		return zdrq;
	}

	public void setZdrq(int zdrq) {
		this.zdrq = zdrq;
	}

	public int getSqfsMan() {
		return sqfsMan;
	}

	public void setSqfsMan(int sqfsMan) {
		this.sqfsMan = sqfsMan;
	}

	public String getSqsjMan() {
		return sqsjMan;
	}

	public void setSqsjMan(String sqsjMan) {
		this.sqsjMan = sqsjMan;
	}

	public String getZsjjMan() {
		return zsjjMan;
	}

	public void setZsjjMan(String zsjjMan) {
		this.zsjjMan = zsjjMan;
	}

	public String getQsnwffz() {
		return qsnwffz;
	}

	public void setQsnwffz(String qsnwffz) {
		this.qsnwffz = qsnwffz;
	}

	public int getYlfnNum() {
		return ylfnNum;
	}

	public void setYlfnNum(int ylfnNum) {
		this.ylfnNum = ylfnNum;
	}

	public int getJlfzNum() {
		return jlfzNum;
	}

	public void setJlfzNum(int jlfzNum) {
		this.jlfzNum = jlfzNum;
	}

	public int getLdrkNum() {
		return ldrkNum;
	}

	public void setLdrkNum(int ldrkNum) {
		this.ldrkNum = ldrkNum;
	}

	public int getSdjtNum() {
		return sdjtNum;
	}

	public void setSdjtNum(int sdjtNum) {
		this.sdjtNum = sdjtNum;
	}

	public int getJsknNum() {
		return jsknNum;
	}

	public void setJsknNum(int jsknNum) {
		this.jsknNum = jsknNum;
	}

	public int getYlfnlc() {
		return ylfnlc;
	}

	public void setYlfnlc(int ylfnlc) {
		this.ylfnlc = ylfnlc;
	}

	public int getYlfnlr() {
		return ylfnlr;
	}

	public void setYlfnlr(int ylfnlr) {
		this.ylfnlr = ylfnlr;
	}

	public int getYlfnbcNum() {
		return ylfnbcNum;
	}

	public void setYlfnbcNum(int ylfnbcNum) {
		this.ylfnbcNum = ylfnbcNum;
	}

	public int getYlfnghNum() {
		return ylfnghNum;
	}

	public void setYlfnghNum(int ylfnghNum) {
		this.ylfnghNum = ylfnghNum;
	}

	public int getQynum() {
		return qynum;
	}

	public void setQynum(int qynum) {
		this.qynum = qynum;
	}

	public int getGtnum() {
		return gtnum;
	}

	public void setGtnum(int gtnum) {
		this.gtnum = gtnum;
	}

	public String getGhr() {
		return ghr;
	}

	public void setGhr(String ghr) {
		this.ghr = ghr;
	}

	public String getGhrPhone() {
		return ghrPhone;
	}

	public void setGhrPhone(String ghrPhone) {
		this.ghrPhone = ghrPhone;
	}

	public String getHjbxs() {
		return hjbxs;
	}

	public void setHjbxs(String hjbxs) {
		this.hjbxs = hjbxs;
	}

	public String getXqwzswls() {
		return xqwzswls;
	}

	public void setXqwzswls(String xqwzswls) {
		this.xqwzswls = xqwzswls;
	}

	public String getYwxcy() {
		return ywxcy;
	}

	public void setYwxcy(String ywxcy) {
		this.ywxcy = ywxcy;
	}

	public String getYwxcyPhone() {
		return ywxcyPhone;
	}

	public void setYwxcyPhone(String ywxcyPhone) {
		this.ywxcyPhone = ywxcyPhone;
	}

	public int getTkwb() {
		return tkwb;
	}

	public void setTkwb(int tkwb) {
		this.tkwb = tkwb;
	}

	public int getZlj() {
		return zlj;
	}

	public void setZlj(int zlj) {
		this.zlj = zlj;
	}

	public int getLset() {
		return lset;
	}

	public void setLset(int lset) {
		this.lset = lset;
	}

	public int getYfdx() {
		return yfdx;
	}

	public void setYfdx(int yfdx) {
		this.yfdx = yfdx;
	}

	public int getJsb() {
		return jsb;
	}

	public void setJsb(int jsb) {
		this.jsb = jsb;
	}

	public int getDzqsn() {
		return dzqsn;
	}

	public void setDzqsn(int dzqsn) {
		this.dzqsn = dzqsn;
	}

	public int getSqjd() {
		return sqjd;
	}

	public void setSqjd(int sqjd) {
		this.sqjd = sqjd;
	}

	public int getJftjy() {
		return jftjy;
	}

	public void setJftjy(int jftjy) {
		this.jftjy = jftjy;
	}

	public int getJfxxy() {
		return jfxxy;
	}

	public void setJfxxy(int jfxxy) {
		this.jfxxy = jfxxy;
	}

	public int getXj() {
		return xj;
	}

	public void setXj(int xj) {
		this.xj = xj;
	}

	public int getFzxcy() {
		return fzxcy;
	}

	public void setFzxcy(int fzxcy) {
		this.fzxcy = fzxcy;
	}

	public int getPfzyz() {
		return pfzyz;
	}

	public void setPfzyz(int pfzyz) {
		this.pfzyz = pfzyz;
	}

	public int getAzbj() {
		return azbj;
	}

	public void setAzbj(int azbj) {
		this.azbj = azbj;
	}

	public int getFlgw() {
		return flgw;
	}

	public void setFlgw(int flgw) {
		this.flgw = flgw;
	}

	public int getJzgd() {
		return jzgd;
	}

	public void setJzgd(int jzgd) {
		this.jzgd = jzgd;
	}

	public int getTzsb() {
		return tzsb;
	}

	public void setTzsb(int tzsb) {
		this.tzsb = tzsb;
	}

	public int getWxhxp() {
		return wxhxp;
	}

	public void setWxhxp(int wxhxp) {
		this.wxhxp = wxhxp;
	}

	public int getCpy() {
		return cpy;
	}

	public void setCpy(int cpy) {
		this.cpy = cpy;
	}

	public String getWgz() {
		return wgz;
	}

	public void setWgz(String wgz) {
		this.wgz = wgz;
	}

	public String getWgzPhone() {
		return wgzPhone;
	}

	public void setWgzPhone(String wgzPhone) {
		this.wgzPhone = wgzPhone;
	}

	public String getSqgj() {
		return sqgj;
	}

	public void setSqgj(String sqgj) {
		this.sqgj = sqgj;
	}

	public String getSqgjPhone() {
		return sqgjPhone;
	}

	public void setSqgjPhone(String sqgjPhone) {
		this.sqgjPhone = sqgjPhone;
	}
}
