/**  
* <p>Title: RestTraceClient.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月23日上午10:14:40  
*/  
package com.luminary.component.util.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: RestTraceClient</p>  
* <p>Description: rest风格http客户端</p>  
* @author wulinfeng
* @date 2018年7月23日上午10:14:40
*/
@Slf4j
public class RestClient {

	private static RestTemplate restTemplate = new RestTemplate();
	
	protected static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
	
	protected RestHolder preHandle(String url, HttpMethod httpMethod, Object requestBody, HttpServletRequest request) {
		return null;
	}
	
	protected <T> T postHandle(T t, RestHolder restHolder, long intevalTime) {
		return t;
	}
	
	protected void exceptionHandle(RestHolder restHolder, long intevalTime, Exception ex) {
		
	}
	
	public <T> T request(String url, HttpMethod httpMethod, Object requestBody, Class<T> responseType, HttpServletRequest request) {
		long startTime = System.currentTimeMillis();
		T responseEntityBody = null;
		RestHolder restHolder = preHandle(url, httpMethod, requestBody, request);
		
		try {
			ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, restHolder.getHttpEntity(), responseType);
			responseEntityBody = responseEntity.getBody();
			long endTime = System.currentTimeMillis();
			responseEntityBody = postHandle(responseEntityBody, restHolder, endTime - startTime);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			long endTime = System.currentTimeMillis();
			exceptionHandle(restHolder, endTime - startTime, e);
		}
		
		return responseEntityBody;
	}
	
	@Data
	public static class RestHolder {
		
		private HttpHeaders httpHeaders;
		private HttpEntity<Object> httpEntity;
		private Object extra;
		
	}
	
}
