package com.example.jiuquwan.bean;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {
	//ͷ��ĵ�ַ
	private BmobFile HeadBit;
	//�ǳ�
	private String nickName;
	//����
	private List<String> loveGatherIds;
	//��ǰ�û�֧���Ļ��id�ļ���
	private List<String> payGatherIds;
	
	public List<String> getPayGatherIds() {
		return payGatherIds;
	}
	public void setPayGatherIds(List<String> payGatherIds) {
		this.payGatherIds = payGatherIds;
	}
	public List<String> getLoveGatherIds() {
		return loveGatherIds;
	}
	public void setLoveGatherIds(List<String> loveGatherIds) {
		this.loveGatherIds = loveGatherIds;
	}
	public BmobFile getHeadBit() {
		return HeadBit;
	}
	public void setHeadBit(BmobFile headBit) {
		HeadBit = headBit;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public User(BmobFile headBit, String nickName) {
		super();
		HeadBit = headBit;
		this.nickName = nickName;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
	
	
	
	 
}
