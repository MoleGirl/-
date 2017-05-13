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
 * 主界面
 * @author Administrator
 *
 */
public class HomeActivity extends BaseActivity implements BaseInterface, android.view.View.OnClickListener{

	//下边的四个lin
	private LinearLayout[] lins=new LinearLayout[4]; 
	private int[] resLinIds={R.id.act_home_lin1,R.id.act_home_lin2,R.id.act_home_lin3,R.id.act_home_lin4};
	//下边的四个img
	private ImageView[] imgs=new ImageView[4]; 
	private int[] resImgIds={R.id.act_home_lin1_img,R.id.act_home_lin2_img,R.id.act_home_lin3_img,R.id.act_home_lin4_img};
	//下边的四个文本
	private TextView[] tvs=new TextView[4]; 
	private int[] resTvIds={R.id.act_home_lin1_tv,R.id.act_home_lin2_tv,R.id.act_home_lin3_tv,R.id.act_home_lin4_tv};
	//下边选中时显示的图片
	private int[] iconIds_on={R.drawable.home,R.drawable.msg,R.drawable.my,R.drawable.more}; 
	//下边未选中时的图片
	private int[] iconIds_off={R.drawable.home_1,R.drawable.msg_1,R.drawable.my_1,R.drawable.more_1}; 
	
	private ViewPager vp;
	//vp的四个数据源(Fragment)
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
		
		//控件初始化
		for (int i = 0; i < 4; i++) {
			lins[i]=linearById(resLinIds[i]);
			imgs[i]=imgById(resImgIds[i]);
			tvs[i]=tvById(resTvIds[i]);
			lins[i].setOnClickListener(this);
		}
		//初始化viewpager
		vp=(ViewPager) findViewById(R.id.act_home_vp);
		
	}
	
	@Override
	public void initData() {
		//初始化四个Fragment
		frags=new ArrayList<>();
		frags.add(new HomeFrament());
		frags.add(new MessageFrament());
		frags.add(new MyCenterFrament());
		frags.add(new MoreFrament());
	}

	@Override
	public void initViewOper() {
		//设置展示的数据源
		vp.setAdapter(new Act_home_PagerAdapter(getSupportFragmentManager(), frags));
		//监听ViewPager的滑动
		vp.addOnPageChangeListener(new OnPageChangeListener() {
			//当页面滑动时
			@Override
			public void onPageSelected(int position) {
				//调用更新下边视图的方法
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
	 * 对于底部4个lin的点击事件
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
	 * 更改当前选中的视图
	 * @param position 当前用户触摸选项卡的下标 0,1,2,3
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
	 *	跳转到发布界面
	 */
	public void onaddClick(View v) {

		startAct(SendGatherActivity.class);
		
		
	}

	
	/**
	 * 点击返回键的时候弹出dialog
	 */
	public void onBackClick(View v) {
		//弹出dialog
		//showBackDialog();
		
		
	}

	/**
	 * 当用户点击退出时,提示用户是否退出
	 */
	private void showBackDialog() {
		if (backDialog==null) {
			backDialog = new AlertDialog.Builder(act).setMessage("您确退出吗?").setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					act.finish();
				}
			}).setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).create();
			backDialog.show();
		}
	}
	

	/**
	 * 当点击返回按键时,同样弹出dialog
	 */
	@Override
	public void onBackPressed() {
		showBackDialog();
	}
	

	

	
}
