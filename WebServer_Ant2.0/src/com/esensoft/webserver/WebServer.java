package com.esensoft.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * WebServer类对象模拟web服务器，监听特定端口的http请求并进行处理返回响应结果。
 * @author 成亚雄
 *
 */
public class WebServer{
	/*
	 * ServerSocket对象，用来监听特定端口的请求
	 */
    ServerSocket server;
    
    /*
     * 监听端口
     */
    int port;
    
	/*
     * 服务器状态，初始为未开启
     */
	boolean run=false;
	
	/*
	 * ExecutorService对象用来管理新建线程的执行
	 */
	ExecutorService executor;
	
	/*
	 * 服务器存放文件的根目录,定义为静态变量，方便HttpResponse类寻找文件时使用
	 */
	public static String rootpath="D:\\WebServer";
    //./WebServer

	/*
	 * 含参构造函数，需要指定port变量值，将run赋值为true，代表服务器开启，
	 * 同时初始化serverSocket
	 * 并且创建缓存线程池来提供多线程服务
	 */
	public WebServer(int port) {
		this.port=port;
		run=true;
		executor=Executors.newCachedThreadPool();
		
		try {
			server=new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ServerSocket初始化失败！");
		}
		finally{
			try {
				//do free
				if(server!=null)
					server.close();
				executor.shutdown();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	

	/*
	 * web服务器处于工作状态下执行的方法，即监听端口请求并作相应处理
	 */
	public void service(){
		int id=0;
		while(run){
			Socket s=null;
			try {
				s=server.accept();
				Client client=new Client(s, id++);
//				new Thread(client).start();
				executor.execute(client);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				try {
					//do free
					if(s!=null)
						s.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	public static void main(String[] args) {
		WebServer webServer=new WebServer(8081);
		webServer.service();
	}
	
}


