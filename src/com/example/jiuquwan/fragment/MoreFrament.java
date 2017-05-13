package com.example.jiuquwan.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.jiuquwan.R;
import com.example.jiuquwan.activity.More_ShowGatherActivity;
import com.example.jiuquwan.adapter.MoreGridAdapter;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuquwan.bean.GatherBean;
import com.example.jiuquwan.utils.FindGatherUtils;
import com.example.jiuquwan.utils.FindGatherUtils.FindGatherListener;
import com.example.jiuwuwan.base.BaseInterface;
import com.example.jiuwuwan.base.BaseV4Fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
/**
 * 更多的Fragment
 * @author Administrator
 *
 */
public class MoreFrament extends BaseV4Fragment implements BaseInterface{

	//输入框
	private EditText et;
	//搜索按钮
	private ImageView so;
	//左边的定位的lin
	private LinearLayout lin;
	//位置的文本
	private TextView tv;
	//点击检索的按钮
	private Button but1,but2,but3; 
	//城市的集合
	private List<String> citys = new ArrayList<>();
	//类型检索
	private GridView grid;
	//数据源
	private String[] titles={"周边","少儿","DIY","健身","集市","演出","展览","沙龙","品茶","聚会"};
	private int[] imgIds={R.drawable.more_zhoubian,R.drawable.more_shaoer,
			R.drawable.more_diy,R.drawable.more_jianshen,
			R.drawable.more_jishi,R.drawable.more_yanchu,
			R.drawable.more_zhanlan,R.drawable.more_shalong,
			R.drawable.more_pincha,R.drawable.more_juhui};
	private ListView popup_list;
	@Override
	public View initLayout(LayoutInflater inflater) {
		View v=inflater.inflate(R.layout.fragment_more, null);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
		initViewOper();
	}
	
	@Override
	public void initView() {
		et=etById(R.id.fragment_more_et);
		so=imgById(R.id.fragment_more_img);
		lin=(LinearLayout) findViewById(R.id.fragment_more_lin);
		tv=tvById(R.id.fragment_more_tv);
		grid=(GridView) findViewById(R.id.more_grid);
		but1=(Button) findViewById(R.id.more_but1);
		but2=(Button) findViewById(R.id.more_but2);
		but3=(Button) findViewById(R.id.more_but3);
	}

	@Override
	public void initData() {
		citys.add("北京市");
		citys.add("上海市");
		citys.add("深圳市");
		citys.add("广州市");
		citys.add("邯郸市");
	}

	@Override
	public void initViewOper() {
		grid.setAdapter(new MoreGridAdapter(imgIds, titles, act));
		//填充一个视图
		final View contentView=act.getLayoutInflater().inflate(R.layout.popup, null);
		lin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popup_list=(ListView) contentView.findViewById(R.id.popup_list);
				popup_list.setAdapter(new com.example.jiuquwan.adapter.popup_list(citys, act));
				
				
				/**
				 * 定点弹出框
				 * 参数1填充的视图
				 * 参数2视图的宽
				 * 参数3视图的高
				 */
				final PopupWindow popup = new PopupWindow(contentView, lin.getWidth(),300 );
				
				//其他之外不可以点击
				popup.setOutsideTouchable(false);
				//获取用户焦点
				popup.setFocusable(true);
				popup.setBackgroundDrawable(new BitmapDrawable());
				/**
				 * 参数1显示的位置
				 * 参数2横向偏移
				 * 参数3纵向偏移
				 */
				popup.showAsDropDown(lin, 0,0);
				popup_list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
						toast("点击了:"+citys.get(position));
						String str = citys.get(position);
						tv.setText(str);
						popup.dismiss();
					}
				});
			}
		});
		
		/**
		 * 判断当前用户定位的城市,是否在数据源中存在
		 * 存在则设置默认的选中是当前的城市
		 */
		int index=citys.indexOf(GatherApplication.location.getCity());
		//数据存在
		if (index!=-1) {
			tv.setText(GatherApplication.location.getCity());
			//spinner时的设置
			//spinner.setSelection(index);
		}
		
		//当用户点击搜索
		so.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//发起检索
				final String text= getTvText(et);
				//弹出dialog
				dialogShow(true, null, null);
				//模糊查询
				FindGatherUtils.findGather(10005, text, 0, 0, new FindGatherListener() {
					@Override
					public void findData(List<GatherBean> beans, BmobException arg1) {
						//关闭dialog
						dialogDismiss();
						if (arg1==null) {
							GatherApplication.put("data-gather", beans);
							GatherApplication.put("data-title", text+"相关活动");
							startAct(More_ShowGatherActivity.class);
						}
						
					}
				});
				
			}
		});
		//免费检索
		but1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogShow(true, null, null);
				FindGatherUtils.findGather(10002, null, 0, 0, new FindGatherListener() {
					@Override
					public void findData(List<GatherBean> beans, BmobException arg1) {
						//关闭dialog
						dialogDismiss();
						if (arg1==null) {
							GatherApplication.put("data-gather", beans);
							GatherApplication.put("data-title", "免费活动");
							startAct(More_ShowGatherActivity.class);
						}
						
					}
				});
			}
		});
		//热门检索
		but2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogShow(true, null, null);
				FindGatherUtils.findGather(10004, null, 0, 0, new FindGatherListener() {
					@Override
					public void findData(List<GatherBean> beans, BmobException arg1) {
						//关闭dialog
						dialogDismiss();
						if (arg1==null) {
							GatherApplication.put("data-gather", beans);
							GatherApplication.put("data-title", "热门活动");
							startAct(More_ShowGatherActivity.class);
						}
						
					}
				});
			}
		});
		//附近检索
		but3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogShow(true, null, null);
				FindGatherUtils.findGather(10003, new BmobGeoPoint(GatherApplication.location.getLongitude(), GatherApplication.location.getLatitude()), 0, 0, new FindGatherListener() {
					@Override
					public void findData(List<GatherBean> beans, BmobException arg1) {
						//关闭dialog
						dialogDismiss();
						if (arg1==null) {
							GatherApplication.put("data-gather", beans);
							GatherApplication.put("data-title", "附近活动");
							startAct(More_ShowGatherActivity.class);
						}
						
					}
				});
			}
		});
		//分类查询
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, final int position, long arg3) {
				dialogShow(true, null, null);
				FindGatherUtils.findGather(10001,titles[position] , 0, 0, new FindGatherListener() {
					@Override
					public void findData(List<GatherBean> beans, BmobException arg1) {
						//关闭dialog
						dialogDismiss();
						if (arg1==null) {
							GatherApplication.put("data-gather", beans);
							GatherApplication.put("data-title", titles[position]+"活动");
							startAct(More_ShowGatherActivity.class);
						}
						
					}
				});
			}
		});
		
	}
}
