package com.example.jiuwuwan.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {

	/**
	 * ������
	 */
	public BaseActivity act;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		act=this;
	}
	
	/**
	 * ����һ��activity
	 * @param cls 
	 */
	public void startAct(Class<?> cls){
		startActivity(new Intent(act, cls));
	}
	
	private ProgressDialog dialog;
	
	/**
	 * ������ʾ���ȵĶԻ���
	 * @param flag �Ƿ����ȡ��
	 * @param message ��Ϣ������ ����Ҫ���Դ���null
	 * @param title ���� ����Ҫ���Դ���null
	 */
	public ProgressDialog dialogShow(boolean flag, CharSequence message, CharSequence title){
		if (dialog==null) {
			dialog=new ProgressDialog(this);
		}
		//�����Ƿ����ȡ��
		dialog.setCancelable(flag);
		//��������
		dialog.setMessage(message);
		//���ñ���
		dialog.setTitle(title);
		dialog.show();
		return dialog;
	}
	/**
	 * �����ر�dialog�ķ���,ǰ���ǵ�����dialogShow�ķ���
	 */
	public void dialogDismiss(){
		if (dialog!=null&& dialog.isShowing()) {
			dialog.dismiss();
		}
		
	}
	
	/**
	 * ��ȡ�ı����е�����
	 * @param et TextView�Ķ���
	 * @return
	 */
	public String getTvText(TextView et){
		return et.getText().toString().trim();
	}
	
	/**
	 * ��ӡ
	 * @param text
	 */
	public void logi(String text){
		Log.i("lewan", text);
	}
	/**
	 * ��ӡ
	 * @param text
	 */
	public void loge(String text){
		Log.e("lewan", text);
	}
	
	/**
	 * ����toast
	 * 
	 * @param text����������
	 */
	public void toast(String text) {
		Toast.makeText(this, text, 0).show();
	}
	/**
	 * ����ͼƬ
	 * 
	 * @param text����������
	 */
	public void toast(int resId) {
		Toast t=new Toast(act);
		ImageView img=new ImageView(act);
		//����img�Ŀ��
		img.setLayoutParams(new LayoutParams(200, 200));
		img.setBackgroundColor(Color.BLUE);
		img.setImageResource(resId);
		t.setView(img);
		//����λ��
		t.setGravity(Gravity.TOP, 0, 0);
		t.show();
		
	}
	
	
	
	
	/**
	 * @param idImageView��id
	 * @return ���imgview�Ķ���,�����ѯʧ��,�򷵻�null
	 */
	public ImageView imgById(int id) {
		return (ImageView) findViewById(id);
	}
	/**
	 * @param id TextView��id
	 * @return ���TextView�Ķ���,�����ѯʧ��,�򷵻�null
	 */
	public TextView tvById(int id) {
		return (TextView) findViewById(id);
	}
	/**
	 * @param id Button��id
	 * @return ���Button�Ķ���,�����ѯʧ��,�򷵻�null
	 */
	public Button butById(int id) {
		return (Button) findViewById(id);
	}
	/**
	 * @param id EditText��id
	 * @return ���EditText�Ķ���,�����ѯʧ��,�򷵻�null
	 */
	public EditText etById(int id) {
		return (EditText) findViewById(id);
	}
	/**
	 * @param id LinearLayout��id
	 * @return ���LinearLayout�Ķ���,�����ѯʧ��,�򷵻�null
	 */
	public LinearLayout linearById(int id) {
		return (LinearLayout) findViewById(id);
	}


	
	
	
}
