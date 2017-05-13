package com.example.jiuquwan.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.jiuquwan.R;
import com.example.jiuquwan.adapter.Gather_list_Adapter;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuquwan.bean.GatherBean;
import com.example.jiuquwan.utils.FindGatherUtils;
import com.example.jiuquwan.utils.FindGatherUtils.FindGatherListener;
import com.example.jiuquwan.view.XListView;
import com.example.jiuquwan.view.XListView.IXListViewListener;
import com.example.jiuwuwan.base.BaseInterface;
import com.example.jiuwuwan.base.BaseV4Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;
import cn.bmob.v3.exception.BmobException;

/**
 * home的fragment
 * 
 * @author Administrator
 *
 */
public class HomeFrament extends BaseV4Fragment implements BaseInterface {

	// 搜索输入框
	private EditText fragment_home_title_et;
	// 返回的按钮
	private ImageView titlt_back, title_so, title_menu;
	private XListView list;

	/**
	 * XListView的数据源
	 */
	private List<GatherBean> datas;
	
	
	private AlertDialog backDialog;
	private Gather_list_Adapter adapter;
	@Override
	public View initLayout(LayoutInflater inflater) {
		View v = inflater.inflate(R.layout.fragment_home, null);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
		initViewOper();

	}

	@Override
	public void initView() {
		fragment_home_title_et = etById(R.id.fragment_home_title_et);
		titlt_back = imgById(R.id.fragment_home_back);
		title_so = imgById(R.id.fragment_home_title_so);
		title_menu = imgById(R.id.fragment_home_title_menu);
		list=(XListView) findViewById(R.id.xlist);
	}

	@Override
	public void initData() {
		//把Application数据源赋值给他
		datas=GatherApplication.gathers;
		for (int i = 0; i < datas.size(); i++) {
			logi("标题:"+i+"="+datas.get(i).getGatherTitle());
			
		}
	}

	@Override
	public void initViewOper() {
			adapter = new Gather_list_Adapter(datas, act) ;
			list.setAdapter(adapter);
			//绑定数据源
			//开启加载更多
			list.setPullLoadEnable(true);
			list.setOnXListViewListener(new IXListViewListener() {
				
				@Override
				public void onRefresh() {
					FindGatherUtils.findGather(1, null, 0, 0, new FindGatherListener() {
						
						@Override
						public void findData(List<GatherBean> beans, BmobException arg1) {
							//停止刷新
							list.stopRefresh();
							if (arg1==null) {
								//加载数据成功,把数据放入Application中
								GatherApplication.gathers=beans;
								//把数据放入当前datas中,保持数据的一致性
								HomeFrament.this.datas=beans;
								//调用设置数据的方法
								adapter.setNewData(beans);
								toast("查询数据成功,共数据条数:"+beans.size());
							}else{
								//加载数据失败
								toast("网络原因刷新失败");
							}
							
						}
					});
				}
				
				@Override
				public void onLoadMore() {
					/**
					 * 参数3 忽略的条数
					 * 参数4 加载的条数
					 */
					FindGatherUtils.findGather(2, null, HomeFrament.this.datas.size(), 10, new FindGatherListener() {
						
						@Override
						public void findData(List<GatherBean> beans, BmobException arg1) {
							//停止加载更多
							list.stopLoadMore();
							if (arg1==null) {
								//加载数据成功,把数据放入Application中
								GatherApplication.gathers.addAll(beans);
								//把数据放入当前datas中,保持数据的一致性
								HomeFrament.this.datas=GatherApplication.gathers;
								//调用设置数据的方法
								adapter.setNewData(HomeFrament.this.datas);
								toast("查询数据成功,共数据条数:"+beans.size());
							}else{
								//加载数据失败
								toast("网络原因加载失败");
							}
							
						}
					});
				}
			});
	}

	/**
	 * 当页面返回时,初始化title 搜索框隐藏
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initTitle();
	}

	private void initTitle() {
		// 隐藏输入框
		fragment_home_title_et.setVisibility(View.GONE);
		// 显示返回图标
		titlt_back.setVisibility(View.VISIBLE);
		titlt_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toast("点击了返回键");
//				if (backDialog==null) {
//					backDialog = new AlertDialog.Builder(act).setMessage("您确退出吗?").setPositiveButton("确定", new OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							act.finish();
//						} 
//					}).setNegativeButton("取消", new OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							
//						}
//					}).create();
//					backDialog.show();
//				}
				
			}
		});
		
		
		// 搜索的点击事件,第一次点击时,搜索框是关闭的
		title_so.setOnClickListener(so_offListener);
	}

	/**
	 * 默认状态下,点击搜索按钮的响应 当第一次点击搜索的时候,输入框显示,返回图标隐藏
	 */
	private OnClickListener so_offListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			// 展示输入框
			fragment_home_title_et.setVisibility(View.VISIBLE);
			// 隐藏back图标
			titlt_back.setVisibility(View.GONE);
			// 设置第二次点击搜索按钮的时候,就是输入框显示的时候
			title_so.setOnClickListener(so_onListener);
		}
	};
	/**
	 * 当输入框已经展示,再次点击搜索按钮时的响应
	 */
	
	private OnClickListener so_onListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//获取输入内容开始搜索
			String soContent=getTvText(fragment_home_title_et);
			toast("搜索的内容是:"+soContent);
			//重新从初始化title开始
			initTitle();
		}
	};
	
	
	

}
