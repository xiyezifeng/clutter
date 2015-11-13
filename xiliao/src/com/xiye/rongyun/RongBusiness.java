package com.xiye.rongyun;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation.ConversationType;
import android.content.Context;

/**
 * @author windows
 * 提供一个 融云 的事务处理
 *
 */
public class RongBusiness {
	
	/**
	 * 打开联系人列表
	 * @param context
	 */
	public static void openConversationList(Context context){
		if(RongIM.getInstance() != null){
			RongIM.getInstance().startConversationList(context);
		}
	}
	
	/**
	 * 开启一个用户的聊天
	 * @param context
	 * @param userId
	 * @param title
	 */
	public static void openConversation(Context context,String userId,String title){
		if(RongIM.getInstance() != null){
			RongIM.getInstance().startPrivateChat(context, userId, title);
		}
	}
	
	/**
	 * 打开群组列表
	 * @param context
	 */
	public static void openSubConvasationList(Context context){
		 RongIM.getInstance().startSubConversationList(context, ConversationType.GROUP);
	}
	
}
