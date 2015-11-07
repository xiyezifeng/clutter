package com.xiye.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyCircleImageView extends ImageView{

	/**
	 * ͼƬ�İ뾶
	 */
	private static float drawableRadius;
	
	/**
	 * bitmap ����
	 */
	private static Paint bitmapPaint = new Paint();
	
	/**
	 * ���ص�bitmap
	 */
	private static Bitmap bitmap;
	
	/**
	 * bitmap ��Ⱦ
	 */
	private static Shader bitmapShader;
	
	/**
	 * ͼ�ξ���
	 */
	private RectF drawableRect = new RectF();
	
	/**
	 * ����ʱ����
	 */
	private Matrix matrix = new Matrix();
	
	/**
	 * �߿����
	 */
	private RectF borderRect = new RectF();
	
	
	/**
	 * �߿�뾶����bitmap�İ뾶�� borderWidth
	 */
	private static float borderRadius;
	
	/**
	 * �߿�뾶
	 */
	private int borderWidth = 2;
	
	/**
	 * border��ɫ
	 */
	private int borderColor = Color.RED;
	
	/**
	 * border ����
	 */
	private static Paint borderPaint = new Paint();
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(getDrawable() == null){
			return;
		}
		//����Բ��ͼƬ cx,cy Բ�� , radius�뾶 ,paint ����
		canvas.drawCircle(getWidth()/2 , getHeight()/2, drawableRadius, bitmapPaint);
		canvas.drawCircle(getWidth()/2,  getHeight()/2,borderRadius, borderPaint);
	}
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		refresh();
	}
	
	/**
	 * ˢ��ͼƬ����Ҫ����뾶��ͼ�ξ����С��ͼƬѹ��������ͼ�μ���ƫ����
	 */
	private void refresh(){
		if(null == bitmap){
			return;
		}
		if(getWidth() == 0 || getHeight() == 0){
			return;
		}
		bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
		//ͼ����Ⱦ
		bitmapPaint.setShader(bitmapShader);
		//�����
		bitmapPaint.setAntiAlias(true);
		
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setColor(borderColor);
		borderPaint.setStrokeWidth(borderWidth);
		
		//��СΪ��ǰͼƬ���ֵĴ�С
		borderRect.set(0, 0, getWidth()-borderWidth, getHeight()-borderWidth);
		borderRadius = Math.min(borderRect.width()/2.0f, borderRect.height()/2.0f);
		drawableRect.set(borderRect);	
		//����뾶
//		drawableRadius = (drawableRect.width()>drawableRect.height())?getHeight()/2:getWidth()/2;
		drawableRadius = Math.min((drawableRect.width())/2.0f, (drawableRect.height())/2.0f);
		updateMatrix();
		//ˢ��
		invalidate();
	}
	/**
	 * ����bitmap���ر���,����,ƫ����
	 */
	private void updateMatrix(){
		float scale = 1;
		//ƫ����
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
