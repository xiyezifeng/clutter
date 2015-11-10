package com.xiye.imageutil;

import java.io.Externalizable;
import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import com.xiye.customview.CircleView;
import com.xiye.model.Demo_1_Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.renderscript.Element;
import android.util.LruCache;

public class EasyImage {
	private static EasyImage easyImage;

	public static EasyImage getInstance(int corePoolSize){
		if(null == easyImage){
			synchronized (EasyImage.class) {
				if(null == easyImage){
					easyImage = new EasyImage(corePoolSize);
				}
			}
		}
		return easyImage;
	}
	
	private EasyImage(int corePoolSize) {
		doBackThread();
		threadpoll = Executors.newScheduledThreadPool(corePoolSize);
		int maxCache = (int) Runtime.getRuntime().maxMemory();
		int cache = maxCache/8;
		imagecache = new LruCache<String, Bitmap>(cache){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes()*value.getHeight();
			}
		};
		semaphore = new  Semaphore(corePoolSize);
		
	}
	
	private Handler pollHandler;
	
	private Thread  pollThread;
	
	private final static  int DEFAULT_THREADCOUTN = 1;
	
	private ExecutorService threadpoll;
	
	private LruCache<String, Bitmap> imagecache;
	
	private LinkedList<Runnable> task = new LinkedList<Runnable>();
	
	private Handler uihandler;
	
	private Semaphore semaphore ;

	private Semaphore handlerSemaphore = new Semaphore(0);
	
	
	/**
	 * 开启线程轮训
	 */
	private void doBackThread(){
		pollThread = new Thread(){
			@Override
			public void run() {
				Looper.prepare();
				pollHandler = new Handler(){
					@Override
					public void handleMessage(Message msg) {
						threadpoll.execute(getTask());
						try {
							semaphore.acquire();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				};
//				handlerSemaphore.release();
				Looper.loop();
			}
		};
		pollThread.start();
	}
//线程池
	
	private Runnable getTask(){
		return task.removeLast();
	}
	
	private Bitmap getBitmapFromLrucache(String key){
		return imagecache.get(key);
	}
	
	private void addBitmapToLrucache(String key,Bitmap bm){
		if(key != null && null != bm){
			imagecache.put(key, bm);
		}
	}
	
	private void addTask(String path,CircleView imageview){
		
		task.add(buildTask(path,imageview));
		try {
//			handlerSemaphore.acquire();
		} catch (Exception e) {
			
		}
		pollHandler.sendEmptyMessage(0x10);
	}
	
	private void refreshImage(String path,Bitmap bm,CircleView imageview){
		Demo_1_Model model = new Demo_1_Model();
		model.bm = bm;
		model.imageview = imageview;
		model.image = path;
		Message message = Message.obtain();
		message.obj = model;
		uihandler.sendMessage(message);
	}
	
	private Runnable buildTask(final String path,final CircleView imageview){
		return new Runnable() {
			@Override
			public void run() {
				//都是从网上下载的所以不需要判断来源
				//判断文件缓存
				File cacheFile = getCacheFromPath(MD5.md5(path),imageview);
				Bitmap bm = null;
				if(cacheFile.exists()){
					bm = getBitmapFromCacheFile(cacheFile.getAbsolutePath(),imageview);
				}else{
					//下载
					boolean isOk = ImageDownloadUtil.downloadImage(path,cacheFile.getAbsolutePath());
					if(isOk){
						bm = getBitmapFromCacheFile(cacheFile.getAbsolutePath(),imageview);
					}else{
					}
				}
				addBitmapToLrucache(MD5.md5(path), bm);
				refreshImage(path, bm, imageview);
				try {
					semaphore.release();
				} catch (Exception e) {
				}
			}
		};
	}
	
	public void loadImage(String path,CircleView imageview){
		imageview.setTag(MD5.md5(path));
		if(null == uihandler){
			uihandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					Demo_1_Model model = (Demo_1_Model) msg.obj;
					String path = model.image;
					Bitmap bitmap = model.getBm();
					CircleView view = model.getImageview();
					if(MD5.md5(path).equals(view.getTag())){
						view.setImageBitmap(bitmap);
					}
				}
			};
		}
		
		//从缓存中读取
		Bitmap bitmap = getBitmapFromLrucache(MD5.md5(path));
		
		if(null != bitmap){
			//压缩加载
			refreshImage(path,bitmap,imageview);
		}else{
			//无缓存，下载
			addTask(path,imageview);
		}
	}
	
	private Bitmap getBitmapFromCacheFile(String path,CircleView view){
		ImageSize size = ImageSizeUtil.getImageSize(view);
//		System.out.println("view 尺寸  size : 高" + size.sizeH +", 宽"+ size.sizeW);
		
		Bitmap bitmap = null;
		Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bitmap  = BitmapFactory.decodeFile(path);
		
		options.inSampleSize = ImageSizeUtil.getinSampleSize(options, size);
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(path, options);
		
		return bitmap;
	}
	
	private File getCacheFromPath(String path, CircleView imageview){
		File rootPaht = imageview.getContext().getCacheDir();
		File cacheFile = new File(rootPaht+File.separator+path);
		return cacheFile;
	}
	
}
