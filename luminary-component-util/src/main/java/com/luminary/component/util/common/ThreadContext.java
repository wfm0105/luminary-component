/**  
* <p>Title: TraceContext.java</p>  
* <p>Description: 链路跟踪的上下文</p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日上午10:54:09  
*/  
package com.luminary.component.util.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**  
* <p>Title: ThreadContext</p>  
* <p>Description: 线程上下文</p>  
* @author wulinfeng
* @date 2018年7月20日上午10:54:09
*/
public class ThreadContext {

	/**
	 * 
	 * 当前线程上下文(Map)持有者
	 * 
	 */
	public static final ThreadLocal<Map<String, Object>> CTX_HOLDER = new ThreadLocal<Map<String, Object>>(); 
	
	static {
		CTX_HOLDER.set(new HashMap<String, Object>());
	}
	
	/**
	 * 
	 * <p>Title: putContext</p>  
	 * <p>Description: 当前线程向上下文中存入变量</p>  
	 * @param key
	 * @param value
	 */
	public static final void putContext(String key, Object value) {
		Optional.ofNullable(CTX_HOLDER.get())
				   .ifPresent(map->{
					   map.put(key, value);
				   });
	}
	
	/**
	 * 
	 * <p>Title: getContext</p>  
	 * <p>Description: 获取当前线程上下文中的变量</p>  
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final <T extends Object> T getContext(String key) {
		return Optional.ofNullable(CTX_HOLDER.get())
					   .map(map->{
						   return (T) map.get(key);
					   })
					   .orElse(null);
	}
	
	/**
	 * 
	 * <p>Title: removeContext</p>  
	 * <p>Description: 从当前线程上下文中删除变量</p>  
	 * @param key
	 */
	public static final void removeContext(String key) {
		Optional.ofNullable(CTX_HOLDER.get())
			.ifPresent(map->{
				map.remove(key);
			});
	}
	
	/**
	 * 
	 * <p>Title: getContext</p>  
	 * <p>Description: 获得当前线程上下文</p>  
	 * @return
	 */
	public static final Map<String, Object> getContext() {
		return Optional.ofNullable(CTX_HOLDER.get()).orElse(null);
	}
	
	/**
	 * 
	 * <p>Title: clean</p>  
	 * <p>Description: 清除当前线程上下文</p>
	 */
	public static final void clean() {
		CTX_HOLDER.set(null);
	}
	
	/**
	 * 
	 * <p>Title: init</p>  
	 * <p>Description: 初始化当前线程上下文</p>
	 */
	public static final void init() {
		CTX_HOLDER.set(new HashMap<String, Object>());
	}
	
}
