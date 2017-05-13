package com.example.jiuquwan.activity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.jiuquwan.R;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuquwan.bean.GatherBean;
import com.example.jiuquwan.bean.User;
import com.example.jiuquwan.utils.FindGatherUtils;
import com.example.jiuquwan.utils.FindUserUtils;
import com.example.jiuquwan.utils.FindUserUtils.FindUserListener;
import com.example.jiuwuwan.base.BaseActivity;
import com.example.jiuwuwan.base.BaseInterface;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import c.b.BP;
import c.b.PListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * չʾ������Activity ��ת����ʱ,������Applicition�м���һ��GatherBean����,key=show_gather
 * 
 * @author Administrator
 *
 */
public class GatherActivity extends BaseActivity implements BaseInterface {

	
//	Timer mTimer;
//	TimerTask mTask;
//	int pageIndex = 1;
//	boolean isTaskRun;
	
	
	
	//�ϱߵ��ֲ�ͼ
	private ViewPager viewPager;
	private ImageView img;
	
	/**
	 * ���Ͻǲ�����ı�,���ݲ�ͬ��tag��ʾ��ͬ������ -1 δ���� 0�Լ�(��ǰ��½�˻�) 1 �Ѳ���
	 */
	private TextView canyu;
	private GatherBean bean;

