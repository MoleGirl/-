package com.example.jiuquwan.utils;

import java.util.List;

import com.example.jiuquwan.bean.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FindUserUtils {

	
	//public static FindUserListener listener;

	/**
	 * ��̬������
	 * ����ͨ��id ��ѯ�û�
	 * ����1 �û�id
	 * ����2 �۲���
	 * @param objectId
	 */
	public static void findUserById(String objectId,final FindUserListener listener){
		//��һ����ѯ�û�
		BmobQuery<User> query=new BmobQuery<>();
		query.addWhereEqualTo("objectId", objectId);
		query.findObjects(new FindListener<User>() {
			//ֻ��һ�����ݷ���,objectId��Ψһ��
			@Override
			public void done(List<User> user, BmobException e) {
				if (e==null) {
					listener.toUser(user.get(0));
				}
			}
		});
		//�ڶ���,�����֪ͨ��linstener
		
	}
	
	
	
	/**
	 * �۲��߻ص��ӿڹ۲��ѯ�û��Ľ������
	 * @author Administrator
	 *
	 */
	public interface FindUserListener{
		
		void toUser(User user);
	}
	
}
