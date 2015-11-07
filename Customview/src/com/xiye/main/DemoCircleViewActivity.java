package com.xiye.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.xiye.customview.R;
import com.xiye.view.MyCircleImageView;

public class DemoCircleViewActivity extends Activity{
	
	private MyCircleImageView circleView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageviewdemo);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_5);
		circleView = (MyCircleImageView) findViewById(R.id.demoiamgeview);
		try {
			circleView.setImageBitmap(bitmap);
		} catch (Exception e) {
			System.out.println("bitmap  ЮЊПе");
		}
	}
	
}
