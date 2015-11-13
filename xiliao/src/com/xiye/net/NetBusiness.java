package com.xiye.net;

import android.content.Context;

/**
 * 处理网络相关事务
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
	
	//用户信息相关
	public void getUserInfo(Context context,String userId){
		
	}

}
