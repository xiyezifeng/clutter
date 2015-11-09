package com.xiye.imageutil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloadUtil {
	
	public static boolean downloadImage(String path,String filePath){
		boolean downloadOk = false;
		URL url = null;
		HttpURLConnection connection = null;
		InputStream in = null;
		FileOutputStream out = null;
		System.out.println("文件下载 : 路径  : " + path + ","  +"存放路径  : " + filePath);
		try {
			url = new URL(path);
			connection = (HttpURLConnection) url.openConnection();
			in = connection.getInputStream();
			out = new FileOutputStream(filePath);
			int len = 0;
			byte temp[] = new byte[1024];
			while ((len = in.read(temp))!=-1) {
				out.write(temp, 0 , len);
			}
			
		} catch (Exception e) {
			System.out.println("文件下载错误");
		}finally{
			try {
				connection.disconnect();
				in.close();
				out.close();
				File file = new File(path);
				if(file.exists()){
					downloadOk = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return downloadOk;
	}
}
