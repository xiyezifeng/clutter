package com.xiye.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyCircleImageView extends ImageView{

	/**
	 * Í¼Æ¬µÄ°ë¾¶
	 */
	private static float drawableRadius;
	
	/**
	 * bitmap »­±Ê
	 */
	private static Paint bitmapPaint = new Paint();
	
	/**
	 * ¼ÓÔØµÄbitmap
	 */
	private static Bitmap bitmap;
	
	/**
	 * bitmap äÖÈ¾
	 */
	private static Shader bitmapShader;
	
	/**
	 * Í¼ÐÎ¾ØÕó
	 */
	private RectF drawableRect = new RectF();
	
	/**
	 * ¼ÓÔØÊ±¾ØÕó
	 */
	private Matrix matrix = new Matrix();
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(getDrawable() == null){
			return;
		}
		//»­³öÔ²ÐÎÍ¼Æ¬ cx,cy Ô²ÐÄ , radius°ë¾¶ ,paint »­±Ê
		canvas.drawCircle(getWidth()/2 , getHeight()/2, drawableRadius, bitmapPaint);
	}
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		refresh();
	}
	
	/**
	 * Ë¢ÐÂÍ¼Æ¬£¬ÐèÒª¼ÆËã°ë¾¶£¬Í¼ÐÎ¾ØÕó´óÐ¡£¬Í¼Æ¬Ñ¹Ëõ±ÈÀý£¬Í¼ÐÎ¼ÓÔØÆ«ÒÆÁ¿
	 */
	private void refresh(){
		if(null == bitmap){
			return;
		}
		if(getWidth() == 0 || getHeight() == 0){
			return;
		}
		bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
		//Í¼ÐÎäÖÈ¾
		bitmapPaint.setShader(bitmapShader);
		//·´¾â³Ý
		bitmapPaint.setAntiAlias(true);
		//´óÐ¡Îªµ±Ç°¿Ø¼þµÄ´óÐ¡
		drawableRect.set(0, 0, getWidth(), getHeight());	
		//¼ÆËã°ë¾¶
//		drawableRadius = (drawableRect.width()>drawableRect.height())?getHeight()/2:getWidth()/2;
		drawableRadius = Math.min(getWidth()/2.0f, getHeight()/2.0f);
		updateMatrix();
		//Ë¢ÐÂ
		invalidate();
	}
	/**
	 * ¼ÆËãbitmap¼ÓÔØ±ÈÀý,¾ØÕó,Æ«ÒÆÁ¿
	 */
	private void updateMatrix(){
		float scale = 1;
		//Æ«ÒÆÁ¿
		float tx = 0 ;
		float ty = 0 ;
		matrix.set(null);
		float bitmapH = bitmap.getHeight();
		float bitmapW = bitmap.getWidth();
		if(bitmapW > bitmapH){
			scale = drawableRect.height()/bitmapH;
			tx = (drawableRect.width()-bitmapW*scale)*0.5f;
		}else{
			scale = drawableRect.width()/bitmapW;
			ty = (drawableRect.height()-bitmapH*scale)*0.5f;
		}
		matrix.setScale(scale, scale);
		matrix.postTranslate(tx, ty);
		bitmapShader.setLocalMatrix(matrix);
	}
	
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		super.setImageBitmap(bm);
		bitmap = bm;
	}
	
	public MyCircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
}
