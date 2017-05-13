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
	
	
	// ��Ŀ��ʼʱ���е�ͼƬ
	private ImageView img;
	// ���䶯��
	private Animation anim;
	/**
	 * ��ע��ǰ������״̬
	 */
	private boolean isNet;
	/**
	 * ������ʾ�û����������dialog
	 */
	private AlertDialog.Builder alert;

	private ProgressDialog dialogShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_init);
		//��ѯ���л����
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
		
		// ���ڶ����ļ���
		anim.setAnimationListener(new AnimationListener() {
			/**
			 * ��������ִ��
			 */
			@Override
			public void onAnimationStart(Animation animation) {
				// �鿴��ǰ����״̬
				isNet = NetUtils.getNetState(act);
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			/**
			 * ��������ִ�н��� ���ݶ�����ʼʱ�����жϵĽ��,��ʾ�û�Ӧ��������/��ת����½����
			 */
			@Override
			public void onAnimationEnd(Animation animation) {
				// ����� û���������
				if (!isNet) {
					// toast("û�л�ȡ������,�����������");
					if (alert == null) {
						// ����dialog
						createAlert();
					}
					alert.show();

				} else {
					if (GatherApplication.findGatherFlag==1) {
						startAct(LoginActivity.class);
						finish();
					}
//					// ����״̬����
//					// ��ת����½����
				}
			}
		});

		// ����һ������
		img.startAnimation(anim);
	}

	/**
	 * �������ú�,������ʱִ�еķ���
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		// ���µ�������״̬�ķ���
		// isNet = NetUtils.getNetState(act);
		// ��ȡ����ɹ�
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
						// ���µ�������״̬�ķ���
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
							// �ٴλ�ȡ����
							// isNet = NetUtils.getNetState(act);
							if (isNet && GatherApplication.findGatherFlag==1) {
								// ��������
								dialogShow.dismiss();
								toast("�����������,��ѯ���ݳɹ�");
								// ��ת����½����
								startAct(LoginActivity.class);
								finish();
							} else {
								// ���dialogShow��null�����ǲ���ʾ״̬,������ʾ
								if (dialogShow == null || !dialogShow.isShowing()) {
									dialogShow = dialogShow(false, message, null);

								}
								dialogShow.setMessage(message);
								//����Ǳ����0,��ʾ����ʧ��,���¼���
								if (GatherApplication.findGatherFlag==0) {
									//�ѱ�ǻ�ԭ
									//flag=-1;
									runOnUiThread(new Runnable() {
										public void run() {
											//���µ��ò�ѯ���ݵķ���
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
					// ��ÿһ��ѭ������ʱ,���߳��ж�����״̬
					if (isNet) {
						// ��ǰ����������ʱ,ֱ����ת(��������)
						dialogShow.dismiss();
						break;
					}
				}

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// �ٴλ�ȡ����
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
	 * ����dialog
	 */
	private void createAlert() {
		alert=new AlertDialog.Builder(act).setMessage("����״̬����").setPositiveButton("��������", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/**
				 * ��ת��ϵͳ����������
				 */
				startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));				
			}
		}).setNegativeButton("�˳�Ӧ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// �����˳�
				System.exit(0);				
			}
		});
		
//		
//		Builder builder = new AlertDialog.Builder(act);
//		builder.setMessage("����״̬����");
//		builder.setPositiveButton("��������", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				/**
//				 * ��ת��ϵͳ����������
//				 */
//				startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
//			}
//		});
//		builder.setNegativeButton("�˳�Ӧ��", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// �����˳�
//				System.exit(0);
//			}
//		});
		alert.create();
		alert.setCancelable(false);

	}

}
