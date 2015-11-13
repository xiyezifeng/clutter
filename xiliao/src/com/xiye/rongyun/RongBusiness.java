package com.xiye.rongyun;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation.ConversationType;
import android.content.Context;

/**
 * @author windows
 * �ṩһ�� ���� ��������
 *
 */
public class RongBusiness {
	
	/**
	 * ����ϵ���б�
	 * @param context
	 */
	public static void openConversationList(Context context){
		if(RongIM.getInstance() != null){
			RongIM.getInstance().startConversationList(context);
		}
	}
	
	/**
	 * ����һ���û�������
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
	 * ��Ⱥ���б�
	 * @param context
	 */
	public static void openSubConvasationList(Context context){
		 RongIM.getInstance().startSubConversationList(context, ConversationType.GROUP);
	}
	
}
