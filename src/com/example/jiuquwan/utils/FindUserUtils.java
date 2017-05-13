package com.example.jiuquwan.utils;

import java.util.List;

import com.example.jiuquwan.bean.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FindUserUtils {

	
	//public static FindUserListener listener;

	/**
	 * 静态工具类
	 * 用来通过id 查询用户
	 * 参数1 用户id
	 * 参数2 观察者
	 * @param objectId
	 */
	public static void findUserById(String objectId,final FindUserListener listener){
		//第一步查询用户
		BmobQuery<User> query=new BmobQuery<>();
		query.addWhereEqualTo("objectId", objectId);
		query.findObjects(new FindListener<User>() {
			//只有一条数据符合,objectId是唯一的
			@Override
			public void done(List<User> user, BmobException e) {
				if (e==null) {
					listener.toUser(user.get(0));
				}
			}
		});
		//第二步,将结果通知给linstener
		
	}
	
	
	
	/**
	 * 观察者回调接口观察查询用户的结果产生
	 * @author Administrator
	 *
	 */
	public interface FindUserListener{
		
		void toUser(User user);
	}
	
}
