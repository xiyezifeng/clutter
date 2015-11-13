package com.xiye.chat;

import com.xiye.resouse.UserInfo;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class MyApp extends Application{
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		/**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
            RongIM.connect(UserInfo.token_xy00002, new ConnectCallback() {
				
				@Override
				public void onSuccess(String arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(MyApp.this, "成功连接融云服务器", 1).show();
				}
				
				@Override
				public void onError(ErrorCode arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(MyApp.this, "连接服务器失败", 1).show();
				}
				
				@Override
				public void onTokenIncorrect() {
					// TODO Auto-generated method stub
					Toast.makeText(MyApp.this, "Token 错误", 1).show();
				}
			});
        }
		super.onCreate();
	}
	
	/**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
