package com.example.jiuquwan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
/**
 * ��ȡ��Ϣ�Ĺ㲥
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
		//��������
		String text = sms.getDisplayMessageBody();
		Log.i("Log", "�յ�����:"+text);
		//[��ȥ��]������֤����%smscode%����Ч��Ϊ%ttl%���ӡ�������ʹ��%appname%����֤�롣
		text=text.substring(13,19);
		Log.i("Log", "��ȡ���������:"+text);
		try {
			Integer i=new Integer(text);
			//�����ȡ���Ķ��Ž�ȡ����6Ϊ������
			setSmsCode(text);
		} catch (Exception e) {
			
		}
	}
	/**
	 * ���ö�������
	 * ����˷�������,˵����������,���ҽ�ȡ������ַ���Ϊ����
	 * @param smsCode
	 */
	public static void setSmsCode(String smsCode) {
		//���ö�������
		SMSReceiver.smsCode = smsCode;
		//����۲��ߴ���,�򽫶������ݴ��ݸ��۲���
		if (listener!=null) {
			listener.onSmsLinstener(smsCode);
		}
	}
	/**
	 * ���ö��Ź۲���
	 * @param linstener
	 */
	public static void setonSmsLinstener(onSmsLinstener linstener){
		SMSReceiver.listener=linstener;
	}
	public interface onSmsLinstener{
		void onSmsLinstener(String smsCode);
	}
	
}
