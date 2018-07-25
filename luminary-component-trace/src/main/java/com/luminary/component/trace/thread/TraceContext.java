/**  
* <p>Title: TraceContext.java</p>  
* <p>Description: 链路跟踪的上下文</p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日上午10:54:09  
*/  
package com.luminary.component.trace.thread;

import com.luminary.component.trace.model.TraceInfo;
import com.luminary.component.util.common.ThreadContext;

/**  
* <p>Title: TraceContext</p>  
* <p>Description: 线程上下文中绑定了trace相关的逻辑</p>  
* @author wulinfeng
* @date 2018年7月20日上午10:54:09
*/
public class TraceContext extends ThreadContext {

	private static final String TRACE_INFO_KEY = "luminroy_trace_info";
	
	/**
	 * 
	 * <p>Title: putTraceInfo</p>  
	 * <p>Description: 把链路调用信息存入当前线程上下文</p>  
	 * @param traceInfo
	 */
	public static final void putTraceInfo(TraceInfo traceInfo) {
		putContext(TRACE_INFO_KEY, traceInfo);
	}
	
	/**
	 * 
	 * <p>Title: getTraceInfo</p>  
	 * <p>Description: 从当前线程上下文中返回链路调用信息</p>  
	 * @return
	 */
	public static final TraceInfo getTraceInfo() {
		return getContext(TRACE_INFO_KEY);
	}
	
	/**
	 * 
	 * <p>Title: removeTraceInfo</p>  
	 * <p>Description: 从当前线程上下文中移除链路调用信息</p>
	 */
	public static final void removeTraceInfo() {
		removeContext(TRACE_INFO_KEY);
	}
	
}
