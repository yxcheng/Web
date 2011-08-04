package com.esensoft.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个HttpHandler对象服务于一个HTTP连接线程，它负责处理从解析HTTP请求信息
 * 到根据信息做决策到查找读取文件并将其发送回浏览器的所有业务逻辑。
 * 负责解析HTTP请求的方法有parse(),parseUrl(),parseType()
 * 根据解析结果进行分析处理的方法有parseContentType()
 * 响应浏览器请求返回响应的方法是returnFile(),其中调用了一下两个方法
 * sendHead()方法用来发送HTTP响应头
 * sendFile()方法用来读取文件并写到到输出流里面
 * 为了降低耦合，便于测试，将socket对象的InputStream和OutputStream引用
 * 传递给HttpHandler对象实例，其余的处理全部在HttpHandler内部进行
 * @author 成亚雄
 *
 */
public class HttpHandler {
	
    /*
     * Logger对象负责日志工作
     */
	private static Logger log = LoggerFactory.getLogger(HttpHandler.class);   

	/*
     * 在浏览器端一个请求对应一个socket，为保存请求信息，需要获取请求所对应socket
     * 为方便测试，降低耦合，将socket对象的输入流作为成员
     */
	private InputStream in;
	
	/*
	 * 指定请求所对应的socket对象的OutputStream，从而使用该输出流向浏览器端输出
	 * 响应结果
	 */
	OutputStream out;
	
	/*
     * 保存请求中的url
     */
	private String url;
	
	/*
	 * 请求文件类型，例如html，jpg等
	 */
	private String fileType;
	
	/*
	 *  contentType根据请求资源的类型来决定返回文件的ContentType
	 */
	private String contenType;
	
	/*
	 * 根据用户在浏览器输入的内容
	 * 用来判断向浏览器输出index.html或者404.html或者后缀提示页面
	 * 1发送index.html 2发送后缀提示页面
	 * 默认为0  根据请求文件查找并返回结果
	 */
	private int key;


	
	/*
	 * 带参的构造函数，需要传入一个InputStream和一个OutputStream对象的引用
	 */
	public HttpHandler(InputStream in, OutputStream out) {
		super();
		this.in = in;
		this.out = out;
	}
	
	
	
	/*
	 * 获取HttpHandler的url
	 */
	public String getUrl() {
		return url;
	}

	/*
	 * 设置HttpHandler的url，用于测试
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/*
	 * 获取HttpHandler的fileType
	 */
	public String getFileType() {
		return fileType;
	}




	/*
	 * 获取HttpHandler的contentType
	 */
	public String getContenType() {
		return contenType;
	}
	
	/*
	 * 获取httpHandler的key，用于测试
	 */
	public int getKey() {
		return key;
	}
	
	/*
	 * 获取httpHandler的OutputStream,测试时用到
	 */
	public OutputStream getOutputStream(){
		return this.out;
	}
	/*
	 * 设置httpHandler的OutputStream，用于测试
	 */
	public void setOutputStream(OutputStream out){
		this.out=out;
	}
	
