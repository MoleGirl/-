package com.example.jiuquwan.utils;

import java.util.List;

import com.example.jiuquwan.bean.GatherBean;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FindGatherUtils {
	/**
	 * ��ѯ����ݵĹ��߷���
	 * @param type ���ݲ�ͬ������,���в�ѯ��ͬ������
	 * 				1.һ�β�ѯ���µ�10������(����������չʾ,�Լ�ˢ�����ݲ���)
	 * 				2.���Բ���3������,��ѯcount������(���ظ������)
	 * 				3.���ݲ���2��id,��ѯһ�������(��������,�鿴�����ʱʹ��)
	 * 
	 * 				10001.���ݲ���2�Ļ����,��ѯ����Ļ���Ͷ���
	 * 				10002.������Ѳ�ѯ,Ҳ���Ǽ۸�Ϊ0
	 * 				10003.��ѯ����,��Ҫ��data�д��뵱ǰ�û����ھ�γ����ϢBmobGeoPoint(<BmobGeoPoint>),
	 * 				10004.���Ų�ѯ,�Զ����ݵ��޵��������в�ѯ
	 * 				10005.ģ����ѯ,��ϲ���2
	 * @param data ����typeʹ��,��typeΪ3ʱ,������id��ѯһ��,��typeΪ4ʱ,��������,��ѯһ������
	 * @param skip ֻ�е�typeΪ2ʱ,����Ч, ��ѯʱ���Ե�����
	 * @param count	ֻ�е�typeΪ2ʱ,����Ч,��ѯ��������
	 * @param listener �ص�����
	 * skip���Ե�����
	 * limit��ѯ������
	 * 
	 */
	public static void findGather(int type,Object data,int skip,int count,final FindGatherListener listener){
		BmobQuery<GatherBean> query =new BmobQuery<>();
		
		switch (type) {
		case 1:
			//���ò�ѯ����Ϊ��ѯ10��
			query.setLimit(10);
			break;
		case 2:
			//���ò�ѯ����Ϊ��ѯ10��
			query.setLimit(10);
			//���Ե�����
			query.setSkip(skip);
			break;
		case 3:
			//����id��ѯһ������
			query.addWhereEqualTo("ObjectId", data);
			break;
			
			
		case 10001:
			//���ݻ���Ͳ�ѯ����
			query.addWhereEqualTo("gatherClass", data);
			break;
		case 10002:
			//������Ѳ�ѯ
			query.addWhereEqualTo("gatherRMB", "0");
			break;
		case 10003:
			//��ѯ����
			query.addWhereNear("point", (BmobGeoPoint)(data));
			break;
		case 10004:
			//���Ų�ѯ
			//���ݵ�������,������ʾ,����������һ����,���ߴ���ʱ�併��
			query.order("-loveCount,-createdAt");
			break;
		case 10005:
			//ģ����ѯ
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
	 * ���������ݻص�
	 * @author Administrator
	 *
	 */
	public interface FindGatherListener{
		void findData(List<GatherBean> beans,BmobException arg1);
	}
	
}
