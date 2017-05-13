package com.example.jiuquwan.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.jiuquwan.R;
import com.example.jiuquwan.activity.More_ShowGatherActivity;
import com.example.jiuquwan.activity.ShezhiActivity;
import com.example.jiuquwan.adapter.MyCenter_GridAdapter;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuquwan.bean.GatherBean;
import com.example.jiuquwan.bean.User;
import com.example.jiuquwan.loader.ImageLoaderutils;
import com.example.jiuquwan.utils.FindGatherUtils;
import com.example.jiuquwan.utils.FindUserUtils;
import com.example.jiuquwan.utils.FindGatherUtils.FindGatherListener;
import com.example.jiuquwan.utils.FindUserUtils.FindUserListener;
import com.example.jiuwuwan.base.BaseInterface;
import com.example.jiuwuwan.base.BaseV4Fragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 个人中心的Fragment
 * 
 * @author Administrator
 *
 */
public class MyCenterFrament extends BaseV4Fragment implements BaseInterface {

	private GridView grid;
	// 数据源
	private List<String> datas;
	/**
	 * 用户头像
	 */
	private ImageView u_head;
	/**
	 * 用户名 用户昵称
	 */
	private TextView u_name, u_nickname;
	// ImgLoader 对象
	private ImageLoader loader;
	private DisplayImageOptions opt;
	//设置剪裁后的头像保存的路径
	private static final File file = new File("sdcard/head.png");

	private String[] titles={"优惠","收藏","发起","订单","活动","设置"};
	private int[] imgIds={R.drawable.my_youhui,R.drawable.my_shoucang,
			R.drawable.my_send,R.drawable.my_order,
			R.drawable.my_gather,R.drawable.my_shezhi};
	
	
	
	@Override
	public View initLayout(LayoutInflater inflater) {
		View v = inflater.inflate(R.layout.fragment_mycenter, null);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// 创建loader
		loader = ImageLoaderutils.getInstance(act);
		/*
		 *  获取配置对象
		 *  参数1获取默认图片
		 *  参数2图片加载失败时的图片
		 */
		opt = ImageLoaderutils.getOpt(R.drawable.logo1, R.drawable.logo1_error);
		initView();
		initData();
		initViewOper();

	}

	@Override
	public void initView() {
		grid = (GridView) findViewById(R.id.fragment_mycenter_grid);
		// 用户头像
		u_head = imgById(R.id.fragment_mycenter_head);
		// 用户名
		u_name = tvById(R.id.fragment_mycenter_uname);
		// 用户昵称
		u_nickname = tvById(R.id.fragment_mycenter_nickname);

	}

