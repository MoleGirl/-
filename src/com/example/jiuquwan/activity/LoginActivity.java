package com.example.jiuquwan.activity;

import com.example.jiuquwan.R;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuquwan.bean.User;
import com.example.jiuwuwan.base.BaseActivity;
import com.example.jiuwuwan.base.BaseInterface;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * ��½�����activity
 * 
 * @author Administrator
 *
 */
public class LoginActivity extends BaseActivity implements BaseInterface {

	private EditText uname, upass;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.act_login);
		initView();
		initData();
		initViewOper();

	}

	/**
	 * ��ת��ע�����
	 */
	public void onRegClick(View v) {
		startAct(RegActivity.class);
		//finish();
	}

	
	/**
	 * ��½
	 */
	public void onLoginClick(View v) {
//		 startAct(HomeActivity.class);
//         finish();
		//��ȡ���û���������
		String mname=getTvText(uname);
		String mpass=getTvText(upass);
		//�������ȵ�dialog
		dialogShow(false, null, null);
		//�ֻ������½
		BmobUser.loginByAccount(mname, mpass, new LogInListener<User>() {

		    @Override
		    public void done(User user, BmobException e) {
		    	//��½�������ʱ,�ر�dialog
		    	dialogDismiss();
		        if(user!=null){
		        	//�ѵ�ǰ��½���û������,����Application��u��
		        	GatherApplication.u=user;
		        	//��ǰ�û�֧����(�Ѿ������)��ĸ���
		        	//GatherApplication.payGatherIds=user.getPayGatherIds().size();
		            Log.i("smile","�û���½�ɹ�");
		            toast("��½�ɹ�");
		            startAct(HomeActivity.class);
		            finish();
		        }else{
		        	toast("�û������������");
		        }
		    }
		});
		
		
		
	}
	@Override
	public void initView() {
		uname=etById(R.id.act_login_uname);
		upass=etById(R.id.act_login_upass);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initViewOper() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		// ����Ǵ�ע����淵�ص�ʱ��,�жϻ����ǲ���������,�����Ϊ��ֱ����ת��������
		if (GatherApplication.u != null) {
			startAct(HomeActivity.class);
			finish();
		}
	}
	
	/**
	 * �Զ���½
	 */
	public void radio1(View v) {
		
		
	}
	/**
	 * ��ס����
	 */
	public void radio2(View v) {
		
	}

}
