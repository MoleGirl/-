package com.example.jiuquwan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import cn.bmob.push.PushConstants;

/**
 * ��Ϣ���͵ķ���
 * @author Administrator
 *
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Log.d("bmob", "�ͻ����յ��������ݣ�"+intent.getStringExtra("message"));
        }
	}

}
