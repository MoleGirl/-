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
 * �������ĵ�Fragment
 * 
 * @author Administrator
 *
 */
public class MyCenterFrament extends BaseV4Fragment implements BaseInterface {

	private GridView grid;
	// ����Դ
	private List<String> datas;
	/**
	 * �û�ͷ��
	 */
	private ImageView u_head;
	/**
	 * �û��� �û��ǳ�
	 */
	private TextView u_name, u_nickname;
	// ImgLoader ����
	private ImageLoader loader;
	private DisplayImageOptions opt;
	//���ü��ú��ͷ�񱣴��·��
	private static final File file = new File("sdcard/head.png");

	private String[] titles={"�Ż�","�ղ�","����","����","�","����"};
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
		// ����loader
		loader = ImageLoaderutils.getInstance(act);
		/*
		 *  ��ȡ���ö���
		 *  ����1��ȡĬ��ͼƬ
		 *  ����2ͼƬ����ʧ��ʱ��ͼƬ
		 */
		opt = ImageLoaderutils.getOpt(R.drawable.logo1, R.drawable.logo1_error);
		initView();
		initData();
		initViewOper();

	}

	@Override
	public void initView() {
		grid = (GridView) findViewById(R.id.fragment_mycenter_grid);
		// �û�ͷ��
		u_head = imgById(R.id.fragment_mycenter_head);
		// �û���
		u_name = tvById(R.id.fragment_mycenter_uname);
		// �û��ǳ�
		u_nickname = tvById(R.id.fragment_mycenter_nickname);

	}

	@Override
	public void initData() {
		datas = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			datas.add(new String());
		}
		// ��ѯ�û�����
		FindUserUtils.findUserById(GatherApplication.u.getObjectId(), new FindUserListener() {

			@Override
			public void toUser(User user) {
				// ���ù۲���,��ǰ��½���û�����
				GatherApplication.u = user;
				// ����ui��ͼ
				initUserView();
			}
		});
	}

	/**
	 * ����ui��ͼ
	 */
	protected void initUserView() {
		// ��Ӧ�û����л�ȡ�û�ͷ��ĵ�ַ
		BmobFile headFile = GatherApplication.u.getHeadBit();
		//���û����������û�ͷ���ַ,,֤���Ѿ����ù�ͷ��
		if (headFile != null) {
			//��ȡurl
			String url = headFile.getFileUrl();
			Log.i("Log", "ͷ��ĵ�ַ��:" + url);
			//����"/"���в��
			String[] split = url.split("/");
			// ָ��ͷ���ļ���·��
			File dir = new File("sdcard/gather/img");
			/**
			 * ����ͼƬ��ָ����·��
			 * ��Ե�ַ+�ļ���
			 */
			final File iconFile = new File(dir.getAbsoluteFile() + "/" + split[split.length - 1]);
			// ���·��������
//			if (!dir.exists()) {
//				
//			}

			// �������
			if (dir.exists()) {
				// ֱ������ͷ��
				u_head.setImageURI(Uri.fromFile(iconFile));
			} else {
				// ����
				dir.mkdirs();
				/**
				 * 
				 * �ѵõ���ͷ�񻺴浽����
				 */
				headFile.download(iconFile, new DownloadFileListener() {

					@Override
					public void onProgress(Integer arg0, long arg1) {

					}

					@Override
					public void done(String arg0, BmobException e) {
						// ����������쳣
						if (e == null) {
							u_head.setImageURI(Uri.fromFile(iconFile));

						}
					}
				});

			}

			/**
			 * ����ͷ�� ����1ͷ�����ַ ����2ImageView���� ����3���ö���
			 */
			//loader.displayImage(url, u_head, opt);
		} else {
			//����ȡ���û�������û��,�û�ͷ���ַ,֤���ǵ�һ��½��û������ͷ��(Ĭ����ʾ��ͷ��)
			u_head.setImageResource(R.drawable.logo1);
		}
		// �����ǳ�
		u_nickname.setText(GatherApplication.u.getNickName());
		// �����û���
		u_name.setText("��ȥ��:" + GatherApplication.u.getUsername());

	}

	private List<GatherBean> beans;
	@Override
	public void initViewOper() {
		// ��������Դ
		grid.setAdapter(new MyCenter_GridAdapter(imgIds, titles, act));
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position==1) {
					List<String> ids=GatherApplication.u.getLoveGatherIds();
					Log.i("log","����ܵĻ"+ids.get(0));
					for (int i = 0; i < ids.size(); i++) {
						logi("����ܵĻ"+ids.get(i));
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
						            Log.i("bmob","ʧ�ܣ�"+e.getMessage()+","+e.getErrorCode());
						        }
						    }

						});
					}
					//�ر�dialog
					dialogDismiss();
					GatherApplication.put("data-gather", beans);
					GatherApplication.put("data-title", "�ҵ��ղ�");
					startAct(More_ShowGatherActivity.class);
				}else if(position==5){
					//��ת�����ý���
					startAct(ShezhiActivity.class);
					
				}
			}
		});
		
		// ���û����ͷ��
		u_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/**
				 * ����ͷ�� ��ת��ϵͳ���,��ȡ����
				 */
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				// ������������
				intent.setType("image/*");
				// ����
				intent.putExtra("crop", "circleCrop");
				// ���ñ���
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 50);
				intent.putExtra("outputY", 50);
				// ����ͷ�񱣴��·��
				intent.putExtra("output", Uri.fromFile(file));
				// ������ֵ����ת
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
		// ����ͷ��ʱ,����dialog
		dialogShow(true, null, null);
		// toast("ͷ���������");
		final User ub = new User();
		// ����User��
		ub.setHeadBit(new BmobFile(file));

		/**
		 * �ϴ�ͼƬ
		 */
		ub.getHeadBit().upload(new UploadFileListener() {

			@Override
			public void done(BmobException e) {
				// ������쳣
				if (e != null) {
					toast("��һ��ͷ������ʧ��");
					dialogDismiss();
					return;
				}
				/**
				 * �ϴ���bmob����
				 */
				ub.update(GatherApplication.u.getObjectId(), new UpdateListener() {

					@Override
					public void done(BmobException ex) {
						// ������쳣
						if (ex != null) {
							toast("�ڶ���ͷ������ʧ��");
							dialogDismiss();
							return;
						}
						dialogDismiss();
						toast("ͷ���ϴ��ɹ�");
						// ���ڴ濨·���л�ȡͼƬ
						Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());
						// ����
						u_head.setImageBitmap(bit);
					}
				});

			}
		});
	}

}
