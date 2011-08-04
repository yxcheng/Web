package com.esensoft.webserver.junit;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import com.esensoft.webserver.HttpHandler;
import com.esensoft.webserver.WebServer;

/*
 * 这个类用来辅助TestHandler，根据实际需要将一些重复度高的代码提取出来，
 * 使得TestHandler的代码简洁清晰
 */
public class Help {
	
	

	/*
	 * 这个方法用来辅助TestHandler中的testSendHead(),能够减少代码的重复
	 * 里面调用了HttpHandler的sendHead()方法
	 * sendHead()方法的参数为File类型，不能为空，否则会出现FileNotFoundException
	 * 因此传入的url必须是存在的
	 */
	public static boolean HelpTestHead(HttpHandler httpHandler,String url){
		httpHandler.setUrl(url);
		httpHandler.parseType(httpHandler.getUrl());
		File src=new File(WebServer.rootpath,httpHandler.getUrl());
		return httpHandler.sendHead(src);

	}
	
	/*
	 * 这个方法用来辅助TestHandler中的testSendFile，能够减少代码的重复
	 * 里面调用了HttpHandler的sendFile()方法
	 * sendFile()方法的参数为File类型，不能为空，否则会出现FileNotFoundException
	 * 因此传入的url必须是存在的
	 */
	public static boolean HelpTestFile(String url){
		boolean sign=false;
		OutputStream out=null;
		try {
			out = new FileOutputStream(new File("D:\\test_src",url));
			HttpHandler httpHandler=new HttpHandler(null, out);
			httpHandler.setUrl(url);
			File src=new File(WebServer.rootpath,httpHandler.getUrl());
			sign=httpHandler.sendFile(src);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(out!=null)
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return sign;
	}
	
	/*
	 * 这个方法用来辅助TestHandler中的testReturnFile方法。
	 */
	public static boolean HelpTestReturn(HttpHandler httpHandler,String url){
		httpHandler.setUrl(url);
		httpHandler.parseType(httpHandler.getUrl());
		boolean sign=httpHandler.returnFile();
		String changeLine="\r\n\r\n\r\n\r\n";
		try {
			httpHandler.getOutputStream().write(changeLine.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sign;
	}
}
