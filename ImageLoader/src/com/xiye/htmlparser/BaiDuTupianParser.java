package com.xiye.htmlparser;

import java.net.URL;
import java.net.URLConnection;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import android.os.Handler;
import android.util.Log;

import com.xiye.imageloader.ImageLoader;

public class BaiDuTupianParser {
	
	private Parser parser ;
	private String parserUrl ;
	private static BaiDuTupianParser baiDuTupianParser;
	private Handler fragmentHandler;
	private Thread backThread;
	
	private BaiDuTupianParser(String url,Handler handler) throws ParserException{
		parserUrl = url;
		fragmentHandler = handler;
		doInBack();
	}
	
	public static BaiDuTupianParser getInstense(String url,Handler handler) throws ParserException{
		if(null == baiDuTupianParser){
			synchronized (ImageLoader.class) {
				if(null == baiDuTupianParser){
					baiDuTupianParser = new BaiDuTupianParser(url,handler);
				}
			}
		}
		return baiDuTupianParser;
	}
	
	/**
	 * 解析
	 */
	private void doInBack() {
		Log.i("xiye", "解析开始");
		backThread = new Thread(){
			@Override
			public void run() {
				try {
					URLConnection connection =  new URL(parserUrl).openConnection();
					parser = new Parser(connection);
					NodeFilter filter = new TagNameFilter("thumbURL");
					NodeList list = parser.extractAllNodesThatMatch(filter);
					if(list != null){
						for (int i = 0; i < list.size(); i++) {
							Node node = list.elementAt(i);
							Log.i("xiye", node.getText());
						}
						
					}else{
						Log.i("xiye", "无内容");
					}
					
				} catch (Exception e) {
					Log.i("xiye", "解析类，解析出错 ");
				}
			}
		};
		backThread.start();
	}
	

}
