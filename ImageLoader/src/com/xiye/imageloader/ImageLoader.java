package com.xiye.imageloader;

import java.io.File;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

/**
 * @author xiye
 * @category util
 * @version 1
 * @meaning 图片读取，处理
 */
public class ImageLoader {
	private static ImageLoader loader;
	
	/**
	 * 加载模式
	 */
	private LoadType type;
	
	/**
	 * 线程池大小
	 */
	private int treadCount;
	
	/**
	 * 默认线程池大小
	 */
	private final int DEFAULT_COUNT = 1;
	
	/**
	 * 线程池
	 */
	private ExecutorService threadPool;
	
	/**
	 * 线程池handler
	 */
	private static Handler poolhandler;
	
	/**
	 * 后台轮训线程
	 */
	private Thread  poolThread;
	
	/**
	 * ui中handler
	 */
	private Handler uihandler;
	
	/**
	 * 任务队列
	 */
	private LinkedList<Runnable> mTaskQueue;
	
	/**
	 * 缓存(内存)
	 */
	private LruCache<String, Bitmap> cache;
	
	private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
	private Semaphore mSemaphoreThreadPool;
	
	private boolean openLocalCache = true;
	private final String TAG = "xiye";
	
	private static ImageLoader getInstense(){
		if(null == loader){
			synchronized (ImageLoader.class) {
				if(null == loader){
//					loader = new ImageLoader();
				}
			}
		}
		return loader;
	}
	
	private static ImageLoader getInstense(int treadCount,LoadType t){
		if(null == loader){
			synchronized (ImageLoader.class) {
				if(null == loader){
					loader = new ImageLoader(treadCount,t);
				}
			}
		}
		return loader;
	}
	
	private ImageLoader(int treadCount,LoadType t) {
		init(treadCount,t);
	}
	
	/**
	 * 初始化，使用默认构造
	 */
	public static ImageLoader getLoader(){
		return getInstense();
	}
	
	/**
	 * 初始化，使用用户配置
	 * @param treadCount 线程池大小
	 * @param t 加载模式
	 * @return 
	 */
	public static ImageLoader getLoader(int treadCount,LoadType t){
		return getInstense(treadCount,t);
	}
	
	private void init(int treadCount,LoadType t){
		initBackThread();
		setCacheMemory();
		mTaskQueue = new LinkedList<Runnable>();
		type = t;
		threadPool =Executors.newFixedThreadPool(treadCount);
		mSemaphoreThreadPool = new Semaphore(treadCount);
	}
	
