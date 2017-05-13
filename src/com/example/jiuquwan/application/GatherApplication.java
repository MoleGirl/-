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
	 * ����ǰ�û�����
	 */
	public static User u;
	/**
	 * �����ļ���
	 */
	public static List<GatherBean> gathers;
	// ������
	private static Context context;
	public static BDLocation location;
	private LocationClient mLocationClient;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// ��ʼ��������
		context = getApplicationContext();
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getApplicationContext());

		// ��ʼ�����ݷ���
		Bmob.initialize(this, "162ca550f0ed247a2f86e030ae67f671");
		// ��ʼ�����ŷ���
		BmobSMS.initialize(this, "162ca550f0ed247a2f86e030ae67f671");
		// ��ʼ��֧������
		BP.init(this, "162ca550f0ed247a2f86e030ae67f671");
		gathers = new ArrayList<>();
		initBaidu();

		// ��Ϣ����
		// ��ʼ��BmobSDK
		Bmob.initialize(this, "162ca550f0ed247a2f86e030ae67f671");
		// ʹ�����ͷ���ʱ�ĳ�ʼ������
		BmobInstallation.getCurrentInstallation().save();
		// �������ͷ���
		BmobPush.startWork(this);

	}

	/**
	 * ��ȡ��ǰ���еĽ�����
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
		mLocationClient = new LocationClient(getApplicationContext()); // ����LocationClient��
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				GatherApplication.location = location;
			}
		}); // ע���������

		initLocation();
		mLocationClient.start();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		int span = 1000;
		option.setScanSpan(span);// ��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		option.setIsNeedAddress(true);// ��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setOpenGps(true);// ��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		option.setLocationNotify(true);// ��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
		option.setIsNeedLocationDescribe(true);// ��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
		option.setIsNeedLocationPoiList(true);// ��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�option.setIgnoreKillProcess(false);//��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
		option.SetIgnoreCacheException(false);// ��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�option.setEnableSimulateGps(false);//��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
		mLocationClient.setLocOption(option);
	}

	/**
	 * ��ǵ�ǰ�������ݵ�״̬ -1 ��ʾ���ڼ����� 0 ��ʾ����ʧ�� 1��ʾ���سɹ�
	 */
	public static int findGatherFlag;

	/**
	 * ��ʼ���������ݵķ���,**����д��initActivity��** ����һ��intֵ, -1 ��ʾ���ڼ�����(Ĭ��) 0 ��ʾ����ʧ��
	 * 1��ʾ���سɹ�
	 */
	public static void initData() {
		findGatherFlag = -1;
		FindGatherUtils.findGather(1, null, 0, 0, new FindGatherListener() {

			@Override
			public void findData(List<GatherBean> beans, BmobException arg1) {
				if (arg1 == null) {
					// �������ݳɹ�
					gathers = beans;
					findGatherFlag = 1;
					Toast.makeText(context, "�������ݳɹ�,����:" + beans.size(), 0).show();
				} else {
					// ��������ʧ��
					// Toast.makeText(context, "��������ʧ��,������������", 0).show();
					findGatherFlag = 0;
				}

			}
		});
		Log.i("Log", "���:" + findGatherFlag);
	}

	public static int intIds;
	public static String titles;

	private static Map<String, Object> datas = new LinkedHashMap<>();

	/**
	 * ��ȫ�����������д������,�������Activity/Fragment֮�䴫������
	 * 
	 * @param key
	 *            ��������key
	 * @param value
	 *            ��������
	 * @return �����key�Ѿ���������ֵ������ӳ���ϵ,��ô���β�����Ѿɵ����ݸ���,���һ᷵�ؾɵ�����,���򷵻�null
	 */
	public static Object put(String key, Object value) {
		return datas.put(key, value);
	}

	/**
	 * ��ȫ�ֵ������л�ȡ����
	 * 
	 * @param key
	 *            ��ȡ���ݵ���Կ
	 * @param isDlete
	 *            �Ƿ�ɾ����������
	 * @return ���λ�ȡ������
	 */
	public static Object get(String key, Boolean isDlete) {
		if (isDlete) {
			return datas.remove(key);
		}
		return datas.get(key);
	}

	// ��ǰ�û�֧����(�Ѿ������)��ĸ���
	public static int payGatherIds;
	// �û�Ѿ�������û��ĸ���
	public static int paymentUserId;
	private static InputStream inputStream;

	// ѹ��ͼƬ�ķ���
	public static Bitmap getimage(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//��ʱ����bmΪ��  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ  
        float hh = 800f;//�������ø߶�Ϊ800f  
        float ww = 480f;//�������ÿ��Ϊ480f  
        //���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��  
        int be = 1;//be=1��ʾ������  
        if (w > h && w > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//�������ű���  
        //���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
        return compressImage(bitmap);//ѹ���ñ�����С���ٽ�������ѹ��  
    } 
	//����ѹ��
	private static Bitmap compressImage(Bitmap image) {  
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>5) {  //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��         
            baos.reset();//����baos�����baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ��������ݴ�ŵ�baos��  
            options -= 10;//ÿ�ζ�����10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ���������baos��ŵ�ByteArrayInputStream��  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//��ByteArrayInputStream��������ͼƬ  
        return bitmap;  
    } 
	

}
