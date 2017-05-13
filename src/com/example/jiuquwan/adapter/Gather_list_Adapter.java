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
 * home�еĵ�����ͼ��adapter
 * 
 * @author Administrator
 *
 */
public class Gather_list_Adapter extends BaseAdapter {

	// ����Դ
	private List<GatherBean> datas;
	// �����
	private LayoutInflater inflater;
	// ������
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
		
		// ��ǰ���bean����
		final GatherBean bean = datas.get(position);
		/**
		 * ���������ת��չʾҳ��
		 */
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GatherApplication.put("show-gather", bean);
				context.startActivity(new Intent(context, GatherActivity.class));
			}
		});
		
		
		final ViewHolder vh2 = vh;

		// ����ʱ��
		vh2.time.setText(bean.getGatherTime());
		// ���ü۸�
		vh2.rmb.setText(bean.getGatherRMB());
		// �����ı�����
		vh2.context.setText(bean.getGatherTitle());
		String kmText="�ü�����";
		
		try {
			/**
			 * �ҵ�λ��
			 * ����1γ��
			 * ����2����
			 */
			LatLng start =new LatLng(GatherApplication.location.getLatitude(), GatherApplication.location.getLongitude()) ;
			
			/**
			 * ��ص�
			 * ����1γ��
			 * ����2����
			 */
			LatLng end = new LatLng(bean.getPoint().getLatitude(), bean.getPoint().getLongitude());
			//����p1,p2����֮���ֱ�߾���,��λ:��
			long m=(long) DistanceUtil.getDistance(start, end);
			
			if (m>=1000) { 
				int km = (int)((m+500)/1000);
				kmText=km+"km";
			}else{
				kmText=m+"m";
			}
		} catch (Exception e) {
			Log.i("Log", "�쳣ԭ��:"+e.getMessage());
			kmText="��λʧ��";
		}
		// ���þ���
		vh2.km.setText(kmText);

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
			//vh2.gatherImg.setImageBitmap(BitmapFactory.decodeFile(gatherImgFile.getAbsolutePath()));
			vh2.gatherImg.setImageBitmap(GatherApplication.getimage(gatherImgFile.getAbsolutePath()));
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
						//vh2.gatherImg.setImageBitmap(BitmapFactory.decodeFile(gatherImgFile.getAbsolutePath()));
						vh2.gatherImg.setImageBitmap(GatherApplication.getimage(gatherImgFile.getAbsolutePath()));
					}
				}
			});
		}
		// ͨ����ǰ���bean�����id,��ȡ�û�����
		FindUserUtils.findUserById(bean.getGatherUserId(), new FindUserListener() {

			@Override
			public void toUser(User user) {
				// �����û���
				vh2.uname.setText(user.getNickName());
				// ��ȡbmob�б���ͼƬʱ������
				String imgHead = user.getHeadBit().getFileUrl().substring(
						user.getHeadBit().getFileUrl().length() - 4 - 32, user.getHeadBit().getFileUrl().length() - 4);
				// ����ͼƬ�����·��(����ͷ��)
				final File imgHeadFile = new File("sdcard/imgHead" + imgHead + ".png");
				if (imgHeadFile.exists()) {
					// �������ֱ��ʹ��
					vh2.head.setImageBitmap(BitmapFactory.decodeFile(imgHeadFile.getAbsolutePath()));
				} else {
					// userΪ��ǰ����û�
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
		 * �жϵ�ǰ�û��Ƿ��Ѿ����� 
		 * ��ȡ���ѵ��޵��û��Ļ��id,�����ж��Ƿ��Ѿ�����
		 */
		
		
		
		
		// ��ǰ����޵��û���id�ļ���
		List<String> praiseUsers = bean.getPraiseUsers();
		//Log.i("Log","����Ļ��id"+bean.getObjectId());
		//Log.i("Log","����Ļ���޵�id����"+bean.getPraiseUsers().size());
		
		//Log.i("Log","��ǰ����޵��û���id"+praiseUsers.get(0));
		
//		for (int i = 0; i < praiseUsers.size(); i++) {
//			Log.i("Log","��ǰ����޵��û���id"+praiseUsers.get(i));
//		}
		//Log.i("Log","��ǰ����޵��û��ĸ���"+bean.getLoveCount());
		// ��ǰ��½�û���id
		String uId = GatherApplication.u.getObjectId();
		Log.i("Log","��ǰ�û�id"+uId);
		// ����Ƿ�δ����
		boolean flag = true;
		for (int i = 0; i < praiseUsers.size(); i++) {
			Log.i("Log","���е��޵��û�"+praiseUsers.get(i));
			if (praiseUsers.get(i).equals(uId)) {
				Log.i("Log","�����˵�ǰ�û�id"+uId);
				// ��(�Ƿ�δ����)
				flag = false;
				// ����,����
				vh2.loveImg.setImageResource(R.drawable.loveon);
				
			}else{
				Log.i("Log","��������ǰ�û�id"+uId);
				// ��(�Ƿ�δ����)
				flag = true;
				// δ����,������
				vh2.loveImg.setImageResource(R.drawable.loveoff);
				
			}
		}
		
		
		// �жϼ������Ƿ������ǰ�û�
//		if (praiseUsers.contains(uId)) {
//			Log.i("Log","�����˵�ǰ�û�id"+uId);
//			// ��(�Ƿ�δ����)
//			flag = false;
//			// ����,����
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
				// ���õ��޵ķ���
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
	 * �������ݵķ��� ����ˢ����ͼ
	 */
	public void setNewData(List<GatherBean> datas) {
		this.datas = new ArrayList<>();
		notifyDataSetChanged();
		this.datas = datas;
		notifyDataSetChanged();
	}

	/**
	 * ���޵ķ���
	 * 
	 * @param bean
	 *            �����
	 * @param uId
	 *            �û�id
	 * @param v
	 *            ��ǰ�����ͼ�����
	 * @param posotion
	 *            ��ǰ����ݵ��±�
	 * @param falg
	 *            true ���� false ȡ������
	 */
	public void loveGather(final ImageView v, final loveHolder holder) {
		Log.i("Log","�����˵��޵ķ���:�������ı��Ϊ"+ holder.falg);
		if (holder.falg) {
			//û�е���
			Log.i("Log","�����ղ�"+ holder.falg);
			
			/**
			 * �ϴ�����
			 * �ڻ������ϴ��û�id
			 * ���û��������ӻid
			 */
			//���ķ������еĻ����
			final GatherBean bean = new GatherBean();
			//bean.setObjectId(holder.bean.getGatherUserId());
			bean.addUnique("praiseUser", GatherApplication.u.getObjectId());
			//ԭ���Ը��ķ������еĵ�������
			bean.increment("loveCount");
			bean.update(holder.bean.getObjectId(), new UpdateListener() {
				
				@Override
				public void done(BmobException arg0) {
					if (arg0==null) {
						//���ı��ػ���޵�����
						holder.bean.getPraiseUsers().add(GatherApplication.u.getObjectId());
						//���ı��ص�������
						holder.bean.addloveCount();
						v.setImageResource(R.drawable.loveon);
						Toast.makeText(context, "�ղسɹ�", 0).show();
					}else{
						Toast.makeText(context, "�쳣:"+arg0.getMessage(), 0).show();
						
					}
				}
			});
			//���ķ������е��û�����
			User ub =new User();
			//ub.setObjectId(GatherApplication.u.getObjectId());
			ub.addUnique("loveGatherIds", holder.bean.getObjectId());
			ub.update(GatherApplication.u.getObjectId(), new UpdateListener() {
				
				@Override
				public void done(BmobException arg0) {
					if (arg0==null) {
						//���ı��ص��û�����
						GatherApplication.u.getLoveGatherIds().add(bean.getObjectId());
					}
				}
			});
			
//			v.setImageResource(R.drawable.loveon);
//			Toast.makeText(context, "�ղسɹ�", 0).show();
		} else {
			Log.i("Log","����ȡ���ղ�"+ holder.falg);
			/**
			 * �ϴ�����
			 * �ڻ������ϴ��û�id
			 * ���û��������ӻid
			 */
			//���ķ������еĻ����
			final GatherBean bean = new GatherBean();
			
			ArrayList<String> removeId = new ArrayList<>();
			removeId.add(GatherApplication.u.getObjectId());
			bean.removeAll("praiseUser", removeId);
			//ԭ���Ը��ķ������еĵ�������
			bean.increment("loveCount",-1);
			bean.update(holder.bean.getObjectId(), new UpdateListener() {
				
				@Override
				public void done(BmobException arg0) {
					if (arg0==null) {
						//���ı��ػ������
						holder.bean.getPraiseUsers().remove(GatherApplication.u.getObjectId());
						//���ı��ص�������
						holder.bean.deleteloveCount();
						v.setImageResource(R.drawable.loveoff);
						Toast.makeText(context, "ȡ���ղسɹ�", 0).show();
					}
				}
			});
			//���ķ������е��û�����
			User ub =new User();
			//ub.setObjectId(GatherApplication.u.getObjectId());
			ArrayList<String> removeuId = new ArrayList<>();
			removeuId.add(holder.bean.getObjectId());
			
			
			ub.removeAll("loveGatherIds", removeuId);
			ub.update(GatherApplication.u.getObjectId(), new UpdateListener() {
				
				@Override
				public void done(BmobException arg0) {
					if (arg0==null) {
						//���ı��ص��û�����
						GatherApplication.u.getLoveGatherIds().remove(bean.getObjectId());
					}
				}
			});
//			
//			v.setImageResource(R.drawable.loveoff);
//			Toast.makeText(context, "ȡ���ղسɹ�", 0).show();
		}
		// ����֮��ѱ��ȡ��
		holder.falg = !holder.falg;
	}

}
