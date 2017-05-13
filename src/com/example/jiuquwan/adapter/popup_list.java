package com.example.jiuquwan.adapter;

import java.util.List;

import com.example.jiuquwan.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class popup_list extends BaseAdapter {

	private List<String> titles;
	private Context context;
	private LayoutInflater inflater;
	
	
	
	public popup_list(List<String> titles, Context context) {
		super();
		this.titles = titles;
		this.context = context;
		this.inflater=LayoutInflater.from(context);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return titles.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder vh=null;
		if (v==null) {
			vh=new ViewHolder();
			v=inflater.inflate(R.layout.popup_list_item, null);
			vh.tv=(TextView) v.findViewById(R.id.popup_list_item_tv);
			v.setTag(vh);
		}else{
			vh=(ViewHolder) v.getTag();
		}
		vh.tv.setText(titles.get(position));
		return v;
	}
	class ViewHolder{
		TextView tv;
	}
}
