package com.example.jiuquwan.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.jiuquwan.R;
import com.example.jiuquwan.activity.GatherActivity;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuquwan.bean.GatherBean;
import com.example.jiuquwan.bean.User;
import com.example.jiuquwan.utils.FindUserUtils;
import com.example.jiuquwan.utils.FindUserUtils.FindUserListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * home中的单个视图的adapter
 * 
 * @author Administrator
 *
 */
public class Gather_list_Adapter extends BaseAdapter {

	// 数据源
	private List<GatherBean> datas;
	// 填充器
	private LayoutInflater inflater;
	// 上下文
	private Context context;

	public Gather_list_Adapter(List<GatherBean> datas, Context context) {
		super();
		this.datas = datas;
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		ViewHolder vh = null;
		if (v == null) {
			v = inflater.inflate(R.layout.home_list_item, null);
			vh = new ViewHolder();
			vh.head = (ImageView) v.findViewById(R.id.list_item_head);
			vh.gatherImg = (ImageView) v.findViewById(R.id.list_item_img);
			vh.loveImg = (ImageView) v.findViewById(R.id.list_item_love);

			vh.uname = (TextView) v.findViewById(R.id.list_item_uname);
			vh.time = (TextView) v.findViewById(R.id.list_item_time);
			vh.km = (TextView) v.findViewById(R.id.list_item_km);
			vh.rmb = (TextView) v.findViewById(R.id.list_item_rmb);
			vh.context = (TextView) v.findViewById(R.id.list_item_context);
			v.setTag(vh);
		} else {
			vh = (ViewHolder) v.getTag();
		}

		Log.i("Log", "position"+position);
		
		// 当前活动的bean对象
		final GatherBean bean = datas.get(position);
		/**
		 * 当被点击跳转至展示页面
		 */
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GatherApplication.put("show-gather", bean);
				context.startActivity(new Intent(context, GatherActivity.class));
			}
		});
		
		
		final ViewHolder vh2 = vh;

		// 设置时间
		vh2.time.setText(bean.getGatherTime());
		// 设置价格
		vh2.rmb.setText(bean.getGatherRMB());
		// 设置文本标题
		vh2.context.setText(bean.getGatherTitle());
		String kmText="好几百米";
		
		try {
			/**
			 * 我的位置
			 * 参数1纬度
			 * 参数2经度
			 */
			LatLng start =new LatLng(GatherApplication.location.getLatitude(), GatherApplication.location.getLongitude()) ;
			
			/**
			 * 活动地点
			 * 参数1纬度
			 * 参数2经度
			 */
			LatLng end = new LatLng(bean.getPoint().getLatitude(), bean.getPoint().getLongitude());
			//计算p1,p2两点之间的直线距离,单位:米
			long m=(long) DistanceUtil.getDistance(start, end);
			
			if (m>=1000) { 
				int km = (int)((m+500)/1000);
				kmText=km+"km";
			}else{
				kmText=m+"m";
			}
		} catch (Exception e) {
			Log.i("Log", "异常原因:"+e.getMessage());
			kmText="定位失败";
		}
		// 设置距离
		vh2.km.setText(kmText);

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
			//vh2.gatherImg.setImageBitmap(BitmapFactory.decodeFile(gatherImgFile.getAbsolutePath()));
			vh2.gatherImg.setImageBitmap(GatherApplication.getimage(gatherImgFile.getAbsolutePath()));
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
						//vh2.gatherImg.setImageBitmap(BitmapFactory.decodeFile(gatherImgFile.getAbsolutePath()));
						vh2.gatherImg.setImageBitmap(GatherApplication.getimage(gatherImgFile.getAbsolutePath()));
					}
				}
			});
		}
		// 通过当前活动的bean对象的id,获取用户对象
		FindUserUtils.findUserById(bean.getGatherUserId(), new FindUserListener() {

			@Override
			public void toUser(User user) {
				// 设置用户名
				vh2.uname.setText(user.getNickName());
				// 截取bmob中保存图片时的名字
				String imgHead = user.getHeadBit().getFileUrl().substring(
						user.getHeadBit().getFileUrl().length() - 4 - 32, user.getHeadBit().getFileUrl().length() - 4);
				// 设置图片保存的路径(设置头像)
				final File imgHeadFile = new File("sdcard/imgHead" + imgHead + ".png");
				if (imgHeadFile.exists()) {
					// 如果存在直接使用
					vh2.head.setImageBitmap(BitmapFactory.decodeFile(imgHeadFile.getAbsolutePath()));
				} else {
					// user为当前活动的用户
					user.getHeadBit().download(imgHeadFile,new DownloadFileListener() {

						@Override
						public void onProgress(Integer arg0, long arg1) {

						}

						@Override
						public void done(String bitFilePath, BmobException arg1) {
							if (arg1 == null) {
								vh2.head.setImageBitmap(BitmapFactory.decodeFile(imgHeadFile.getAbsolutePath()));
							}
						}
					});
				}
			}
		});

		/**
		 * 判断当前用户是否已经点赞 
		 * 获取到已点赞的用户的活动的id,进行判断是否已经点赞
		 */
		
		
		
		
		// 当前活动点赞的用户的id的集合
		List<String> praiseUsers = bean.getPraiseUsers();
		//Log.i("Log","点击的活动的id"+bean.getObjectId());
		//Log.i("Log","点击的活动点赞的id集合"+bean.getPraiseUsers().size());
		
		//Log.i("Log","当前活动点赞的用户的id"+praiseUsers.get(0));
		
