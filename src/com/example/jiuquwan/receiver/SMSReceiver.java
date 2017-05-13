package com.example.jiuquwan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
/**
 * 读取短息的广播
 * @author Administrator
 *
 */
public class SMSReceiver extends BroadcastReceiver {

	private static String smsCode;
	private static onSmsLinstener listener;
	
	

	@Override
	public void onReceive(Context arg0, Intent arg1) {

		Bundle bd = arg1.getExtras();
		Object[] obj = (Object[]) bd.get("pdus");
		byte[] bytes=(byte[]) obj[0];
		SmsMessage sms = SmsMessage.createFromPdu(bytes);
		//短信内容
		String text = sms.getDisplayMessageBody();
		Log.i("Log", "收到短信:"+text);
		//[就去玩]您的验证码是%smscode%，有效期为%ttl%分钟。您正在使用%appname%的验证码。
		text=text.substring(13,19);
		Log.i("Log", "截取过后的数字:"+text);
		try {
			Integer i=new Integer(text);
			//如果获取到的短信截取后是6为的数字
			setSmsCode(text);
		} catch (Exception e) {
			
		}
	}
	/**
	 * 设置短信内容
	 * 如果此方法触发,说明短信来了,并且截取过后的字符串为数字
	 * @param smsCode
	 */
	public static void setSmsCode(String smsCode) {
		//设置短信内容
		SMSReceiver.smsCode = smsCode;
		//如果观察者存在,则将短信内容传递给观察者
		if (listener!=null) {
			listener.onSmsLinstener(smsCode);
		}
	}
	/**
	 * 设置短信观察者
	 * @param linstener
	 */
	public static void setonSmsLinstener(onSmsLinstener linstener){
		SMSReceiver.listener=linstener;
	}
	public interface onSmsLinstener{
		void onSmsLinstener(String smsCode);
	}
	
}
