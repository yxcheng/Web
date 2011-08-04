package com.esensoft.webserver.junit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.esensoft.webserver.HttpHandler;
import com.esensoft.webserver.WebServer;

/**
 * 这个测试类用来测试HttpHandler类的方法，对其中涉及到逻辑业务处理的每个方法都要进行
 * 测试，如果需要用到输入输出流的时候使用FileInputStream和FileOutputStream模拟
 * 对一些以字符串为参数的方法测试起来会简单一些
 * @author 成亚雄
 *
 */
public class TestHandler {

	/*
	 * 测试HttpHandler能否正确获取HTTP请求中包含url的信息，
	 * 经过验证，IE、FireFox、Chrome等浏览器发送的请求中第一行均由如下组成 ：
	 * 请求方法+url+HTTP协议版本
	 * 此方法测试HttpHandler类中负责解析url和fileType的方法parseUrl()和parseType
	 * 首先解析出url，然后从url中解析出文件后缀名
	 * 不需要用到InputStream和OutputStream，用null代替
	 */
	@Test
	public void testUrlAndType() {
		HttpHandler httpHandler=new HttpHandler(null, null);
		
		httpHandler.parseUrl(null);
		httpHandler.parseType(httpHandler.getUrl());
		assertEquals("", httpHandler.getUrl());
		assertEquals("nofile", httpHandler.getFileType());
		
		httpHandler.parseUrl("");
		httpHandler.parseType(httpHandler.getUrl());
		assertEquals("", httpHandler.getUrl());
		assertEquals("nofile", httpHandler.getFileType());
		
		httpHandler.parseUrl("GET /1.html HTTP/1.1");
		httpHandler.parseType(httpHandler.getUrl());
		assertEquals("1.html", httpHandler.getUrl());
		assertEquals("html", httpHandler.getFileType());
		
		httpHandler.parseUrl("GET / HTTP/1.1");
		httpHandler.parseType(httpHandler.getUrl());
		assertEquals("", httpHandler.getUrl());
		assertEquals("nofile", httpHandler.getFileType());
		
		httpHandler.parseUrl("GET /11224 HTTP/1.1");
		httpHandler.parseType(httpHandler.getUrl());
		assertEquals("11224", httpHandler.getUrl());
		assertEquals("nopostfix", httpHandler.getFileType());
		
		httpHandler.parseUrl("GET /##$$ HTTP/1.1");
		httpHandler.parseType(httpHandler.getUrl());
		assertEquals("##$$", httpHandler.getUrl());
		assertEquals("nopostfix", httpHandler.getFileType());
		
		httpHandler.parseUrl("GET /##$$.@@ HTTP/1.1");
		httpHandler.parseType(httpHandler.getUrl());
		assertEquals("##$$.@@", httpHandler.getUrl());
		assertEquals("@@", httpHandler.getFileType());
		
		httpHandler.parseUrl("GET /..jpg HTTP/1.1");
		httpHandler.parseType(httpHandler.getUrl());
		assertEquals("..jpg", httpHandler.getUrl());
		assertEquals("jpg", httpHandler.getFileType());
		
		httpHandler.parseUrl("GET /  .jpg HTTP/1.1");
		httpHandler.parseType(httpHandler.getUrl());
		assertEquals("  .jpg", httpHandler.getUrl());
		assertEquals("jpg", httpHandler.getFileType());
	}

