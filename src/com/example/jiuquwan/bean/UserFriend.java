package com.example.jiuquwan.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class UserFriend extends BmobObject {
	
	//���õļ���
	private List<User> FriendIds;
	
	//���͵���Ϣ
	private List<String> messages;

	
	public List<User> getFriendIds() {
		return FriendIds;
	}

	public void setFriendIds(List<User> friendIds) {
		FriendIds = friendIds;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	
	
	
}
