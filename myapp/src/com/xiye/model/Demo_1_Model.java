package com.xiye.model;

import com.xiye.customview.CircleView;

import android.graphics.Bitmap;

public class Demo_1_Model {
	public String image;
	public String name;
	public String info;
	public Bitmap bm;
	public CircleView imageview;
	
	public Demo_1_Model() {
		// TODO Auto-generated constructor stub
	}
	
	public Demo_1_Model(String iamge,String name,String info){
		this.image = iamge;
		this.name = name;
		this.info = info;
	}
	
	public CircleView getImageview() {
		return imageview;
	}
	
	public void setImageview(CircleView imageview) {
		this.imageview = imageview;
	}
	
	public void setBm(Bitmap bm) {
		this.bm = bm;
	}
	public Bitmap getBm() {
		return bm;
	}
	
}
