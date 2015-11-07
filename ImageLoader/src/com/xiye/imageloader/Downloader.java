package com.xiye.imageloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * @author xiye
 * @category util
 * @version 1
 * @meaning Õº∆¨œ¬‘ÿ 
 */
public class Downloader {
	
	public static boolean downFileFromNetUrl(String path,String fileDir){
		URL url;
		HttpURLConnection urlConn = null;
		InputStream in = null;
		FileOutputStream out = null;
		try {
			url = new URL(path);
			urlConn = (HttpURLConnection) url.openConnection();  
			urlConn.connect();
			in = urlConn.getInputStream();
			out = new FileOutputStream(fileDir);
			byte[] buffer = new byte[2048];
			int len = 0 ;
			while((len = in.read(buffer)) !=-1){
				out.write(buffer,0, len);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				in.close();
				out.flush();
				out.close();
				urlConn.disconnect();
			} catch (Exception e2) {
			}
			
		}
		File file = new File(fileDir);
		if (file.exists()) {
			return true;
		}
		return false;
	}
	
	public static Bitmap downBitMapFromUrl(String path,ImageView ImageView){
		Bitmap bitmap = null;
		URL url;
		HttpURLConnection urlConn = null;
		InputStream in = null;
		try {
			url = new URL(path);
			urlConn = (HttpURLConnection) url.openConnection();  
			urlConn.connect();
			in = new BufferedInputStream( urlConn.getInputStream() );
			in.mark(in.available());
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			
			ImageSize size = ImageSizeUtil.getImageViewSize(ImageView);
			int samplesize = ImageSizeUtil.calculateInSampleSize(options, size.width, size.height);
			options.inSampleSize = samplesize;
			options.inJustDecodeBounds = false;
			in.reset();
			bitmap = BitmapFactory.decodeStream(in, null, options);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
				urlConn.disconnect();
			} catch (Exception e2) {
			}
		}
		return bitmap;
	}
	
	
}
