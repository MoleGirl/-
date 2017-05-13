package com.example.jiuquwan.activity;

import java.util.List;

import com.example.jiuquwan.R;
import com.example.jiuquwan.adapter.Gather_list_Adapter;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuquwan.bean.GatherBean;
import com.example.jiuwuwan.base.BaseActivity;
import com.example.jiuwuwan.base.BaseInterface;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 用来展示活动列表的Activity
 * 1.在Application中以data-gather为key进行展示活动的存储(ArrayList(<GatherBean>))
 * 2.在Application中以data-title为key进行展示标题的存储(String)
 * @author Administrator
 *
 */
public class More_ShowGatherActivity extends BaseActivity implements BaseInterface {

	private ListView lv;
	private TextView tv;
	//数据源
	private List<GatherBean> gathers;
	private String title;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.act_showgather);
		initView();
		initData();
		initViewOper();
		
	}
	
	@Override
	public void initView() {
		lv=(ListView) findViewById(R.id.act_showgather_list);
		tv=tvById(R.id.act_showgather_tv);
	}

	@Override
	public void initData() {
		gathers=(List<GatherBean>) GatherApplication.get("data-gather", true);
		title=(String) GatherApplication.get("data-title", true);
		
	}

	@Override
	public void initViewOper() {
		/**
		 * 参数1活动的结合
		 * 参数2上下文
		 */
		lv.setAdapter(new Gather_list_Adapter(gathers,act));
		tv.setText(title);
	}

}
