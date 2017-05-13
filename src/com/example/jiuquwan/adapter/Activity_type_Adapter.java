package com.example.jiuquwan.adapter;

import java.util.List;

import com.example.jiuquwan.R;
import com.example.jiuquwan.adapter.Act_SpinnerAdapter.ViewHolder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ѡ�����͵�Adapter
 * @author Administrator
 *
 */
public class Activity_type_Adapter extends BaseAdapter {

	//��ʾͼƬ�ļ���
	private int[] imgIds;
	//�±���ʾ������
	private String[] titles;
	//�����
	private LayoutInflater inflater;
	//������
	private Context context;
	
	
	
	/**
	 * ���췽��
	 * @param bits
	 * @param titles
	 * @param context
	 */
	public Activity_type_Adapter(int[] imgIds, String[] titles, Context context) {
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
			//���һ������
			v=inflater.inflate(R.layout.act_spinner_item, null);
			vh=new ViewHolder();
			vh.img=(ImageView) v.findViewById(R.id.act_spinner_item_img);
			vh.tv=(TextView) v.findViewById(R.id.act_spinner_item_tv);
			v.setTag(vh);
		}else{
			vh=(ViewHolder) v.getTag();
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
