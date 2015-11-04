package com.xiye.main;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.xiye.custom.ViewPagerAdapter;
import com.xiye.imageloader.ImageLoader;
import com.xiye.imageloader.LoadType;
import com.xiye.imageloader.R;


public class MainActivity extends FragmentActivity {
	
	private ViewPager viewPager;
	private ViewPagerAdapter adapter;
	private List<Fragment> fragments;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        ImageLoader.getLoader(3, LoadType.LIFO);
        fragments  = new ArrayList<Fragment>();
        fragments.add(new DemoFragment());
        fragments.add(new BaiDuTuPianFragment());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }
    
    private void initView() {
    	viewPager = (ViewPager) findViewById(R.id.viewpage);
    	
	}
}
