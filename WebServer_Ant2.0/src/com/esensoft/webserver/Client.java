package com.esensoft.webserver;

import java.io.IOException;
import java.io.*;
import java.net.Socket;

/**
 * Client类用来封装socket，同时实现runnable接口，用来提供多线程访问
 * 每一个Client对象都有一个编号
 * 可以获取浏览器端请求并对其进行分析判断，从而返回响应
 * @author 成亚雄
 *
 */
public class Client implements Runnable{
	/*
	 * socket对象
	 */
	Socket socket;
	
	/*
	 * client编号
	 */
	int id;
	
	/*
	 * 带参构造函数
	 */
	public Client(Socket socket, int id) {
		super();
		this.socket = socket;
		this.id = id;
	}

	@Override
	/*
	 * 线程执行函数
	 */
	public void run() {
		InputStream in=null;
		OutputStream out=null;
		try {
			in=socket.getInputStream();
			try{
				out=socket.getOutputStream();
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			

			HttpHandler httpHandler=new HttpHandler(in, out);
			httpHandler.handle();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				//do free
				if(out!=null)
					out.close();
				if(in!=null)
					in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		
	
}
