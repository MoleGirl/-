package com.example.jiuquwan.handler;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
/**
 * ��ʱͨѶ
 * @author Administrator
 *
 */
public class DemoMessageHandler extends BmobIMMessageHandler {
	 @Override
	    public void onMessageReceive(final MessageEvent event) {
	        //�����յ���������������Ϣʱ���˷���������
	    }

	    @Override
	    public void onOfflineReceive(final OfflineMessageEvent event) {
	        //ÿ�ε���connect����ʱ���ѯһ��������Ϣ������У��˷����ᱻ����
	    }
}
