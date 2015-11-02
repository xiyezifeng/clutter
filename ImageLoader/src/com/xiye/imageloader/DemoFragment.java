package com.xiye.imageloader;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.xiye.util.ImageBeanHolder;
import com.xiye.util.ImageLoader;
import com.xiye.util.Images;
import com.xiye.util.LoadType;

public class DemoFragment extends Fragment{
	
	private View rootView;
	private ImageLoader imageLoader;
	private GridView gridView;
	private String images[] = Images.images; 
	private myAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		imageLoader = ImageLoader.getLoader(3, LoadType.LIFO);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment, container , false);
		gridView = (GridView) rootView.findViewById(R.id.gv);
		setAdapter();
		return rootView; 
	}
	
	private void setAdapter(){
		if(getActivity() == null || gridView == null){
			return;
		}
		if( images != null ){
			adapter = new myAdapter(getActivity(), 0, images);
			gridView.setAdapter(adapter);
		}else{
			gridView.setAdapter(null);
		}
	}
	
	class myAdapter extends ArrayAdapter<String>
	{
		
		public myAdapter(Context context, int resource, String[] objects) {
			super(getActivity(), 0, objects);
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ImageBeanHolder beanHolder;
			if(null == convertView){
				beanHolder = new ImageBeanHolder();
				convertView = getActivity().getLayoutInflater().inflate(R.layout.iamge_item, null);
				convertView.setTag(beanHolder);
			}else{
				beanHolder = (ImageBeanHolder) convertView.getTag();
			}
			final ImageView  imageView = (ImageView) convertView.findViewById(R.id.iamge);
			beanHolder.imageView = imageView;
			imageView.setImageResource(R.drawable.ic_launcher);
			imageLoader.loadImage(getItem(position), imageView, true);
			return convertView;
		}
		
	}

}
