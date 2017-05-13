package com.example.jiuquwan.adapter;

import com.example.jiuquwan.R;
import com.example.jiuquwan.adapter.MyCenter_GridAdapter.ViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MoreGridAdapter extends BaseAdapter {

	private int[] resIds;
	private String[] titles;
	private Context context;
	private LayoutInflater inflater;

	public MoreGridAdapter(int[] resIds, String[] titles, Context context) {
		super();
		this.resIds = resIds;
		this.titles = titles;
		this.context = context;
		this.inflater = LayoutInflater.from(context);

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
			v = inflater.inflate(R.layout.more_grid_item, null);
			vh.img = (ImageView) v.findViewById(R.id.more_grid_item_img);
			vh.tv = (TextView) v.findViewById(R.id.more_grid_item_tv);
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
