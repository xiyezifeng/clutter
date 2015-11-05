package com.xiye.imagepager;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyPagerAdapter extends PagerAdapter{
	
	private List<ImageView> mImageViews ; 
	
	public MyPagerAdapter(List<ImageView> views) {
		// TODO Auto-generated constructor stub
		mImageViews = views;
	}

	@Override  
    public Object instantiateItem(ViewGroup container, int position)  
    {  
		
        container.addView(mImageViews.get(position));  
        return mImageViews.get(position);  
    }  

    @Override  
    public void destroyItem(ViewGroup container, int position,  
            Object object)  
    {  

        container.removeView(mImageViews.get(position));  
    }  

    @Override  
    public boolean isViewFromObject(View view, Object object)  
    {  
        return view == object;  
    }  

    @Override  
    public int getCount()  
    {  
        return mImageViews.size();  
    }  
	
}