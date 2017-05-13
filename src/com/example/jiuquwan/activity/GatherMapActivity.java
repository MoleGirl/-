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
 * 展示地图的Activity
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
				//弹出窗覆盖物
	        	//创建InfoWindow展示的view  
	        	View v= getLayoutInflater().inflate(R.layout.popup_map, null);
	        	//定义用于显示该InfoWindow的坐标点  
	        	LatLng pt = new LatLng(poiInfo.location.latitude, poiInfo.location.longitude);  
	        	//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量 
	        	InfoWindow mInfoWindow = new InfoWindow(v, pt, -47);  
	        	//显示InfoWindow  
	        	mBaiduMap.showInfoWindow(mInfoWindow);
	        	
	        	TextView text = (TextView) v.findViewById(R.id.popup_map_title);
	        	text.setText(poiInfo.name);
	        	TextView context = (TextView) v.findViewById(R.id.popup_map_context);
	        	context.setText("城市:"+poiInfo.city+"\n\r详细地址:"+poiInfo.address+"\n\r电话:"+poiInfo.phoneNum);
	        	Button but1=(Button) v.findViewById(R.id.popup_map_but1);
	        	Button but2=(Button) v.findViewById(R.id.popup_map_but2);
	        	//确定按钮
	        	but1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//将结果返回给上一个界面(将数据存放到Application中)
						GatherApplication.put("data-poi", poiInfo);
						finish();
						
						
					}
				});
	        	//取消按钮
	        	but2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//取消显示InfoWindow
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
	
	// 获取POI检索结果
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
		// 第一步，创建POI检索实例
		mPoiSearch = PoiSearch.newInstance();
		// 第二步，创建POI检索监听者；
		OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {

			public void onGetPoiResult(PoiResult result) {
				// 获取POI检索结果
				allPoi = result.getAllPoi();
				//展示到地图上
				  //创建PoiOverlay  
		        PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);  
		        //设置overlay可以处理标注点击事件  
		        mBaiduMap.setOnMarkerClickListener(overlay);  
		        //设置PoiOverlay数据  
		        overlay.setData(result);  
		        //添加PoiOverlay到地图中  
		        overlay.addToMap();  
		        overlay.zoomToSpan();  
				
				
			}

			public void onGetPoiDetailResult(PoiDetailResult result) {
				// 获取Place详情页检索结果
			}
		};
		// 第三步，设置POI检索监听者；
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

	}

	@Override
	public void initView() {
		map_et = etById(R.id.act_map_et);
		// 获取地图控件引用
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
	 * 点击搜索按钮
	 */
	public void onClick(View v) {
		String text = map_et.getText().toString().trim();
		//当前定位的城市
		String city=null;
		try {
			city = GatherApplication.location.getCity();
		} catch (Exception e) {
			e.printStackTrace();
			city="北京";
		}
		if (city==null) {
			city="北京";
		}
		// 第四步，发起检索请求；
		mPoiSearch.searchInCity((new PoiCitySearchOption())
				.city(city)
				.keyword(text)
				.pageNum(0));

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

}
