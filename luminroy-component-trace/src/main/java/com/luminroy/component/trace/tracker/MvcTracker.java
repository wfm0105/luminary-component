/**  
* <p>Title: MvcTracker.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日下午4:58:49  
*/  
package com.luminroy.component.trace.tracker;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;

import com.google.gson.Gson;
import com.luminroy.component.trace.client.TraceClient;
import com.luminroy.component.trace.holder.MvcHolder;
import com.luminroy.component.trace.model.RpcTraceInfoVO;
import com.luminroy.component.trace.model.RpcTypeEnum;
import com.luminroy.component.trace.model.TraceInfo;
import com.luminroy.component.trace.thread.TraceContext;
import com.luminroy.component.util.web.HostUtil;

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
	
	protected TraceClient traceClient;
	
	public MvcTracker(TraceClient traceClient) {
		this.traceClient = traceClient;
	}
	
	@Override
	public void preHandle(MvcHolder holder) {
		initTraceData(holder.getRequest(), holder.getHandler());
	}
	
	@Override
	public void postHandle(MvcHolder holder) {
		finishTraceData(holder.getRequest(), holder.getResponse(), holder.getException());
	}
	
	private void initTraceData (HttpServletRequest request, Object handler) {
		// 开始时间
		request.setAttribute("beginTime", System.currentTimeMillis());
		// 为当前线程设置上下文
		TraceContext.init();
		// 链路跟踪信息基础对象
		TraceInfo traceInfo = new TraceInfo();
		
		// 封装链路跟踪数据
		if(handler != null && handler instanceof HandlerMethod) {
			 HandlerMethod handlerMethod = (HandlerMethod) handler;
			 
			 Gson gson =new Gson();
			 
			 RpcTraceInfoVO rpcTraceInfoVO = new RpcTraceInfoVO();
			 rpcTraceInfoVO.setRequestDateTime(ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS.format(Calendar.getInstance().getTime()));
			 rpcTraceInfoVO.setTraceId(traceInfo.getTraceId());
			 rpcTraceInfoVO.setRpcId(traceInfo.getRpcId());
			 rpcTraceInfoVO.setRpcType(RpcTypeEnum.HTTP.name());
			 rpcTraceInfoVO.setServiceCategory("spring mvc");
			 rpcTraceInfoVO.setServiceName(handlerMethod.getBean().getClass().getSimpleName());
			 rpcTraceInfoVO.setMethodName(handlerMethod.getMethod().getName());
			 rpcTraceInfoVO.setRequestJson(gson.toJson(request.getParameterMap()));
			 rpcTraceInfoVO.setServiceHostName(HostUtil.getIP()+":"+request.getLocalPort()+request.getServletPath());
			 rpcTraceInfoVO.setClientHost(HostUtil.getIP(request));
			 
			 request.setAttribute("rpcTraceInfoVO", rpcTraceInfoVO);
		}
		
		// 增加层级
		traceInfo.addHierarchy();
		
		// 链路跟踪信息保存到当前线程上下文中
		TraceContext.putTraceInfo(traceInfo);
		MDC.put("traceId", traceInfo.getTraceId());
		MDC.put("rpcId", traceInfo.getRpcId());
	}
	
	private void finishTraceData(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		try {
			RpcTraceInfoVO rpcTraceInfoVO = (RpcTraceInfoVO) request.getAttribute("rpcTraceInfoVO");
			if(rpcTraceInfoVO != null) {
					long beginTime = (Long) request.getAttribute("beginTime");
					rpcTraceInfoVO.setRunTime(System.currentTimeMillis() - beginTime);
					if(ex == null) {
						rpcTraceInfoVO.setResult("OK");
					} else {
						rpcTraceInfoVO.setResult("ERROR");
						rpcTraceInfoVO.setResponseJson(ex.getMessage());
					}
					traceClient.sendTraceInfo(rpcTraceInfoVO);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			request.removeAttribute("rpcTraceInfoVO");
			TraceContext.removeTraceInfo();
			MDC.remove("traceId");
			MDC.remove("rpcId");
		}
	}
	
}
