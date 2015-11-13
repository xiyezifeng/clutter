package com.xiye.rongyun;

import com.xiye.resouse.UserBusiness;

import android.net.Uri;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 对接融云的接口类，主要为传递参数给融云
 * @author windows
 *
 */
public class RongJoint {
	
	
	/**
	 * 设置用户信息的提供者，供 RongIM 调用获取用户名称和头像信息。
	 *
	 * @param userInfoProvider 用户信息提供者。
	 * @param isCacheUserInfo  设置是否由 IMKit 来缓存用户信息。<br>
	 *                         如果 App 提供的 UserInfoProvider。
	 *                         每次都需要通过网络请求用户数据，而不是将用户数据缓存到本地内存，会影响用户信息的加载速度；<br>
	 *                         此时最好将本参数设置为 true，由 IMKit 将用户信息缓存到本地内存中。
	 * @see UserInfoProvider
	 */
	public static void setUserInfoToRongYun(){
		RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

		    @Override
		    public UserInfo getUserInfo(String userId) {
		        return findUserById(userId);//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
		    }
		}, true);
	}
	
	
	/**
	 * 需要从服务器上获取用户信息
	 * @param userId
	 * @return
	 */
	private static  UserInfo findUserById(String userId){
		UserInfo userInfo = null;
		
		return userInfo;
	}
	
}
