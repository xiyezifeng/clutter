package com.xiye.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author xiye
 * Բ��imageview
 */
public class CircleView extends ImageView{
	
	/**
	 * ��ͼbitmap
	 */
	private Bitmap bitmap;
	
	/**
	 * bitmap ����
	 */
	private Paint drawPaint = new Paint();
	
	/**
	 * ��Ⱦ
	 */
	private Shader drawShader;
	
	/**
	 * �뾶
	 */
	private float drawRadius;
	
	/**
	 * �����С
	 */
	private RectF drawRect = new RectF();
	
	/**
	 * bitmap ��Ⱦ����
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
		// TODO Auto-generated method stub
		this.bitmap = bm;
		setup();
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		super.setImageDrawable(drawable);
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
		//����ʹ��float�н� bitmap�Ŀ�ߣ���ȻscaleΪ0
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
