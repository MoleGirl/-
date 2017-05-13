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
	 * 上下文
	 */
	public BaseActivity act;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		act=this;
	}
	
	/**
	 * 启动一个activity
	 * @param cls 
	 */
	public void startAct(Class<?> cls){
		startActivity(new Intent(act, cls));
	}
	
	private ProgressDialog dialog;
	
	/**
	 * 用来显示进度的对话框
	 * @param flag 是否可以取消
	 * @param message 消息的内容 不需要可以传入null
	 * @param title 标题 不需要可以传入null
	 */
	public ProgressDialog dialogShow(boolean flag, CharSequence message, CharSequence title){
		if (dialog==null) {
			dialog=new ProgressDialog(this);
		}
		//设置是否可以取消
		dialog.setCancelable(flag);
		//设置内容
		dialog.setMessage(message);
		//设置标题
		dialog.setTitle(title);
		dialog.show();
		return dialog;
	}
	/**
	 * 用来关闭dialog的方法,前提是调用了dialogShow的方法
	 */
	public void dialogDismiss(){
		if (dialog!=null&& dialog.isShowing()) {
			dialog.dismiss();
		}
		
	}
	
	/**
	 * 获取文本框中的内容
	 * @param et TextView的对象
	 * @return
	 */
	public String getTvText(TextView et){
		return et.getText().toString().trim();
	}
	
	/**
	 * 打印
	 * @param text
	 */
	public void logi(String text){
		Log.i("lewan", text);
	}
	/**
	 * 打印
	 * @param text
	 */
	public void loge(String text){
		Log.e("lewan", text);
	}
	
	/**
	 * 弹出toast
	 * 
	 * @param text弹出的内容
	 */
	public void toast(String text) {
		Toast.makeText(this, text, 0).show();
	}
	/**
	 * 弹出图片
	 * 
	 * @param text弹出的内容
	 */
	public void toast(int resId) {
		Toast t=new Toast(act);
		ImageView img=new ImageView(act);
		//设置img的宽高
		img.setLayoutParams(new LayoutParams(200, 200));
		img.setBackgroundColor(Color.BLUE);
		img.setImageResource(resId);
		t.setView(img);
		//设置位置
		t.setGravity(Gravity.TOP, 0, 0);
		t.show();
		
	}
	
	
	
	
	/**
	 * @param idImageView的id
	 * @return 这个imgview的对象,如果查询失败,则返回null
	 */
	public ImageView imgById(int id) {
		return (ImageView) findViewById(id);
	}
	/**
	 * @param id TextView的id
	 * @return 这个TextView的对象,如果查询失败,则返回null
	 */
	public TextView tvById(int id) {
		return (TextView) findViewById(id);
	}
	/**
	 * @param id Button的id
	 * @return 这个Button的对象,如果查询失败,则返回null
	 */
	public Button butById(int id) {
		return (Button) findViewById(id);
	}
	/**
	 * @param id EditText的id
	 * @return 这个EditText的对象,如果查询失败,则返回null
	 */
	public EditText etById(int id) {
		return (EditText) findViewById(id);
	}
	/**
	 * @param id LinearLayout的id
	 * @return 这个LinearLayout的对象,如果查询失败,则返回null
	 */
	public LinearLayout linearById(int id) {
		return (LinearLayout) findViewById(id);
	}


	
	
	
}
