package com.xiye.imageutil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;

import com.xiye.customview.CircleView;

public class ImageSizeUtil {
	
	public static ImageSize getImageSize(CircleView view){
		ImageSize size = new ImageSize();
		size.sizeH = view.getHeight();
		size.sizeW = view.getWidth();
		return size;
	}
	
	public static  int getinSampleSize(Options options,ImageSize size){
		int inSampleSize = 0 ;
		int bitmapH = options.outHeight;
		int bitmapW = options.outWidth;
		
		int x = Math.round(1.0f*bitmapW/size.sizeW);
		int y = Math.round(10.f*bitmapH/size.sizeH);
		
		inSampleSize = Math.max(x, y);
		
		return inSampleSize;
	}

}
