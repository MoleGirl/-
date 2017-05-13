package com.example.jiuquwan.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobPointer;

public class GatherBean extends BmobObject {

	private String gatherClass;//活动类型  用户选择 ````
	private String gatherTitle;//活动名称   用户输入 `````
	private String gatherTime;//活动开始时间   用户选择 ````
	private String gatherUserId;//活动发起用户Id 用来查询用户表  系统自动获取填入
	
	private String gatherCity;//活动所在城市   用户选择/输入
	private String gatherSite;//活动集合地点   用户选择/输入 
	private String gatherRMB; //活动价格       用户输入 ````
	private String gatherIntro;//活动详细介绍  用户输入 ````
	private BmobGeoPoint point;//活动经纬度   用户选择/输入
	private List<String> praiseUsers;//记录点赞的用户名  //系统录入
	//当前点赞的人数
	private Integer loveCount ;
	
	
	public Integer getLoveCount() {
		if (loveCount==null) {
			loveCount=0;
		}
		return loveCount;
	}
	//增加本地点赞数量的方法
	public void addloveCount(){
		++loveCount;
	} 
	//减少本地点赞数量的方法
	public void deleteloveCount(){
		--loveCount;
	} 
	
	public void setLoveCount(Integer loveCount) {
		this.loveCount = loveCount;
	}
	//private List<UserMsg> msgs;//用户评论内容   //系统录入
	private List<BmobFile> gatherJPG;//活动图片    //用户上传````
	private List<String> paymentUserId;//参与付款的用户Id   系统录入
	private List<String> startUserId;//活动开始 ，到活动现场的用户Id   系统录入
	private Boolean flag;// 当前活动状态(是否开始)   系统录入
	
	
	
	public BmobGeoPoint getPoint() {
		return point;
	}
	public void setPoint(BmobGeoPoint point) {
		this.point = point;
	}
	public String getGatherClass() {
		return gatherClass;
	}
	public void setGatherClass(String gatherClass) {
		this.gatherClass = gatherClass;
	}
	public String getGatherTitle() {
		return gatherTitle;
	}
	public void setGatherTitle(String gatherTitle) {
		this.gatherTitle = gatherTitle;
	}
	public String getGatherTime() {
		return gatherTime;
	}
	public void setGatherTime(String gatherTime) {
		this.gatherTime = gatherTime;
	}
	public String getGatherUserId() {
		return gatherUserId;
	}
	public void setGatherUserId(String gatherUserId) {
		this.gatherUserId = gatherUserId;
	}
	public String getGatherCity() {
		return gatherCity;
	}
	public void setGatherCity(String gatherCity) {
		this.gatherCity = gatherCity;
	}
	public String getGatherSite() {
		return gatherSite;
	}
	public void setGatherSite(String gatherSite) {
		this.gatherSite = gatherSite;
	}
	public String getGatherRMB() {
		return gatherRMB;
	}
	public void setGatherRMB(String gatherRMB) {
		this.gatherRMB = gatherRMB;
	}
	public String getGatherIntro() {
		return gatherIntro;
	}
	public void setGatherIntro(String gatherIntro) {
		this.gatherIntro = gatherIntro;
	}
	public List<String> getPraiseUsers() {
		//判断点赞是否为空
		if (praiseUsers==null) {
			praiseUsers = new ArrayList<>();
		}
		return praiseUsers;
	}
	public void setPraiseUsers(List<String> praiseUsers) {
		this.praiseUsers = praiseUsers;
	}
	public List<BmobFile> getGatherJPG() {
		return gatherJPG;
	}
	public void setGatherJPG(List<BmobFile> gatherJPG) {
		this.gatherJPG = gatherJPG;
	}
	/**
	 * 获取当前活动已经参与的用户的id的集合
	 * @return
	 */
	public List<String> getPaymentUserId() {
		if (paymentUserId==null) {
			paymentUserId=new ArrayList<>();
			
		}
		return paymentUserId;
	}
	public void setPaymentUserId(List<String> paymentUserId) {
		this.paymentUserId = paymentUserId;
	}
	public List<String> getStartUserId() {
		return startUserId;
	}
	public void setStartUserId(List<String> startUserId) {
		this.startUserId = startUserId;
	}
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	public GatherBean(String gatherClass, String gatherTitle, String gatherTime, String gatherUserId, String gatherCity,
			String gatherSite, String gatherRMB, String gatherIntro, List<String> praiseUsers, List<BmobFile> gatherJPG,
			List<String> paymentUserId, List<String> startUserId, Boolean flag) {
		super();
		this.gatherClass = gatherClass;
		this.gatherTitle = gatherTitle;
		this.gatherTime = gatherTime;
		this.gatherUserId = gatherUserId;
		this.gatherCity = gatherCity;
		this.gatherSite = gatherSite;
		this.gatherRMB = gatherRMB;
		this.gatherIntro = gatherIntro;
		this.praiseUsers = praiseUsers;
		this.gatherJPG = gatherJPG;
		this.paymentUserId = paymentUserId;
		this.startUserId = startUserId;
		this.flag = flag;
	}
	public GatherBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GatherBean(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
}
