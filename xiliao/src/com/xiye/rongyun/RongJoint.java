package com.xiye.rongyun;

import com.xiye.resouse.UserBusiness;

import android.net.Uri;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * �Խ����ƵĽӿ��࣬��ҪΪ���ݲ���������
 * @author windows
 *
 */
public class RongJoint {
	
	
	/**
	 * �����û���Ϣ���ṩ�ߣ��� RongIM ���û�ȡ�û����ƺ�ͷ����Ϣ��
	 *
	 * @param userInfoProvider �û���Ϣ�ṩ�ߡ�
	 * @param isCacheUserInfo  �����Ƿ��� IMKit �������û���Ϣ��<br>
	 *                         ��� App �ṩ�� UserInfoProvider��
	 *                         ÿ�ζ���Ҫͨ�����������û����ݣ������ǽ��û����ݻ��浽�����ڴ棬��Ӱ���û���Ϣ�ļ����ٶȣ�<br>
	 *                         ��ʱ��ý�����������Ϊ true���� IMKit ���û���Ϣ���浽�����ڴ��С�
	 * @see UserInfoProvider
	 */
	public static void setUserInfoToRongYun(){
		RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

		    @Override
		    public UserInfo getUserInfo(String userId) {
		        return findUserById(userId);//���� userId ȥ����û�ϵͳ���ѯ��Ӧ���û���Ϣ���ظ����� SDK��
		    }
		}, true);
	}
	
	
	/**
	 * ��Ҫ�ӷ������ϻ�ȡ�û���Ϣ
	 * @param userId
	 * @return
	 */
	private static  UserInfo findUserById(String userId){
		UserInfo userInfo = null;
		
		return userInfo;
	}
	
}
