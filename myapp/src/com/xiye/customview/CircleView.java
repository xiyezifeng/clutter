package com.xiye.customview;

import com.xiye.imageutil.ImageSize;
import com.xiye.imageutil.ImageSizeUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author xiye
 * 圆形imageview
 */
public class CircleView extends ImageView{
	
	/**
	 * 绘图bitmap
	 */
	private Bitmap bitmap;
	
	/**
	 * bitmap 画笔
	 */
	private Paint drawPaint = new Paint();
	
	/**
	 * 渲染
	 */
	private Shader drawShader;
	
	/**
	 * 半径
	 */
	private float drawRadius;
	
	/**
	 * 矩阵大小
	 */
	private RectF drawRect = new RectF();
	
	/**
	 * bitmap 渲染矩阵
	 */
	private Matrix matrix = new Matrix();
	
	

	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(null == bitmap||null == getDrawable()){
			return;
		}
		canvas.drawCircle(getWidth()/2, getHeight()/2, drawRadius, drawPaint);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		this.bitmap = null;
		this.bitmap = bm;
		setup();
		super.setImageBitmap(bm);
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		
//		System.out.println("Drawable转Bitmap");
		/*int w = drawable.getIntrinsicWidth();
	    int h = drawable.getIntrinsicHeight();
		Bitmap.Config config = 
		 drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
		 : Bitmap.Config.RGB_565;
		this.bitmap = Bitmap.createBitmap(w,h,config);*/
//		setup();
		
		super.setImageDrawable(drawable);
	}
	
	@Override
	public void setImageResource(int resId) {
		Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(),resId);
		ImageSize size = new ImageSize();
		size.sizeH = getHeight();
		size.sizeW = getWidth();
		option.inSampleSize = ImageSizeUtil.getinSampleSize(option, size);
		option.inJustDecodeBounds = false;
		this.bitmap = BitmapFactory.decodeResource(getResources(),resId,option);
		setup();
		super.setImageResource(resId);
	}
	
	private void setup(){
		if(null == bitmap){
			return;
		}
	
		if(getWidth() == 0 || getHeight() == 0){
			return;
		}
		drawShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
		drawPaint.setAntiAlias(true);
		drawPaint.setShader(drawShader);
		
		drawRect.set(0, 0, getWidth(), getHeight());
		drawRadius = Math.min(drawRect.width()/2,drawRect.height()/2);
		mackShaderMatrix();
		
		invalidate();
	}
	
	private void mackShaderMatrix(){
		float scale = 1.0f;
		float dx = 0;
		float dy = 0;
		//必须使用float承接 bitmap的宽高，不然scale为0
		float bitmapH,bitmapW = 1.0f;
		bitmapH = bitmap.getHeight();
		bitmapW = bitmap.getWidth();
		if(bitmap.getHeight()>bitmap.getWidth()){
			scale = (getWidth() / bitmapW)*1.0f;
			dy = 0.5f*(getHeight() - bitmapH*scale);
		}else{
			scale = (getHeight() /bitmapH)*1.0f;
			dx = 0.5f*(getWidth() - bitmapW*scale);
		}
		matrix.setScale(scale, scale);
		matrix.postTranslate(dx, dy);
		drawShader.setLocalMatrix(matrix);
	}
}
