/**  
* <p>Title: Interceptor.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年8月1日上午9:51:51  
*/  
package com.luminary.component.cache.plugin;

/**  
* <p>Title: Interceptor</p>  
* <p>Description: 拦截器</p>  
* @author wulinfeng
* @date 2018年8月1日上午9:51:51
*/
public interface Interceptor {

	Object intercept(Invocation invocation) throws Throwable;

	Object plugin(Object target);
	
}
