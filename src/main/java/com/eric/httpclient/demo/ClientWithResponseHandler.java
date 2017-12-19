package com.eric.httpclient.demo;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author xizhijin 
 * This example demonstrates how to process HTTP responses using a response handler. 
 * This is the recommended way of executing HTTP requests and processing HTTP responses.
 * This approach enables the caller to concentrate on the process of digesting HTTP responses
 * and to delegate the task of system resource deallocation to HttpClient.
 * The use of an HTTP response handler guarantees that the underlying HTTP connection 
 * will be released back to the connection manager automatically in all cases.
 */
public class ClientWithResponseHandler {

	public static void main(String[] args) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet("http://www.baidu.com/");
			System.out.println("Executing request " + httpGet.getRequestLine());
			
			//Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				
				@Override
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if(status >= 200 && status < 300) {
						HttpEntity httpEntity = response.getEntity();
						return httpEntity != null ? EntityUtils.toString(httpEntity, "UTF-8") : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			
			String responseBody = httpClient.execute(httpGet, responseHandler);
			System.out.println("---------------------------------------------------------------");
            System.out.println(responseBody);
		} finally {
			httpClient.close();
		}
	}

}
