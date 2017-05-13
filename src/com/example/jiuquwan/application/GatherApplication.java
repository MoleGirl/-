package com.example.jiuquwan.application;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.example.jiuquwan.bean.GatherBean;
import com.example.jiuquwan.bean.User;
import com.example.jiuquwan.handler.DemoMessageHandler;
import com.example.jiuquwan.utils.FindGatherUtils;
import com.example.jiuquwan.utils.FindGatherUtils.FindGatherListener;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.widget.Toast;
import c.b.BP;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.push.BmobPush;
import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class GatherApplication extends Application {
	/**
	 * 代表当前用户对象
	 */
	public static User u;
	/**
	 * 活动对象的集合
	 */
	public static List<GatherBean> gathers;
	// 上下文
	private static Context context;
	public static BDLocation location;
	private LocationClient mLocationClient;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 初始化上下文
		context = getApplicationContext();
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());

		// 初始化数据服务
		Bmob.initialize(this, "162ca550f0ed247a2f86e030ae67f671");
		// 初始化短信服务
		BmobSMS.initialize(this, "162ca550f0ed247a2f86e030ae67f671");
		// 初始化支付服务
		BP.init(this, "162ca550f0ed247a2f86e030ae67f671");
		gathers = new ArrayList<>();
		initBaidu();

		// 消息推送
		// 初始化BmobSDK
		Bmob.initialize(this, "162ca550f0ed247a2f86e030ae67f671");
		// 使用推送服务时的初始化操作
		BmobInstallation.getCurrentInstallation().save();
		// 启动推送服务
		BmobPush.startWork(this);

	}

	/**
	 * 获取当前运行的进程名
	 * 
	 * @return
	 */
	public static String getMyProcessName() {
		try {
			File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
			BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
			String processName = mBufferedReader.readLine().trim();
			mBufferedReader.close();
			return processName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void initBaidu() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				GatherApplication.location = location;
			}
		}); // 注册监听函数

		initLocation();
		mLocationClient.start();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	/**
	 * 标记当前加载数据的状态 -1 表示正在加载中 0 表示加载失败 1表示加载成功
	 */
	public static int findGatherFlag;

	/**
	 * 初始化所有数据的方法,**可以写在initActivity中** 返回一个int值, -1 表示正在加载中(默认) 0 表示加载失败
	 * 1表示加载成功
	 */
	public static void initData() {
		findGatherFlag = -1;
		FindGatherUtils.findGather(1, null, 0, 0, new FindGatherListener() {

			@Override
			public void findData(List<GatherBean> beans, BmobException arg1) {
				if (arg1 == null) {
					// 加载数据成功
					gathers = beans;
					findGatherFlag = 1;
					Toast.makeText(context, "加载数据成功,条数:" + beans.size(), 0).show();
				} else {
					// 加载数据失败
					// Toast.makeText(context, "加载数据失败,请检查您的网络", 0).show();
					findGatherFlag = 0;
				}

			}
		});
		Log.i("Log", "标记:" + findGatherFlag);
	}

	public static int intIds;
	public static String titles;

	private static Map<String, Object> datas = new LinkedHashMap<>();

	/**
	 * 向全局数据容器中存放数据,用来多个Activity/Fragment之间传递数据
	 * 
	 * @param key
	 *            填充的数据key
	 * @param value
	 *            填充的数据
	 * @return 如果此key已经与其他的值产生了映射关系,那么本次操作会把旧的数据覆盖,并且会返回旧的数据,否则返回null
	 */
	public static Object put(String key, Object value) {
		return datas.put(key, value);
	}

	/**
	 * 从全局的容器中获取数据
	 * 
	 * @param key
	 *            获取数据的秘钥
	 * @param isDlete
	 *            是否删除本次数据
	 * @return 本次获取的数据
	 */
	public static Object get(String key, Boolean isDlete) {
		if (isDlete) {
			return datas.remove(key);
		}
		return datas.get(key);
	}

	// 当前用户支付的(已经参与的)活动的个数
	public static int payGatherIds;
	// 该活动已经参与的用户的个数
	public static int paymentUserId;
	private static InputStream inputStream;

	// 压缩图片的方法
	public static Bitmap getimage(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 800f;//这里设置高度为800f  
        float ww = 480f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
    } 
	//质量压缩
	private static Bitmap compressImage(Bitmap image) {  
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>5) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    } 
	

}
