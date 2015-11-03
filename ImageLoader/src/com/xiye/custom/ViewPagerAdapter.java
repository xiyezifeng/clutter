package com.xiye.custom;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter{
	
	private List<Fragment> items;
	
	public ViewPagerAdapter(FragmentManager fm , List<Fragment> items) {
		super(fm);
		this.items = items;
	}

	@Override
	public Fragment getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	
	
	

}
