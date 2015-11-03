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

import com.xiye.htmlparser.BaiDuTupianParser;
import com.xiye.htmlparser.ImageBaiDuBeanHolder;
import com.xiye.imageloader.ImageLoader;
import com.xiye.imageloader.LoadType;
import com.xiye.imageloader.R;

public class BaiDuTuPianFragment extends Fragment{
	
	private View rootView;
	private ImageLoader imageLoader;
	private GridView gridView;
	private List<String> images = new ArrayList<String>(); 
	private MyAdapter adapter;
	private BaiDuTupianParser parser;
	
	private Handler tupianhandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			//�������� parser�� ��Ϣ��ΪͼƬ��url,Ȼ��ˢ��UI
			String url = (String) msg.obj;
			if(url != null){
				images.add(url);
			}
			//�յ���Ϣ��Ϊ����ͼƬˢ���ˣ�����ȥ�����ڼ��صı�ʾ
			adapter.notifyDataSetChanged();
			
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//imageloader��ʼ��
		imageLoader = ImageLoader.getLoader(2, LoadType.LIFO);
		//baidu ͼƬ���� ��ʼ��
		try {
			parser = BaiDuTupianParser.getInstense("http://image.baidu.com/search/index?tn=baiduimage&ps=1&ct=201326592&lm=-1&cl=2&nc=1&ie=utf-8&word=����", tupianhandler);
		} catch (ParserException e) {
			Log.i("xiye", "������������ʧ��");
		}
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_baidutupian, container , false);
		gridView = (GridView) rootView.findViewById(R.id.baidu_gv);
		setAdapter();
		return rootView; 
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
