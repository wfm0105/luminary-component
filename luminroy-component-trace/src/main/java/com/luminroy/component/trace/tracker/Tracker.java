/**  
* <p>Title: Tracker.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日上午10:21:05  
*/  
package com.luminroy.component.trace.tracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**  
* <p>Title: Tracker</p>  
* <p>Description: tracker用来执行跟踪逻辑</p>  
* @author wulinfeng
* @date 2018年7月20日上午10:21:05
*/
public interface Tracker<T> {
	
	public static final Logger log = LoggerFactory.getLogger(Tracker.class);
	
	/**
	 * 
	 * <p>Title: preHandle</p>  
	 * <p>Description: 业务执行前处理</p>
	 */
	default void preHandle(T holder) {
		log.warn("默认空实现！");
	};
	
	/**
	 * 
	 * <p>Title: postHandle</p>  
	 * <p>Description: 业务执行后处理</p>
	 */
	default void postHandle(T holder) {
		log.warn("默认空实现！");
	};
	
	/**
	 * 
	 * <p>Title: exceptionHandle</p>  
	 * <p>Description: 业务执行异常处理</p>  
	 * @param holder
	 */
	default void exceptionHandle(T holder, Exception ex) {
		log.warn("默认空实现！");
	};
	
}
