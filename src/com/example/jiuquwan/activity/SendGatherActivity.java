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
 * 发布信息的Activity
 * 
 * @author Administrator
 *
 */
public class SendGatherActivity extends BaseActivity implements BaseInterface {

	// 时间,地址,金额的lin,活动类型
	private LinearLayout send_time, send_address, send_money,act_type;
	//时间的文本
	private TextView act_time;
	//接收时间年月日
	private String dateTime;
	//接收时间的时分  年月日与时分产生结果后把字符串赋值给time
	private String time;
	//设置年月日dialog
	private DatePickerDialog dateDialog; 
	//设置时间dialog
	private TimePickerDialog timeDialog; 
	//设置Spinner的Adapter的数据源
	private String[] titles={"DIY","讲座","节日","聚餐","美食","少儿","演出","运动","展览","周边"};
	private int[] imgIds={R.drawable.sort_diy,R.drawable.sort_jiangzuo,
			R.drawable.sort_jieri,R.drawable.sort_jucan,
			R.drawable.sort_meishi,R.drawable.sort_shaoer,
			R.drawable.sort_yanchu,R.drawable.sort_yundong,
			R.drawable.sort_zhanlan,R.drawable.sort_zhoubian};
	//选择类型的文本
	private TextView type_tv;
	private String data_class;
	/**
	 * type_img隐藏的图片,选择类型后显示的类型图片
	 * type_img2向右箭头的图片
	 */
	private ImageView type_img,type_img2;
	
	//关于图片上传+++++++++++++++++++++++++++++++++++
	/**
	 * 使用标记,记录当前用户上传照片的数量
	 */
	private List<Bitmap> bits = new ArrayList<>();
	//图片保存的路径
	private File file = new File("sdcard/gather.png");
	
	//两个linear每个人都可以添加3张图片
	private LinearLayout addimgslin1,addimgslin2;
	//点击<加号>的图片
	private ImageView imgadd;
	//图片的宽
	private int width;
	//图片的高
	private int height;
	//++++++++++++++++++++++++++++++++++++++++++++
	//地图检索结果
	private PoiInfo poiInfo;
	//显示地址的TextView
	private TextView act_city;
	//活动的标题
	private EditText act_title;
	private String data_title;
	
