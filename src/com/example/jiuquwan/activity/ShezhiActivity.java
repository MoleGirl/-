package com.example.jiuquwan.activity;

import com.example.jiuquwan.R;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuwuwan.base.BaseActivity;
import com.example.jiuwuwan.base.BaseInterface;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
/**
 * ���ý����activity
 * @author Administrator
 *
 */
public class ShezhiActivity extends BaseActivity implements BaseInterface {
	//������޸�����,�޸��ǳ�
	private TextView pass,nickname;
	//���������,ԭʼ����,,������,,������2
	private EditText pass1,pass2,pass3,nickname1;
	private String mpass1,mpass2,mpass3,mnickname1;
	//���ص�lin1,lin2
	private LinearLayout lin1,lin2;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.shezhi);
		initView();
		initData();
		initViewOper();
		
	}
	
	
	@Override
	public void initView() {
		pass=tvById(R.id.shezhi_pass);
		nickname=tvById(R.id.shezhi_nickname);
		
		pass1=(EditText) findViewById(R.id.shezhi_pass1);
		pass2=(EditText) findViewById(R.id.shezhi_pass2);
		pass3=(EditText) findViewById(R.id.shezhi_pass3);
		nickname1=(EditText) findViewById(R.id.shezhi_nickname1);
		
		lin1=linearById(R.id.shezhi_lin1);
		lin2=linearById(R.id.shezhi_lin2);
		
		
	}

	@Override
	public void initData() {
		
		
		
	}

	@Override
	public void initViewOper() {
		
	}
	
	/**
	 *
	 */
	public void back(View v) {
		finish();
	}

	/**
	 * ����޸�����
	 */
	public void onClick1(View v) {
		mpass1=pass1.getText().toString().trim();
		mpass2=pass2.getText().toString().trim();
		mpass3=pass3.getText().toString().trim();
		
		
		if (!mpass2.equals(mpass3)) {
			toast("���������벻һ��");
			return;
		}
		Log.i("Log", "ԭ����:"+mpass1);
		Log.i("Log", "������:"+mpass2);
		BmobUser.updateCurrentUserPassword(mpass1, mpass2, new UpdateListener() {

		    @Override
		    public void done(BmobException e) {
		        if(e==null){
		            toast("�����޸ĳɹ�����������������е�¼��");
		            lin1.setVisibility(View.GONE);
		            finish();
		            startAct(LoginActivity.class);
		        }else{
		            toast("ʧ��:" + e.getMessage());
		            toast("ԭ���벻��ȷ");
					return;
		        }
		    }

		});
		
	}
	/**
	 * ����޸��ǳ�
	 */
	public void onClick2(View v) {
		mnickname1=nickname1.getText().toString().trim();
	}
	
	/**
	 * �����ʾ��������
	 */
	public void lin(View v) {

		switch (v.getId()) {
		case R.id.shezhi_pass:
			
			if (lin1.getVisibility()==View.VISIBLE) {
				lin1.setVisibility(View.GONE);
				
				
				
				
				
			}else{
				//������ʱ,��ʾ����
				lin1.setVisibility(View.VISIBLE);
			}
			
			
			break;
		case R.id.shezhi_nickname:
			
			if (lin2.getVisibility()==View.VISIBLE) {
				lin2.setVisibility(View.GONE);
			}else{
				lin2.setVisibility(View.VISIBLE);
			}
			
			break;

		default:
			break;
		}
	}
	
}
