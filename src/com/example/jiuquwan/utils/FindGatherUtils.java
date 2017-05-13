package com.example.jiuquwan.utils;

import java.util.List;

import com.example.jiuquwan.bean.GatherBean;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FindGatherUtils {
	/**
	 * 查询活动数据的工具方法
	 * @param type 根据不同的类型,进行查询不同的数据
	 * 				1.一次查询最新的10条数据(正常的数据展示,以及刷新数据操作)
	 * 				2.忽略参数3的条数,查询count个数据(加载更多操作)
	 * 				3.根据参数2的id,查询一个活动对象(点击活动对象,查看活动详情时使用)
	 * 
	 * 				10001.根据参数2的活动类型,查询具体的活动类型对象
	 * 				10002.进行免费查询,也就是价格为0
	 * 				10003.查询附近,需要在data中传入当前用户所在经纬度信息BmobGeoPoint(<BmobGeoPoint>),
	 * 				10004.热门查询,自动根据点赞的数量进行查询
	 * 				10005.模糊查询,结合参数2
	 * @param data 搭配type使用,当type为3时,传入活动的id查询一条,当type为4时,传入活动类型,查询一类数据
	 * @param skip 只有当type为2时,才生效, 查询时忽略的条数
	 * @param count	只有当type为2时,才生效,查询的总条数
	 * @param listener 回调监听
	 * skip忽略的条数
	 * limit查询的条数
	 * 
	 */
	public static void findGather(int type,Object data,int skip,int count,final FindGatherListener listener){
		BmobQuery<GatherBean> query =new BmobQuery<>();
		
		switch (type) {
		case 1:
			//设置查询条件为查询10个
			query.setLimit(10);
			break;
		case 2:
			//设置查询条件为查询10个
			query.setLimit(10);
			//忽略的条数
			query.setSkip(skip);
			break;
		case 3:
			//根据id查询一条数据
			query.addWhereEqualTo("ObjectId", data);
			break;
			
			
		case 10001:
			//根据活动类型查询数据
			query.addWhereEqualTo("gatherClass", data);
			break;
		case 10002:
			//进行免费查询
			query.addWhereEqualTo("gatherRMB", "0");
			break;
		case 10003:
			//查询附近
			query.addWhereNear("point", (BmobGeoPoint)(data));
			break;
		case 10004:
			//热门查询
			//根据点赞数量,降序显示,当点赞数量一样是,更具创建时间降序
			query.order("-loveCount,-createdAt");
			break;
		case 10005:
			//模糊查询
			query.addWhereContains("gatherTitle", (String)(data));
			break;
		
		}
		
		query.findObjects(new FindListener<GatherBean>() {
			
			@Override
			public void done(List<GatherBean> arg0, BmobException arg1) {
				listener.findData(arg0, arg1);
				
				
			}
		});
		
		
		
	}
	
	
	
	/**
	 * 用来做数据回调
	 * @author Administrator
	 *
	 */
	public interface FindGatherListener{
		void findData(List<GatherBean> beans,BmobException arg1);
	}
	
}
