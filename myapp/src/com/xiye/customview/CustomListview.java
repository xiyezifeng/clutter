package com.xiye.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author windows
 *
 */
/**
 * @author windows
 *
 */
/**
 * @author windows
 *
 */
/**
 * @author windows
 *
 */
public class CustomListview extends ListView{
	
	/**
	 * 显示头部刷新信息
	 */
	private LinearLayout headView;
	
	/**
	 * 箭头
	 */
	private ImageView arrowsImage;
	
	/**
	 * 刷新时间
	 */
	private TextView time;
	
	/**
	 * 正在刷新/下拉刷新/松开刷新
	 */
	private TextView state;
	
	private static String[] STATE_INFO = {"正在刷新","下拉刷新","松开刷新"};
	
	private final int SUSTAIN_TIME = 800; 
	
	
	public CustomListview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setAdapter(adapter);
	}
}
