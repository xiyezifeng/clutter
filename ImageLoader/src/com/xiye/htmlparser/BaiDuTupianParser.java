package com.xiye.htmlparser;

import java.net.URL;
import java.net.URLConnection;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import android.util.Log;

import com.xiye.imageloader.ImageLoader;

public class BaiDuTupianParser {
	
	private Parser parser ;
	private String parserUrl ;
	private static BaiDuTupianParser baiDuTupianParser;
	private callback callback;
	
	private BaiDuTupianParser(String url,callback name) throws ParserException{
		parserUrl = url;
		callback = name;
		doInBack();
	}
	
	public static BaiDuTupianParser getInstense(String url,callback name) throws ParserException{
		if(null == baiDuTupianParser){
			synchronized (ImageLoader.class) {
				if(null == baiDuTupianParser){
					baiDuTupianParser = new BaiDuTupianParser(url,name);
				}
			}
		}
		return baiDuTupianParser;
	}
	
	public void reload(){
		doInBack();
	}
	
	/**
	 * 解析
	 */
	private void doInBack() {
		Log.i("xiye", "解析开始");
		new Thread(){
			@Override
			public void run() {
				try {
					URLConnection connection =  new URL(parserUrl).openConnection();
					parser = new Parser(connection);
					parser.setEncoding("utf-8");
					TagNameFilter nameFilter = new TagNameFilter(){
						@Override
						public boolean accept(Node node) {
							if(node.getText().startsWith("img src=")){
								return true;
							}else{
								return false;
							}
						}
					};
					HasAttributeFilter attributeFilter = new HasAttributeFilter("class", "photo_wrap"){
						@Override
						public boolean accept(Node arg0) {
							if(arg0.getText().startsWith("img src=")){
								return true;
							}else{
								return false;
							}
						}
					};
					NodeList list = parser.extractAllNodesThatMatch(attributeFilter);
					if(list != null){
						for (int i = 0; i < list.size(); i++) {
							Node node = list.elementAt(i);
							if(regularString(node.getText())){
//								Message msg = Message.obtain();
								String str = splitStr(node.getText());
//								msg.obj = str;
								callback.callbackmesg(str);
							}
						}
					}else{
						Log.i("xiye", "无内容");
					}
					
				} catch (Exception e) {
					Log.i("xiye", "解析类，解析出错 ");
				}
			}
		}.start();
	}
	
	/**
	 * 连接中含有指定字符串
	 * @param str
	 * @return
	 */
	private boolean regularString(String str){
		return str.contains("photo");
	}
	
	/**
	 * 裁剪， 取出""中的连接
	 * @param str
	 * @return
	 */
	private String splitStr(String str){
		int start = str.indexOf("\"");
		int end	  = str.lastIndexOf("\"");
		return str.substring(start+1, end);
	}
	
	public interface callback{
		public void callbackmesg(String images);
	}
}
