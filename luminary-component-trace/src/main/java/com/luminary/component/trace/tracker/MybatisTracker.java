/**  
* <p>Title: MybatisTracker.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月27日下午5:14:39  
*/  
package com.luminary.component.trace.tracker;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.gson.Gson;
import com.luminary.component.trace.client.TraceClient;
import com.luminary.component.trace.model.RpcTypeEnum;

import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: MybatisTracker</p>  
* <p>Description: 基于mybatis的链路跟踪器</p>  
* @author wulinfeng
* @date 2018年7月27日下午5:14:39
*/
@Slf4j
@Intercepts({
	@Signature(type = Executor.class, args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}, method = "query"),
	@Signature(type = Executor.class, args = {MappedStatement.class, Object.class}, method = "update")
})
public class MybatisTracker extends GenericTracker implements Interceptor {
	
	private static String MAPPER_SEPARATOR = "Dao.";
	private static String JDBC_PREFIX = "jdbc:";
	
	public MybatisTracker() {
		super();
	}
	
	/**  
	* <p>Title: </p>  
	* <p>Description: </p>  
	* @param traceClient  
	*/  
	public MybatisTracker(TraceClient traceClient) {
		super(traceClient);
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
			
			if(traceClient == null)
				traceClient = getTraceClient();
			
		    String sqlId = "";
		    Object parameter = null;
		    MappedStatement mappedStatement = null;
		    BoundSql boundSql = null;
		    Configuration configuration = null;
		    String jdbcUrl = "";
		    
			Object[] args = invocation.getArgs();
		    for(Object arg : args) {
		    	if(arg != null) {
		    		if(arg instanceof MappedStatement) {
		    			mappedStatement = (MappedStatement) arg;
		    			sqlId = mappedStatement.getId();
		    		}
		    		else if(!(arg instanceof RowBounds) && !(arg instanceof ResultHandler)) {
		    			parameter = arg;
		    		}
		    	}
		    }

		    if(mappedStatement !=null && parameter != null) {
		    	 boundSql = mappedStatement.getBoundSql(parameter);
		    	 configuration = mappedStatement.getConfiguration();
		    }
		    
		    String methodName = sqlId.substring(sqlId.indexOf(MAPPER_SEPARATOR)+MAPPER_SEPARATOR.length());
		    String serviceName = sqlId.substring(0, sqlId.lastIndexOf("."));
		    
		    String requestParam = boundSql.getSql();
		    
		    if(parameter != null) {
		    	requestParam = requestParam + "|" + gson.toJson(parameter);
		    }
		    
		    traceHolder.setProfile(getProfile());
		    traceHolder.setRpcType(RpcTypeEnum.DB.name());
			traceHolder.setServiceCategory("mybatis");
			traceHolder.setServiceName(serviceName);
			traceHolder.setMethodName(methodName);
			traceHolder.setRequestParam(requestParam);
			
			preHandle(traceHolder);
			result = invocation.proceed();
			
			if(configuration != null) {
				DataSource dataSource = configuration.getEnvironment().getDataSource();
		    	 if(dataSource instanceof DruidDataSource) {
		    		 jdbcUrl = ((DruidDataSource) dataSource).getUrl();
		    	 }
			}
			String serviceHost = jdbcUrl.substring(jdbcUrl.indexOf(JDBC_PREFIX)+JDBC_PREFIX.length(), jdbcUrl.indexOf("?"));
			
			traceHolder.getEntity().setServiceHost(serviceHost);
			
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
	
	public String getProfile() {
		log.warn("默认实现方法");
		return null;
	}
	
	public TraceClient getTraceClient() {
		log.warn("默认实现方法");
		return null;
	}

}
