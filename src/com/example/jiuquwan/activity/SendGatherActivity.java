package com.example.jiuquwan.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.baidu.mapapi.search.core.PoiInfo;
import com.example.jiuquwan.R;
import com.example.jiuquwan.adapter.Act_SpinnerAdapter;
import com.example.jiuquwan.adapter.Activity_type_Adapter;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuquwan.bean.GatherBean;
import com.example.jiuwuwan.base.BaseActivity;
import com.example.jiuwuwan.base.BaseInterface;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.bluetooth.le.ScanCallback;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.DialerFilter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * ������Ϣ��Activity
 * 
 * @author Administrator
 *
 */
public class SendGatherActivity extends BaseActivity implements BaseInterface {

	// ʱ��,��ַ,����lin,�����
	private LinearLayout send_time, send_address, send_money,act_type;
	//ʱ����ı�
	private TextView act_time;
	//����ʱ��������
	private String dateTime;
	//����ʱ���ʱ��  ��������ʱ�ֲ����������ַ�����ֵ��time
	private String time;
	//����������dialog
	private DatePickerDialog dateDialog; 
	//����ʱ��dialog
	private TimePickerDialog timeDialog; 
	//����Spinner��Adapter������Դ
	private String[] titles={"DIY","����","����","�۲�","��ʳ","�ٶ�","�ݳ�","�˶�","չ��","�ܱ�"};
	private int[] imgIds={R.drawable.sort_diy,R.drawable.sort_jiangzuo,
			R.drawable.sort_jieri,R.drawable.sort_jucan,
			R.drawable.sort_meishi,R.drawable.sort_shaoer,
			R.drawable.sort_yanchu,R.drawable.sort_yundong,
			R.drawable.sort_zhanlan,R.drawable.sort_zhoubian};
	//ѡ�����͵��ı�
	private TextView type_tv;
	private String data_class;
	/**
	 * type_img���ص�ͼƬ,ѡ�����ͺ���ʾ������ͼƬ
	 * type_img2���Ҽ�ͷ��ͼƬ
	 */
	private ImageView type_img,type_img2;
	
	//����ͼƬ�ϴ�+++++++++++++++++++++++++++++++++++
	/**
	 * ʹ�ñ��,��¼��ǰ�û��ϴ���Ƭ������
	 */
	private List<Bitmap> bits = new ArrayList<>();
	//ͼƬ�����·��
	private File file = new File("sdcard/gather.png");
	
	//����linearÿ���˶��������3��ͼƬ
	private LinearLayout addimgslin1,addimgslin2;
	//���<�Ӻ�>��ͼƬ
	private ImageView imgadd;
	//ͼƬ�Ŀ�
	private int width;
	//ͼƬ�ĸ�
	private int height;
	//++++++++++++++++++++++++++++++++++++++++++++
	//��ͼ�������
	private PoiInfo poiInfo;
	//��ʾ��ַ��TextView
	private TextView act_city;
	//��ı���
	private EditText act_title;
	private String data_title;
	