	/*
	 * parse方法根据从socket对象获取的输入流来截取http请求中的所有信息，例如url等
	 * 并保存在一个字符串中
	 */
	public void parse(){
		StringBuffer info = new StringBuffer();
		BufferedReader br=new BufferedReader(new InputStreamReader(in));
		String line;
/*	    int flag;
	    byte[] buffer = new byte[1024];
*/
	    try {
	       line=br.readLine();
	       line=line.replaceAll("%20", " ");
//	       line=URLDecoder.decode(line, "UTF-8");
//	       System.out.println(line);
		   this.parseUrl(line);
		   this.parseType(url);
		   log.trace("请求头第一行为：{}",line);
		   log.trace("解析出的url为：{}",this.getUrl());
		   log.trace("文件类型为：{}",this.getFileType());
	     while(line!=null&&(!"".equals(line))){
//	    	 System.out.println(line);
	    	 info.append(line);
	    	 line=br.readLine();
	     }
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	 
	    
/*	    
	    System.out.print(info.toString());
	    System.out.println(this.getUrl());
	    System.out.println(this.getFileType());*/
	    
	}
	
	/*
	 * 在HTTP请求的第一行中查找出URL
	 * 经过HttpWatch及eclipse控制台输出测试，在IE、FireFox、Chrome等浏览器中
	 * 发送的请求第一行均由"请求类型+URL+HTTP协议版本"组成
	 * 例如GET /2.txt HTTP/1.1
	 * 如要截取URL，需要找出字符串中第一个空格和HTTP字符串的位置，将中间的url取出来
	 * 需要说明的是，只要发送了HTTP请求，那么url不可能为空
	 */
	public void parseUrl(String info){
		if(info==null)
			url="";
		else {
			int begin, end;
			begin = info.indexOf(' ');
		    if(begin!=-1){
		    	end=info.indexOf("HTTP",begin+1);
		    	if(end>begin)
		    		url= info.substring(begin+2, end-1);    	
		    	}
		    }
	}
	
	/*
	 * 从url中获取请求资源的类型信息并赋值给httpHandler对象的fileType
	 * 首先判断url中是否包含内容，对于GET / HTTP/1.1这样的请求头，其中
	 * 不包含URL信息，及浏览器端未请求任何资源，则截取的url不含任何内容
	 * 这种情况服务器返回index.html给予用户一些提示
	 * 如果url中包含内容，则从中根据截取文件后缀名保存起来，这根据url中最后一个'.'
	 * 的位置来判断
	 * 如果url中不包含后缀名，则认为用户可能遗漏后缀名
	 */
	public void parseType(String url){
		if("".equals(url))
			fileType="nofile";
		else{
			int index=url.lastIndexOf('.');
			if(index!=-1&&index!=(url.length()-1)){
				fileType=url.substring(index+1);
			}
			else fileType="nopostfix";
		}
	}
	
	/*
	 * 根据HTTP请求的文件类型决定返回的Content-Type。
	 * 这一点对于浏览器端的显示效果极其重要
	 * 默认情况下是text/html 当请求文件是图片或者txt文档时需要改变
	 */
	public void parseContentType(String type){
		contenType="text/html;charset=GB2312";
		if("nofile".equals(type))
			key=1;
		else if("nopostfix".equals(type))
			key=2;
		else if(type!=null){
			key=0;
			if("jpg".equals(type)||"jpe".equals(type)||"jpeg".equals(type)
			   ||"asc".equals(type))
				contenType="image/jpeg";
			else if("png".equals(type))
				contenType="image/png";
			else if("txt".equals(type)||"asc".equals(type)||"c".equals(type)||
					"cpp".equals(type)||"h".equals(type))
				contenType="text/plain";
			else if("mp3".equals(type))
				contenType="audio/mpeg";
			else contenType="text/html;charset=GB2312";
		}
		log.trace("ContentType为：{}",this.getContenType());
//		System.out.println(contenType);
	}
	
	/*
	 * 根据HTTP请求的url以及其他信息查找请求文件
	 * 首先根据key值判断，进行第一步筛选，如果用户输入url异常则返回相应处理页面
	 * 例如没有输入请求文件名或者后缀名
	 * 如果用户url正常则根据url查找文件
	 * 如果文件存在就返回给浏览器
	 * 如果不存在则返回指定的错误提醒
	 * 返回类型为boolean，便于测试
	 */

	public boolean returnFile(){
		this.parseContentType(this.fileType);
		File file = null;
		switch (key) {
		case 1:
			file  = new File(WebServer.rootpath, "index.html");
			break;
		case 2:
			file  = new File(WebServer.rootpath, "postfix.html");
			break;
		case 0:
			file  = new File(WebServer.rootpath, this.url);
//			System.out.println(file.exists());
			if(!file.exists()){
				file = new File(WebServer.rootpath, "404.html");	
				contenType="text/html;charset=GB2312";
			}
			
				
			break;
		default:
			break;
		}
		log.trace("要返回的文件为：{}",file.toString());
		sendHead(file);
		return sendFile(file);
		
	}



	
	
	/*
	 * 这个方法用来发送响应头，参数是文件对象，因为要将文件长度信息发送出去
	 * 为了方便测试，方法返回 一个boolean值
	 */
	 
	public boolean sendHead(File file){
		//sign变量用来表示发送结果 true为成功，false表示出现异常
		boolean sign=false;
		//发送响应头
		StringBuilder head=new StringBuilder();
		head.append("HTTP/1.1 200 OK\r\n").append(
				"Content-Type: ").append(
				this.getContenType()).append(
				"\r\nDate: ").append(
				new Date().toString()).append(
				"\r\nContent-Length: ").append(
				file.length()).append(
				"\r\n\r\n");
		try {
			out.write(new String(head).getBytes());
			sign=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sign;
	}
	
	/*
	 * 将发送文件的代码抽取为一个方法，参数是file对象，因为要读取文件内容发送出去
	 * 为了方便测试，方法返回 一个boolean值
	 */
	public boolean sendFile(File file){
		boolean sign=false;
		byte[] bytes = new byte[1024];
	    FileInputStream fis = null;
		try {
			fis=new FileInputStream(file);
			int flag=fis.read(bytes);
			while(flag!=-1){
				out.write(bytes,0,flag);
//				System.out.println(new String(bytes, 0, flag));
				flag=fis.read(bytes);
			}
			sign=true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				if(fis!=null)
					fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return sign;
	}
	
	/*
	 * 将具体处理细节封装到一个方法里面。
	 */
	public boolean handle(){
		this.parse();
		return this.returnFile();
	}
}
