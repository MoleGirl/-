package com.example.jiuquwan.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobPointer;

public class GatherBean extends BmobObject {

	private String gatherClass;//�����  �û�ѡ�� ````
	private String gatherTitle;//�����   �û����� `````
	private String gatherTime;//���ʼʱ��   �û�ѡ�� ````
	private String gatherUserId;//������û�Id ������ѯ�û���  ϵͳ�Զ���ȡ����
	
	private String gatherCity;//����ڳ���   �û�ѡ��/����
	private String gatherSite;//����ϵص�   �û�ѡ��/���� 
	private String gatherRMB; //��۸�       �û����� ````
	private String gatherIntro;//���ϸ����  �û����� ````
	private BmobGeoPoint point;//���γ��   �û�ѡ��/����
	private List<String> praiseUsers;//��¼���޵��û���  //ϵͳ¼��
	//��ǰ���޵�����
	private Integer loveCount ;
	
	
	public Integer getLoveCount() {
		if (loveCount==null) {
			loveCount=0;
		}
		return loveCount;
	}
	//���ӱ��ص��������ķ���
	public void addloveCount(){
		++loveCount;
	} 
	//���ٱ��ص��������ķ���
	public void deleteloveCount(){
		--loveCount;
	} 
	
	public void setLoveCount(Integer loveCount) {
		this.loveCount = loveCount;
	}
	//private List<UserMsg> msgs;//�û���������   //ϵͳ¼��
	private List<BmobFile> gatherJPG;//�ͼƬ    //�û��ϴ�````
	private List<String> paymentUserId;//���븶����û�Id   ϵͳ¼��
	private List<String> startUserId;//���ʼ ������ֳ����û�Id   ϵͳ¼��
	private Boolean flag;// ��ǰ�״̬(�Ƿ�ʼ)   ϵͳ¼��
	
	
	
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
		//�жϵ����Ƿ�Ϊ��
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
	 * ��ȡ��ǰ��Ѿ�������û���id�ļ���
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