	//�����
	private EditText act_context;
	private String data_context;
	//����
	private EditText act_rmb;
	private Integer data_rmb;
	/**
	 * ����
	 */
	public void sendonClick(View v) {
		//��ȡ��ı���
		//data_title=act_title.getText().toString().trim();
		data_title=getTvText(act_title);
		//toast("�����:"+data_title);
		if (data_title.equals("")) {
			toast("����ⲻ��Ϊ��");
			return;
		}
		//�����
		data_class=getTvText(type_tv);
		if (data_class.equals("��ѡ������")) {
			toast("����δѡ������");
			return;
		}
		//��ȡ�������
		data_context=getTvText(act_context);
		if (data_context.length()<10) {
			toast("���������,����10��");
			return;
		}
		//�ʱ��
		if (time==null) {
			toast("����δѡ��ʱ��");
			return;
		}
		try {
			//��ȡ���
			data_rmb= new Integer(getTvText(act_rmb));
		} catch (Exception e) {
			e.printStackTrace();
			toast("����Ľ��������,����");
			return;
		}
		
		
		if (poiInfo==null) {
			toast("����δѡ���ص�");
			return;
		}
		
		
		if (bits == null && bits.size() < 1) {
			toast("ϵͳҪ���û������ϲ�ͼƬ");
			return;
		}
		//�ϴ�ͼƬ֮ǰ������ʾ��
		final ProgressDialog progressshow = dialogShow(false, "��ǰ����:0/"+bits.size(), "������:");
		
		
		
		/**
		 * ���ݵ�ǰͼƬ����,�����ȳ���·������,�����ϴ�ͼƬ
		 */
		final String[] filePaths=new String[bits.size()];
		for (int i = 0; i < bits.size(); i++) {
			//·��
			File dir=new File("sdcard/gather/send");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File bitFile=new File(dir, "bit"+i+".jpeg"); 
			/**
			 * ��ͼƬд������
			 * ����1��ʽ
			 * ����2ͼƬ����
			 * ����3 ��
			 */
			try {
				bits.get(i).compress(CompressFormat.JPEG, 100,new FileOutputStream(bitFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//��ȡ����·��
			filePaths[i]=bitFile.getAbsolutePath();
		}
		
		//�ϴ�ͼƬ
		BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
			//�ɹ�
			@Override
			public void onSuccess(List<BmobFile> arg0, List<String> arg1) {
				/**
				 * ���ϴ�һ���ɹ���,�ͻ����ü�����
				 * List<BmobFile> arg0
				 */
				if (arg0.size() != filePaths.length) {
					return;
				}
				dialogDismiss();
				//ִ�з������߼�
				GatherBean gather = new GatherBean(); 
				//���ñ���
				gather.setGatherTitle(data_title);
				//��������
				gather.setGatherIntro(data_context);
				//���ûʱ��
				gather.setGatherTime(time);
				//���û����
				gather.setGatherClass(data_class);
				//���ý��
				gather.setGatherRMB(data_rmb+"");
				//���õص�
				gather.setGatherCity(poiInfo.city);
				//���û�ľ�γ��
				gather.setPoint(new BmobGeoPoint(poiInfo.location.longitude, poiInfo.location.latitude));
				//���û��ʼ�ص�����
				gather.setGatherSite(poiInfo.name);
				//���ûͼƬ
				gather.setGatherJPG(arg0);
				//���û��״̬
				gather.setFlag(false);
				//������id
				gather.setGatherUserId(GatherApplication.u.getObjectId());
				//�����
				dialogShow(false, null, null);
				//����
				gather.save(new SaveListener<String>() {
					
					@Override
					public void done(String arg0, BmobException arg1) {
						if (arg1==null) {
							toast("������ɹ�");
							//���������
//							type_tv.setText("");
//							type_img2.setVisibility(View.GONE);
//							type_img.setVisibility(View.VISIBLE);
							finish();
						}else{
							toast("�����ʧ��,������������");
						}
						dialogDismiss();
					}
				});
				
			}
			/**
			 * ��ǰ����
			 * @param arg0 ��ʾ��ǰ�ڼ����ļ������ϴ�
			 * @param arg1 ��ʾ��ǰ�ϴ��Ľ�����
			 * @param arg2��ʾ�ܵ��ϴ��ļ���
			 * @param arg3��ʾ�ܵ��ϴ�����
			 */
			@Override
			public void onProgress(int arg0, int arg1, int arg2, int arg3) {
				//�ı�dailog�е�ֵ
				progressshow.setMessage("�����ϴ�ͼƬ"+arg0+"/"+arg2);
				
				
			}
			//ʧ��
			@Override
			public void onError(int arg0, String arg1) {
				logi("�ϴ��ͼƬʧ��:ʧ��ԭ��"+arg1+",������:"+arg0);
				dialogDismiss();
				toast("�����������");
			}
		});
		
	}
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_send);
		initView();
		initData();
		initViewOper();

	}

	@Override
	public void initView() {
		// ʱ��lin
		send_time = (LinearLayout) findViewById(R.id.act_send_time);
		// ��ַlin
		send_address = (LinearLayout) findViewById(R.id.act_send_time);
		// ���lin
		send_money = (LinearLayout) findViewById(R.id.act_send_time);
		//ʱ����ı�
		act_time=tvById(R.id.act_time);
		//����ѡ���
		//act_spinner=(Spinner) findViewById(R.id.act_send_spinner);
		//�ϴ�ͼƬ��linearLayout
		addimgslin1=(LinearLayout) findViewById(R.id.act_addimgs1);
		addimgslin2=(LinearLayout) findViewById(R.id.act_addimgs2);
		//�Ӻŵ�ͼƬ
		imgadd = new ImageView(act);
		//ͼƬ�Ŀ�
		width = getWindowManager().getDefaultDisplay().getWidth()/4;
		//ͼƬ�ĸ�
		height =width/3*2;
		/**
		 * ���üӺ�ͼƬ����ͼ
		 * ������Ļ��ȵ�4��֮1.���ǿ�ȵ�3��֮2
		 */
		imgadd.setLayoutParams(new LayoutParams(width, height));
		//��ӼӺŵ�ͼƬ
		imgadd.setImageResource(R.drawable.gather_send_img_add);
		imgadd.setPadding(10, 10, 0, 0);
		//�ڵ�һ��linear�����һ���Ӻ�ͼƬ����ͼ
		addimgslin1.addView(imgadd);
		//��initViewOper�����addimgslin1�ĵ���¼�
		
		
		type_img=imgById(R.id.type_img);
		type_img2=imgById(R.id.type_img2);
		act_type=(LinearLayout) findViewById(R.id.act_type);
		
		//�������
		type_tv=tvById(R.id.type_tv);
		//��ʾ��ַ
		act_city=tvById(R.id.act_city);
		//�����
		act_title=etById(R.id.act_title);
		//�����
		act_context=etById(R.id.act_context);
		//����
		act_rmb=etById(R.id.act_rmb);
	}

	@Override
	public void initData() {
		
		//+++++++++++++++++++++++++++++++
		/**
		 * ����ʱ��ķ���
		 */
		//��ȡʱ����Ϣ(�������Ի���)
		final Calendar c= Calendar.getInstance();
		
		dateDialog= new DatePickerDialog(act, new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				//�жϵ�ǰʱ���Ƿ�����û�ѡ��ʱ��
				if (c.get(Calendar.YEAR)>year) {
					toast("��ѡ���ʱ�䲻��");
					return;
				}
				//�û�ѡ���ʱ������ʱ��
				if (c.get(Calendar.YEAR)==year) {
					//�жϵ�ǰ�·��Ƿ�����û�ѡ��ĵ��·�
					if (c.get(Calendar.MONTH)>monthOfYear) {
						toast("��ѡ���ʱ�䲻��");
						return;
						//�û�ѡ����·��ǵ���
					}else if(c.get(Calendar.MONTH)==monthOfYear){
						//***�ߵ���������һ��,�·�һ��***
						//��ǰ���ڴ��ڻ�����û�ѡ���ʱ��
						if (c.get(Calendar.DAY_OF_MONTH)>=dayOfMonth) {
							toast("��ѡ���ʱ�䲻��");
							return;
						}
					}
				}
				
				
				
				
				dateTime=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
				if (timeDialog!=null) {
					timeDialog.show();
				}
			}
			//��,��,��
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		timeDialog = new TimePickerDialog(act, new OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				
				time=" "+hourOfDay+":"+minute;
				act_time.setText(dateTime+time);
				time=dateTime+time;
				
			}
			//ʱ,��
		}, c.get(Calendar.HOUR), c.get(Calendar.HOUR_OF_DAY), true);
		
		//+++++++++++++++++++++++++++++++++
		//spinner����
		//act_spinner.setAdapter(new Act_SpinnerAdapter(imgIds, titles, act));
