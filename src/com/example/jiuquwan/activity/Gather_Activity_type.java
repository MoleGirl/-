package com.example.jiuquwan.activity;

import com.example.jiuquwan.R;
import com.example.jiuquwan.adapter.Activity_type_Adapter;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuwuwan.base.BaseActivity;
import com.example.jiuwuwan.base.BaseInterface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * ѡ�����͵�ʱ�����Ĵ��ڵ�Activity
 * @author Administrator
 *
 */

public class Gather_Activity_type extends BaseActivity implements BaseInterface {

	private GridView act_type;
//	private String[] titles={"diy","����","����","�۲�","��ʳ","�ٶ�","�ݳ�","�˶�","չ��","�ܱ�"};
//	private int[] imgIds={R.drawable.sort_diy,R.drawable.sort_jiangzuo,
//			R.drawable.sort_jieri,R.drawable.sort_jucan,
//			R.drawable.sort_meishi,R.drawable.sort_shaoer,
//			R.drawable.sort_yanchu,R.drawable.sort_yundong,
//			R.drawable.sort_zhanlan,R.drawable.sort_zhoubian};
	
	private String[] titles={"�ܱ�","�ٶ�","DIY","����","����","�ݳ�","չ��","ɳ��","Ʒ��","�ۻ�"};
	private int[] imgIds={R.drawable.more_zhoubian,R.drawable.more_shaoer,
			R.drawable.more_diy,R.drawable.more_jianshen,
			R.drawable.more_jishi,R.drawable.more_yanchu,
			R.drawable.more_zhanlan,R.drawable.more_shalong,
			R.drawable.more_pincha,R.drawable.more_juhui};
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_type);
		initView();
		initData();
		initViewOper();
		
	}
	
	@Override
	public void initView() {
		act_type=(GridView) findViewById(R.id.activity_type_grid);
		
	}

	@Override
	public void initData() {
		
		
	}

	@Override
	public void initViewOper() {
		act_type.setAdapter(new Activity_type_Adapter(imgIds, titles, act));
		act_type.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				//logi("��ѡ��:"+position);
				Log.i("jiuquwan", "��ѡ��:"+position);
				Log.i("jiuquwan", "���ݵ�id:"+imgIds[position]);
				Log.i("jiuquwan", "���ݵ��ı�:"+titles[position]);
				
				GatherApplication.intIds=imgIds[position];
				GatherApplication.titles=titles[position];
				//Intent intent =new Intent(act, SendGatherActivity.class);
//				Bundle bundle= new Bundle();
//				bundle.putInt("imgIds", imgIds[position]);
//				bundle.putString("titles", titles[position]);
				//startActivity(intent);
				Log.i("jiuquwan", "��ת��");
				finish();
				
				
			}
		});
	}
	
	/**
	 * ����Ż�
	 */
	public void type_img_back(View v) {
		finish();
	}

}
