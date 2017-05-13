package com.example.jiuquwan.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.jiuquwan.R;
import com.example.jiuquwan.activity.MessageActivity;
import com.example.jiuquwan.adapter.Fragment_Message_list_Adapter;
import com.example.jiuwuwan.base.BaseInterface;
import com.example.jiuwuwan.base.BaseV4Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.sms.exception.BmobException;

/**
 * 消息Fragment
 * 
 * @author Administrator
 *
 */
public class MessageFrament extends BaseV4Fragment implements BaseInterface, OnClickListener {

	private ListView list;
	private List<String> datas;

	private ImageView tianjia;

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

		list = (ListView) findViewById(R.id.fragment_message_list);
		datas = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			datas.add("大家好" + i);
		}
	}

	@Override
	public void initData() {

	}

	@Override
	public void initViewOper() {
		list.setAdapter(new Fragment_Message_list_Adapter(datas, act));
		tianjia = imgById(R.id.message_tianjia);
		//tianjia.setOnClickListener(this);
	}

	@Override
	public View initLayout(LayoutInflater inflater) {
		View v = inflater.inflate(R.layout.fragment_message, null);
		return v;
	}

	//private Bundle bundle;
	@Override
	public void onClick(View v) {
		//如果需要更新用户资料，开发者只需要传新的info进去就可以了
//		BmobIM.getInstance().startPrivateConversation(null, new ConversationListener() {
//			@Override
//			public void done(BmobIMConversation c, cn.bmob.v3.exception.BmobException e) {
//				if(e==null){
//		            //在此跳转到聊天页面
//					Bundle  bundle = new Bundle();
//		            bundle.putSerializable("c", c);
//		            //startActivity(MessageActivity.class, bundle);
//		        }else{
//		            toast(e.getMessage()+"("+e.getErrorCode()+")");
//		        }
//			}
//		});
	}

	
	
}
