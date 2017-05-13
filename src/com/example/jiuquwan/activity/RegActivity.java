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
 * ע������activity
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
 * ע��Ľ���
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
		// �绰����
		mPhone = etById(R.id.phoneNumber);
		// �ǳ�
		mNickname = etById(R.id.nickName);
		// ����1
		mUpass1 = etById(R.id.upass1);
		// �ظ�����
		mUpass2 = etById(R.id.upass2);
		// �ֻ���֤��
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
	 * ��ȡ��֤��
	 */
	public void onGetCodeClick(View v) {
		final Button but = (Button) v;
		// ��ȡ�ֻ�������еĺ���
		String phoneNumber = getTvText(mPhone);
		Log.i("Log", "������ֻ�������:" + phoneNumber);
		if (phoneNumber.length() != 11) {
			toast("�����ֻ�����!");
			return;
		}
		// ���ð�ť���ɵ��
		but.setClickable(false);
		but.setTextColor(Color.parseColor("#a2a2a2"));
		// ����ʱ
		CountDownTimer timer = new CountDownTimer(60000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				but.setText((millisUntilFinished / 1000) + "s");
			}

			@Override
			public void onFinish() {
				// ��ԭ��ʼ �ı�
				but.setText("��ȡ��֤��");
				// ���ð�ť���Ե��
				but.setClickable(true);
				// ��ԭ��ʼ��ɫ
				but.setTextColor(Color.parseColor("#000000"));
			}
		};
		timer.start();
		BmobSMS.requestSMSCode(this, phoneNumber, "��ȥ��", new RequestSMSCodeListener() {

			@Override
			public void done(Integer smsId, BmobException ex) {
				// TODO Auto-generated method stub
				if (ex == null) {// ��֤�뷢�ͳɹ�
					Log.i("bmob", "����id��" + smsId);// ���ڲ�ѯ���ζ��ŷ�������
				}
			}
		});
		// ���ö��Ź۲���,���������˽�ȡ֮��,���õ���֤����������
		SMSReceiver.setonSmsLinstener(new onSmsLinstener() {

			@Override
			public void onSmsLinstener(String smsCode) {
				mUcode.setText(smsCode);
			}
		});

	}

	/**
	 * ���ص���½����
	 */
	public void regBack(View v) {
		// startAct(LoginActivity.class);
		//���ٹ۲���
		destroyAct();
		finish();
	}

	/**
	 * ע��
	 */
	public void onreg(View v) {
		//��ȡ�ֻ�����
		final String phoneNumber =getTvText(mPhone);
		//��֤��
		String code =getTvText(mUcode);
		BmobSMS.verifySmsCode(this, phoneNumber, code, new VerifySMSCodeListener() {

			@Override
			public void done(BmobException ex) {
				// TODO Auto-generated method stub
				if (ex == null) {// ������֤������֤�ɹ�
					Log.i("bmob", "��֤ͨ��");
					//��ȡ�ǳ�
					String nikeName=getTvText(mNickname);
					//��ȡ����
					String pass1=getTvText(mUpass1);
					//��ȡ�ظ�����
					String pass2=getTvText(mUpass2);
					if (pass1.length()<6||pass1.length()>18) {
						toast("���벻����Ҫ��,6-18λ�ַ�");
						return;
					}
					if (!pass1.equals(pass2)) {
						toast("�������벻һ��");
						return;
					}
					//��֤ͨ���󵯳�dialog
					dialogShow(false, null, null);
					User u=new User();
					u.setUsername(phoneNumber);
					u.setMobilePhoneNumber(phoneNumber);
					u.setPassword(pass1);
					u.setNickName(nikeName);
					//���ö����Ѿ���֤ͨ��
					u.setMobilePhoneNumberVerified(true);
					//ע��
					u.signUp(new SaveListener<User>() {
						@Override
						public void done(User user, cn.bmob.v3.exception.BmobException arg1) {
							//����ע��ɹ���ʧ��,ȡ��dialog
							dialogDismiss();
							if (arg1==null) {
								toast("ע��ɹ�!");
								//���ٹ۲���
								destroyAct();
								//���뵽������
								GatherApplication.u=user;
								//�رձ�����
								finish();
							}else{
								toast("ע��ʧ��!ԭ��"+arg1.getMessage());
								
								
							}
						}
					});
					
				} else {
					Log.i("bmob", "��֤ʧ�ܣ�code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
					toast("��֤�����");
				}
			}
		});
	}
	

	// ���ٵķ���
	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyAct();
	}

	private void destroyAct() {
		//��ҳ�����ٵ�ʱ��ע���۲���
		SMSReceiver.setonSmsLinstener(null);
	}
	

}