	@Override
	public void initData() {
		datas = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			datas.add(new String());
		}
		// 查询用户数据
		FindUserUtils.findUserById(GatherApplication.u.getObjectId(), new FindUserListener() {

			@Override
			public void toUser(User user) {
				// 设置观察者,当前登陆的用户对象
				GatherApplication.u = user;
				// 更改ui视图
				initUserView();
			}
		});
	}

	/**
	 * 更改ui视图
	 */
	protected void initUserView() {
		// 从应用缓存中获取用户头像的地址
		BmobFile headFile = GatherApplication.u.getHeadBit();
		//当用户对象中有用户头像地址,,证明已经设置过头像
		if (headFile != null) {
			//获取url
			String url = headFile.getFileUrl();
			Log.i("Log", "头像的地址是:" + url);
			//根据"/"进行拆分
			String[] split = url.split("/");
			// 指定头像文件夹路径
			File dir = new File("sdcard/gather/img");
			/**
			 * 缓存图片到指定的路径
			 * 相对地址+文件名
			 */
			final File iconFile = new File(dir.getAbsoluteFile() + "/" + split[split.length - 1]);
			// 如果路径不存在
//			if (!dir.exists()) {
//				
//			}

			// 如果存在
			if (dir.exists()) {
				// 直接设置头像
				u_head.setImageURI(Uri.fromFile(iconFile));
			} else {
				// 创建
				dir.mkdirs();
				/**
				 * 
				 * 把得到的头像缓存到本地
				 */
				headFile.download(iconFile, new DownloadFileListener() {

					@Override
					public void onProgress(Integer arg0, long arg1) {

					}

					@Override
					public void done(String arg0, BmobException e) {
						// 如果不出现异常
						if (e == null) {
							u_head.setImageURI(Uri.fromFile(iconFile));

						}
					}
				});

			}

			/**
			 * 更改头像 参数1头像的网址 参数2ImageView对象 参数3配置对象
			 */
			//loader.displayImage(url, u_head, opt);
		} else {
			//当获取的用户对象中没有,用户头像地址,证明是第一登陆还没有设置头像(默认显示的头像)
			u_head.setImageResource(R.drawable.logo1);
		}
		// 设置昵称
		u_nickname.setText(GatherApplication.u.getNickName());
		// 设置用户名
		u_name.setText("就去玩:" + GatherApplication.u.getUsername());

	}

	private List<GatherBean> beans;
	@Override
	public void initViewOper() {
		// 设置数据源
		grid.setAdapter(new MyCenter_GridAdapter(imgIds, titles, act));
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position==1) {
					List<String> ids=GatherApplication.u.getLoveGatherIds();
					Log.i("log","点过攒的活动"+ids.get(0));
					for (int i = 0; i < ids.size(); i++) {
						logi("点过攒的活动"+ids.get(i));
					}
					dialogShow(true, null, null);
					beans = new ArrayList<>();
					for (int i = 0; i < ids.size(); i++) {
						BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
						query.getObject(ids.get(i), new QueryListener<GatherBean>() {

						    @Override
						    public void done(GatherBean bean, BmobException e) {
						        if(e==null){
						        	beans.add(bean);
						        }else{
						            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
						        }
						    }

						});
					}
					//关闭dialog
					dialogDismiss();
					GatherApplication.put("data-gather", beans);
					GatherApplication.put("data-title", "我的收藏");
					startAct(More_ShowGatherActivity.class);
				}else if(position==5){
					//跳转到设置界面
					startAct(ShezhiActivity.class);
					
				}
			}
		});
		
		// 当用户点击头像
		u_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/**
				 * 更改头像 跳转到系统相册,获取内容
				 */
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				// 设置内容类型
				intent.setType("image/*");
				// 剪裁
				intent.putExtra("crop", "circleCrop");
				// 剪裁比例
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 50);
				intent.putExtra("outputY", 50);
				// 设置头像保存的路径
				intent.putExtra("output", Uri.fromFile(file));
				// 带返回值的跳转
				startActivityForResult(intent, 0);

			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && data != null && data.getExtras() != null) {
			updateUserHead();
		}

	}

	private void updateUserHead() {
		// 更新头像时,弹出dialog
		dialogShow(true, null, null);
		// toast("头像设置完毕");
		final User ub = new User();
		// 传入User中
		ub.setHeadBit(new BmobFile(file));

		/**
		 * 上传图片
		 */
		ub.getHeadBit().upload(new UploadFileListener() {

			@Override
			public void done(BmobException e) {
				// 如果有异常
				if (e != null) {
					toast("第一个头像设置失败");
					dialogDismiss();
					return;
				}
				/**
				 * 上传到bmob云中
				 */
				ub.update(GatherApplication.u.getObjectId(), new UpdateListener() {

					@Override
					public void done(BmobException ex) {
						// 如果有异常
						if (ex != null) {
							toast("第二个头像设置失败");
							dialogDismiss();
							return;
						}
						dialogDismiss();
						toast("头像上传成功");
						// 从内存卡路径中获取图片
						Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());
						// 设置
						u_head.setImageBitmap(bit);
					}
				});

			}
		});
	}

}
