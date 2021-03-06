package com.xiye.imageutil;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory.Options;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 * @author xiye
 * @category util
 * @version 1
 * @meaning 图片尺寸计算
 */
public class ImageSizeUtil {
	/**
	 * 计算控件大小
	 * @param imageview
	 * @return
	 */
	@SuppressLint("NewApi")
	public static ImageSize getImageViewSize(ImageView imageview){
		ImageSize imageSize = new ImageSize();
		DisplayMetrics displayMetrics = imageview.getContext().getResources().getDisplayMetrics();
//		LayoutParams lp =  imageview.getLayoutParams();
		int width = imageview.getWidth();
		if(width <= 0){
//			width = lp.width;
		}
		if(width <= 0){
			width = getImageViewFieldValue(imageview, "mMaxWidth");
		}
		if(width <= 0){
			width = displayMetrics.widthPixels;
		}
		int height = imageview.getHeight();
		if(height <= 0){
//			height = lp.height;
		}
		if(height <= 0){
			height = getImageViewFieldValue(imageview, "mMaxHeight");
		}
		if(height <= 0){
			height = displayMetrics.heightPixels;
		}
		imageSize.height = height;
		imageSize.width  = width;
		return imageSize;
	}


	/**
	 * 通过反射获取imageview的某个属性值
	 *
	 * @param object
	 * @param fieldName
	 * @return
	 */
	private static int getImageViewFieldValue(Object object, String fieldName)
	{
		int value = 0;
		try
		{
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = field.getInt(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
			{
				value = fieldValue;
			}
		} catch (Exception e)
		{
		}
		return value;

	}
	/**
	 * 计算压缩倍数
	 * @param options
	 * @param reqwidth
	 * @param reqheight
	 * @return
	 */
	public static int calculateInSampleSize(Options options,int reqwidth,int reqheight){
		int insamplesize = 1;
		int width = options.outWidth;
		int height = options.outHeight;
		if(width > reqwidth || height > reqheight){
			int widhtRadio = Math.round(width * 1.0f / reqwidth);
			int heightRadio = Math.round(height * 1.0f / reqheight);
			insamplesize = Math.max(widhtRadio, heightRadio);
		}
		return insamplesize;
	}

}
