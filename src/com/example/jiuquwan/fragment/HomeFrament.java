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
 * home��fragment
 * 
 * @author Administrator
 *
 */
public class HomeFrament extends BaseV4Fragment implements BaseInterface {

	// ���������
	private EditText fragment_home_title_et;
	// ���صİ�ť
	private ImageView titlt_back, title_so, title_menu;
	private XListView list;

	/**
	 * XListView������Դ
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
		//��Application����Դ��ֵ����
		datas=GatherApplication.gathers;
		for (int i = 0; i < datas.size(); i++) {
			logi("����:"+i+"="+datas.get(i).getGatherTitle());
			
		}
	}

	@Override
	public void initViewOper() {
			adapter = new Gather_list_Adapter(datas, act) ;
			list.setAdapter(adapter);
			//������Դ
			//�������ظ���
			list.setPullLoadEnable(true);
			list.setOnXListViewListener(new IXListViewListener() {
				
				@Override
				public void onRefresh() {
					FindGatherUtils.findGather(1, null, 0, 0, new FindGatherListener() {
						
						@Override
						public void findData(List<GatherBean> beans, BmobException arg1) {
							//ֹͣˢ��
							list.stopRefresh();
							if (arg1==null) {
								//�������ݳɹ�,�����ݷ���Application��
								GatherApplication.gathers=beans;
								//�����ݷ��뵱ǰdatas��,�������ݵ�һ����
								HomeFrament.this.datas=beans;
								//�����������ݵķ���
								adapter.setNewData(beans);
								toast("��ѯ���ݳɹ�,����������:"+beans.size());
							}else{
								//��������ʧ��
								toast("����ԭ��ˢ��ʧ��");
							}
							
						}
					});
				}
				
				@Override
				public void onLoadMore() {
					/**
					 * ����3 ���Ե�����
					 * ����4 ���ص�����
					 */
					FindGatherUtils.findGather(2, null, HomeFrament.this.datas.size(), 10, new FindGatherListener() {
						
						@Override
						public void findData(List<GatherBean> beans, BmobException arg1) {
							//ֹͣ���ظ���
							list.stopLoadMore();
							if (arg1==null) {
								//�������ݳɹ�,�����ݷ���Application��
								GatherApplication.gathers.addAll(beans);
								//�����ݷ��뵱ǰdatas��,�������ݵ�һ����
								HomeFrament.this.datas=GatherApplication.gathers;
								//�����������ݵķ���
								adapter.setNewData(HomeFrament.this.datas);
								toast("��ѯ���ݳɹ�,����������:"+beans.size());
							}else{
								//��������ʧ��
								toast("����ԭ�����ʧ��");
							}
							
						}
					});
				}
			});
	}

	/**
	 * ��ҳ�淵��ʱ,��ʼ��title ����������
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initTitle();
	}

	private void initTitle() {
		// ���������
		fragment_home_title_et.setVisibility(View.GONE);
		// ��ʾ����ͼ��
		titlt_back.setVisibility(View.VISIBLE);
		titlt_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toast("����˷��ؼ�");
//				if (backDialog==null) {
//					backDialog = new AlertDialog.Builder(act).setMessage("��ȷ�˳���?").setPositiveButton("ȷ��", new OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							act.finish();
//						} 
//					}).setNegativeButton("ȡ��", new OnClickListener() {
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
		
		
		// �����ĵ���¼�,��һ�ε��ʱ,�������ǹرյ�
		title_so.setOnClickListener(so_offListener);
	}

	/**
	 * Ĭ��״̬��,���������ť����Ӧ ����һ�ε��������ʱ��,�������ʾ,����ͼ������
	 */
	private OnClickListener so_offListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			// չʾ�����
			fragment_home_title_et.setVisibility(View.VISIBLE);
			// ����backͼ��
			titlt_back.setVisibility(View.GONE);
			// ���õڶ��ε��������ť��ʱ��,�����������ʾ��ʱ��
			title_so.setOnClickListener(so_onListener);
		}
	};
	/**
	 * ��������Ѿ�չʾ,�ٴε��������ťʱ����Ӧ
	 */
	
	private OnClickListener so_onListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//��ȡ�������ݿ�ʼ����
			String soContent=getTvText(fragment_home_title_et);
			toast("������������:"+soContent);
			//���´ӳ�ʼ��title��ʼ
			initTitle();
		}
	};
	
	
	

}