//		for (int i = 0; i < praiseUsers.size(); i++) {
//			Log.i("Log","当前活动点赞的用户的id"+praiseUsers.get(i));
//		}
		//Log.i("Log","当前活动点赞的用户的个数"+bean.getLoveCount());
		// 当前登陆用户的id
		String uId = GatherApplication.u.getObjectId();
		Log.i("Log","当前用户id"+uId);
		// 标记是否未点赞
		boolean flag = true;
		for (int i = 0; i < praiseUsers.size(); i++) {
			Log.i("Log","所有点赞的用户"+praiseUsers.get(i));
			if (praiseUsers.get(i).equals(uId)) {
				Log.i("Log","包含了当前用户id"+uId);
				// 否(是否未点赞)
				flag = false;
				// 点赞,包含
				vh2.loveImg.setImageResource(R.drawable.loveon);
				
			}else{
				Log.i("Log","不包含当前用户id"+uId);
				// 是(是否未点赞)
				flag = true;
				// 未点赞,不包含
				vh2.loveImg.setImageResource(R.drawable.loveoff);
				
			}
		}
		
		
		// 判断集合中是否包含当前用户
//		if (praiseUsers.contains(uId)) {
//			Log.i("Log","包含了当前用户id"+uId);
//			// 否(是否未点赞)
//			flag = false;
//			// 点赞,包含
//			vh2.loveImg.setImageResource(R.drawable.loveon);
//		} else {
//		}

		final loveHolder holder = new loveHolder();
		holder.bean = bean;
		holder.posotion = position;
		holder.uId = uId;
		holder.falg = flag;
		vh2.loveImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 调用点赞的方法
				loveGather(vh2.loveImg, holder);

			}
		});
		return v;
	}

	class loveHolder {
		GatherBean bean;
		String uId;
		int posotion;
		boolean falg;

	}

	class ViewHolder {
		ImageView head, gatherImg, loveImg;
		TextView uname, time, km, rmb, context;

	}

	/**
	 * 设置数据的方法 用来刷新视图
	 */
	public void setNewData(List<GatherBean> datas) {
		this.datas = new ArrayList<>();
		notifyDataSetChanged();
		this.datas = datas;
		notifyDataSetChanged();
	}

	/**
	 * 点赞的方法
	 * 
	 * @param bean
	 *            活动对象
	 * @param uId
	 *            用户id
	 * @param v
	 *            当前活动点赞图标对象
	 * @param posotion
	 *            当前活动数据的下标
	 * @param falg
	 *            true 点赞 false 取消点赞
	 */
	public void loveGather(final ImageView v, final loveHolder holder) {
		Log.i("Log","调用了点赞的方法:传过来的标记为"+ holder.falg);
		if (holder.falg) {
			//没有点赞
			Log.i("Log","进入收藏"+ holder.falg);
			
			/**
			 * 上传数据
			 * 在活动表格中上传用户id
			 * 在用户表格中添加活动id
			 */
			//更改服务器中的活动数据
			final GatherBean bean = new GatherBean();
			//bean.setObjectId(holder.bean.getGatherUserId());
			bean.addUnique("praiseUser", GatherApplication.u.getObjectId());
			//原子性更改服务器中的点赞数量
			bean.increment("loveCount");
			bean.update(holder.bean.getObjectId(), new UpdateListener() {
				
				@Override
				public void done(BmobException arg0) {
					if (arg0==null) {
						//更改本地活动点赞的数据
						holder.bean.getPraiseUsers().add(GatherApplication.u.getObjectId());
						//更改本地点赞数量
						holder.bean.addloveCount();
						v.setImageResource(R.drawable.loveon);
						Toast.makeText(context, "收藏成功", 0).show();
					}else{
						Toast.makeText(context, "异常:"+arg0.getMessage(), 0).show();
						
					}
				}
			});
			//更改服务器中的用户数据
			User ub =new User();
			//ub.setObjectId(GatherApplication.u.getObjectId());
			ub.addUnique("loveGatherIds", holder.bean.getObjectId());
			ub.update(GatherApplication.u.getObjectId(), new UpdateListener() {
				
				@Override
				public void done(BmobException arg0) {
					if (arg0==null) {
						//更改本地的用户数据
						GatherApplication.u.getLoveGatherIds().add(bean.getObjectId());
					}
				}
			});
			
//			v.setImageResource(R.drawable.loveon);
//			Toast.makeText(context, "收藏成功", 0).show();
		} else {
			Log.i("Log","进入取消收藏"+ holder.falg);
			/**
			 * 上传数据
			 * 在活动表格中上传用户id
			 * 在用户表格中添加活动id
			 */
			//更改服务器中的活动数据
			final GatherBean bean = new GatherBean();
			
			ArrayList<String> removeId = new ArrayList<>();
			removeId.add(GatherApplication.u.getObjectId());
			bean.removeAll("praiseUser", removeId);
			//原子性更改服务器中的点赞数量
			bean.increment("loveCount",-1);
			bean.update(holder.bean.getObjectId(), new UpdateListener() {
				
				@Override
				public void done(BmobException arg0) {
					if (arg0==null) {
						//更改本地活动的数据
						holder.bean.getPraiseUsers().remove(GatherApplication.u.getObjectId());
						//更改本地点赞数量
						holder.bean.deleteloveCount();
						v.setImageResource(R.drawable.loveoff);
						Toast.makeText(context, "取消收藏成功", 0).show();
					}
				}
			});
			//更改服务器中的用户数据
			User ub =new User();
			//ub.setObjectId(GatherApplication.u.getObjectId());
			ArrayList<String> removeuId = new ArrayList<>();
			removeuId.add(holder.bean.getObjectId());
			
			
			ub.removeAll("loveGatherIds", removeuId);
			ub.update(GatherApplication.u.getObjectId(), new UpdateListener() {
				
				@Override
				public void done(BmobException arg0) {
					if (arg0==null) {
						//更改本地的用户数据
						GatherApplication.u.getLoveGatherIds().remove(bean.getObjectId());
					}
				}
			});
//			
//			v.setImageResource(R.drawable.loveoff);
//			Toast.makeText(context, "取消收藏成功", 0).show();
		}
		// 点赞之后把标记取反
		holder.falg = !holder.falg;
	}

}
