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
 * �����Fragment
 * @author Administrator
 *
 */
public class MoreFrament extends BaseV4Fragment implements BaseInterface{

	//�����
	private EditText et;
	//������ť
	private ImageView so;
	//��ߵĶ�λ��lin
	private LinearLayout lin;
	//λ�õ��ı�
	private TextView tv;
	//��������İ�ť
	private Button but1,but2,but3; 
	//���еļ���
	private List<String> citys = new ArrayList<>();
	//���ͼ���
	private GridView grid;
	//����Դ
	private String[] titles={"�ܱ�","�ٶ�","DIY","����","����","�ݳ�","չ��","ɳ��","Ʒ��","�ۻ�"};
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
		citys.add("������");
		citys.add("�Ϻ���");
		citys.add("������");
		citys.add("������");
		citys.add("������");
	}

	@Override
	public void initViewOper() {
		grid.setAdapter(new MoreGridAdapter(imgIds, titles, act));
		//���һ����ͼ
		final View contentView=act.getLayoutInflater().inflate(R.layout.popup, null);
		lin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popup_list=(ListView) contentView.findViewById(R.id.popup_list);
				popup_list.setAdapter(new com.example.jiuquwan.adapter.popup_list(citys, act));
				
				
				/**
				 * ���㵯����
				 * ����1������ͼ
				 * ����2��ͼ�Ŀ�
				 * ����3��ͼ�ĸ�
				 */
				final PopupWindow popup = new PopupWindow(contentView, lin.getWidth(),300 );
				
				//����֮�ⲻ���Ե��
				popup.setOutsideTouchable(false);
				//��ȡ�û�����
				popup.setFocusable(true);
				popup.setBackgroundDrawable(new BitmapDrawable());
				/**
				 * ����1��ʾ��λ��
				 * ����2����ƫ��
				 * ����3����ƫ��
				 */
				popup.showAsDropDown(lin, 0,0);
				popup_list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
						toast("�����:"+citys.get(position));
						String str = citys.get(position);
						tv.setText(str);
						popup.dismiss();
					}
				});
			}
		});
		
		/**
		 * �жϵ�ǰ�û���λ�ĳ���,�Ƿ�������Դ�д���
		 * ����������Ĭ�ϵ�ѡ���ǵ�ǰ�ĳ���
		 */
		int index=citys.indexOf(GatherApplication.location.getCity());
		//���ݴ���
		if (index!=-1) {
			tv.setText(GatherApplication.location.getCity());
			//spinnerʱ������
			//spinner.setSelection(index);
		}
		
		//���û��������
		so.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�������
				final String text= getTvText(et);
				//����dialog
				dialogShow(true, null, null);
				//ģ����ѯ
				FindGatherUtils.findGather(10005, text, 0, 0, new FindGatherListener() {
					@Override
					public void findData(List<GatherBean> beans, BmobException arg1) {
						//�ر�dialog
						dialogDismiss();
						if (arg1==null) {
							GatherApplication.put("data-gather", beans);
							GatherApplication.put("data-title", text+"��ػ");
							startAct(More_ShowGatherActivity.class);
						}
						
					}
				});
				
			}
		});
		//��Ѽ���
		but1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogShow(true, null, null);
				FindGatherUtils.findGather(10002, null, 0, 0, new FindGatherListener() {
					@Override
					public void findData(List<GatherBean> beans, BmobException arg1) {
						//�ر�dialog
						dialogDismiss();
						if (arg1==null) {
							GatherApplication.put("data-gather", beans);
							GatherApplication.put("data-title", "��ѻ");
							startAct(More_ShowGatherActivity.class);
						}
						
					}
				});
			}
		});
		//���ż���
		but2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogShow(true, null, null);
				FindGatherUtils.findGather(10004, null, 0, 0, new FindGatherListener() {
					@Override
					public void findData(List<GatherBean> beans, BmobException arg1) {
						//�ر�dialog
						dialogDismiss();
						if (arg1==null) {
							GatherApplication.put("data-gather", beans);
							GatherApplication.put("data-title", "���Ż");
							startAct(More_ShowGatherActivity.class);
						}
						
					}
				});
			}
		});
		//��������
		but3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogShow(true, null, null);
				FindGatherUtils.findGather(10003, new BmobGeoPoint(GatherApplication.location.getLongitude(), GatherApplication.location.getLatitude()), 0, 0, new FindGatherListener() {
					@Override
					public void findData(List<GatherBean> beans, BmobException arg1) {
						//�ر�dialog
						dialogDismiss();
						if (arg1==null) {
							GatherApplication.put("data-gather", beans);
							GatherApplication.put("data-title", "�����");
							startAct(More_ShowGatherActivity.class);
						}
						
					}
				});
			}
		});
		//�����ѯ
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, final int position, long arg3) {
				dialogShow(true, null, null);
				FindGatherUtils.findGather(10001,titles[position] , 0, 0, new FindGatherListener() {
					@Override
					public void findData(List<GatherBean> beans, BmobException arg1) {
						//�ر�dialog
						dialogDismiss();
						if (arg1==null) {
							GatherApplication.put("data-gather", beans);
							GatherApplication.put("data-title", titles[position]+"�");
							startAct(More_ShowGatherActivity.class);
						}
						
					}
				});
			}
		});
		
	}
}
