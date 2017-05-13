package com.example.jiuquwan.adapter;

import java.util.List;

import com.example.jiuquwan.R;
import com.example.jiuquwan.adapter.popup_list.ViewHolder;
import com.example.jiuquwan.bean.GatherBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 个人中心的Grid的Adapter
 * 
 * @author Administrator
 *
 */
public class MyCenter_GridAdapter extends BaseAdapter {

	private int[] resIds;
	private String[] titles;
	// 填充器
	private LayoutInflater inflater;
	// 上下文
	private Context context;

	public MyCenter_GridAdapter(int[] resIds, String[] titles, Context context) {
		super();
		this.resIds = resIds;
		this.titles = titles;
		inflater = LayoutInflater.from(context);
		this.context = context;
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
	public View getView(int position, View v, ViewGroup parent) {

		ViewHolder vh = null;
		if (v == null) {
			vh = new ViewHolder();
			v = inflater.inflate(R.layout.fragment_mycenter_grid_item, null);
			vh.img = (ImageView) v.findViewById(R.id.grid_item_img);
			vh.tv = (TextView) v.findViewById(R.id.grid_item_tv);
			v.setTag(vh);
		} else {
			vh = (ViewHolder) v.getTag();
		}

		vh.img.setImageResource(resIds[position]);
		vh.tv.setText(titles[position]);
		return v;
	}

	class ViewHolder {
		ImageView img;
		TextView tv;
	}

}
