package com.xiye.net;

import android.content.Context;

/**
 * ���������������
 * @author windows
 *
 */
public class NetBusiness {
	
	private static NetBusiness business = new NetBusiness();
	
	private NetBusiness() {
		// TODO Auto-generated constructor stub
	}
	
	public static NetBusiness getInstence(){
		if(null != business){
			return business;
		}
		return null;
	}
	
	//�û���Ϣ���
	public void getUserInfo(Context context,String userId){
		
	}

}
