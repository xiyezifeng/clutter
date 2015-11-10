package com.xiye.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView{
	
	public CustomScrollView(Context context){
		super(context);
	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		
		super.onScrollChanged(l, t, oldl, oldt);
	}

}
