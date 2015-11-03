package com.xiye.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerCompat extends ViewPager{
	

	//viewpager是否监管全局的滑动事件
	private boolean touchMode = false;

	public ViewPagerCompat(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void setViewTouchMode(boolean mode){
		if(mode && !isFakeDragging()){
			//全局监控
			beginFakeDrag();
		}else if(!mode && isFakeDragging()){
			//取消全局监控
			endFakeDrag();
		}
		touchMode = mode;
	}
	
	
	/** (non-Javadoc)
	 * @see android.view.ViewGroup#onInterceptHoverEvent(android.view.MotionEvent)
	 * mode 为true的时候，viewpage 不拦截点击事件，点击事件由view处理 
	 */
	@Override
	public boolean onInterceptHoverEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(touchMode){
			return false;
		}
		return super.onInterceptHoverEvent(event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		try {
			return super.onTouchEvent(arg0);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	@Override
	public boolean arrowScroll(int arg0) {
		// TODO Auto-generated method stub
		if(touchMode)return false;
		if(arg0 != FOCUS_LEFT  && arg0 != FOCUS_RIGHT) return false;
		return super.arrowScroll(arg0);
	}
	
}
