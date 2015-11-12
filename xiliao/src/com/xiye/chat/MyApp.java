package com.xiye.chat;

import io.rong.imkit.RongIM;
import android.app.Application;

public class MyApp extends Application{
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		RongIM.init(this);
		super.onCreate();
	}
}
