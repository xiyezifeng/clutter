package com.xiye.imagepager;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 轮询播放图片的pager
 * @author xiye
 *
 */
public class ImagePager extends ViewPager implements PageTransformer{
	
	Handler refreshHandler;
	private static final float MIN_SCALE = 0.75f;  
	private static final float MAX_ANGLE = 180;
	
	public ImagePager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setPageTransformer(true, this);
	}
	
	@Override
	public void transformPage(View view, float position) {
		
		/*
		 * view 指 待操作的view ，同时存在2个，退出页和进入页，也就是左边页和右边页
		 * 具体看手势操作
		 */
		int pageWidth = view.getWidth();  //宽高主要用来计算view的缩放
		  
        if (position < -1) { // [-Infinity,-1)   左边页
            // This page is way off-screen to the left.  
//            view.setAlpha(0);  
            view.setRotation(0);
            Log.i("xiye", "position  <  -1      --------      "    +  position);
        } else if (position <= 0) { // [-1,0]  左边页,当前页的设置
            // Use the default slide transition when moving to the left page  
            view.setAlpha(position + 1);  	//透明度
            view.setRotation(MAX_ANGLE*position);//旋转角度 
//            view.setTranslationX(0);  //偏移量 
            view.setScaleX(1);  	  //比例
            view.setScaleY(1);  
            Log.i("xiye", "position  <=  0      --------      "   +  position);
        } else if (position <= 1) { // (0,1]  右边页的设置
            // Fade the page out.  
            view.setAlpha(1 - position);  
            view.setRotation(MAX_ANGLE*position); 
            // Counteract the default slide transition  
//            view.setTranslationX(pageWidth * -position);  
  
            // Scale the page down (between MIN_SCALE and 1)  
            float scaleFactor = MIN_SCALE  
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));  //abs 绝对值
            view.setScaleX(1);  
            view.setScaleY(1);  
            Log.i("xiye", "position  <=   1      --------      "    +  position);
        } else { // (1,+Infinity]  右边页
            // This page is way off-screen to the right.  
//        	 view.setAlpha(0);  
        	 view.setRotation(0);
            Log.i("xiye", "position  >1      --------      "    +  position);
        }  
	}
	
	public class MyPagerFragmentAdapter extends FragmentPagerAdapter{

		public MyPagerFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
}
