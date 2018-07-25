/**  
* <p>Title: RestTraceClient.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月23日上午10:25:47  
*/  
package com.luminary.component.trace.util;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.google.gson.Gson;
import com.luminary.component.trace.client.TraceClient;
import com.luminary.component.trace.model.RpcTraceInfoVO;
import com.luminary.component.trace.model.RpcTypeEnum;
import com.luminary.component.trace.model.TraceInfo;
import com.luminary.component.trace.thread.TraceContext;
import com.luminary.component.util.web.HostUtil;
import com.luminary.component.util.web.RestClient;
import com.luminary.component.util.web.RestClient.RestHolder;

import ch.qos.logback.core.helpers.ThrowableToStringArray;
import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: RestTraceClient</p>  
* <p>Description: 加入了链路跟踪的rest风格的http client</p>  
* @author wulinfeng
* @date 2018年7月23日上午10:25:47
*/
@Slf4j
public class RestTraceClient extends RestClient {

	private TraceClient traceClient;
	
	public RestTraceClient(TraceClient traceClient) {
		this.traceClient = traceClient;
	}
	
	@Override
	protected RestHolder preHandle(String url, HttpMethod httpMethod, Object requestBody, HttpServletRequest request) {
		
		TraceInfo traceInfo = TraceContext.getTraceInfo();
		if(traceInfo == null) {
			traceInfo = new TraceInfo();
		} else {
			traceInfo.addSequenceNo();
		}
		MDC.put(TraceInfo.TRACE_ID_KEY, traceInfo.getTraceId());
		MDC.put(TraceInfo.RPC_ID_KEY, traceInfo.getRpcId());
		
		Gson gson = new Gson();
		
		RpcTraceInfoVO rpcTraceInfoVO = new RpcTraceInfoVO();
		rpcTraceInfoVO.setRequestDateTime(ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS.format(Calendar.getInstance().getTime()));
		rpcTraceInfoVO.setTraceId(traceInfo.getTraceId());
		rpcTraceInfoVO.setRpcId(traceInfo.getRpcId());
		rpcTraceInfoVO.setRpcType(RpcTypeEnum.HTTP.name());
		rpcTraceInfoVO.setServiceCategory("rest");
		rpcTraceInfoVO.setServiceName(url);
		rpcTraceInfoVO.setMethodName(httpMethod.name());
		rpcTraceInfoVO.setRequestJson(gson.toJson(requestBody));
		rpcTraceInfoVO.setServiceHost(url);
		rpcTraceInfoVO.setClientHost(HostUtil.getIP(request));
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(TraceInfo.TRACE_ID_KEY, traceInfo.getTraceId());
		headers.add(TraceInfo.RPC_ID_KEY, traceInfo.getRpcId());
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);

		RestHolder restHolder = new RestHolder();
		restHolder.setHttpHeaders(headers);
		restHolder.setHttpEntity(requestEntity);
		restHolder.setExtra(rpcTraceInfoVO);
		
		return restHolder;
		
	}
	
	@Override
	protected <T> T postHandle(T t, RestHolder restHolder, long intevalTime) {
		Gson gson = new Gson();
		
		RpcTraceInfoVO rpcTraceInfoVO = (RpcTraceInfoVO) restHolder.getExtra();
		rpcTraceInfoVO.setResponseJson(gson.toJson(rpcTraceInfoVO));
		rpcTraceInfoVO.setRunTime(intevalTime);
		rpcTraceInfoVO.setResult(RpcTraceInfoVO.RESULT_SUCCESS);
		
		try {
			log.debug(gson.toJson(rpcTraceInfoVO));
			traceClient.sendTraceInfo(rpcTraceInfoVO);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return t;
	}
	
	@Override
	protected void exceptionHandle(RestHolder restHolder, long intevalTime, Exception ex) {
		String[] traceStr = ThrowableToStringArray.convert(ex);
		StringBuilder sb = new StringBuilder();
		for(String trace : traceStr) {
			sb.append(trace);
			sb.append("\n");
		}
		
		RpcTraceInfoVO rpcTraceInfoVO = (RpcTraceInfoVO) restHolder.getExtra();
		rpcTraceInfoVO.setResponseJson(sb.toString());
		rpcTraceInfoVO.setRunTime(intevalTime);
		rpcTraceInfoVO.setResult(RpcTraceInfoVO.RESULT_FAILURE);
		try {
			Gson gson = new Gson();
			log.debug(gson.toJson(rpcTraceInfoVO));
			traceClient.sendTraceInfo(rpcTraceInfoVO);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
}
