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
 * 登陆界面的activity
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
	 * 跳转到注册界面
	 */
	public void onRegClick(View v) {
		startAct(RegActivity.class);
		//finish();
	}

	
	/**
	 * 登陆
	 */
	public void onLoginClick(View v) {
//		 startAct(HomeActivity.class);
//         finish();
		//获取到用户名和密码
		String mname=getTvText(uname);
		String mpass=getTvText(upass);
		//弹出进度的dialog
		dialogShow(false, null, null);
		//手机号码登陆
		BmobUser.loginByAccount(mname, mpass, new LogInListener<User>() {

		    @Override
		    public void done(User user, BmobException e) {
		    	//登陆结果返回时,关闭dialog
		    	dialogDismiss();
		        if(user!=null){
		        	//把当前登陆的用户对象放,放入Application的u中
		        	GatherApplication.u=user;
		        	//当前用户支付的(已经参与的)活动的个数
		        	//GatherApplication.payGatherIds=user.getPayGatherIds().size();
		            Log.i("smile","用户登陆成功");
		            toast("登陆成功");
		            startAct(HomeActivity.class);
		            finish();
		        }else{
		        	toast("用户名或密码错误");
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
		// 如果是从注册界面返回的时候,判断缓存是不是有数据,如果不为空直接跳转到主界面
		if (GatherApplication.u != null) {
			startAct(HomeActivity.class);
			finish();
		}
	}
	
	/**
	 * 自动登陆
	 */
	public void radio1(View v) {
		
		
	}
	/**
	 * 记住密码
	 */
	public void radio2(View v) {
		
	}

}
