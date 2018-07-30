/**  
* <p>Title: MInteceptor.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月30日下午5:14:58  
*/  
package com.luminary.component.trace.demo;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: MInteceptor</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月30日下午5:14:58
*/
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class })})
public class MInteceptor implements Interceptor {
	
	/* (non-Javadoc)  
	 * <p>Title: intercept</p>  
	 * <p>Description: </p>  
	 * @param invocation
	 * @return
	 * @throws Throwable  
	 * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.Invocation)  
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		log.info("--------------------------------");
		return invocation.proceed();
	}

	/* (non-Javadoc)  
	 * <p>Title: plugin</p>  
	 * <p>Description: </p>  
	 * @param target
	 * @return  
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)  
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/* (non-Javadoc)  
	 * <p>Title: setProperties</p>  
	 * <p>Description: </p>  
	 * @param properties  
	 * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)  
	 */
	@Override
	public void setProperties(Properties properties) {
		
	}

}