	/**
	 * 后台轮训开启
	 */
	private void initBackThread(){
		poolThread = new Thread(){
			@Override
			public void run() {
				Looper.prepare();
				poolhandler = new Handler(){
					@Override
					public void handleMessage(Message msg) {
						//取出任务
						threadPool.execute(getTask());
						try {
							mSemaphoreThreadPool.acquire();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				};
				//释放一个信号量
//				mSemaphorePoolThreadHandler.release();
				Looper.loop();
			}
		};
		poolThread.start();
	}
	
	private void setCacheMemory(){
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheMemory = maxMemory/8;
		cache = new LruCache<String, Bitmap>(cacheMemory){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes()*value.getHeight();
			}
		};
	}
	
	/**
	 * @return 任务，从头/从尾
	 */
	private Runnable getTask(){
		if(type == LoadType.FIFO){
			return mTaskQueue.removeFirst();
		}else if(type == LoadType.LIFO){
			return mTaskQueue.removeLast();
		}
		return null;
	}
	
	/**
	 * @param runnable 加入任务
	 */
	private synchronized void addTask(Runnable runnable){
		mTaskQueue.add(runnable);	
		try {
			if(poolhandler != null){
//				mSemaphorePoolThreadHandler.acquire();
			}
		} catch (Exception e) {
		}
		poolhandler.sendEmptyMessage(0x10);
	}
	
	/**
	 * @param path 图片路径，网络，本地
	 * @param imageView 图片加载view
	 * @param isFromNet 是否从网络加载
	 * @return 任务
	 */
	private Runnable butildTask(final String path,final ImageView imageView,final boolean isFromNet){
		return new Runnable() {
			@Override
			public void run() {
				Bitmap bp = null;
				//判断是否从网络加载
				if(isFromNet){
					//判断存在于缓存目录
					File file = getFileFromLruCacheDir(imageView.getContext(),md5(path));
					if(file.exists()){
						bp = getBitMapFromLocal(file.getAbsolutePath(),imageView);
					}else{
						//判断是否开启硬盘缓存
						if(openLocalCache){
							//下载文件到指定目录
							boolean downState = Downloader.downFileFromNetUrl(path,file.getAbsolutePath());
							if(downState){
								//下载成功
								bp = getBitMapFromLocal(file.getAbsolutePath(),imageView);
							}
						}else{
								//直接从网上下载
								bp = Downloader.downBitMapFromUrl(path,imageView);
						}
					}
				}else{
				//从本地加载	path不做加密处理
					bp = getBitMapFromLocal(path,imageView);
				}
				//写入缓存
				addBitMapToLruCache(path, bp);
				//通知刷新UI
				refreshUI(path,imageView,bp);
				mSemaphoreThreadPool.release();
			}
		};
	}
	
	/**
	 * 刷新UI
	 * @param path
	 * @param imageview
	 * @param bp
	 */
	private void refreshUI(final String path,final ImageView imageview,final Bitmap bp){
		Message meg = Message.obtain();
		ImageBeanHolder holder = new ImageBeanHolder();
		holder.path = md5(path);
		holder.imageView = imageview;
		holder.bitmap  = bp;
		meg.obj = holder;
		uihandler.sendMessage(meg);
	}
	
	
	/**
	 * 加入到缓存中
	 * 需先判断是否不存在于缓存文件中。
	 * @param path 缓存文件路径
 	 * @param bp   缓存bitmap
	 * 
	 */
	private void addBitMapToLruCache(String path,Bitmap bp){
		if(null == getBitMapToLruCache(path)){
			if(null != bp){
				cache.put(md5(path), bp);
			}
		}
	}
	
	/**
	 * @param path key值
	 * @return value bitmap
	 */
	private Bitmap getBitMapToLruCache(String path){
		if(path != null){
			return cache.get(md5(path));
		}
		return null;
	}
	
	/**
	 * 读取图片，根据imageview的大小压缩
	 * @param path
	 * @param imageView
	 * @return
	 */
	private Bitmap getBitMapFromLocal(String path,ImageView imageView){
		Bitmap bitmap = null;
		//获取控件大小
		ImageSize size = ImageSizeUtil.getImageViewSize(imageView);
		//压缩图片 
		bitmap = decodeSampledBitmapFromPath(path,size.width,size.height);
		return bitmap;
	}
	
	/**
	 * 压缩图片
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	private Bitmap decodeSampledBitmapFromPath(String path,int width,int height){
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		
		options.inSampleSize  = ImageSizeUtil.calculateInSampleSize(options,width,height);
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}
	
	/**
	 * 获取文件缓存目录
	 * @param context
	 * @param path
	 * @return file
	 */
	private File getFileFromLruCacheDir(Context context,String path){
		File cacheDir = context.getCacheDir();
		return new File(cacheDir+File.separator+path);
	}
	
	/**
	 * MD5
	 * 加密
	 * @param path
	 * @return
	 */
	private String md5(String path){
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = path.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
	/**
	 * 核心函数，读取图片的时刻到了
	 * @param path
	 * @param imageView
	 * @param isframnet
	 */
	public void loadImage(String path,ImageView imageView,boolean isFromNet){
		//加入tag保证，图片不错位
		imageView.setTag(md5(path));
		if(null == uihandler){
			 uihandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					ImageBeanHolder beanHolder = (ImageBeanHolder) msg.obj;
					String path = beanHolder.path;
					ImageView imageView = beanHolder.imageView;
					Bitmap bitmap = beanHolder.bitmap;
					if(path.equals(imageView.getTag())){
						imageView.setImageBitmap(bitmap);
					}
				} 
			 };
		}
		Bitmap bitmap = getBitMapToLruCache(path);
		if(null != bitmap){
			refreshUI(path, imageView, bitmap);
		}else{
			addTask(butildTask(path,imageView,isFromNet));
		}
	}
}
