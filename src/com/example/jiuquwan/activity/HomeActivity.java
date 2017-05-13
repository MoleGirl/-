package com.example.jiuquwan.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.jiuquwan.R;
import com.example.jiuquwan.adapter.Act_home_PagerAdapter;
import com.example.jiuquwan.fragment.HomeFrament;
import com.example.jiuquwan.fragment.MessageFrament;
import com.example.jiuquwan.fragment.MoreFrament;
import com.example.jiuquwan.fragment.MyCenterFrament;
import com.example.jiuwuwan.base.BaseActivity;
import com.example.jiuwuwan.base.BaseInterface;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * ������
 * @author Administrator
 *
 */
public class HomeActivity extends BaseActivity implements BaseInterface, android.view.View.OnClickListener{

	//�±ߵ��ĸ�lin
	private LinearLayout[] lins=new LinearLayout[4]; 
	private int[] resLinIds={R.id.act_home_lin1,R.id.act_home_lin2,R.id.act_home_lin3,R.id.act_home_lin4};
	//�±ߵ��ĸ�img
	private ImageView[] imgs=new ImageView[4]; 
	private int[] resImgIds={R.id.act_home_lin1_img,R.id.act_home_lin2_img,R.id.act_home_lin3_img,R.id.act_home_lin4_img};
	//�±ߵ��ĸ��ı�
	private TextView[] tvs=new TextView[4]; 
	private int[] resTvIds={R.id.act_home_lin1_tv,R.id.act_home_lin2_tv,R.id.act_home_lin3_tv,R.id.act_home_lin4_tv};
	//�±�ѡ��ʱ��ʾ��ͼƬ
	private int[] iconIds_on={R.drawable.home,R.drawable.msg,R.drawable.my,R.drawable.more}; 
	//�±�δѡ��ʱ��ͼƬ
	private int[] iconIds_off={R.drawable.home_1,R.drawable.msg_1,R.drawable.my_1,R.drawable.more_1}; 
	
	private ViewPager vp;
	//vp���ĸ�����Դ(Fragment)
	private List<Fragment> frags;
	private AlertDialog backDialog;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.act_home);
		initView();
		initData();
		initViewOper();
		
	}
	
	@Override
	public void initView() {
		
		//�ؼ���ʼ��
		for (int i = 0; i < 4; i++) {
			lins[i]=linearById(resLinIds[i]);
			imgs[i]=imgById(resImgIds[i]);
			tvs[i]=tvById(resTvIds[i]);
			lins[i].setOnClickListener(this);
		}
		//��ʼ��viewpager
		vp=(ViewPager) findViewById(R.id.act_home_vp);
		
	}
	
	@Override
	public void initData() {
		//��ʼ���ĸ�Fragment
		frags=new ArrayList<>();
		frags.add(new HomeFrament());
		frags.add(new MessageFrament());
		frags.add(new MyCenterFrament());
		frags.add(new MoreFrament());
	}

	@Override
	public void initViewOper() {
		//����չʾ������Դ
		vp.setAdapter(new Act_home_PagerAdapter(getSupportFragmentManager(), frags));
		//����ViewPager�Ļ���
		vp.addOnPageChangeListener(new OnPageChangeListener() {
			//��ҳ�滬��ʱ
			@Override
			public void onPageSelected(int position) {
				//���ø����±���ͼ�ķ���
				updateView(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	
	/**
	 * ���ڵײ�4��lin�ĵ���¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_home_lin1:
			updateView(0);
			vp.setCurrentItem(0);
			break;
		case R.id.act_home_lin2:
			updateView(1);
			vp.setCurrentItem(1);
			
			break;
		case R.id.act_home_lin3:
			updateView(2);
			vp.setCurrentItem(2);
			
			break;
		case R.id.act_home_lin4:
			
			updateView(3);
			vp.setCurrentItem(3);
			break;

		
		}
		
		
	}

	/**
	 * ���ĵ�ǰѡ�е���ͼ
	 * @param position ��ǰ�û�����ѡ����±� 0,1,2,3
	 */
	public void updateView(int position){
		for (int i = 0; i < 4; i++) {
			if (i==position) {
				imgs[i].setImageResource(iconIds_on[i]);
				//tvs[i].setTextColor(Color.parseColor("#00ff00"));
				//lins[i].setBackgroundColor(Color.parseColor("#e2e2e2"));
			}else{
				imgs[i].setImageResource(iconIds_off[i]);
				//tvs[i].setTextColor(Color.parseColor("#000000"));
				//lins[i].setBackgroundColor(Color.parseColor("#ffffff"));
			}
		}
	}

	/**
	 *	��ת����������
	 */
	public void onaddClick(View v) {

		startAct(SendGatherActivity.class);
		
		
	}

	
	/**
	 * ������ؼ���ʱ�򵯳�dialog
	 */
	public void onBackClick(View v) {
		//����dialog
		//showBackDialog();
		
		
	}

	/**
	 * ���û�����˳�ʱ,��ʾ�û��Ƿ��˳�
	 */
	private void showBackDialog() {
		if (backDialog==null) {
			backDialog = new AlertDialog.Builder(act).setMessage("��ȷ�˳���?").setPositiveButton("ȷ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					act.finish();
				}
			}).setNegativeButton("ȡ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).create();
			backDialog.show();
		}
	}
	

	/**
	 * ��������ذ���ʱ,ͬ������dialog
	 */
	@Override
	public void onBackPressed() {
		showBackDialog();
	}
	

	

	
}
