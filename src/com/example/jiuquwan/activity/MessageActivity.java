package com.example.jiuquwan.activity;

import com.example.jiuquwan.R;
import com.example.jiuwuwan.base.BaseActivity;
import com.example.jiuwuwan.base.BaseInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
/**
 * 消息界面,,该界面没有用到
 * @author Administrator
 *
 */
public class MessageActivity extends BaseActivity implements BaseInterface {
	private EditText et;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.act_message);
		
		initView();
		initData();
		initViewOper();
		
	}
	
	/**
	 *
	 */
	public void fasong(View v) {
		//参数action
		Intent intent =new Intent("cn.bmob.push.action.MESSAGE");
		intent.putExtra("message", et.getText().toString().trim());
		sendBroadcast(intent , null);
		
		
	}
	@Override
	public void initView() {
		et=etById(R.id.et);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initViewOper() {
		// TODO Auto-generated method stub

	}

}
