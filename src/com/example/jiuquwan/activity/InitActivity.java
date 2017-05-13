package com.example.jiuquwan.activity;

import com.example.jiuquwan.R;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuquwan.utils.NetUtils;
import com.example.jiuwuwan.base.BaseActivity;
import com.example.jiuwuwan.base.BaseInterface;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class InitActivity extends BaseActivity implements BaseInterface {
	
	
	// 项目开始时运行的图片
	private ImageView img;
	// 渐变动画
	private Animation anim;
	/**
	 * 标注当前的网络状态
	 */
	private boolean isNet;
	/**
	 * 用来提示用户设置网络的dialog
	 */
	private AlertDialog.Builder alert;

	private ProgressDialog dialogShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_init);
		//查询所有活动数据
		GatherApplication.initData();
		initView();
		initData();
		initViewOper();
	}

	@Override
	public void initView() {
		img = (ImageView) findViewById(R.id.act_init_img);

	}

	@Override
	public void initData() {
		anim = AnimationUtils.loadAnimation(this, R.anim.act_init_alpha);
	}

	@Override
	public void initViewOper() {
		
		// 对于动画的监听
		anim.setAnimationListener(new AnimationListener() {
			/**
			 * 监听动画执行
			 */
			@Override
			public void onAnimationStart(Animation animation) {
				// 查看当前网络状态
				isNet = NetUtils.getNetState(act);
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			/**
			 * 监听动画执行结束 根据动画开始时网络判断的结果,提示用户应设置网络/跳转到登陆界面
			 */
			@Override
			public void onAnimationEnd(Animation animation) {
				// 如果是 没网的情况下
				if (!isNet) {
					// toast("没有获取到网络,请检查网络情况");
					if (alert == null) {
						// 创建dialog
						createAlert();
					}
					alert.show();

				} else {
					if (GatherApplication.findGatherFlag==1) {
						startAct(LoginActivity.class);
						finish();
					}
//					// 网络状态良好
//					// 跳转到登陆界面
				}
			}
		});

		// 启动一个动画
		img.startAnimation(anim);
	}

	/**
	 * 网络设置后,返回来时执行的方法
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		// 重新调用网络状态的方法
		// isNet = NetUtils.getNetState(act);
		// 获取网络成功
		new Thread() {
			public void run() {
				for (int i = 0; i < 60; i++) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (i > 15) {
						// 重新调用网络状态的方法
						isNet = NetUtils.getNetState(act);

					}
					String dialogMassage = "";
					switch (i % 6) {
					case 6:
						dialogMassage += "......";
						break;
					case 1:
						dialogMassage += ".";

						break;
					case 2:
						dialogMassage += "..";

						break;
					case 3:
						dialogMassage += "...";

						break;
					case 4:
						dialogMassage += "....";

						break;
					case 5:
						dialogMassage += "....";

						break;
					}
					final String message = dialogMassage;
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// 再次获取网络
							// isNet = NetUtils.getNetState(act);
							if (isNet && GatherApplication.findGatherFlag==1) {
								// 网络正常
								dialogShow.dismiss();
								toast("网络设置完成,查询数据成功");
								// 跳转到登陆界面
								startAct(LoginActivity.class);
								finish();
							} else {
								// 如果dialogShow是null或者是不显示状态,重新显示
								if (dialogShow == null || !dialogShow.isShowing()) {
									dialogShow = dialogShow(false, message, null);

								}
								dialogShow.setMessage(message);
								//如果是标记是0,表示加载失败,重新加载
								if (GatherApplication.findGatherFlag==0) {
									//把标记还原
									//flag=-1;
									runOnUiThread(new Runnable() {
										public void run() {
											//重新调用查询数据的方法
											GatherApplication.initData();
											if (GatherApplication.findGatherFlag==1) {
												startAct(LoginActivity.class);
												finish();
											}
										}
									});
								}
							}
						}
					});
					// 当每一次循环结束时,子线程判断网络状态
					if (isNet) {
						// 当前几秒中有网时,直接跳转(网络正常)
						dialogShow.dismiss();
						break;
					}
				}

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// 再次获取网络
						isNet = NetUtils.getNetState(act);
						if (!isNet) {
							dialogShow.dismiss();
							alert.show();
						}
					}
				});

			};

		}.start();

	}

	/**
	 * 创建dialog
	 */
	private void createAlert() {
		alert=new AlertDialog.Builder(act).setMessage("网络状态不佳").setPositiveButton("设置网络", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/**
				 * 跳转到系统的设置网络
				 */
				startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));				
			}
		}).setNegativeButton("退出应用", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 正常退出
				System.exit(0);				
			}
		});
		
//		
//		Builder builder = new AlertDialog.Builder(act);
//		builder.setMessage("网络状态不佳");
//		builder.setPositiveButton("设置网络", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				/**
//				 * 跳转到系统的设置网络
//				 */
//				startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
//			}
//		});
//		builder.setNegativeButton("退出应用", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// 正常退出
//				System.exit(0);
//			}
//		});
		alert.create();
		alert.setCancelable(false);

	}

}
