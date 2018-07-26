/**  
* <p>Title: MvcTracker.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日下午4:58:49  
*/  
package com.luminary.component.trace.tracker;

import java.util.Calendar;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.MDC;
import org.springframework.aop.support.AopUtils;
import org.springframework.web.method.HandlerMethod;

import com.google.gson.Gson;
import com.luminary.component.trace.client.TraceClient;
import com.luminary.component.trace.holder.MvcHolder;
import com.luminary.component.trace.model.RpcTraceInfoVO;
import com.luminary.component.trace.model.RpcTypeEnum;
import com.luminary.component.trace.model.TraceInfo;
import com.luminary.component.trace.thread.TraceContext;
import com.luminary.component.util.common.TargetUtils;
import com.luminary.component.util.web.HostUtil;

/**  
* <p>Title: MvcTracker</p>  
* <p>Description: 通用的mvc链路跟踪tracker。
* 		由于只和HttpServletRequest，HttpServletResponse依赖，
* 		应该可以适用于spring mvc，struts2等mvc框架</p>  
* @author wulinfeng
* @date 2018年7月20日下午4:58:49
*/
public class MvcTracker implements Tracker<MvcHolder> {

	private static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
	
	protected static final String BEGIN_TIME_KEY = "beginTime";
	
	protected static final String RPC_TRACE_INFO_VO_KEY = "rpcTraceInfoVO";
	
	protected TraceClient traceClient;
	
	public MvcTracker(TraceClient traceClient) {
		this.traceClient = traceClient;
	}
	
	@Override
	public void preHandle(MvcHolder holder) {
		initTraceData(holder.getRequest(), holder.getHandler(), holder.getProfile());
	}
	
	@Override
	public void postHandle(MvcHolder holder) {
		finishTraceData(holder.getRequest(), holder.getResponse(), holder.getException());
	}
	
	private void initTraceData (HttpServletRequest request, Object handler, String profile) {
		// 开始时间
		request.setAttribute(BEGIN_TIME_KEY, System.currentTimeMillis());
		// 为当前线程设置上下文
		TraceContext.init();
		// 链路跟踪信息基础对象
		TraceInfo traceInfo = new TraceInfo();
		
		String traceIdFromHeader = request.getHeader(TraceInfo.TRACE_ID_KEY);
		
		Optional.ofNullable(traceIdFromHeader)
			.ifPresent(traceId->{
			traceInfo.setTraceId(traceId);
		});
		
		Optional.ofNullable(
				request.getHeader(TraceInfo.RPC_ID_KEY)
		).ifPresent(rpcId->{
			traceInfo.setRpcId(rpcId);
		});
		
		// 封装链路跟踪数据
		if(handler != null && handler instanceof HandlerMethod && traceIdFromHeader == null) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			 
			Gson gson =new Gson();
			 
			String serviceName = null;
			try {
				serviceName = TargetUtils.getTarget(handlerMethod.getBean()).getClass().getSimpleName();
			} catch (Exception e) {
				 serviceName = handlerMethod.getBean().getClass().getSimpleName();
			}
			 if(serviceName == null) {
				 serviceName = handlerMethod.getBean().getClass().getSimpleName();
			 }
			 
			 RpcTraceInfoVO rpcTraceInfoVO = new RpcTraceInfoVO();
			 rpcTraceInfoVO.setProfile(profile);
			 rpcTraceInfoVO.setRequestDateTime(ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS.format(Calendar.getInstance().getTime()));
			 rpcTraceInfoVO.setTraceId(traceInfo.getTraceId());
			 rpcTraceInfoVO.setRpcId(traceInfo.getRpcId());
			 rpcTraceInfoVO.setRpcType(RpcTypeEnum.HTTP.name());
			 rpcTraceInfoVO.setServiceCategory("spring mvc");
			 rpcTraceInfoVO.setServiceName(AopUtils.getTargetClass(handlerMethod.getBean().getClass()).getSimpleName());
			 rpcTraceInfoVO.setMethodName(handlerMethod.getMethod().getName());
			 rpcTraceInfoVO.setRequestParam(gson.toJson(request.getParameterMap()));
			 rpcTraceInfoVO.setServiceHost(HostUtil.getIP()+":"+request.getLocalPort()+request.getServletPath());
			 rpcTraceInfoVO.setClientHost(HostUtil.getIP(request));
			 
			 request.setAttribute(RPC_TRACE_INFO_VO_KEY, rpcTraceInfoVO);
		}
		
		// 增加层级
		traceInfo.addHierarchy();
		
		// 链路跟踪信息保存到当前线程上下文中
		TraceContext.putTraceInfo(traceInfo);
		MDC.put(TraceInfo.TRACE_ID_KEY, traceInfo.getTraceId());
		MDC.put(TraceInfo.RPC_ID_KEY, traceInfo.getRpcId());
	}
	
	private void finishTraceData(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		try {
			String traceIdFromHeader = request.getHeader(TraceInfo.TRACE_ID_KEY);
			if(traceIdFromHeader != null)
				return;
			RpcTraceInfoVO rpcTraceInfoVO = (RpcTraceInfoVO) request.getAttribute(RPC_TRACE_INFO_VO_KEY);
			if(rpcTraceInfoVO != null) {
					long beginTime = (Long) request.getAttribute(BEGIN_TIME_KEY);
					rpcTraceInfoVO.setRunTime(System.currentTimeMillis() - beginTime);
					if(ex == null) {
						rpcTraceInfoVO.setResult(RpcTraceInfoVO.RESULT_SUCCESS);
					} else {
						rpcTraceInfoVO.setResult(RpcTraceInfoVO.RESULT_FAILURE);
						rpcTraceInfoVO.setResponseInfo(ex.getMessage());
					}
					Gson gson = new Gson();
					log.debug(gson.toJson(rpcTraceInfoVO));
					traceClient.sendTraceInfo(rpcTraceInfoVO);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			request.removeAttribute(RPC_TRACE_INFO_VO_KEY);
			TraceContext.removeTraceInfo();
			MDC.remove(TraceInfo.TRACE_ID_KEY);
			MDC.remove(TraceInfo.RPC_ID_KEY);
		}
	}
	
}
