package com.xiye.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * @author xiye
 *
 */
public class SlideingLinearlayout extends LinearLayout {
	
	private CustomScrollView mScrollView;
	

	public SlideingLinearlayout(Context context, AttributeSet attrs) {
		 super(context, attrs);
	     mScrollView = new CustomScrollView(context);
		 LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(     
                 LinearLayout.LayoutParams.MATCH_PARENT,     
                 LinearLayout.LayoutParams.MATCH_PARENT     
         );   
		 this.addView(mScrollView);
	}
	
}
