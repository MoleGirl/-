package com.example.jiuquwan.adapter;

import com.example.jiuquwan.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 下拉选择活动内容的Spinner的Adapter
 * 这是适配器在初始化时,需要传入两个数据源,一个是图片id,一个是文本
 * 文本默认显示的是(选择活动类型,0下标)
 * @author Administrator
 *
 */

public class Act_SpinnerAdapter extends BaseAdapter {

	//右边显示的图片
	private int[] imgIds;
	//左边显示的内容
	private String[] titles;
	//填充器
	private LayoutInflater inflater;
	//上下文
	private Context context;
	
	
	
	
	public Act_SpinnerAdapter(int[] imgIds, String[] titles, Context context) {
		super();
		this.imgIds = imgIds;
		this.titles = titles;
		this.context = context;
		this.inflater=LayoutInflater.from(context);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return titles[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		ViewHolder vh=null;
		if (v==null) {
			//填充一个布局
			v=inflater.inflate(R.layout.act_spinner_item, null);
			vh=new ViewHolder();
			vh.img=(ImageView) v.findViewById(R.id.act_spinner_item_img);
			vh.tv=(TextView) v.findViewById(R.id.act_spinner_item_tv);
			v.setTag(vh);
		}else{
			vh=(ViewHolder) v.getTag();
		}
		if (position==0) {
			vh.tv.setTextSize(20);
			vh.tv.setTextColor(Color.parseColor("#808080"));;
			
		}
		
		vh.img.setImageResource(imgIds[position]);
		vh.tv.setText(titles[position]);
		return v;
	}

	class  ViewHolder{
		ImageView img;
		TextView tv;
		
	}
}
