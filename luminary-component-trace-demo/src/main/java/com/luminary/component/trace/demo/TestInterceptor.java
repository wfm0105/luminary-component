/**  
* <p>Title: TestInterceptor.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年8月1日上午11:13:02  
*/  
package com.luminary.component.trace.demo;

import com.luminary.component.cache.plugin.Interceptor;
import com.luminary.component.cache.plugin.Invocation;
import com.luminary.component.cache.plugin.Plugin;

import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: TestInterceptor</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年8月1日上午11:13:02
*/
@Slf4j
public class TestInterceptor implements Interceptor {

	/* (non-Javadoc)  
	 * <p>Title: intercept</p>  
	 * <p>Description: </p>  
	 * @param invocation
	 * @return
	 * @throws Throwable  
	 * @see com.luminary.component.cache.plugin.Interceptor#intercept(com.luminary.component.cache.plugin.Invocation)  
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		log.info("----------------------------------");
		return null;
	}

	/* (non-Javadoc)  
	 * <p>Title: plugin</p>  
	 * <p>Description: </p>  
	 * @param target
	 * @return  
	 * @see com.luminary.component.cache.plugin.Interceptor#plugin(java.lang.Object)  
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

}