	/*
	 * HttpHandler类中通过获取到socket的InputStream来读取HTTP请求中
	 * 的信息，此处使用FileInputStream模拟socket对象的输入流
	 * 将模拟的HTTP请求信息写入文件测试httpHandler能否正确读取输入流中信息
	 * 测试HttpHandler类中读取HTTP请求信息的方法parse()
	 * 输出流用null代替
	 */
	@Test
	public void testParse(){
		InputStream in=null;
		try {
			in=new FileInputStream("D:\\test_src/http.txt");
//			assertEquals(in==null,false);
			HttpHandler httpHandler=new HttpHandler(in,null);
			httpHandler.parse();
			assertEquals("1.html", httpHandler.getUrl());
			assertEquals("html", httpHandler.getFileType());
			
			in=new FileInputStream("D:\\test_src/http1.txt");
			httpHandler=new HttpHandler(in,null);
			httpHandler.parse();
			assertEquals("", httpHandler.getUrl());
			assertEquals("nofile", httpHandler.getFileType());
			
			in=new FileInputStream("D:\\test_src/http2.txt");
			httpHandler=new HttpHandler(in,null);
			httpHandler.parse();
			assertEquals("11111", httpHandler.getUrl());
			assertEquals("nopostfix", httpHandler.getFileType());
			
			in=new FileInputStream("D:\\test_src/http3.txt");
			httpHandler=new HttpHandler(in,null);
			httpHandler.parse();
			assertEquals("..jpg", httpHandler.getUrl());
			assertEquals("jpg", httpHandler.getFileType());
			
			in=new FileInputStream("D:\\test_src/http4.txt");
			httpHandler=new HttpHandler(in,null);
			httpHandler.parse();
			assertEquals("  .jpg", httpHandler.getUrl());
			assertEquals("jpg", httpHandler.getFileType());
			
			in=new FileInputStream("D:\\test_src/http5.txt");
			httpHandler=new HttpHandler(in,null);
			httpHandler.parse();
			assertEquals("%%%.txt", httpHandler.getUrl());
			assertEquals("txt", httpHandler.getFileType());
			
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		finally{
			try {
				if(in!=null)
					in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	}
	
	/*
	 * 测试HttpHandler类中parseContentType()方法
	 * 该方法根据文件后缀名确定ContentType
	 * InputStream和OutputStream用null代替
	 */
	@Test
	public void testContentType() {
		HttpHandler httpHandler=new HttpHandler(null, null);
		httpHandler.parseContentType(null);
		assertEquals("text/html;charset=GB2312",httpHandler.getContenType());
		assertEquals(0, httpHandler.getKey());
		
		httpHandler.parseContentType("nofile");
		assertEquals("text/html;charset=GB2312",httpHandler.getContenType());
		assertEquals(1, httpHandler.getKey());
		
		httpHandler.parseContentType("nopostfix");
		assertEquals("text/html;charset=GB2312",httpHandler.getContenType());
		assertEquals(2, httpHandler.getKey());
		
		httpHandler.parseContentType("");
		assertEquals("text/html;charset=GB2312",httpHandler.getContenType());
		assertEquals(0, httpHandler.getKey());
		
		httpHandler.parseContentType("5555");
		assertEquals("text/html;charset=GB2312",httpHandler.getContenType());
		assertEquals(0, httpHandler.getKey());
		
		httpHandler.parseContentType("jpg");
		assertEquals("image/jpeg", httpHandler.getContenType());
		assertEquals(0, httpHandler.getKey());
		
		httpHandler.parseContentType("txt");
		assertEquals("text/plain", httpHandler.getContenType());
		assertEquals(0, httpHandler.getKey());
		
		httpHandler.parseContentType("mp3");
		assertEquals("audio/mpeg", httpHandler.getContenType());
		assertEquals(0, httpHandler.getKey());
	}
	
	/*
	 * 测试HttpHandler类的sendHead，使用FileOutputstream来模拟socket的
	 * OutputStream，将响应头信息发送到一个txt文档中
	 * 为了在增加测试覆盖范围的同时减少代码的重复度，新建了一个Help类，里面
	 * 将那些重复度很多的代码提取出来
	 */
	@Test
	public void testSendHead(){
	   OutputStream out=null;
	   File des=new File("D:\\test_src/headInfo.txt");
	   try {
		   out=new FileOutputStream(des);
		   HttpHandler httpHandler=new HttpHandler(null, out);
		   assertEquals(true, Help.HelpTestHead(httpHandler,"1.html"));
		   assertEquals(true, Help.HelpTestHead(httpHandler,"404.html"));
		   assertEquals(true, Help.HelpTestHead(httpHandler,"2.txt"));
		   assertEquals(true, Help.HelpTestHead(httpHandler,"index.html"));
		   assertEquals(true, Help.HelpTestHead(httpHandler,"hills.jpg"));
		   assertEquals(true, Help.HelpTestHead(httpHandler,"love.mp3"));
		   assertEquals(true, Help.HelpTestHead(httpHandler,"hello.c"));
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
	 	   
	}
	
	/*
	 * 测试HttpHandler类的sendFile方法，测试思路和testSendHead类似
	 * 用FileOutputStream模拟socket的OutputStream,不过此时不能简单地将文件信息输出
	 * 到一个文本信息中，因为读取的文件可能是html，也可能是txt，也可能是图片
	 * 为了直观一些，决定将读取的文件信息写到和源文件类型相同的文件中，相当于是
	 * 完成了一次copy
	 */
	@Test
	public void testSendFile(){
		assertEquals(true, Help.HelpTestFile("1.html"));
		assertEquals(true, Help.HelpTestFile("life.jpg"));
		assertEquals(true, Help.HelpTestFile("2.txt"));
		assertEquals(true, Help.HelpTestFile("404.html"));
		assertEquals(true, Help.HelpTestFile("sorry.gif"));
		assertEquals(true, Help.HelpTestFile("love.mp3"));
		assertEquals(true, Help.HelpTestFile("hello.c"));
		assertEquals(true, Help.HelpTestFile("egg.jpg"));
		assertEquals(true, Help.HelpTestFile("index.html"));
	}
	
	/*
	 * 测试HttpHandler类的returnFile方法，return方法里面调用了sendHead()
	 * 和sendFile()方法，使用同一个输出流，最后输出流的信息包含响应头以及
	 * 文件信息，因此只好把它们放到一个txt文档里面
	 */
	@Test
	public void testReturnFile(){
		OutputStream out=null;
		File des=new File("D:\\test_src/des.txt");
		try {
			out=new FileOutputStream(des);
			HttpHandler httpHandler=new HttpHandler(null, out);
			assertEquals(true, Help.HelpTestReturn(httpHandler,"1.html"));
			assertEquals(true, Help.HelpTestReturn(httpHandler,""));
			assertEquals(true, Help.HelpTestReturn(httpHandler,"567"));
			assertEquals(true, Help.HelpTestReturn(httpHandler,"##$$"));
			assertEquals(true, Help.HelpTestReturn(httpHandler,"2.txt"));
			assertEquals(true, Help.HelpTestReturn(httpHandler,"hello.c"));
			assertEquals(true, Help.HelpTestReturn(httpHandler,"p ic.html"));
			assertEquals(true, Help.HelpTestReturn(httpHandler,"egg.jpg"));
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
	}

}
