package com.example.jiuquwan.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {

	/**
	 * �˷��������жϵ�ǰ���ֻ�����״̬
	 * @return  false û�� true ����
	 */
			
	public static boolean getNetState(Context context){
		//��ȡϵͳ����
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//��ǰ������Ϣ
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info==null) {
			return false;
		}
		/**
		 * ���ص�ǰ������״̬
		 */
		return info.isConnected();
	}
}
