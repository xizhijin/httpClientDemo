package com.eric.httpclient.demo;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

/**
 * This example demonstrates the recommended way of using API to make sure the
 * underlying connection gets released back to the connection manager.
 */
public class ClientConnectionRelease {

	public static void main(String[] args) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		//使用EntityUtils.toString(entity, "UTF-8")方法自动解析		
		/*try {
			HttpGet httpGet = new HttpGet("http://www.baidu.com/");
			System.out.println("Executing request: " + httpGet.getRequestLine());
			
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				System.out.println("---------------------------------------------------------------");
				System.out.println("Response line: " + response.getStatusLine());
				System.out.println("---------------------------------------------------------------");
				HttpEntity entity = response.getEntity();
				System.out.println(EntityUtils.toString(entity, "UTF-8"));
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}*/
		
		//手动解析
		try {
			HttpGet httpGet = new HttpGet("http://www.baidu.com/");
			System.out.println("Executing request: " + httpGet.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				System.out.println("---------------------------------------------------------------");
				System.out.println("Response line: " + response.getStatusLine());
				System.out.println("---------------------------------------------------------------");
				HttpEntity httpEntity = response.getEntity();
				if(httpEntity != null) {
					int capacity = (int) httpEntity.getContentLength();
					if (capacity < 0) {
			                capacity = 4096;
			        }
					Charset charset = Charset.forName("UTF-8");
					final InputStream is = httpEntity.getContent();
					final Reader reader = new InputStreamReader(is, charset);
			        final CharArrayBuffer charArrayBuffer = new CharArrayBuffer(capacity);
			        final char[] tempChar = new char[1024];
			        int index;
		            while((index = reader.read(tempChar)) != -1) {
		            	charArrayBuffer.append(tempChar, 0, index);
		            }
		            System.out.println(charArrayBuffer.toString());
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		
	}

}
