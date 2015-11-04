package com.xiye.main;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.util.ParserException;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiye.htmlparser.BaiDuTupianParser;
import com.xiye.htmlparser.ImageBaiDuBeanHolder;
import com.xiye.htmlparser.BaiDuTupianParser.callback;
import com.xiye.imageloader.ImageLoader;
import com.xiye.imageloader.R;

public class BaiDuTuPianFragment extends Fragment implements callback{
	
	private View rootView;
	private ImageLoader imageLoader;
	private GridView gridView;
	//images 需要static 不然每次进入都得初始化
	private static List<String> images = new ArrayList<String>();
	private MyAdapter adapter;
	private BaiDuTupianParser parser;
	private TextView baiduhint;
	
	
	/**
	 * 图片解析出来的回调
	 */
	private Handler tupianhandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			//接收来自 parser的 消息，为图片的url,然后刷新UI
			baiduhint.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
		}
	};
	
	@Override
	public void callbackmesg(String image) {
		if(images != null){
			images.add(image);
		}
		tupianhandler.sendEmptyMessage(0X10);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//imageloader初始化
		imageLoader = ImageLoader.getLoader();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_baidutupian, container , false);
		gridView = (GridView) rootView.findViewById(R.id.baidu_gv);
		baiduhint = (TextView) rootView.findViewById(R.id.baiduhint);
		//baidu 图片解析 初始化
		try {
			parser = BaiDuTupianParser.getInstense("http://www.douban.com/photos/album/129889623/",this);
		} catch (ParserException e) {
			Log.i("xiye", "解析出错，解析失败");
		}
		setAdapter();
		return rootView; 
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		if(null != images && !images.isEmpty()){
			baiduhint.setVisibility(View.GONE);
		}else{
			baiduhint.setVisibility(View.VISIBLE);
		}
		super.onStart();
	}

	private void setAdapter(){
		if(getActivity() == null || gridView == null){
			return;
		}
		if( images != null ){
			adapter = new MyAdapter(getActivity(), 0, images);
			gridView.setAdapter(adapter);
		}else{
			gridView.setAdapter(null);
		}
	}
	
	class MyAdapter extends ArrayAdapter<String>
	{
		
		public MyAdapter(Context context, int resource, List<String> objects) {
			super(getActivity(), 0, objects);
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ImageBaiDuBeanHolder beanHolder;
			if(null == convertView){
				beanHolder = new ImageBaiDuBeanHolder();
				convertView = getActivity().getLayoutInflater().inflate(R.layout.iamge_item, null);
				convertView.setTag(beanHolder);
			}else{
				beanHolder = (ImageBaiDuBeanHolder) convertView.getTag();
			}
			final ImageView  imageView = (ImageView) convertView.findViewById(R.id.iamge);
			beanHolder.imageView = imageView;
			imageView.setImageResource(R.drawable.ic_launcher);
			imageLoader.loadImage(getItem(position), imageView, true);
			return convertView;
		}
		
	}

	

}