	//活动详情
	private EditText act_context;
	private String data_context;
	//活动金额
	private EditText act_rmb;
	private Integer data_rmb;
	/**
	 * 发布
	 */
	public void sendonClick(View v) {
		//获取活动的标题
		//data_title=act_title.getText().toString().trim();
		data_title=getTvText(act_title);
		//toast("活动标题:"+data_title);
		if (data_title.equals("")) {
			toast("活动标题不能为空");
			return;
		}
		//活动类型
		data_class=getTvText(type_tv);
		if (data_class.equals("请选择活动类型")) {
			toast("您尚未选择活动类型");
			return;
		}
		//获取活动的描述
		data_context=getTvText(act_context);
		if (data_context.length()<10) {
			toast("活动描述较少,不于10字");
			return;
		}
		//活动时间
		if (time==null) {
			toast("您尚未选择活动时间");
			return;
		}
		try {
			//获取金额
			data_rmb= new Integer(getTvText(act_rmb));
		} catch (Exception e) {
			e.printStackTrace();
			toast("输入的金额有问题,请检查");
			return;
		}
		
		
		if (poiInfo==null) {
			toast("您尚未选择活动地点");
			return;
		}
		
		
		if (bits == null && bits.size() < 1) {
			toast("系统要求用户必须上产图片");
			return;
		}
		//上传图片之前弹出提示框
		final ProgressDialog progressshow = dialogShow(false, "当前进度:0/"+bits.size(), "发布中:");
		
		
		
		/**
		 * 根据当前图片数量,创建等长的路径数组,用来上传图片
		 */
		final String[] filePaths=new String[bits.size()];
		for (int i = 0; i < bits.size(); i++) {
			//路径
			File dir=new File("sdcard/gather/send");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File bitFile=new File(dir, "bit"+i+".jpeg"); 
			/**
			 * 把图片写到本地
			 * 参数1格式
			 * 参数2图片质量
			 * 参数3 流
			 */
			try {
				bits.get(i).compress(CompressFormat.JPEG, 100,new FileOutputStream(bitFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//获取绝对路径
			filePaths[i]=bitFile.getAbsolutePath();
		}
		
		//上传图片
		BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
			//成功
			@Override
			public void onSuccess(List<BmobFile> arg0, List<String> arg1) {
				/**
				 * 当上传一个成功后,就会放入该集合中
				 * List<BmobFile> arg0
				 */
				if (arg0.size() != filePaths.length) {
					return;
				}
				dialogDismiss();
				//执行发布的逻辑
				GatherBean gather = new GatherBean(); 
				//设置标题
				gather.setGatherTitle(data_title);
				//设置内容
				gather.setGatherIntro(data_context);
				//设置活动时间
				gather.setGatherTime(time);
				//设置活动类型
				gather.setGatherClass(data_class);
				//设置金额
				gather.setGatherRMB(data_rmb+"");
				//设置地点
				gather.setGatherCity(poiInfo.city);
				//设置活动的经纬度
				gather.setPoint(new BmobGeoPoint(poiInfo.location.longitude, poiInfo.location.latitude));
				//设置活动开始地点名称
				gather.setGatherSite(poiInfo.name);
				//设置活动图片
				gather.setGatherJPG(arg0);
				//设置活动的状态
				gather.setFlag(false);
				//发布者id
				gather.setGatherUserId(GatherApplication.u.getObjectId());
				//发布活动
				dialogShow(false, null, null);
				//发送
				gather.save(new SaveListener<String>() {
					
					@Override
					public void done(String arg0, BmobException arg1) {
						if (arg1==null) {
							toast("活动发布成功");
							//把类型清空
//							type_tv.setText("");
//							type_img2.setVisibility(View.GONE);
//							type_img.setVisibility(View.VISIBLE);
							finish();
						}else{
							toast("活动发布失败,请检查您的网络");
						}
						dialogDismiss();
					}
				});
				
			}
			/**
			 * 当前进度
			 * @param arg0 表示当前第几个文件正在上传
			 * @param arg1 表示当前上传的进度条
			 * @param arg2表示总的上传文件数
			 * @param arg3表示总的上传进度
			 */
			@Override
			public void onProgress(int arg0, int arg1, int arg2, int arg3) {
				//改变dailog中的值
				progressshow.setMessage("正在上传图片"+arg0+"/"+arg2);
				
				
			}
			//失败
			@Override
			public void onError(int arg0, String arg1) {
				logi("上传活动图片失败:失败原因"+arg1+",错误码:"+arg0);
				dialogDismiss();
				toast("请检查你的网络");
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
		// 时间lin
		send_time = (LinearLayout) findViewById(R.id.act_send_time);
		// 地址lin
		send_address = (LinearLayout) findViewById(R.id.act_send_time);
		// 金额lin
		send_money = (LinearLayout) findViewById(R.id.act_send_time);
		//时间的文本
		act_time=tvById(R.id.act_time);
		//下拉选择框
		//act_spinner=(Spinner) findViewById(R.id.act_send_spinner);
		//上传图片的linearLayout
		addimgslin1=(LinearLayout) findViewById(R.id.act_addimgs1);
		addimgslin2=(LinearLayout) findViewById(R.id.act_addimgs2);
		//加号的图片
		imgadd = new ImageView(act);
		//图片的宽
		width = getWindowManager().getDefaultDisplay().getWidth()/4;
		//图片的高
		height =width/3*2;
		/**
		 * 设置加号图片的视图
		 * 宽是屏幕宽度的4分之1.高是宽度的3分之2
		 */
		imgadd.setLayoutParams(new LayoutParams(width, height));
		//添加加号的图片
		imgadd.setImageResource(R.drawable.gather_send_img_add);
		imgadd.setPadding(10, 10, 0, 0);
		//在第一个linear中添加一个加号图片的视图
		addimgslin1.addView(imgadd);
		//在initViewOper中添加addimgslin1的点击事件
		
		
		type_img=imgById(R.id.type_img);
		type_img2=imgById(R.id.type_img2);
		act_type=(LinearLayout) findViewById(R.id.act_type);
		
		//活动的类型
		type_tv=tvById(R.id.type_tv);
		//显示地址
		act_city=tvById(R.id.act_city);
		//活动标题
		act_title=etById(R.id.act_title);
		//活动详情
		act_context=etById(R.id.act_context);
		//活动金额
		act_rmb=etById(R.id.act_rmb);
	}

	@Override
	public void initData() {
		
		//+++++++++++++++++++++++++++++++
		/**
		 * 设置时间的方法
		 */
		//获取时间信息(本地语言环境)
		final Calendar c= Calendar.getInstance();
		
		dateDialog= new DatePickerDialog(act, new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				//判断当前时间是否大于用户选择时间
				if (c.get(Calendar.YEAR)>year) {
					toast("您选择的时间不符");
					return;
				}
				//用户选择的时间今年的时间
				if (c.get(Calendar.YEAR)==year) {
					//判断当前月份是否大于用户选择的的月份
					if (c.get(Calendar.MONTH)>monthOfYear) {
						toast("您选择的时间不符");
						return;
						//用户选择的月份是当月
					}else if(c.get(Calendar.MONTH)==monthOfYear){
						//***走到这代表年份一致,月份一致***
						//当前日期大于或等于用户选择的时间
						if (c.get(Calendar.DAY_OF_MONTH)>=dayOfMonth) {
							toast("您选择的时间不符");
							return;
						}
					}
				}
				
				
				
				
				dateTime=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
				if (timeDialog!=null) {
					timeDialog.show();
				}
			}
			//年,月,日
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		timeDialog = new TimePickerDialog(act, new OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				
				time=" "+hourOfDay+":"+minute;
				act_time.setText(dateTime+time);
				time=dateTime+time;
				
			}
			//时,分
		}, c.get(Calendar.HOUR), c.get(Calendar.HOUR_OF_DAY), true);
		
		//+++++++++++++++++++++++++++++++++
		//spinner例子
		//act_spinner.setAdapter(new Act_SpinnerAdapter(imgIds, titles, act));
//		act_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
//				toast("点击了"+position);
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
		
		//添加图片的点击事件(点击的是加号的图片)
		imgadd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//如果上次的图片存在
				if (file.exists()) {
					//删除
					file.delete();
				}
				/**
				 * 更改头像 跳转到系统相册,获取内容
				 */
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				// 设置内容类型
				intent.setType("image/*");
				// 剪裁
				intent.putExtra("crop", "circleCrop");
				// 剪裁比例
				intent.putExtra("aspectX", 3);
				intent.putExtra("aspectY", 2);
				intent.putExtra("outputX", 90);
				intent.putExtra("outputY", 60);
				// 设置头像保存的路径
				intent.putExtra("output", Uri.fromFile(file));
				startActivityForResult(intent, 0);
			}
		});
		
	}

	/**
	 * 点击事件
	 */
	public void act_send_onClick(View v) {

		switch (v.getId()) {
		// 点击时间的lin
		case R.id.act_send_time:
			//弹出设置日期的dialog
			dateDialog.show();
			break;
		// 点击地址的lin
		case R.id.act_send_address:
			startAct(GatherMapActivity.class);
			break;
			// 点击类型
		case R.id.act_type:
			startAct(Gather_Activity_type.class);
			break;
		}
	}
