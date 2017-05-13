package com.example.jiuquwan.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class UserFriend extends BmobObject {
	
	//好用的集合
	private List<User> FriendIds;
	
	//发送的消息
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
