package com.example.jiuquwan.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.jiuquwan.R;
import com.example.jiuquwan.activity.GatherActivity;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuquwan.bean.GatherBean;
import com.example.jiuquwan.bean.User;
import com.example.jiuquwan.utils.FindUserUtils;
import com.example.jiuquwan.utils.FindUserUtils.FindUserListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * home中的单个视图的adapter
 * 
 * @author Administrator
 *
 */
public class Fragment_Message_list_Adapter extends BaseAdapter {

	// 数据源
	private List<String> datas;
	// 填充器
	private LayoutInflater inflater;
	// 上下文
	private Context context;

	public Fragment_Message_list_Adapter(List<String> datas, Context context) {
		super();
		this.datas = datas;
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		ViewHolder vh = null;
		if (v == null) {
			v = inflater.inflate(R.layout.fragment_message_list_item, null);
			vh = new ViewHolder();
			vh.nickname=(TextView) v.findViewById(R.id.message_nickname);
			vh.message=(TextView) v.findViewById(R.id.message);
			vh.time=(TextView) v.findViewById(R.id.message_time);
			v.setTag(vh);
		} else {
			vh = (ViewHolder) v.getTag();
		}

		vh.message.setText(datas.get(position));

		return v;
	}

	class ViewHolder {
		ImageView head;
		TextView nickname,message,time;

	}

}