	// lin1Ʒ�۵�lin,lin2�ǵ����ʾ�ֻ�����
	private LinearLayout lin1, lin2;
	// ͷ��,
	private ImageView head;
	// �ǳ�(�û���)|��ľ���ص�|����|�����|�����|�绰����|����|��������|����|��ʼʱ��|����
	private TextView nickname, address, city, title, context, phoneNumber, km, loveNumber, rmb, time, canyuNuber;
	//����������
	private int paymentUserIds;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.act_gather);
		
		initView();
		initData();
		initViewOper();

	}

	@Override
	public void initView() {
		canyu = tvById(R.id.act_gather_canyu);
		nickname = tvById(R.id.act_gather_nickname);
		address = tvById(R.id.act_gather_address);
		city = tvById(R.id.act_gather_city);
		title = tvById(R.id.act_gather_title);
		context = tvById(R.id.act_gather_context);
		phoneNumber = tvById(R.id.act_gather_phoneNumber);
		km = tvById(R.id.act_gather_km);
		loveNumber = tvById(R.id.act_gather_loveNumber);
		rmb = tvById(R.id.act_gather_rmb);
		time = tvById(R.id.act_gather_time);
		canyuNuber = tvById(R.id.act_gather_canyuNuber);

		//viewPager=(ViewPager) findViewById(R.id.vp);
		
		head = imgById(R.id.act_gather_head);
		lin1 = linearById(R.id.act_gather_lin1);
		lin2 = linearById(R.id.act_gather_lin2);

		img=imgById(R.id.act_gather_img);
		
		// ��ǰӦչʾ�Ļ����
		bean = (GatherBean) GatherApplication.get("show-gather", true);
		initUserStart();

	}

	/**
	 * ��ʼ�����Ͻ���ʵ���ı�
	 */
	private void initUserStart() {
		// �������Ķ����������Լ�,���Ͻ���ʾ����
		Log.i("Log", "��ǰ�û�id" + GatherApplication.u.getObjectId());
		if (bean.getGatherUserId().equals(GatherApplication.u.getObjectId())) {
			canyu.setText("����");
			canyu.setTag("0");
			return;
		}
		// �������Ѿ�����Ķ��󼯺��а�����ǰ��½�û�,�����ǰ�û����Ѿ������û�
		 if
		 (bean.getPaymentUserId().contains(GatherApplication.u.getObjectId()))
		 {
		 canyu.setText("�Ѹ���");
		 canyu.setTextColor(Color.parseColor("#e2e2e2"));
		 canyu.setTag("1");
		 return;
		 }

	}

	@Override
	public void initData() {
		// ��������(����String����)
		loveNumber.setText(bean.getLoveCount() + "");
		// ��ϸ����
		context.setText(bean.getGatherIntro());
		// �����
		title.setText(bean.getGatherTitle());
		// ����
		city.setText(bean.getGatherCity());
		// ��ʼ�ص�
		address.setText(bean.getGatherSite());
		// ���
		rmb.setText(bean.getGatherRMB());
		// ��ʼʱ��
		time.setText(bean.getGatherTime());
		Log.i("Log", "���������:" + bean.getPaymentUserId().size());
		paymentUserIds = bean.getPaymentUserId().size();
		// ���������
		canyuNuber.setText(paymentUserIds + "");
		// ���ݻ��id,��ȡ��ǰ����û�����
		FindUserUtils.findUserById(bean.getGatherUserId(), new FindUserListener() {

			@Override
			public void toUser(User user) {
				// �����ǳ�(�û���)
				nickname.setText(user.getNickName());
				// �绰
				phoneNumber.setText(user.getMobilePhoneNumber());
				// ����ͷ��
				// ��ȡbmob�б���ͼƬʱ������
				String imgHead = user.getHeadBit().getFileUrl().substring(
						user.getHeadBit().getFileUrl().length() - 4 - 32, user.getHeadBit().getFileUrl().length() - 4);
				// ����ͼƬ�����·��(����ͷ��)
				final File imgHeadFile = new File("sdcard/imgHead" + imgHead + ".png");
				if (imgHeadFile.exists()) {
					// �������ֱ��ʹ��
					head.setImageBitmap(BitmapFactory.decodeFile(imgHeadFile.getAbsolutePath()));
				} else {
					// userΪ��ǰ����û�
					user.getHeadBit().download(imgHeadFile, new DownloadFileListener() {

						@Override
						public void onProgress(Integer arg0, long arg1) {

						}

						@Override
						public void done(String bitFilePath, BmobException arg1) {
							if (arg1 == null) {
								head.setImageBitmap(BitmapFactory.decodeFile(imgHeadFile.getAbsolutePath()));
							}
						}
					});
				}

			}
		});

		// ���þ���
		String kmText = "0m";
		try {
			/**
			 * �ҵ�λ�� ����1γ�� ����2����
			 */
			LatLng start = new LatLng(GatherApplication.location.getLatitude(),
					GatherApplication.location.getLongitude());

			/**
			 * ��ص� ����1γ�� ����2����
			 */
			LatLng end = new LatLng(bean.getPoint().getLatitude(), bean.getPoint().getLongitude());
			// ����p1,p2����֮���ֱ�߾���,��λ:��
			long m = (long) DistanceUtil.getDistance(start, end);

			if (m >= 1000) {
				int kms = (int) ((m + 500) / 1000);
				kmText = kms + "km";
			} else {
				kmText = m + "m";
			}
		} catch (Exception e) {
			Log.i("Log", "�쳣ԭ��:" + e.getMessage());
			kmText = "��λʧ��";
		}
		km.setText(kmText);

		//���ϱ���ʾ��ͼƬ
		// ��ȡ��ĵ�һ��ͼƬ
		// ��ȡbmob�б���ͼƬʱ������
		String imgName = bean.getGatherJPG().get(0).getFileUrl().substring(
				bean.getGatherJPG().get(0).getFileUrl().length() - 32 - 5,
				bean.getGatherJPG().get(0).getFileUrl().length() - 5);
		//Log.i("Log","��һ��ͼƬ:"+bean.getGatherJPG().get(0).getFileUrl());
		//Log.i("Log","��ȡ��:"+imgName);
		
		// ����ͼƬ�����·��
		final File gatherImgFile = new File("sdcard/gatherImg" + imgName + ".jpeg");
		if (gatherImgFile.exists()) {
			// �������ֱ��ʹ��
			//img.setImageBitmap(BitmapFactory.decodeFile(gatherImgFile.getAbsolutePath()));
			img.setImageBitmap(GatherApplication.getimage(gatherImgFile.getAbsolutePath()));
		} else {
			// ��������ھ�ȥ����
			bean.getGatherJPG().get(0).download(gatherImgFile,new DownloadFileListener() {

				@Override
				public void onProgress(Integer arg0, long arg1) {
					// TODO Auto-generated method stub

				}

				@Override
				public void done(String bitFilePath, BmobException arg1) {
					if (arg1 == null) {
						//img.setImageBitmap(BitmapFactory.decodeFile(gatherImgFile.getAbsolutePath()));
						img.setImageBitmap(GatherApplication.getimage(gatherImgFile.getAbsolutePath()));
					}
				}
			});
		}
		
		
	}

	@Override
	public void initViewOper() {

	}
	


	

	
	/**
	 * 
	 */
	public void onClick(View v) {
		// ���ݲ�ͬtagֵ,��ʾ��ͬ���ı�����Ӧ�¼�
		switch (canyu.getTag().toString()) {
		// ����
		case "-1":
			toast("����˲���");
			AlertDialog builder = new AlertDialog.Builder(act).setMessage("��ȷ��Ҫ����?").setPositiveButton("֧����", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// ֧������
					BP.pay(null, null, 0.02, true, new PListener() {

						// ֧���ɹ�,������ϴ����ֶ���ѯȷ��
						@Override
						public void succeed() {
							Toast.makeText(act, "֧���ɹ�!", Toast.LENGTH_SHORT).show();

						}

						@Override
						public void fail(int arg0, String arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void orderId(String arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void unknow() {

						}
					});
				}
			}).setNegativeButton("΢��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(act, "�����΢��֧��", Toast.LENGTH_SHORT).show();
					// ֧������
					BP.pay(null, null, 0.02, false, new PListener() {

						// ֧���ɹ�,������ϴ����ֶ���ѯȷ��
						@Override
						public void succeed() {
							GatherBean bean1= new GatherBean();
							/**
							 * ����1�����޸ĵ���
							 * ����2��ǰ�û�id
							 */
							bean1.addUnique("paymentUserId", GatherApplication.u.getObjectId());
							//�������ݿ��е�����
							bean1.update(bean.getObjectId(), new UpdateListener() {
								@Override
								public void done(BmobException arg0) {
									if (arg0==null) {
										Toast.makeText(act, "֧���ɹ�!", Toast.LENGTH_SHORT).show();
										// ֧���ɹ���ѵ�ǰ�û���ӵ���ǰ��Ѳ���ļ�����
										bean.getPaymentUserId().add(GatherApplication.u.getObjectId());
										// �ѵ�ǰ�û�֧���Ļ��id��ӵ�,�Լ����Ѿ�����Ļ�ļ��ϵ�id��
										// GatherApplication.u.getPayGatherIds().add(bean.getGatherUserId());
										// ��ǰ�û�֧����(�Ѿ������)��ĸ���+1
										// GatherApplication.payGatherIds+=1;
										//GatherApplication.paymentUserId = GatherApplication.paymentUserId + 1;
										paymentUserIds=paymentUserIds+1;
										Log.i("Log", "��ǰ�����ĸ���" + paymentUserIds);
										// ���������
										canyuNuber.setText(paymentUserIds + "");
										//tag�޸ĳ�1(�Ѿ�����)
										canyu.setText("�Ѹ���");
										canyu.setTextColor(Color.parseColor("#e2e2e2"));
										canyu.setTag("1");
									}
								}
							});
							
							

						}
						// ��Ϊ�����ԭ��,֧�����δ֪(С�����¼�),���ڱ�������Ժ��ֶ���ѯ
						@Override
						public void unknow() {
							Toast.makeText(act, "֧�����δ֪,���Ժ��ֶ���ѯ", Toast.LENGTH_SHORT)
									.show();
						}
						// ֧��ʧ��,ԭ��������û��ж�֧������,Ҳ����������ԭ��
						@Override
						public void fail(int code, String reason) {

							// ��codeΪ-2,��ζ���û��ж��˲���
							// codeΪ-3��ζ��û�а�װBmobPlugin���
							if (code == -3) {
								Toast.makeText(
										act,
										"��⵽����δ��װ֧�����,�޷�����֧��,���Ȱ�װ���(�Ѵ���ڱ���,����������),��װ����������֧��",
										0).show();
							} else {
								Toast.makeText(act, "֧���ж�!", Toast.LENGTH_SHORT)
										.show();
							}
						}

						// ���۳ɹ����,���ض�����
						@Override
						public void orderId(String orderId) {
							Toast.makeText(
									act,
									"��ȡ�����ɹ�!��ȴ���ת��֧��ҳ��~",
									0).show();
							// �˴�Ӧ�ñ��涩����,���籣������ݿ��,�Ա��Ժ��ѯ
						}
						
						
					});
				}
			}).create();
			builder.setCancelable(true);
			builder.show();

			break;
		// �����˵Ĺ�����Ӧ
		case "0":

			break;
		// �Ѹ���
		case "1":

			break;

		}
	}

	/**
	 * �����ʾ�ֻ�����
	 */
	public void showphone(View v) {
		// �������ʾ,��Ӱ��
		if (lin2.getVisibility() == View.VISIBLE) {
			lin2.setVisibility(View.GONE);
		} else {
			lin2.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * �������
	 */
	public void back(View v) {
		this.finish();
	}
	
	
	
}
