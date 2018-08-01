/**  
* <p>Title: CacheTracker.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年8月1日下午1:48:34  
*/  
package com.luminary.component.trace.tracker;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.luminary.component.cache.operator.redis.JedisRedisOperator;
import com.luminary.component.cache.operator.redis.RedisTemplateRedisOperator;
import com.luminary.component.cache.plugin.Interceptor;
import com.luminary.component.cache.plugin.Invocation;
import com.luminary.component.cache.plugin.Plugin;
import com.luminary.component.trace.client.TraceClient;
import com.luminary.component.trace.model.RpcTypeEnum;

import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: CacheTracker</p>  
* <p>Description: 基于redis缓存的链路跟踪器</p>  
* @author wulinfeng
* @date 2018年8月1日下午1:48:34
*/
@Slf4j
public class RedisCacheTracker extends GenericTracker implements Interceptor {
	
	public RedisCacheTracker() {
		super();
	}
	
	public RedisCacheTracker(TraceClient traceClient) {
		super(traceClient);
	}
	
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

		log.info("cache tracker");
		
		TraceHolder traceHolder = new TraceHolder();
		Object result = null;
		
		try {
			
			Gson gson = new Gson();
			
			if(traceClient == null)
				traceClient = getTraceClient();
			
			String serverHost = "";
			
			Map<String, Object> params = new HashMap<String, Object>();
			Object target = invocation.getTarget();
			if(target instanceof JedisRedisOperator) {
				JedisRedisOperator jedisRedisOperator = (JedisRedisOperator) target;
				serverHost = jedisRedisOperator.getHostPort();
			}
			else if(target instanceof RedisTemplateRedisOperator) {
				serverHost = getServerHost();
			}
			
			Method method = invocation.getMethod();
			Object[] args = invocation.getArgs();
			if(method.getName().equals("setBit")) {
				params.put("key", args[0]);
				params.put("offset", args[1]);
				params.put("value", args[2]);
			}
			else if(method.getName().equals("getBit")) {
				params.put("key", args[0]);
				params.put("offset", args[1]);
			}
			else if(method.getName().equals("set")) {
				params.put("key", args[0]);
				params.put("data", args[1]);
				params.put("expiredSeconds", args[2]);
			}
			else if(method.getName().equals("get")) {
				params.put("key", args[0]);
			}
			else if(method.getName().equals("del")) {
				params.put("key", args[0]);
			}
			
			traceHolder.setProfile(getProfile());
		    traceHolder.setRpcType(RpcTypeEnum.CACHE.name());
			traceHolder.setServiceCategory("redis");
			traceHolder.setServiceName(invocation.getTarget().getClass().getName());
			traceHolder.setServiceHost(serverHost);
			traceHolder.setMethodName(method.getName());
			traceHolder.setRequestParam(gson.toJson(params));
			
			preHandle(traceHolder);
			result = invocation.proceed();
			
			traceHolder.getEntity().setResponseInfo(result == null ? "" : result.toString());
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
	 * @param target
	 * @return  
	 * @see com.luminary.component.cache.plugin.Interceptor#plugin(java.lang.Object)  
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
	
	public String getProfile() {
		log.warn("默认实现方法");
		return null;
	}
	
	public TraceClient getTraceClient() {
		log.warn("默认实现方法");
		return null;
	}
	
	public String getServerHost() {
		log.warn("默认实现方法");
		return null;
	}

}
