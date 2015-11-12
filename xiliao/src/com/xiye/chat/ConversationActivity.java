package com.xiye.chat;

import io.rong.imkit.RongIM.UserInfoProvider;
import io.rong.imlib.model.UserInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ConversationActivity extends FragmentActivity implements UserInfoProvider{
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.conversation);
	}

	@Override
	public UserInfo getUserInfo(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
