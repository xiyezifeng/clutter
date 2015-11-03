package com.xiye.htmlparser;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageBaiDuBeanHolder {
	
	public String path;
	public ImageView imageView;
	public Bitmap bitmap;
	//objurl是百度图片上的大图连接，不同于缩略图的连接,使用imageloader加载时只加载小图，该值用来查看大图用
	public String objurl;

}
