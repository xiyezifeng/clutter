package com.xiye.chat;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ConversationListActivity extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.content_convresation);
		enterFragment();
	}
	
	/**
     * ���� �Ự�б� ConversationListFragment
     */
      private void enterFragment() {

          ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversation_list);

          Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                  .appendPath("conversationlist")
                  .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //����˽�ĻỰ�Ǿۺ���ʾ
                  .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//����Ⱥ��Ự�ۺ���ʾ
                  .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//����������Ự�Ǿۺ���ʾ
                  .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//����ϵͳ�Ự�Ǿۺ���ʾ
                  .build();

          fragment.setUri(uri);
      }

}
