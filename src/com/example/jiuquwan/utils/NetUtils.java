package com.example.jiuquwan.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {

	/**
	 * 此方法用来判断当前的手机网络状态
	 * @return  false 没网 true 有网
	 */
			
	public static boolean getNetState(Context context){
		//获取系统服务
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//当前网络信息
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info==null) {
			return false;
		}
		/**
		 * 返回当前的网络状态
		 */
		return info.isConnected();
	}
}
