/**  
* <p>Title: TraceClient.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日下午1:19:04  
*/  
package com.luminroy.component.trace.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luminroy.component.trace.model.RpcTraceInfoVO;

/**  
* <p>Title: TraceClient</p>  
* <p>Description: 封装链路跟踪结果处理逻辑</p>  
* @author wulinfeng
* @date 2018年7月20日下午1:19:04
*/
public interface TraceClient {
	
	public static final Logger log = LoggerFactory.getLogger(TraceClient.class);
	
	/**
	 * 
	 * <p>Title: sendTraceInfo</p>  
	 * <p>Description: 将链路跟踪结果转发，比如进行存储或者交给消息队列异步处理</p>  
	 * @param rpcTraceInfoVO
	 * @throws Exception
	 */
	default void sendTraceInfo(final RpcTraceInfoVO rpcTraceInfoVO) throws Exception {
		log.warn("默认空实现！");
	}
	
}
