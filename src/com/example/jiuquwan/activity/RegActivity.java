package com.example.jiuquwan.activity;

import com.example.jiuquwan.R;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuquwan.bean.User;
import com.example.jiuquwan.receiver.SMSReceiver;
import com.example.jiuquwan.receiver.SMSReceiver.onSmsLinstener;
import com.example.jiuwuwan.base.BaseActivity;
import com.example.jiuwuwan.base.BaseInterface;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
/**
 * 注册界面的activity
 * @author Administrator
 *
 */
import android.widget.EditText;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册的界面
 * @author Administrator
 *
 */
public class RegActivity extends BaseActivity implements BaseInterface {

	private EditText mPhone, mNickname, mUpass1, mUpass2, mUcode;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.act_reg);
		initView();
		initData();
		initViewOper();

	}


	@Override
	public void initView() {
		// 电话号码
		mPhone = etById(R.id.phoneNumber);
		// 昵称
		mNickname = etById(R.id.nickName);
		// 密码1
		mUpass1 = etById(R.id.upass1);
		// 重复密码
		mUpass2 = etById(R.id.upass2);
		// 手机验证码
		mUcode = etById(R.id.ucode);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initViewOper() {
		// TODO Auto-generated method stub

	}

	/**
	 * 获取验证码
	 */
	public void onGetCodeClick(View v) {
		final Button but = (Button) v;
		// 获取手机号码框中的号码
		String phoneNumber = getTvText(mPhone);
		Log.i("Log", "输入的手机号码是:" + phoneNumber);
		if (phoneNumber.length() != 11) {
			toast("请检查手机号码!");
			return;
		}
		// 设置按钮不可点击
		but.setClickable(false);
		but.setTextColor(Color.parseColor("#a2a2a2"));
		// 倒计时
		CountDownTimer timer = new CountDownTimer(60000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				but.setText((millisUntilFinished / 1000) + "s");
			}

			@Override
			public void onFinish() {
				// 还原初始 文本
				but.setText("获取验证码");
				// 设置按钮可以点击
				but.setClickable(true);
				// 还原初始颜色
				but.setTextColor(Color.parseColor("#000000"));
			}
		};
		timer.start();
		BmobSMS.requestSMSCode(this, phoneNumber, "就去玩", new RequestSMSCodeListener() {

			@Override
			public void done(Integer smsId, BmobException ex) {
				// TODO Auto-generated method stub
				if (ex == null) {// 验证码发送成功
					Log.i("bmob", "短信id：" + smsId);// 用于查询本次短信发送详情
				}
			}
		});
		// 调用短信观察者,当短信来了截取之后,设置到验证码的输入框中
		SMSReceiver.setonSmsLinstener(new onSmsLinstener() {

			@Override
			public void onSmsLinstener(String smsCode) {
				mUcode.setText(smsCode);
			}
		});

	}

	/**
	 * 返回到登陆界面
	 */
	public void regBack(View v) {
		// startAct(LoginActivity.class);
		//销毁观察者
		destroyAct();
		finish();
	}

	/**
	 * 注册
	 */
	public void onreg(View v) {
		//获取手机号码
		final String phoneNumber =getTvText(mPhone);
		//验证码
		String code =getTvText(mUcode);
		BmobSMS.verifySmsCode(this, phoneNumber, code, new VerifySMSCodeListener() {

			@Override
			public void done(BmobException ex) {
				// TODO Auto-generated method stub
				if (ex == null) {// 短信验证码已验证成功
					Log.i("bmob", "验证通过");
					//获取昵称
					String nikeName=getTvText(mNickname);
					//获取密码
					String pass1=getTvText(mUpass1);
					//获取重复密码
					String pass2=getTvText(mUpass2);
					if (pass1.length()<6||pass1.length()>18) {
						toast("密码不符合要求,6-18位字符");
						return;
					}
					if (!pass1.equals(pass2)) {
						toast("两次密码不一致");
						return;
					}
					//验证通过后弹出dialog
					dialogShow(false, null, null);
					User u=new User();
					u.setUsername(phoneNumber);
					u.setMobilePhoneNumber(phoneNumber);
					u.setPassword(pass1);
					u.setNickName(nikeName);
					//设置短信已经验证通过
					u.setMobilePhoneNumberVerified(true);
					//注册
					u.signUp(new SaveListener<User>() {
						@Override
						public void done(User user, cn.bmob.v3.exception.BmobException arg1) {
							//无论注册成功与失败,取消dialog
							dialogDismiss();
							if (arg1==null) {
								toast("注册成功!");
								//销毁观察者
								destroyAct();
								//加入到缓存中
								GatherApplication.u=user;
								//关闭本界面
								finish();
							}else{
								toast("注册失败!原因"+arg1.getMessage());
								
								
							}
						}
					});
					
				} else {
					Log.i("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
					toast("验证码错误");
				}
			}
		});
	}
	

	// 销毁的方法
	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyAct();
	}

	private void destroyAct() {
		//当页面销毁的时候注销观察者
		SMSReceiver.setonSmsLinstener(null);
	}
	

}