//		act_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
//				toast("�����"+position);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				
//			}
//		});
		
	}

	@Override
	public void initViewOper() {
		
		//���ͼƬ�ĵ���¼�(������ǼӺŵ�ͼƬ)
		imgadd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//����ϴε�ͼƬ����
				if (file.exists()) {
					//ɾ��
					file.delete();
				}
				/**
				 * ����ͷ�� ��ת��ϵͳ���,��ȡ����
				 */
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				// ������������
				intent.setType("image/*");
				// ����
				intent.putExtra("crop", "circleCrop");
				// ���ñ���
				intent.putExtra("aspectX", 3);
				intent.putExtra("aspectY", 2);
				intent.putExtra("outputX", 90);
				intent.putExtra("outputY", 60);
				// ����ͷ�񱣴��·��
				intent.putExtra("output", Uri.fromFile(file));
				startActivityForResult(intent, 0);
			}
		});
		
	}

	/**
	 * ����¼�
	 */
	public void act_send_onClick(View v) {

		switch (v.getId()) {
		// ���ʱ���lin
		case R.id.act_send_time:
			//�����������ڵ�dialog
			dateDialog.show();
			break;
		// �����ַ��lin
		case R.id.act_send_address:
			startAct(GatherMapActivity.class);
			break;
			// �������
		case R.id.act_type:
			startAct(Gather_Activity_type.class);
			break;
		}
	}
