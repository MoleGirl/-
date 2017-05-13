package com.example.jiuquwan.activity;

import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.jiuquwan.R;
import com.example.jiuquwan.application.GatherApplication;
import com.example.jiuquwan.poi.PoiOverlay;
import com.example.jiuwuwan.base.BaseActivity;
import com.example.jiuwuwan.base.BaseInterface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * չʾ��ͼ��Activity
 * 
 * @author Administrator
 *
 */
public class GatherMapActivity extends BaseActivity implements BaseInterface {

	
	private class MyPoiOverlay extends PoiOverlay {  
	    public MyPoiOverlay(BaiduMap baiduMap) {  
	        super(baiduMap);  
	    }  
	    @Override  
	    public boolean onPoiClick(int index) {  
	        super.onPoiClick(index); 
	        final PoiInfo poiInfo = allPoi.get(index);
	        if (poiInfo!=null) {
				//������������
	        	//����InfoWindowչʾ��view  
	        	View v= getLayoutInflater().inflate(R.layout.popup_map, null);
	        	//����������ʾ��InfoWindow�������  
	        	LatLng pt = new LatLng(poiInfo.location.latitude, poiInfo.location.longitude);  
	        	//����InfoWindow , ���� view�� �������꣬ y ��ƫ���� 
	        	InfoWindow mInfoWindow = new InfoWindow(v, pt, -47);  
	        	//��ʾInfoWindow  
	        	mBaiduMap.showInfoWindow(mInfoWindow);
	        	
	        	TextView text = (TextView) v.findViewById(R.id.popup_map_title);
	        	text.setText(poiInfo.name);
	        	TextView context = (TextView) v.findViewById(R.id.popup_map_context);
	        	context.setText("����:"+poiInfo.city+"\n\r��ϸ��ַ:"+poiInfo.address+"\n\r�绰:"+poiInfo.phoneNum);
	        	Button but1=(Button) v.findViewById(R.id.popup_map_but1);
	        	Button but2=(Button) v.findViewById(R.id.popup_map_but2);
	        	//ȷ����ť
	        	but1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//��������ظ���һ������(�����ݴ�ŵ�Application��)
						GatherApplication.put("data-poi", poiInfo);
						finish();
						
						
					}
				});
	        	//ȡ����ť
	        	but2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//ȡ����ʾInfoWindow
						mBaiduMap.hideInfoWindow();
					}
				});
			}
	        
	        
	        return true;  
	    }  
	}
	
	
	
	
	private EditText map_et;
	private MapView mMapView;
	private PoiSearch mPoiSearch;
	private BaiduMap mBaiduMap;
	
	// ��ȡPOI�������
	private List<PoiInfo> allPoi;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.act_map);
		initView();
		initData();
		initViewOper();
		initPoi();
	}

	private void initPoi() {
		// ��һ��������POI����ʵ��
		mPoiSearch = PoiSearch.newInstance();
		// �ڶ���������POI���������ߣ�
		OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {

			public void onGetPoiResult(PoiResult result) {
				// ��ȡPOI�������
				allPoi = result.getAllPoi();
				//չʾ����ͼ��
				  //����PoiOverlay  
		        PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);  
		        //����overlay���Դ����ע����¼�  
		        mBaiduMap.setOnMarkerClickListener(overlay);  
		        //����PoiOverlay����  
		        overlay.setData(result);  
		        //���PoiOverlay����ͼ��  
		        overlay.addToMap();  
		        overlay.zoomToSpan();  
				
				
			}

			public void onGetPoiDetailResult(PoiDetailResult result) {
				// ��ȡPlace����ҳ�������
			}
		};
		// ������������POI���������ߣ�
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

	}

	@Override
	public void initView() {
		map_et = etById(R.id.act_map_et);
		// ��ȡ��ͼ�ؼ�����
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap=mMapView.getMap();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initViewOper() {
		// TODO Auto-generated method stub

	}

	/**
	 * ���������ť
	 */
	public void onClick(View v) {
		String text = map_et.getText().toString().trim();
		//��ǰ��λ�ĳ���
		String city=null;
		try {
			city = GatherApplication.location.getCity();
		} catch (Exception e) {
			e.printStackTrace();
			city="����";
		}
		if (city==null) {
			city="����";
		}
		// ���Ĳ��������������
		mPoiSearch.searchInCity((new PoiCitySearchOption())
				.city(city)
				.keyword(text)
				.pageNum(0));

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onPause();
	}

}
