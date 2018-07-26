/**  
* <p>Title: GenericTracker.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月24日上午11:41:23  
*/  
package com.luminary.component.trace.tracker;

import java.util.Calendar;

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.MDC;

import com.google.gson.Gson;
import com.luminary.component.trace.client.TraceClient;
import com.luminary.component.trace.model.RpcTraceInfoVO;
import com.luminary.component.trace.model.RpcTypeEnum;
import com.luminary.component.trace.model.TraceInfo;
import com.luminary.component.trace.thread.TraceContext;
import com.luminary.component.trace.tracker.GenericTracker.TraceHolder;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: GenericTracker</p>  
* <p>Description: 基础的链路跟踪器</p>  
* @author wulinfeng
* @date 2018年7月24日上午11:41:23
*/
@Slf4j
public class GenericTracker implements Tracker<TraceHolder> {
	
	private static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");

	private TraceClient traceClient;
	
	public GenericTracker (TraceClient traceClient) {
		this.traceClient = traceClient;
	}
	
	@Override
	public void preHandle(TraceHolder holder) {
		
		TraceInfo traceInfo = TraceContext.getTraceInfo();
		if(traceInfo == null) {
			traceInfo = new TraceInfo();
		} else {
			traceInfo.addSequenceNo();
		}
		
		// 允许通过holder传递traceId和rpcId，比如基于hystrix的tracker实现
		if(holder.getTraceId() != null)
			traceInfo.setTraceId(holder.getTraceId());
		if(holder.getRpcId() != null) 
			traceInfo.setRpcId(holder.getRpcId());
		
		MDC.put(TraceInfo.TRACE_ID_KEY, traceInfo.getTraceId());
		MDC.put(TraceInfo.RPC_ID_KEY, traceInfo.getRpcId());
		
		RpcTraceInfoVO rpcTraceInfoVO = new RpcTraceInfoVO();
		rpcTraceInfoVO.setProfile(holder.getProfile());
		rpcTraceInfoVO.setRequestDateTime(ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS.format(Calendar.getInstance().getTime()));
		rpcTraceInfoVO.setTraceId(traceInfo.getTraceId());
		rpcTraceInfoVO.setRpcId(traceInfo.getRpcId());
		rpcTraceInfoVO.setRpcType(RpcTypeEnum.HTTP.name());
		rpcTraceInfoVO.setServiceCategory(holder.getServiceCategory());
		rpcTraceInfoVO.setServiceName(holder.getServiceName());
		rpcTraceInfoVO.setMethodName(holder.getMethodName());
		rpcTraceInfoVO.setRequestParam(holder.getRequestJson());
		rpcTraceInfoVO.setServiceHost(holder.getServiceHost());
		rpcTraceInfoVO.setClientHost(holder.getClientHost());
		
		holder.setEntity(rpcTraceInfoVO);
		
		holder.setStartTime(System.currentTimeMillis());
		
	};
	
	@Override
	public void postHandle(TraceHolder holder) {
		
		try {
			RpcTraceInfoVO rpcTraceInfoVO = holder.getEntity();
			if(rpcTraceInfoVO != null) {
				
				String traceId = rpcTraceInfoVO.getTraceId();
				if(traceId == null)
					return;
				
				rpcTraceInfoVO.setRunTime(System.currentTimeMillis() - holder.getStartTime());
				
				Gson gson = new Gson();
				log.debug(gson.toJson(rpcTraceInfoVO));
				traceClient.sendTraceInfo(rpcTraceInfoVO);
				
				rpcTraceInfoVO.setResult(RpcTraceInfoVO.RESULT_SUCCESS);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} 
		
	};
	
	@Override
	public void exceptionHandle(TraceHolder holder, Exception ex) {
		
		try {
			
			RpcTraceInfoVO rpcTraceInfoVO = holder.getEntity();
			if(rpcTraceInfoVO != null) {
				rpcTraceInfoVO.setResult(RpcTraceInfoVO.RESULT_FAILURE);
				rpcTraceInfoVO.setResponseInfo(ex.getMessage());
				Gson gson = new Gson();
				log.debug(gson.toJson(rpcTraceInfoVO));
				traceClient.sendTraceInfo(rpcTraceInfoVO);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} 
	}
	
	@Data
	public static class TraceHolder {
		private long startTime;
		private String traceId;
		private String rpcId;
		private String profile;
		private String serviceCategory;
		private String serviceName;
		private String methodName;
		private String requestJson;
		private String serviceHost;
		private String clientHost;
		private RpcTraceInfoVO entity;
	}
	
}
