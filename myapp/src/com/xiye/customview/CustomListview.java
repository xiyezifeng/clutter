package com.xiye.customview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiye.myapp.R;

/**
 * @author windows
 *
 */

/**
 * @author windows
 *
 */
public class CustomListview extends ListView implements OnScrollListener{
	
	
	/**
	 * 显示头部刷新信息
	 */
	private View headView;
	
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
	
	/**
	 * 刷新时长，单位秒
	 */
	private final int SUSTAIN_TIME = 5; 
	
	/**
	 * 可刷新的最小高度
	 */
	private int MIN_HEIGHT = 0;
	
	/**
	 * 可向下拉动的最大距离 
	 */
	private int MAX_HEIGHT = 0;
	
	/**
	 * 头部高度
	 */
	private static int header_content_height;
	
	/**
	 * 速率变化比
	 */
	private int next_slide_scale = 3;
	
	private int startY;
	private int startX;
	private boolean isBack;
	
	/**
	 * 数据组
	 */
	private List<Object> datalist = new ArrayList<Object>();
	
	/**
	 * 旋转动画
	 */
	private RotateAnimation arrowsAnimation;
	
	private RotateAnimation resetAnimation;
	
	/**
	 * 开启刷新，默认是true
	 */
	private boolean refresh_able = true;
	
	private static final int DONE = 0; //常态
	private static final int REFRESHING = 1; //正在刷新
	private static final int LOADING = 2;  // 
	private static final int RELEASE_TO_REFRESH = 3; // 下拉过程的状态
	private static final int PULL_TO_REFRESH = 4; //从下拉状态返回不刷新的状态
	
	/**
	 * 刷新完成? 默认为true
	 */
	private int refresh_state = 0;
	
	/**
	 * 保证一个完整的touch事件中，只记录一次
	 */
	private boolean isRecored = false;
	
	
	public CustomListview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public CustomListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public CustomListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setAdapter(adapter);
	}
	
	private void init(){
		headView = LayoutInflater.from(getContext()).inflate(R.layout.custom_listview_header, null);
		arrowsImage = (ImageView) headView.findViewById(R.id.arrows);
		state = (TextView) headView.findViewById(R.id.state);
		//计算高度
		arrowsImage.setMinimumHeight(45);
		arrowsImage.setMinimumWidth(45);
		measureView(headView);
		
		//计算头部高度
		header_content_height = headView.getMeasuredHeight();
		//内边距为 负高度
		headView.setPadding(0, -1*header_content_height, 0, 0);
		//重绘
		headView.invalidate();
		//加入布局
		addHeaderView(headView,null,false);
		
		setOnScrollListener(this);
		
		arrowsAnimation = new RotateAnimation(0, -180,RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		arrowsAnimation.setInterpolator(new LinearInterpolator());		
		arrowsAnimation.setDuration(250);
		arrowsAnimation.setFillAfter(true);
		
		resetAnimation = new RotateAnimation(-180,0,RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		resetAnimation.setInterpolator(new LinearInterpolator());
		resetAnimation.setDuration(200);
		resetAnimation.setFillAfter(true);
		
		refresh_state = DONE;
		refresh_able = false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(refresh_able){
			switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
				 //放开有两种形式，一种去刷新，一种回弹
				if(refresh_state != REFRESHING && refresh_state != LOADING){
					if(refresh_state == PULL_TO_REFRESH){
						refresh_state = DONE;
						changeHeaderViewByState();
					}
					if(refresh_state == RELEASE_TO_REFRESH){
						refresh_state = REFRESHING;
						changeHeaderViewByState();
						onLvRefresh();
					}
				}
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_DOWN:
				if(!isRecored){
					 isRecored = true;
					 startY = (int) ev.getY(); //当前按下的位置
				 }
				break;
				
			case MotionEvent.ACTION_MOVE:
				
				break;

			default:
				break;
			}
		}
		return super.onTouchEvent(ev);
	}
	
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		
		super.onScrollChanged(l, t, oldl, oldt);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(firstVisibleItem == 0){
			refresh_able = true;
		}else{
			refresh_able = false;
		}
	}
	private void measureView(View child){
		ViewGroup.LayoutParams params = child.getLayoutParams();
		if (params == null) {
			params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0,
				params.width);
		int lpHeight = params.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}
	
}
