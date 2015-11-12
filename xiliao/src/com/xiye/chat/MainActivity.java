package com.xiye.chat;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Conversation.ConversationType;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity{
	
	private Button conn,conver,list;
	private final String token = "HpIQj43zba2Cg0ONShqKe3/S0Xz7a4VWJxE8ySw0tU2IWyCzbobQknfx6HORvtVw47blP4jsvMhWKwlUZBPh3ml18rYZi6cc";
	private final String token_xiye_2 = "xAdhqnihH41TFvtNSHJVocjvWEjw/Ns3fZ4AN9JYBuf6buOlRlCpm+Nm6+w3OCMQ9gsZJCW/il1zy6lirqVlW/V40dw4fa5x";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        conn = (Button) findViewById(R.id.connection);
        conver = (Button) findViewById(R.id.conversation);
        list = (Button) findViewById(R.id.conversation_list);
        conn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RongIM.connect(token, new ConnectCallback() {
					
					@Override
					public void onSuccess(String arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplication(), "成功连接融云服务器", 1).show();
					}
					
					@Override
					public void onError(ErrorCode arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplication(), "连接服务器失败", 1).show();
					}
					
					@Override
					public void onTokenIncorrect() {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "Token 错误", 1).show();
					}
				});
			}
		});
        list.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RongIM.getInstance().startConversationList(MainActivity.this);
			}
		});
        conver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RongIM.getInstance().startConversation(MainActivity.this, ConversationType.PRIVATE, "xy00002", "聊天标记");
			}
		});
        
    }



}
