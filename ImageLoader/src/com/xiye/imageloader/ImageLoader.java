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
 * @meaning ͼƬ��ȡ������
 */
public class ImageLoader {
	private static ImageLoader loader;
	
	/**
	 * ����ģʽ
	 */
	private LoadType type;
	
	/**
	 * �̳߳ش�С
	 */
	private int treadCount;
	
	/**
	 * Ĭ���̳߳ش�С
	 */
	private final int DEFAULT_COUNT = 1;
	
	/**
	 * �̳߳�
	 */
	private ExecutorService threadPool;
	
	/**
	 * �̳߳�handler
	 */
	private static Handler poolhandler;
	
	/**
	 * ��̨��ѵ�߳�
	 */
	private Thread  poolThread;
	
	/**
	 * ui��handler
	 */
	private Handler uihandler;
	
	/**
	 * �������
	 */
	private LinkedList<Runnable> mTaskQueue;
	
	/**
	 * ����(�ڴ�)
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
	 * ��ʼ����ʹ��Ĭ�Ϲ���
	 */
	public static ImageLoader getLoader(){
		return getInstense();
	}
	
	/**
	 * ��ʼ����ʹ���û�����
	 * @param treadCount �̳߳ش�С
	 * @param t ����ģʽ
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
	 * ��̨��ѵ����
	 */
	private void initBackThread(){
		poolThread = new Thread(){
			@Override
			public void run() {
				Looper.prepare();
				poolhandler = new Handler(){
					@Override
					public void handleMessage(Message msg) {
						//ȡ������
						threadPool.execute(getTask());
						try {
							mSemaphoreThreadPool.acquire();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				};
				//�ͷ�һ���ź���
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
	 * @return ���񣬴�ͷ/��β
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
	 * @param runnable ��������
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
	 * @param path ͼƬ·�������磬����
	 * @param imageView ͼƬ����view
	 * @param isFromNet �Ƿ���������
	 * @return ����
	 */
	private Runnable butildTask(final String path,final ImageView imageView,final boolean isFromNet){
		return new Runnable() {
			@Override
			public void run() {
				Bitmap bp = null;
				//�ж��Ƿ���������
				if(isFromNet){
					//�жϴ����ڻ���Ŀ¼
					File file = getFileFromLruCacheDir(imageView.getContext(),md5(path));
					if(file.exists()){
						bp = getBitMapFromLocal(file.getAbsolutePath(),imageView);
					}else{
						//�ж��Ƿ���Ӳ�̻���
						if(openLocalCache){
							//�����ļ���ָ��Ŀ¼
							boolean downState = Downloader.downFileFromNetUrl(path,file.getAbsolutePath());
							if(downState){
								//���سɹ�
								bp = getBitMapFromLocal(file.getAbsolutePath(),imageView);
							}
						}else{
								//ֱ�Ӵ���������
								bp = Downloader.downBitMapFromUrl(path,imageView);
						}
					}
				}else{
				//�ӱ��ؼ���	path�������ܴ���
					bp = getBitMapFromLocal(path,imageView);
				}
				//д�뻺��
				addBitMapToLruCache(path, bp);
				//֪ͨˢ��UI
				refreshUI(path,imageView,bp);
				mSemaphoreThreadPool.release();
			}
		};
	}
	
	/**
	 * ˢ��UI
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
	 * ���뵽������
	 * �����ж��Ƿ񲻴����ڻ����ļ��С�
	 * @param path �����ļ�·��
 	 * @param bp   ����bitmap
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
	 * @param path keyֵ
	 * @return value bitmap
	 */
	private Bitmap getBitMapToLruCache(String path){
		if(path != null){
			return cache.get(md5(path));
		}
		return null;
	}
	
	/**
	 * ��ȡͼƬ������imageview�Ĵ�Сѹ��
	 * @param path
	 * @param imageView
	 * @return
	 */
	private Bitmap getBitMapFromLocal(String path,ImageView imageView){
		Bitmap bitmap = null;
		//��ȡ�ؼ���С
		ImageSize size = ImageSizeUtil.getImageViewSize(imageView);
		//ѹ��ͼƬ 
		bitmap = decodeSampledBitmapFromPath(path,size.width,size.height);
		return bitmap;
	}
	
	/**
	 * ѹ��ͼƬ
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
	 * ��ȡ�ļ�����Ŀ¼
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
	 * ����
	 * @param path
	 * @return
	 */
	private String md5(String path){
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = path.getBytes();
            // ���MD5ժҪ�㷨�� MessageDigest ����
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // ʹ��ָ�����ֽڸ���ժҪ
            mdInst.update(btInput);
            // �������
            byte[] md = mdInst.digest();
            // ������ת����ʮ�����Ƶ��ַ�����ʽ
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
	 * ���ĺ�������ȡͼƬ��ʱ�̵���
	 * @param path
	 * @param imageView
	 * @param isframnet
	 */
	public void loadImage(String path,ImageView imageView,boolean isFromNet){
		//����tag��֤��ͼƬ����λ
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
