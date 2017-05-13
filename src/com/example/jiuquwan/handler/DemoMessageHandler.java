package com.example.jiuquwan.handler;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
/**
 * 即时通讯
 * @author Administrator
 *
 */
public class DemoMessageHandler extends BmobIMMessageHandler {
	 @Override
	    public void onMessageReceive(final MessageEvent event) {
	        //当接收到服务器发来的消息时，此方法被调用
	    }

	    @Override
	    public void onOfflineReceive(final OfflineMessageEvent event) {
	        //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
	    }
}
