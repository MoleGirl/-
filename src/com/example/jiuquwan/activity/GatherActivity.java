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
 * 展示活动详情的Activity 跳转此类时,必须在Applicition中加入一个GatherBean对象,key=show_gather
 * 
 * @author Administrator
 *
 */
public class GatherActivity extends BaseActivity implements BaseInterface {

	
//	Timer mTimer;
//	TimerTask mTask;
//	int pageIndex = 1;
//	boolean isTaskRun;
	
	
	
	//上边的轮播图
	private ViewPager viewPager;
	private ImageView img;
	
	/**
	 * 右上角参与的文本,根据不同的tag显示不同的内容 -1 未参与 0自己(当前登陆账户) 1 已参与
	 */
	private TextView canyu;
	private GatherBean bean;

	// lin1品论的lin,lin2是点击显示手机号码
	private LinearLayout lin1, lin2;
	// 头像,
	private ImageView head;
	// 昵称(用户名)|活动的具体地点|城市|活动标题|活动详情|电话号码|距离|点赞数量|活动金额|开始时间|参与
	private TextView nickname, address, city, title, context, phoneNumber, km, loveNumber, rmb, time, canyuNuber;
	//参与活动的人数
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
		
		// 当前应展示的活动对象
		bean = (GatherBean) GatherApplication.get("show-gather", true);
		initUserStart();

	}

	/**
	 * 初始化右上角现实的文本
	 */
	private void initUserStart() {
		// 如果点击的对象详情是自己,右上角显示管理
		Log.i("Log", "当前用户id" + GatherApplication.u.getObjectId());
		if (bean.getGatherUserId().equals(GatherApplication.u.getObjectId())) {
			canyu.setText("管理");
			canyu.setTag("0");
			return;
		}
		// 如果活动中已经参与的对象集合中包含当前登陆用户,则代表当前用户是已经付款用户
		 if
		 (bean.getPaymentUserId().contains(GatherApplication.u.getObjectId()))
		 {
		 canyu.setText("已付款");
		 canyu.setTextColor(Color.parseColor("#e2e2e2"));
		 canyu.setTag("1");
		 return;
		 }

	}

	@Override
	public void initData() {
		// 点赞数量(传入String类型)
		loveNumber.setText(bean.getLoveCount() + "");
		// 详细内容
		context.setText(bean.getGatherIntro());
		// 活动标题
		title.setText(bean.getGatherTitle());
		// 城市
		city.setText(bean.getGatherCity());
		// 开始地点
		address.setText(bean.getGatherSite());
		// 金额
		rmb.setText(bean.getGatherRMB());
		// 开始时间
		time.setText(bean.getGatherTime());
		Log.i("Log", "参与的人数:" + bean.getPaymentUserId().size());
		paymentUserIds = bean.getPaymentUserId().size();
		// 参与的数量
		canyuNuber.setText(paymentUserIds + "");
		// 根据活动的id,获取当前活动的用户对象
		FindUserUtils.findUserById(bean.getGatherUserId(), new FindUserListener() {

			@Override
			public void toUser(User user) {
				// 设置昵称(用户名)
				nickname.setText(user.getNickName());
				// 电话
				phoneNumber.setText(user.getMobilePhoneNumber());
				// 设置头像
				// 截取bmob中保存图片时的名字
				String imgHead = user.getHeadBit().getFileUrl().substring(
						user.getHeadBit().getFileUrl().length() - 4 - 32, user.getHeadBit().getFileUrl().length() - 4);
				// 设置图片保存的路径(设置头像)
				final File imgHeadFile = new File("sdcard/imgHead" + imgHead + ".png");
				if (imgHeadFile.exists()) {
					// 如果存在直接使用
					head.setImageBitmap(BitmapFactory.decodeFile(imgHeadFile.getAbsolutePath()));
				} else {
					// user为当前活动的用户
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

		// 设置距离
		String kmText = "0m";
		try {
			/**
			 * 我的位置 参数1纬度 参数2经度
			 */
			LatLng start = new LatLng(GatherApplication.location.getLatitude(),
					GatherApplication.location.getLongitude());

			/**
			 * 活动地点 参数1纬度 参数2经度
			 */
			LatLng end = new LatLng(bean.getPoint().getLatitude(), bean.getPoint().getLongitude());
			// 计算p1,p2两点之间的直线距离,单位:米
			long m = (long) DistanceUtil.getDistance(start, end);

			if (m >= 1000) {
				int kms = (int) ((m + 500) / 1000);
				kmText = kms + "km";
			} else {
				kmText = m + "m";
			}
		} catch (Exception e) {
			Log.i("Log", "异常原因:" + e.getMessage());
			kmText = "定位失败";
		}
		km.setText(kmText);

		//最上边显示的图片
		// 获取活动的第一张图片
		// 截取bmob中保存图片时的名字
		String imgName = bean.getGatherJPG().get(0).getFileUrl().substring(
				bean.getGatherJPG().get(0).getFileUrl().length() - 32 - 5,
				bean.getGatherJPG().get(0).getFileUrl().length() - 5);
		//Log.i("Log","第一张图片:"+bean.getGatherJPG().get(0).getFileUrl());
		//Log.i("Log","截取后:"+imgName);
		
		// 设置图片保存的路径
		final File gatherImgFile = new File("sdcard/gatherImg" + imgName + ".jpeg");
		if (gatherImgFile.exists()) {
			// 如果存在直接使用
			//img.setImageBitmap(BitmapFactory.decodeFile(gatherImgFile.getAbsolutePath()));
			img.setImageBitmap(GatherApplication.getimage(gatherImgFile.getAbsolutePath()));
		} else {
			// 如果不存在就去下载
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
		// 根据不同tag值,显示不同的文本的响应事件
		switch (canyu.getTag().toString()) {
		// 参与
		case "-1":
			toast("点击了参与");
			AlertDialog builder = new AlertDialog.Builder(act).setMessage("您确定要参吗?").setPositiveButton("支付宝", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 支付代码
					BP.pay(null, null, 0.02, true, new PListener() {

						// 支付成功,如果金额较大请手动查询确认
						@Override
						public void succeed() {
							Toast.makeText(act, "支付成功!", Toast.LENGTH_SHORT).show();

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
			}).setNegativeButton("微信", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(act, "点击了微信支付", Toast.LENGTH_SHORT).show();
					// 支付代码
					BP.pay(null, null, 0.02, false, new PListener() {

						// 支付成功,如果金额较大请手动查询确认
						@Override
						public void succeed() {
							GatherBean bean1= new GatherBean();
							/**
							 * 参数1表中修改的列
							 * 参数2当前用户id
							 */
							bean1.addUnique("paymentUserId", GatherApplication.u.getObjectId());
							//更新数据库中的数据
							bean1.update(bean.getObjectId(), new UpdateListener() {
								@Override
								public void done(BmobException arg0) {
									if (arg0==null) {
										Toast.makeText(act, "支付成功!", Toast.LENGTH_SHORT).show();
										// 支付成功后把当前用户添加到当前活动已参与的集合中
										bean.getPaymentUserId().add(GatherApplication.u.getObjectId());
										// 把当前用户支付的活动的id添加到,自己的已经参与的活动的集合的id中
										// GatherApplication.u.getPayGatherIds().add(bean.getGatherUserId());
										// 当前用户支付的(已经参与的)活动的个数+1
										// GatherApplication.payGatherIds+=1;
										//GatherApplication.paymentUserId = GatherApplication.paymentUserId + 1;
										paymentUserIds=paymentUserIds+1;
										Log.i("Log", "当前活动参与的个数" + paymentUserIds);
										// 参与的数量
										canyuNuber.setText(paymentUserIds + "");
										//tag修改成1(已经付款)
										canyu.setText("已付款");
										canyu.setTextColor(Color.parseColor("#e2e2e2"));
										canyu.setTag("1");
									}
								}
							});
							
							

						}
						// 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
						@Override
						public void unknow() {
							Toast.makeText(act, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
									.show();
						}
						// 支付失败,原因可能是用户中断支付操作,也可能是网络原因
						@Override
						public void fail(int code, String reason) {

							// 当code为-2,意味着用户中断了操作
							// code为-3意味着没有安装BmobPlugin插件
							if (code == -3) {
								Toast.makeText(
										act,
										"监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
										0).show();
							} else {
								Toast.makeText(act, "支付中断!", Toast.LENGTH_SHORT)
										.show();
							}
						}

						// 无论成功与否,返回订单号
						@Override
						public void orderId(String orderId) {
							Toast.makeText(
									act,
									"获取订单成功!请等待跳转到支付页面~",
									0).show();
							// 此处应该保存订单号,比如保存进数据库等,以便以后查询
						}
						
						
					});
				}
			}).create();
			builder.setCancelable(true);
			builder.show();

			break;
		// 发起人的管理响应
		case "0":

			break;
		// 已付款
		case "1":

			break;

		}
	}

	/**
	 * 点击显示手机号码
	 */
	public void showphone(View v) {
		// 如果是显示,就影藏
		if (lin2.getVisibility() == View.VISIBLE) {
			lin2.setVisibility(View.GONE);
		} else {
			lin2.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 点击返回
	 */
	public void back(View v) {
		this.finish();
	}
	
	
	
}