//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//		toast("返回来了");
//		if (GatherApplication.titles!=null) {
//			//toast("得到了结果"+GatherApplication.titles);
//			//toast("得到了结果"+GatherApplication.intIds);
//			type_img2.setImageResource(GatherApplication.intIds);
//			type_img2.setVisibility(View.VISIBLE);
//			type_img.setVisibility(View.GONE);
//			type_tv.setText(GatherApplication.titles);
//		}
//	}
	//当界面从新获取焦点时
	@Override
	protected void onRestart() {
		super.onRestart();
		toast("从地址页面返回来了");
		poiInfo= (PoiInfo) GatherApplication.get("data-poi", true);
		if (poiInfo!=null) {
			act_city.setText(poiInfo.city+" "+poiInfo.name);
		}
		
		//给类型textView赋值的
		if (GatherApplication.titles!=null) {
			//toast("得到了结果"+GatherApplication.titles);
			//toast("得到了结果"+GatherApplication.intIds);
			type_img2.setImageResource(GatherApplication.intIds);
			type_img2.setVisibility(View.VISIBLE);
			type_img.setVisibility(View.GONE);
			type_tv.setText(GatherApplication.titles);
		}
	}
	
	
	
	/**
	 * 当用户选择退出,提示用户是否放弃编辑
	 */
	public void img_back(View v) {
		new AlertDialog.Builder(act).setTitle("提示信息").setMessage("是否放弃编辑").setPositiveButton("确定",new  DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}).setNegativeButton("取消", null).show();
		
		
		
	}
	
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		updateUserHead();
		
		
	}

	//设置图片
	private void updateUserHead() {
		//当有结果返回时,如果是空,直接返回
		if (!file.exists()) {
			return;
		}
		
		
		//步骤1 将用户选择的图片添加到集合中 
		/**
		 * 步骤2 判断集合中的数据是否已经超过或等于了3个
		 * 		如果小与3个清空当前LinearLayout重新进行添加所有的ImageView
		 */
		//获取一张图片
		Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());
		//把图片添加到集合中
		bits.add(bit);
		if (bits.size()<3) {
			//清除所有视图
			addimgslin1.removeAllViews();
			//重新从集合中添加所有视图
			for (int i = 0; i < bits.size(); i++) {
				//获取每一个视图
				Bitmap bitmap = bits.get(i);
				//设置每一个视图的宽高
				ImageView image=new ImageView(act);
				image.setLayoutParams(new LayoutParams(width, height));
				image.setPadding(10, 10, 0, 0);
				//image.setPadding(left, top, right, bottom);
				//把获取到的每一个视图添加进去
				image.setImageBitmap(bitmap);
				//然后添加到LinearLayout中
				addimgslin1.addView(image);
			}
			//最后添加一个加号视图
			addimgslin1.addView(imgadd);
		}else{
			if (bits.size()==3) {
				//清除最后一个加号的图片
				addimgslin1.removeView(imgadd);
				ImageView image1=new ImageView(act);
				image1.setLayoutParams(new LayoutParams(width, height));
				image1.setPadding(10, 10, 0, 0);
				//获取到最后一个图片,设置到清除的位置(最后加号的位置)
				image1.setImageBitmap(bits.get(2));
				//把最后一张图片添加到linear1中
				addimgslin1.addView(image1);
				//把加号的视图添加到linear2中
				addimgslin2.addView(imgadd);
				
			}else{
				if (bits.size()==6) {
					//清除lin2中的加号的视图
					addimgslin2.removeView(imgadd);
					
					ImageView image2=new ImageView(act);
					image2.setLayoutParams(new LayoutParams(width, height));
					image2.setPadding(10, 10, 0, 0);
					//获取到最后一个图片,设置到清除的位置(最后加号的位置)
					image2.setImageBitmap(bits.get(5));
					//把最后的视图添加到linear2中
					addimgslin2.addView(image2);
					
					
				}else{
					//清除lin2中的所有视图
					addimgslin2.removeAllViews();
					//从3开始  只有3.4.5
					for (int i = 3; i < bits.size() ; i++) {
						Bitmap bitmap1 = bits.get(i);
						ImageView image3=new ImageView(act);
						image3.setLayoutParams(new LayoutParams(width, height));
						image3.setPadding(10, 10, 0, 0);
						image3.setImageBitmap(bitmap1);
						addimgslin2.addView(image3);
					}
					//最后一个添加加号视图
					addimgslin2.addView(imgadd);
					
				}
			}
		}
	}
	
}