//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//		toast("��������");
//		if (GatherApplication.titles!=null) {
//			//toast("�õ��˽��"+GatherApplication.titles);
//			//toast("�õ��˽��"+GatherApplication.intIds);
//			type_img2.setImageResource(GatherApplication.intIds);
//			type_img2.setVisibility(View.VISIBLE);
//			type_img.setVisibility(View.GONE);
//			type_tv.setText(GatherApplication.titles);
//		}
//	}
	//��������»�ȡ����ʱ
	@Override
	protected void onRestart() {
		super.onRestart();
		toast("�ӵ�ַҳ�淵������");
		poiInfo= (PoiInfo) GatherApplication.get("data-poi", true);
		if (poiInfo!=null) {
			act_city.setText(poiInfo.city+" "+poiInfo.name);
		}
		
		//������textView��ֵ��
		if (GatherApplication.titles!=null) {
			//toast("�õ��˽��"+GatherApplication.titles);
			//toast("�õ��˽��"+GatherApplication.intIds);
			type_img2.setImageResource(GatherApplication.intIds);
			type_img2.setVisibility(View.VISIBLE);
			type_img.setVisibility(View.GONE);
			type_tv.setText(GatherApplication.titles);
		}
	}
	
	
	
	/**
	 * ���û�ѡ���˳�,��ʾ�û��Ƿ�����༭
	 */
	public void img_back(View v) {
		new AlertDialog.Builder(act).setTitle("��ʾ��Ϣ").setMessage("�Ƿ�����༭").setPositiveButton("ȷ��",new  DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}).setNegativeButton("ȡ��", null).show();
		
		
		
	}
	
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		updateUserHead();
		
		
	}

	//����ͼƬ
	private void updateUserHead() {
		//���н������ʱ,����ǿ�,ֱ�ӷ���
		if (!file.exists()) {
			return;
		}
		
		
		//����1 ���û�ѡ���ͼƬ��ӵ������� 
		/**
		 * ����2 �жϼ����е������Ƿ��Ѿ������������3��
		 * 		���С��3����յ�ǰLinearLayout���½���������е�ImageView
		 */
		//��ȡһ��ͼƬ
		Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());
		//��ͼƬ��ӵ�������
		bits.add(bit);
		if (bits.size()<3) {
			//���������ͼ
			addimgslin1.removeAllViews();
			//���´Ӽ��������������ͼ
			for (int i = 0; i < bits.size(); i++) {
				//��ȡÿһ����ͼ
				Bitmap bitmap = bits.get(i);
				//����ÿһ����ͼ�Ŀ��
				ImageView image=new ImageView(act);
				image.setLayoutParams(new LayoutParams(width, height));
				image.setPadding(10, 10, 0, 0);
				//image.setPadding(left, top, right, bottom);
				//�ѻ�ȡ����ÿһ����ͼ��ӽ�ȥ
				image.setImageBitmap(bitmap);
				//Ȼ����ӵ�LinearLayout��
				addimgslin1.addView(image);
			}
			//������һ���Ӻ���ͼ
			addimgslin1.addView(imgadd);
		}else{
			if (bits.size()==3) {
				//������һ���Ӻŵ�ͼƬ
				addimgslin1.removeView(imgadd);
				ImageView image1=new ImageView(act);
				image1.setLayoutParams(new LayoutParams(width, height));
				image1.setPadding(10, 10, 0, 0);
				//��ȡ�����һ��ͼƬ,���õ������λ��(���Ӻŵ�λ��)
				image1.setImageBitmap(bits.get(2));
				//�����һ��ͼƬ��ӵ�linear1��
				addimgslin1.addView(image1);
				//�ѼӺŵ���ͼ��ӵ�linear2��
				addimgslin2.addView(imgadd);
				
			}else{
				if (bits.size()==6) {
					//���lin2�еļӺŵ���ͼ
					addimgslin2.removeView(imgadd);
					
					ImageView image2=new ImageView(act);
					image2.setLayoutParams(new LayoutParams(width, height));
					image2.setPadding(10, 10, 0, 0);
					//��ȡ�����һ��ͼƬ,���õ������λ��(���Ӻŵ�λ��)
					image2.setImageBitmap(bits.get(5));
					//��������ͼ��ӵ�linear2��
					addimgslin2.addView(image2);
					
					
				}else{
					//���lin2�е�������ͼ
					addimgslin2.removeAllViews();
					//��3��ʼ  ֻ��3.4.5
					for (int i = 3; i < bits.size() ; i++) {
						Bitmap bitmap1 = bits.get(i);
						ImageView image3=new ImageView(act);
						image3.setLayoutParams(new LayoutParams(width, height));
						image3.setPadding(10, 10, 0, 0);
						image3.setImageBitmap(bitmap1);
						addimgslin2.addView(image3);
					}
					//���һ����ӼӺ���ͼ
					addimgslin2.addView(imgadd);
					
				}
			}
		}
	}
	
}
