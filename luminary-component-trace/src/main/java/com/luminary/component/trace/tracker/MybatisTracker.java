/**  
* <p>Title: MybatisTracker.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月27日下午5:14:39  
*/  
package com.luminary.component.trace.tracker;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.google.gson.Gson;
import com.luminary.component.trace.client.TraceClient;

import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: MybatisTracker</p>  
* <p>Description: 基于mybatis的链路跟踪器</p>  
* @author wulinfeng
* @date 2018年7月27日下午5:14:39
*/
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class })})
public class MybatisTracker extends GenericTracker implements Interceptor {

	private String profile;
	
	/**  
	* <p>Title: </p>  
	* <p>Description: </p>  
	* @param traceClient  
	*/  
	public MybatisTracker(TraceClient traceClient) {
		super(traceClient);
	}

	public MybatisTracker(TraceClient traceClient, String profile) {
		super(traceClient);
		this.profile = profile;
	}
	
	/* (non-Javadoc)  
	 * <p>Title: intercept</p>  
	 * <p>Description: </p>  
	 * @param arg0
	 * @return
	 * @throws Throwable  
	 * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.Invocation)  
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		log.info("mybatis tracker");
		
		TraceHolder traceHolder = new TraceHolder();
		Object result = null;
		
		try {
			
			Gson gson = new Gson();
			
			Map<String, Object> requestMap = new HashMap<String, Object>();
		    List<String> requestList = new ArrayList<String>();
			
			Object[] args = invocation.getArgs();
		    for(Object arg : args) {
		    	requestList.add(arg.toString());
		    }
		    
		    requestMap.put("params", requestList);
		    
			traceHolder.setProfile(profile);
			traceHolder.setServiceCategory("mybatis");
			traceHolder.setServiceName(invocation.getTarget().getClass().getName());
			traceHolder.setMethodName(invocation.getMethod().getName());
			traceHolder.setRequestJson(gson.toJson(requestMap));
			
			preHandle(traceHolder);
			result = invocation.proceed();
			traceHolder.getEntity().setResponseInfo(result.toString());
			postHandle(traceHolder);
			
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			exceptionHandle(traceHolder, e);
		} 
		
		return result;
		
	}

	/* (non-Javadoc)  
	 * <p>Title: plugin</p>  
	 * <p>Description: </p>  
	 * @param arg0
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
	 * @param arg0  
	 * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)  
	 */
	@Override
	public void setProperties(Properties properties) {
		
	}

}
