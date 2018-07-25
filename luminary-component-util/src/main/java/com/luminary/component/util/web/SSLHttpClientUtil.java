package com.luminary.component.util.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSLHttpClientUtil {
	
	private static Logger log = LoggerFactory.getLogger(SSLHttpClientUtil.class);
	
	private static PoolingHttpClientConnectionManager connMgr;
	private static RequestConfig requestConfig;
	private static final int MAX_TIMEOUT = 30000; // 超时时间设置为30秒

	static {
		// 设置连接池
		connMgr = new PoolingHttpClientConnectionManager();
		// 设置连接池大小
		connMgr.setMaxTotal(100);
		connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(MAX_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(MAX_TIMEOUT);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
		// 在提交请求之前 测试连接是否可用
		configBuilder.setStaleConnectionCheckEnabled(true);
		requestConfig = configBuilder.build();
	}
  
	
	/**
	 * 发送 POST 请求（HTTP），XML形式
	 * 
	 * @param apiUrl
	 * @param xml
	 *            xml对象
	 * @return
	 * @throws Exception 
	 */
	public static String doPostXml(String apiUrl, Map<String, Object> headers, Object xml, String pkcs12, String pkcsSecret) throws Exception {
		//指定读取证书格式为PKCS12
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		//读取本机存放的PKCS12证书文件
		FileInputStream instream = new FileInputStream(new File(pkcs12));
		try {
			//指定PKCS12的密码(商户ID)
			keyStore.load(instream, pkcsSecret.toCharArray());
		} 
		finally {
			instream.close();	
		}
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, pkcsSecret.toCharArray()).build();
		//指定TLS版本
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,new String[] { "TLSv1" },null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		//设置httpclient的SSLSocketFactory
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		
		String httpStr = null;
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;

		try {
			httpPost.setConfig(requestConfig);
			StringEntity stringEntity = new StringEntity(xml.toString(), "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/xml");
			httpPost.setEntity(stringEntity);
			// 设置请求头
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpPost.addHeader(key, (String) headers.get(key));
				}
			}
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			// System.out.println(response.getStatusLine().getStatusCode());
			httpStr = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpStr;
	}
 
}
