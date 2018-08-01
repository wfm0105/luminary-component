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
import java.util.concurrent.atomic.AtomicInteger;

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

	protected TraceClient traceClient;
	
	public GenericTracker () {
		
	}
	
	public GenericTracker (TraceClient traceClient) {
		this.traceClient = traceClient;
	}
	
	@Override
	public void preHandle(TraceHolder holder) {
		
		TraceInfo traceInfo = TraceContext.getTraceInfo();
		if(traceInfo == null) {
			traceInfo = new TraceInfo();
		} else {
			
			int level = 1;
			if(TraceInfo.ORIGINAL_ROOT_RPC_ID.equals(traceInfo.getRpcId())) {
				// 第一次入栈的处理逻辑，调用的总入口，此时rpcId为1
				traceInfo.addHierarchy();
				int maxSequenceNo = traceInfo.getHierarchyMaxSeqNo(level);
				traceInfo.setSequenceNo(new AtomicInteger(maxSequenceNo+1));
			}
			else if(TraceInfo.RE_ORIGINAL_ROOT_RPC_ID.equals(traceInfo.getRpcId())) {
				// 出栈到第一层的处理逻辑，traceInfo.subHierarchy()后，rpcId变为1.0的时候
				int maxSequenceNo = traceInfo.getHierarchyMaxSeqNo(level);
				traceInfo.setSequenceNo(new AtomicInteger(maxSequenceNo+1));
			} 
			else if(traceInfo.getRootRpcId().equals(traceInfo.getRpcId())) {
				// 跨线程后第一次入栈的处理逻辑，比如通过http请求到另一个服务的mvc，或者hystrix新创建一个线程
				traceInfo.addHierarchy();
				level = traceInfo.getRpcId().split("[.]").length-1;
				int maxSequenceNo = traceInfo.getHierarchyMaxSeqNo(level);
				traceInfo.setSequenceNo(new AtomicInteger(maxSequenceNo+1));
			} 
			else {
				// 普通情况的处理逻辑，一般大部分情况都是这个逻辑
				traceInfo.addHierarchy();
				level = traceInfo.getRpcId().split("[.]").length-1;
				int maxSequenceNo = traceInfo.getHierarchyMaxSeqNo(level);
				traceInfo.setSequenceNo(new AtomicInteger(maxSequenceNo+1));
			}
			
		}
		
		// 允许通过holder传递traceId和rpcId，比如基于hystrix的tracker实现
		if(holder.getTraceId() != null)
			traceInfo.setTraceId(holder.getTraceId());
		if(holder.getRpcId() != null) 
			traceInfo.setRpcId(holder.getRpcId());
		
		traceInfo.cache();
		
		TraceContext.putTraceInfo(traceInfo);
		
		MDC.put(TraceInfo.TRACE_ID_KEY, traceInfo.getTraceId());
		MDC.put(TraceInfo.RPC_ID_KEY, traceInfo.getRpcId());
		
		RpcTraceInfoVO rpcTraceInfoVO = new RpcTraceInfoVO();
		rpcTraceInfoVO.setProfile(holder.getProfile());
		rpcTraceInfoVO.setRequestDateTime(ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS.format(Calendar.getInstance().getTime()));
		rpcTraceInfoVO.setTraceId(traceInfo.getTraceId());
		rpcTraceInfoVO.setRpcId(traceInfo.getRpcId());
		rpcTraceInfoVO.setRpcType(holder.getRpcType());
		rpcTraceInfoVO.setServiceCategory(holder.getServiceCategory());
		rpcTraceInfoVO.setServiceName(holder.getServiceName());
		rpcTraceInfoVO.setMethodName(holder.getMethodName());
		rpcTraceInfoVO.setRequestParam(holder.getRequestParam());
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
				rpcTraceInfoVO.setResult(RpcTraceInfoVO.RESULT_SUCCESS);
				
				Gson gson = new Gson();
				log.debug(gson.toJson(rpcTraceInfoVO));
				traceClient.sendTraceInfo(rpcTraceInfoVO);
				
				TraceInfo traceInfo = TraceContext.getTraceInfo();
				traceInfo.subHierarchy();
				
				TraceContext.putTraceInfo(traceInfo);
				
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
				
				TraceInfo traceInfo = TraceContext.getTraceInfo();
				traceInfo.subHierarchy();
				
				TraceContext.putTraceInfo(traceInfo);
				
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
		private String rpcType;
		private String profile;
		private String serviceCategory;
		private String serviceName;
		private String methodName;
		private String requestParam;
		private String serviceHost;
		private String clientHost;
		private RpcTraceInfoVO entity;
	}
	
}
