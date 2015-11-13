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
         * OnCreate �ᱻ����������룬��α������룬ȷ��ֻ������Ҫʹ�� RongIM �Ľ��̺� Push ����ִ���� init��
         * io.rong.push Ϊ���� push �������ƣ������޸ġ�
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK���õ�һ�� ��ʼ��
             */
            RongIM.init(this);
            RongIM.connect(UserInfo.token_xy00002, new ConnectCallback() {
				
				@Override
				public void onSuccess(String arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(MyApp.this, "�ɹ��������Ʒ�����", 1).show();
				}
				
				@Override
				public void onError(ErrorCode arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(MyApp.this, "���ӷ�����ʧ��", 1).show();
				}
				
				@Override
				public void onTokenIncorrect() {
					// TODO Auto-generated method stub
					Toast.makeText(MyApp.this, "Token ����", 1).show();
				}
			});
        }
		super.onCreate();
	}
	
	/**
     * ��õ�ǰ���̵�����
     *
     * @param context
     * @return ���̺�
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
