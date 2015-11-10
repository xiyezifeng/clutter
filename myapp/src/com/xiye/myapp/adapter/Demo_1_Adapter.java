package com.xiye.myapp.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xiye.customview.CircleView;
import com.xiye.imageutil.EasyImage;
import com.xiye.model.Demo_1_Model;
import com.xiye.myapp.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Demo_1_Adapter extends BaseAdapter{
	
	private  List<Demo_1_Model> model_1 ;
	private Context context;
	private EasyImage easyImage;
	
	public Demo_1_Adapter(Context context , List<Demo_1_Model> model_1) {
		this.context = context;
		this.model_1 = model_1;
		easyImage = EasyImage.getInstance(3);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return model_1.size();
	}

	@Override
	public Object getItem(int position) {
		return model_1.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final BeanHolder holder;
		Demo_1_Model model = (Demo_1_Model) getItem(position);
		if(null == convertView){
			holder = new BeanHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.demo_listview_1_item	, null);
			convertView.setTag(holder);
			holder.headView  = (CircleView) convertView.findViewById(R.id.demo_listview_1_head);
			holder.name = (TextView) convertView.findViewById(R.id.demo_listview_1_name);
			holder.info = (TextView) convertView.findViewById(R.id.demo_listview_1_intro);
		}else{
			holder = (BeanHolder) convertView.getTag();
		}
		//加载图片至view中，需要前去下载
		if(holder.headView.getVisibility() == View.VISIBLE){
			
			holder.headView.setImageBitmap(null);
			easyImage.loadImage(model.image, holder.headView);
		}else
			holder.headView.setImageBitmap(null);
		
		holder.name.setText(model.name);
		holder.info.setText(model.info);
		return convertView;
	}

	public class BeanHolder{
		CircleView headView;
		TextView name;
		TextView info;
	}
}
